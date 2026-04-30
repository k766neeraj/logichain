
package com.example.logichain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Source cannot be empty")
    @Size(min = 5, message = "Source must be at least 5 characters")
    private String source;

    @NotBlank(message = "Destination cannot be empty")
    @Size(min = 5, message = "Destination must be at least 5 characters")
    private String destination;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Shipment() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() { return updatedAt;}

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt;}
}
