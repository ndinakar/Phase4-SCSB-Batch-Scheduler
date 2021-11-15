# Batch-Scheduler

Batch schedule module for  ReCAP Middleware

The SCSB Middleware codebase and components are all licensed under the Apache 2.0 license, with the exception of a set of API design components (JSF, JQuery, and Angular JS), which are licensed under MIT X11.

SCSB-BATCH-SCHEDULER is a microservice application where all the batch jobs in the SCSB application are configured and managed using [Spring
Batch](https://spring.io/projects/spring-batch) and [Quartz Scheduler
framework](http://www.quartz-scheduler.org/) 

## Software Required

  - Java 11
  - Docker 19.03.13   
  
## Prerequisite

1.**Cloud Config Server**

Dspring.cloud.config.uri=http://phase4-scsb-config-server:<port>

## Build

Download the Project , navigate inside project folder and build the project using below command

**./gradlew clean build -x test**

## Docker Image Creation

Naviagte Inside project folder where Dockerfile is present and Execute the below command

**sudo docker build -t phase4-scsb-batch-scheduler .**

## Docker Run

User the below command to Run the Docker

sudo docker run --name phase4-scsb-batch-scheduler   -v <volume> --label collect_logs_with_filebeat="true" --label decode_log_event_to_json_object="true" -p <port>:<port> -e "ENV= -Xms400m  -Xmx750m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/recap-vol/phase4-scsb-batch-scheduler/heapdump/ -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*  -Dspring.cloud.config.uri=http://phase4-scsb-config-server:<port>  " --network=scsb  -d phase4-scsb-batch-scheduler **
