package com.example.logichain.service;

import com.example.logichain.dto.ShipmentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShipmentConsumer {

    @KafkaListener(topics = "shipment-topic", groupId = "shipment-group")
    public  void consume(ShipmentEvent event){
        System.out.println("Received Event: "+event.getShipmentId()+" Staus: "+event.getStatus());
    }
}
