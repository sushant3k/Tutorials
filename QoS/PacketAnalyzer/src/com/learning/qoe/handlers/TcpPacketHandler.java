/**
 *  Statefull TcpPacketHandler.
 *  State management is done using the different values.
 */
package com.learning.qoe.handlers;

import java.io.IOException;
import java.util.List;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Request;
import org.jnetpcap.protocol.tcpip.Http.Response;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.learn.RealTimePacketCapture;
import com.learning.qoe.model.HTTPParams;
import com.learning.qoe.model.Params;
import com.learning.qoe.model.QoEPacket;
import com.learning.qoe.model.TSParams;
import com.learning.qoe.queuing.service.EnqueueService;
import com.learning.qoe.queuing.service.EnqueueServiceImpl;
import com.learning.qoe.utils.EnumUtils.FileTypeEnum;
import com.learning.qoe.utils.FileManagementUtils;

/**
 * @author Sushant
 *
 */
public class TcpPacketHandler {

    /**
     * Final packet to be sent to the server
     */
    private QoEPacket qp = new QoEPacket();

    /**
     * Variable maintaining the current state of the TCP Packet.
     * 
     */
    // Packet Receiving,
    short currentstate = -1;

    /**
     * Request headers
     */
    byte[] requestHeaders;

    /**
     * request payloads
     */
    byte[] requestPayload;

    /**
     * Response headers
     */
    byte[] responseHeaders;

    /**
     * Response payload
     */
    byte[] responsePayload;

    /**
     * Sample filename in which the .ts content are to be stored.
     */
    String filename = "test";

    /**
     * TCP Packet retransmission count
     */
    int retransmissionsCount = 0;

    /**
     * Number of connection resets
     */
    private int resets = 0;

    /**
     * Packet sequence number. May be used in future
     */
    private Long srcSeqNo;

    /**
     * Expected next sequence number.
     */
    private Long srcNextSeqNoExpected;

    /**
     * Source ack number
     */
    private long srcAck;

    /**
     * Destination sequence number
     */
    private Long destSeqNo;

    /**
     * Expected next desitnation sequence number
     */
    private Long destNextSeqNoExpected;

    /**
     * Destination ack number
     */
    private long destAck;

    /**
     * Clean up flag
     */
    private boolean cleanUP = false;

    /**
     * Maintains count of duplicate acks
     */
    private int dupAcks;

    /**
     * Maintains count of outOfOrder packets.
     */
    private int outOfOrder;

    /**
     * Filename extension used for identifying different types of files such
     * m3u8, .ts
     */
    private String fileType = "txt"; // TODO: Make it as an Enum

    /**
     * Path to the temp directory where the .ts files after assembly are to be
     * stored.
     */

    private String filepath = System.getProperty("java.io.tmpdir");
    // private String filepath = "c:/sushant/learnings_workspace/" ;

    /**
     * Session ID
     */
    private String sessionId;

    // private List<TcpSe>
    public TcpPacketHandler() {

    }

    public TcpPacketHandler(String sessionId) {
        this.sessionId = sessionId;
    }

    private void populateSequenceAndAck(Tcp tcp) {
        this.srcSeqNo = tcp.seq();
        this.srcAck = tcp.ack();

    }

    /**
     * Analyze request TCP packet.
     * @param streamIndex
     * @param packet
     * @param tcp
     */
    public void analyzeRequest(long streamIndex, PcapPacket packet, Tcp tcp) {

        if (tcp.flags_SYN() && !tcp.flags_ACK()) {
            currentstate = 0;
            qp.setFirstPacketReceivedTime(packet.getCaptureHeader()
                    .timestampInMillis());
            qp.setStreamIndex(streamIndex);

            populateSequenceAndAck(tcp);
            return;
        }

        if (tcp.flags_ACK() && tcp.getPayloadLength() == 0 && currentstate < 2) {
            currentstate = 2;
            populateSequenceAndAck(tcp);
            return;
        }

        if (tcp.flags_ACK() && tcp.flags_RST()) {
            resets++;
            populateSequenceAndAck(tcp);
            return;
        }

        /**
         * Connection Closure succeeded.
         */
        if (tcp.flags_ACK() && tcp.flags_FIN()) {

            if (!cleanUP) {
                qp.setLastPacketReceivedTime(packet.getCaptureHeader()
                        .timestampInMillis());

                cleanUp(packet, tcp);
                cleanUP = true;

                populateSequenceAndAck(tcp);
            }
            return;
        }

        if (tcp.flags_ACK() && currentstate == 6) // Server closed the
                                                  // connection so perform a
                                                  // cleanup.
        {
            if (!cleanUP) {
                qp.setLastPacketReceivedTime(packet.getCaptureHeader()
                        .timestampInMillis());
                cleanUp(packet, tcp);
                cleanUP = true;
                return;
            }
        }

        if (tcp.flags_ACK() && tcp.getPayloadLength() == 0) {
            if (tcp.ack() == this.srcAck) {
                System.out.println("Dup Ack");
                dupAcks++;
                return;
            }
        }
        populateSequenceAndAck(tcp);
        this.srcNextSeqNoExpected = calculateNextSequenceNumber(packet, tcp);

        // /**
        // * Now perform the check for Duplicate Acks
        // */
        // if (this.srcNextSeqNoExpected != null) {
        // if (tcp.seq() != destAck) {
        // dupAcks++;
        // populateSequenceAndAck(tcp);
        // return;
        //
        // }
        // else {
        // this.srcSeqNo = tcp.seq();
        // this.srcNextSeqNoExpected = calculateNextSequenceNumber(packet, tcp);
        // }
        // }
        // calculateNextSequenceNumber(packet, tcp);

        Http http = new Http();
        if (packet.hasHeader(http)) {
            handleHTTPRequest(http);
        } else {
            handleTCPRequest(tcp);
        }

    }

    private long calculateNextSequenceNumber(PcapPacket packet, Tcp tcp) {
        Ethernet et = new Ethernet();
        packet.getHeader(et);
        Ip4 ip = new Ip4();
        packet.getHeader(ip);
        return tcp.seq()
                + (packet.getPacketWirelen() - (et.getLength() + ip.getLength() + tcp
                        .getLength()));
    }

    @SuppressWarnings("unchecked")
    private void cleanUp(PcapPacket packet, Tcp tcp) {
        Ip4 ip = new Ip4();
        packet.getHeader(ip);
        final String p = filepath + filename;
        try {
            // System.out.println("Writing file:="+p);
            FileManagementUtils.writeToFile(responsePayload, p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Params params = qp.getParams();
        if (fileType.equals(FileTypeEnum.M3U8.getFileType())) {
            List<HTTPParams> paramSet = null;
            ContentTypeHandlerSvc<HTTPParams> svc = ContentTypeHandlerFactory
                    .getContentTypeHandlerFactory(FileTypeEnum.valueOf(fileType
                            .toUpperCase()));
            paramSet = svc.processFile(requestHeaders, requestPayload,
                    responseHeaders, responsePayload, filepath, filename);

            if (params == null) {
                params = new Params();
            }
            params.setHttpParams(paramSet);

        } else if (fileType.equals(FileTypeEnum.TS.getFileType())) {
            List<TSParams> paramSet = null;
            ContentTypeHandlerSvc<TSParams> svc = ContentTypeHandlerFactory
                    .getContentTypeHandlerFactory(FileTypeEnum.valueOf(fileType
                            .toUpperCase()));
            paramSet = svc.processFile(requestHeaders, requestPayload,
                    responseHeaders, responsePayload, filepath, filename);

            if (params == null) {
                params = new Params();
            }
            params.setTsParams(paramSet);
        } else {
            // Ignore now.
        }

        qp.setNumberOfConnectionResets(resets);
        qp.setDupAcks(dupAcks);
        qp.setOutOfOrder(outOfOrder);
        qp.setSourceIp(FormatUtils.ip(ip.source()));
        qp.setDestIp(FormatUtils.ip(ip.destination()));
        qp.setSourcePort(tcp.source());
        qp.setDestPort(tcp.destination());
        qp.setParams(params);

        // Eventually should get rid of this.
        qp.setSessionId(this.sessionId == null ? RealTimePacketCapture.SESSIONID
                : this.sessionId);

        if (responsePayload != null) {
            qp.setTotalNumberOfBytes(responsePayload.length);
        }
        // tsFile = false;

        EnqueueService<QoEPacket> es = new EnqueueServiceImpl<QoEPacket>();
        es.enqueue(qp);

        // try {
        // FileManagementUtils.deleteFile(p);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

    private void handleTCPRequest(Tcp tcp) {
        if (tcp.flags_ACK() && tcp.getPayloadLength() > 0) {
            requestPayload = merge(requestPayload, tcp.getPayload());
        }
    }

    private void handleTCPResponse(Tcp tcp) {
        if (tcp.flags_ACK() && tcp.getPayloadLength() > 0) {            
            responsePayload = merge(responsePayload, tcp.getPayload());
        }
    }

    private void handleHTTPRequest(Http http) {

        if (requestHeaders == null || requestHeaders.length == 0) {
            // A Way to Identify that this is the initial request
            final String url = http.fieldValue(Request.RequestUrl);
            filename = url.substring(url.lastIndexOf("/") + 1);
            // System.out.printf("\nextracted filename and Stream Index = %s ",filename);
            requestHeaders = http.getHeader();
        }

        requestHeaders = merge(requestHeaders, http.getHeader());

        requestPayload = merge(requestPayload, http.getPayload());
    }

    private void handleHttpResponse(Http http) {
       
        /**
         * I don't expect content type to be null though.
         */
        final String contentType = http.fieldValue(Response.Content_Type);

        if (contentType != null) {
            fileType = filename.substring(filename.indexOf(".") + 1);
        }

      
        responseHeaders = merge(responseHeaders, http.getHeader());
        responsePayload = merge(responsePayload, http.getPayload());
    }

    private byte[] merge(byte[] mergeInto, byte[] mergeFrom) {

        if (mergeFrom == null || mergeFrom.length == 0) {
            return mergeInto;
        }
        if (mergeInto == null || mergeInto.length == 0) {
            return mergeFrom;
        }

        byte temp[] = new byte[mergeInto.length + mergeFrom.length];
        int i = 0;
        for (; i < mergeInto.length; i++) {
            temp[i] = mergeInto[i];
        }
        for (int j = 0; j < mergeFrom.length; j++) {
            temp[i++] = mergeFrom[j];
        }

        return temp;
    }

    /**
     * Handle public TCP response.
     * @param streamIndex
     * @param packet
     * @param tcp
     */
    public void analyzeResponse(long streamIndex, PcapPacket packet, Tcp tcp) {        

        if (tcp.flags_RST()) {
            resets++;
        }
        if (tcp.flags_SYN() && tcp.flags_ACK()) {
            currentstate = 1;
            return;
        }

        if (tcp.flags_ACK() && tcp.flags_FIN()) {
            currentstate = 6;
        }

        /**
         * Now perform the check for Duplicate Acks
         */
        if (this.destNextSeqNoExpected != null) {
            if (tcp.seq() != destNextSeqNoExpected) {
                outOfOrder++;
                return;

            } else {
                this.destSeqNo = tcp.seq();
                this.destNextSeqNoExpected = calculateNextSequenceNumber(
                        packet, tcp);
            }
        }

        Http http = new Http();
        if (packet.hasHeader(http)) {
            handleHttpResponse(http);
        } else {
            handleTCPResponse(tcp);
        }
    }
   
    public QoEPacket getPacket() {
        return qp;
    }
}
