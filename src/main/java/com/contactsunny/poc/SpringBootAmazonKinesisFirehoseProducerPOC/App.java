package com.contactsunny.poc.SpringBootAmazonKinesisFirehoseProducerPOC;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.ByteBuffer;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Value("${aws.auth.accessKey}")
    private String awsAccessKey;

    @Value("${aws.auth.secretKey}")
    private String awsSecretKey;

    @Value("${aws.kinesis.firehose.deliveryStream.name}")
    private String fireHoseDeliveryStreamName;

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        AmazonKinesisFirehose firehoseClient = AmazonKinesisFirehoseClient.builder()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        JSONObject messageJson = new JSONObject();
        messageJson.put("key1", "We are testing Amazon Kinesis Firehose!");
        messageJson.put("integerKey", 123);
        messageJson.put("booleanKey", true);
        messageJson.put("anotherString", "This should work!");

        logger.info("Message to Firehose: " + messageJson.toString());

        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(fireHoseDeliveryStreamName);

        Record record = new Record().withData(ByteBuffer.wrap(messageJson.toString().getBytes()));
        putRecordRequest.setRecord(record);

        PutRecordResult putRecordResult = firehoseClient.putRecord(putRecordRequest);

        logger.info("Message record ID: " + putRecordResult.getRecordId());
    }
}
