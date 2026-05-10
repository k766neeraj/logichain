package com.example.logichain.service;

import com.example.logichain.dto.ShipmentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, ShipmentEvent> kafkaTemplate;

    public void sendShipmentEvent(ShipmentEvent event){
        kafkaTemplate.send("shipment-topic",event);
        System.out.println("Event Published: "+event.getShipmentId());
    }
}
