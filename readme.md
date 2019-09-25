# Spring Boot Amazon Kinesis Firehose Delivery Stream Producer POC

This is a simple Spring Boot CommandLine application to demonstrate how we can put a record to 
an Amazon Kinesis Firehose Delivery Stream. The pipeline could be setup to write these records to
an S3 location, or stream to other delivery streams.

# How To Run?

Make sure you add your AWS access key and access secret to the ```application.properties``` file.
Also, in the ```App.java``` class, change the region according to your Kinesis Firehose setup.
