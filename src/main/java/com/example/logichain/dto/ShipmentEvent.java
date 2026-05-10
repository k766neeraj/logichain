package com.example.logichain.dto;

import com.example.logichain.model.ShipmentStatus;

import java.io.Serializable;

public class ShipmentEvent implements Serializable {

    private int ShipmentId;
    private String status;
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
