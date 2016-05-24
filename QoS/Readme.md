## Quality of Service (QoS) of Video Streaming

#### A learning project that accomplishes following items: 
* Reading Pcap packet on Win-7 64 bit machine in real time using JnetPcap library
* Building the correlation between these packets for a session
* Capturing the following metrics
    - HLS Profile details
    - HLS Profile variation based on the bandwidth
    - Device details (currently hard coded)
    - Stall Duration
    - Buffers
    - MPEG2TS parameters 
    - Codec Information (H.264 etc.) > Using Xuggler
* Post these metrics to a REST Server
* Persist these metrics in MongoDB
* View the analyzed data in real time


### The project consists of three sub-projects, namely:

* JavaPlaylistParser-master
    - A third party HLS parser. Tweaked according to your needs. The tweak is still buggy and doesn't comply to HLS standards completely. However, it should do the job.

* PacketAnalyzer
    - A standalone Java program that receives TCP Packets, analyzes them and sends to the server
* qoe
    - A web application, that displays the data on the UI in real-time using Websockets.

### Configuration
See project specific configuration files.

### License
See license.md