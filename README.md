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
1. Get experience with **tree data structures** (implemented as XML documents) and (in-memory) **searching algorithms**.
2. In **First** deliverable, parse a huge XML configuration file in memory and execute commands (in-memory).
3. In **Second** deliverable, use the parsed XML to store everything in a relational database (**Oracle**) and execute different commands on database.
4. Analyse the efficiency of both approaches.

Technologies used:
==================
1. Mainly project is developed using **Java**.
2. For XML parsing, we are using **SAX Parser**.

How to use:
===========
Deliverable 1:
-----------------
1. Open cmd.
2. Copy ADS.jar to present working directory.
3. Copy the file containing commands to be run ( say test.txt ) to the present working directory.
4. Copy the xml file (say edgeconfig2_sample.xml) to the present working directory.
5. Run the following command:
	java -Xmx2048m -jar ADS.jar < test.txt

_Xmx2048 added to support multiple configuration file parsing_  
_Memory leaks are handled_

Deliverable 2:
-------------------
1. Run using - java -Xmx2048m -jar 001U_del2proj2.jar <XML FILE NAME>

System Requirements
-----------------------------------------------------------------
java version "1.7.0_09"   
Java(TM) SE Runtime Environment (build 1.7.0_09-b05)  
Java HotSpot(TM) 64-Bit Server VM (build 23.5-b02, mixed mode)

More information about the project, please see the project requirement docs present in **docs** directory.
