## Packet Analyzer

* The main file of this project is RealTimePacketCapture.java 

* The configuration is contained in the config.properties file. The config file is self explanatory and doesn't have too many parameters for configuration right now. 
	
* The program doesn't support CDN/IP switching and only captures packets between defined IPs. However, capturing packets from multiple IPs is trivial. 

* The Program reads Pcap packet on Win-7 64 bit machine in real time using JnetPcap library
* It builds the correlation packets based on different stream indexes and sessions.
* It captures the following metrics
    - HLS Profile details
    - HLS Profile variation based on the bandwidth
    - Device details (currently hard coded)
    - Stall Duration
    - Buffers
    - MPEG2TS parameters 
    - Codec Information (H.264 etc.) > Using Xuggler
* Post these metrics to a REST Server

## Libraries required

* It is not a maven/gradle project because of unavailibility of all the third party libraries via the maven repos.
* All the relevant and required libraries are present under the libs folder and can be imported. 
* The relevant windows DLLs are also in the relevant library sub-directory under libs directory.

### Build
No custom build file is provided. Request you to import the project in Eclipse and then build it. I had issues with the DLLs and never bothered to resolve them.

### License
Apache License v 2.0