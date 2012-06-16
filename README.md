Cloud Bursting Example Architecture
===================================

Introduction
------------
This is an example implementation of a Cloud Bursting architecture, with Workload Migration and Data Synchronization patterns.
It uses Spring's PetClinic reference application running on Tomcat with a MySQL back-end DB on each site, and GigaSpaces XAP to replicate data between the sites. 
GigaSpaces Cloudify is used to support workload migration by automating spin-up of the secondary site in case of peak load.

Architecture
------------
Topology: 2 sites, single mysql server on each site with WAN replication (space+gateway). Tomcat servers running Petclinic against the mysql on each site.
1.	IaaS providers: EC2 and RackSpace
2.	DB: MySQL
3.	WAN replication channel: the following XAP PUs: G/W, feeder, processor.
o	Listening on DB queries log file to intercept data mutation.
4.	Load Balancer: RackSpace LB
5.	Demo application: PetClinic running in Tomcat, packaged as a Cloudify application.
Order of system bootstrap is as per above.

Content
-------
* DB scripts for setting up the logging, schema and demo data for the PetClinic application
* PetClinic application (.war) file
* WAN replication gateway module
* Cloudify recipe for automating the PetClinic deployment

Reference
---------
Blog post: http://horovits.wordpress.com/2012/06/12/cloud-bursting/
