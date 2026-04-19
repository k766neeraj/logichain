package com.example.logichain.mapper;

import com.example.logichain.dto.ShipmentDTO;
import com.example.logichain.model.Shipment;


public class ShipmentMapper {

    public static ShipmentDTO toDTO(Shipment sp){
        ShipmentDTO dto = new ShipmentDTO();
        dto.setSource(sp.getSource());
        dto.setDestination(sp.getDestination());
        return dto;
    }

    public static Shipment toEntity(ShipmentDTO dto){
        Shipment sp = new Shipment();
        sp.setSource(dto.getSource());
        sp.setDestination(dto.getDestination());
        return sp;
    }
}