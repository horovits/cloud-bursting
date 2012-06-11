cloud-bursting
==============

This is an example implementation of a Cloud Bursting architecture. It uses Spring's PetClinic reference application running on Tomcat with a MySQL back-end DB on each site, and GigaSpaces XAP to replicate data between the sites. GigaSpaces Cloudify is used to automate spin-up of the secondary site in case of peak load.