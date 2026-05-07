package com.example.logichain.dto;

import com.example.logichain.model.ShipmentStatus;
import com.example.logichain.model.ShipmentTracking;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ShipmentTrackingDTO implements Serializable {

    private ShipmentStatus status;
    private LocalDateTime timestamp;

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


}
