package com.frank.zheng.tinyurl.service;

import com.frank.zheng.tinyurl.entity.RequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private static final String TOPIC_REQUEST_LOG = "tiny_url_request_logs";

    @Autowired
    private KafkaTemplate<String, RequestLog> kafkaTemplateForRequestLog;

    public void publishRequestLog(@NonNull RequestLog requestLog) {
        try {
            //Seems the kafka library will create threads for sending
            //So here won't throw exceptions immediately
            kafkaTemplateForRequestLog.send(TOPIC_REQUEST_LOG, requestLog);
        } catch (Exception e) {
            logger.error("Failed to send the request log to kafka, ", e);
        }
    }

}
