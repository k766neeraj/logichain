package com.example.logichain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ShipmentDTO{

    @NotBlank(message = "Source cannot be empty")
    @Size(min = 3, message = "Source must be at least 3 characters")
    private String source;

    @NotBlank(message = "Destination cannot be empty")
    @Size(min = 3, message = "Destination must be at least 3 characters")
    private String destination;

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



    // getters & setters
}