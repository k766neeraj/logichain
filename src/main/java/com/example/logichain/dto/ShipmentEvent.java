package com.example.logichain.dto;

import com.example.logichain.model.ShipmentStatus;

import java.io.Serializable;

public class ShipmentEvent implements Serializable {

    private int ShipmentId;
    private String source;
    private String destination;
    private String status;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getShipmentId() {
        return ShipmentId;
    }

    public void setShipmentId(int shipmentId) {
        ShipmentId = shipmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
