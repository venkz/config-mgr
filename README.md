SOA-based Enterprise Configuration Management
=============================================

Large Service-Oriented-Architecture (SOA) based enterprise applications are generally 
dynamic, complex, multi-tiered, distributed across multiple machines (e.g. a data center). Management
of such applications is a challenge and XML-based configuration files are widely used in the 
management of such applications.

Goal of this project includes specific management tasks like **deployment of applications**,
**selective restart** of applications after a reboot and **capacity planning**. 

Such tasks require efficient querying, transformation and persistence of configuration 
data which is the main goal of any enterprise management tool.

Objective
=========
The main objective of this project is to get experience with **tree data structures** (implemented
as XML documents) and (in-memory) **searching algorithms**.

Technologies used:
==================
1. Mainly project is developed using **Java**.
2. For XML parsing, we are using **SAX Parser**.

How to use:
===========
1. Open cmd.
2. Copy ADS.jar to present working directory.
3. Copy the file containing commands to be run ( say test.txt ) to the present working directory.
4. Copy the xml file (say edgeconfig2_sample.xml) to the present working directory.
5. Run the following command:
	java -Xmx2048m -jar ADS.jar < test.txt

_Xmx2048 added to support multiple configuration file parsing_
_Memory leaks are handled_

System Requirements
-----------------------------------------------------------------
java version "1.7.0_09"
Java(TM) SE Runtime Environment (build 1.7.0_09-b05)
Java HotSpot(TM) 64-Bit Server VM (build 23.5-b02, mixed mode)
