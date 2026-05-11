package com.example.logichain.service;

import com.example.logichain.dto.ShipmentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsConsumer {

    @KafkaListener(topics = "shipment-topic", groupId = "analytics-group")
    public void consume(ShipmentEvent event){
        System.out.println("Analytics Updated for Shipment: "+ event.getShipmentId());
    }
}
