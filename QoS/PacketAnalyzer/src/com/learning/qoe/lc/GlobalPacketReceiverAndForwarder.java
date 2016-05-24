/**
 * 
 * The object of this class is used for receiving a TCP Packet. 
 * It creates the custom model from the received TCP Packet and delegates the 
 * further processing to the respective handler.
 */
package com.learning.qoe.lc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.learning.qoe.utils.StreamIndexManager;

/**
 * @author Sushant
 *
 */
public class GlobalPacketReceiverAndForwarder implements Runnable {

    /**
     * Blocking Queue instance from which messages are to be read
     */
    private BlockingQueue<PcapPacket> q;
    
    /**
     * Generated session ID for identifying each packet individually
     */
    private String sessionId;

    
    /**
     * The interface manager maps contains entries for the stream index as the key 
     * and the corresponding TCP Segments queue.
     */
    private ConcurrentMap<Long, BlockingQueue<WrapperPcapPacket>> interfaceManager = new ConcurrentHashMap<Long, BlockingQueue<WrapperPcapPacket>>();

    /**
     * Source IP i.e. your IP
     */
    private String aSrcIP;
    
    /**
     * Destination IP, i.e. the source from where the streaming HLS content is being presented.
     */
    private String aDestIP;
    
    /**
     * Destination port, that is part of the destination socket.
     */
    private int aDestPort;

    public GlobalPacketReceiverAndForwarder(BlockingQueue<PcapPacket> q,
            final String actualSrcIP, final String actualDestIP,
            final int actualDestPort, String sessionId) {
        this.q = q;
        this.aSrcIP = actualSrcIP;
        this.aDestIP = actualDestIP;
        this.aDestPort = actualDestPort;
        this.sessionId = sessionId;
    }

    @Override
    public void run() {
        while (true) {

            try {
                processPacket(q.take());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void spawnThread(BlockingQueue<WrapperPcapPacket> q) {
        IndividualPacketReceiver ip = new IndividualPacketReceiver(q);
        Thread t = new Thread(ip);
        t.start();
    }

    private void processPacket(PcapPacket packet) {

        Ip4 ip = new Ip4();

        packet.getHeader(ip);

        String srcip = FormatUtils.ip(ip.source());
        String destip = FormatUtils.ip(ip.destination());

        Tcp tcp = new Tcp();
        packet.getHeader(tcp);
        int srcport = tcp.source();
        int destport = tcp.destination();
        Long streamIndex = null;
        if (tcp.flags_SYN() && !tcp.flags_ACK()) {
            streamIndex = StreamIndexManager.getInstance().addStreamIndex(
                    srcip, destip, srcport, destport);

            // TcpPacketHandler handler = new TcpPacketHandler();
            BlockingQueue<WrapperPcapPacket> bq = new ArrayBlockingQueue<WrapperPcapPacket>(
                    10000);
            interfaceManager.put(streamIndex, bq);
            spawnThread(bq);

            WrapperPcapPacket wpp = new WrapperPcapPacket(packet, streamIndex,
                    true, sessionId, tcp);
            try {
                bq.add(wpp);
            } catch (IllegalStateException e) {

                e.printStackTrace();
            }
        }

        streamIndex = StreamIndexManager.getInstance().addAndGetStreamIndex(
                srcip, destip, srcport, destport);

        BlockingQueue<WrapperPcapPacket> bq = interfaceManager.get(streamIndex);
        if (bq == null) {
            bq = new ArrayBlockingQueue<WrapperPcapPacket>(10000);
            interfaceManager.put(streamIndex, bq);
        }

        if (this.aSrcIP.equalsIgnoreCase(srcip)
                && this.aDestIP.equalsIgnoreCase(destip)
                && this.aDestPort == destport) {
            WrapperPcapPacket wpp = new WrapperPcapPacket(packet, streamIndex,
                    true, sessionId, tcp);
            try {
                bq.add(wpp);
            } catch (IllegalStateException e) {

                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // handler.analyzeRequest(streamIndex, packet, tcp);
        }

        else if (this.aSrcIP.equalsIgnoreCase(destip)
                && this.aDestIP.equals(srcip) && srcport == this.aDestPort) {
            // handler.analyzeResponse(streamIndex, packet, tcp);
            WrapperPcapPacket wpp = new WrapperPcapPacket(packet, streamIndex,
                    false, sessionId, tcp);
            try {
                bq.add(wpp);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
    }

}
