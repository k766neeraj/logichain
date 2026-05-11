package com.example.logichain.service;

import com.example.logichain.dto.ShipmentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TrackingConsume {

    @KafkaListener(topics = "shipment-topic", groupId = "tracking-group")
    public void consume(ShipmentEvent event){
        System.out.println("Tracking created for Shipment: "+event.getShipmentId());
    }
}
