package com.example.logichain.service;

import com.example.logichain.dto.ShipmentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsume {

    @KafkaListener(topics = "shipment-topic", groupId = "notification-group")
    public void consume(ShipmentEvent event){
        System.out.println("Notification Sent for Shipment: "+event.getShipmentId());
    }
}
