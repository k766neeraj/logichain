
package com.example.logichain.service;

import com.example.logichain.ShipmentNotFoundException;
import com.example.logichain.dto.ApiResponse;
import com.example.logichain.dto.ShipmentDTO;
import com.example.logichain.dto.ShipmentEvent;
import com.example.logichain.dto.ShipmentTrackingDTO;
import com.example.logichain.mapper.ShipmentMapper;
import com.example.logichain.model.AuditAction;
import com.example.logichain.model.Shipment;
import com.example.logichain.model.ShipmentStatus;
import com.example.logichain.model.ShipmentTracking;
import com.example.logichain.repository.ShipmentRepository;
import com.example.logichain.repository.ShipmentTrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository repo;

    @Autowired
    private ShipmentTrackingRepository trackingRepo;

    @Autowired
    private KafkaProducerService producer;

    @Autowired
    private AuditLogService auditLogService;

    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);


    public ApiResponse<List<ShipmentDTO>> getAllShipments(int page, int size, String sortBy, String source, String status){

        log.info("Fetching shipment - page: {}, size: {}, sortBy: {}, source: {}, status: {}", page, size, sortBy, source,status);

        page = Math.max(page,0);
        size = Math.max(size,1);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Shipment> shipmentPage;

        ShipmentStatus shipmentStatus = null;
        if(status!=null && !status.trim().isEmpty()){
            try {
                shipmentStatus = ShipmentStatus.valueOf(status.toUpperCase());
            }catch(Exception e){
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        if(source!=null  && !source.isEmpty() && shipmentStatus!=null){
            shipmentPage = repo.findBySourceContainingIgnoreCaseAndStatus(source,shipmentStatus,pageable);
        }else if(source != null && !source.trim().isEmpty()){
            shipmentPage = repo.findBySourceContainingIgnoreCase(source,pageable);
        }else if(shipmentStatus!=null){
            shipmentPage = repo.findByStatus(shipmentStatus,pageable);
        }
         else {
            shipmentPage = repo.findAll(pageable);
        }

        List<ShipmentDTO> list = new ArrayList<>();

        for(Shipment sp : shipmentPage.getContent()){
            list.add(ShipmentMapper.toDTO(sp));
        }

        return new ApiResponse<>(
                list,
                shipmentPage.getNumber(),
                shipmentPage.getSize(),
                Math.toIntExact(shipmentPage.getTotalElements()),
                "Shipment fetched successfully"
        );
    }

    public ApiResponse<ShipmentDTO> saveShipment(ShipmentDTO dto){

        log.info("Creating shipment with source: {} and destination: {}", dto.getSource(), dto.getDestination());

        Shipment sp = ShipmentMapper.toEntity(dto);
        sp.setStatus(ShipmentStatus.CREATED);
        sp.setCreatedAt(LocalDateTime.now());

        Shipment saved = repo.save(sp);

        auditLogService.logAction(
                AuditAction.CREATE_SHIPMENT,
                "SHIPMENT",
                saved.getId()
        );

        ShipmentEvent event = new ShipmentEvent();
        event.setShipmentId(saved.getId());
        event.setSource(saved.getSource());
        event.setDestination(saved.getDestination());
        event.setStatus(saved.getStatus().name());
        producer.sendShipmentEvent(event);


        ShipmentTracking tracking = new ShipmentTracking();
        tracking.setShipmentId(saved.getId());
        tracking.setStatus(saved.getStatus());
        tracking.setTimestamp(LocalDateTime.now());
        trackingRepo.save(tracking);

        auditLogService.logAction(
                AuditAction.CREATE_TRACKING,
                "SHIPMENT TRACKING",
                saved.getId()
        );

        log.info("Shipment created successfully with id: {}", saved.getId());

        return new ApiResponse<>(
                ShipmentMapper.toDTO(saved),
                "Shipment created successfully"
        );
    }

    @Cacheable(value = "shipments", key = "#id")
    public ApiResponse<ShipmentDTO> getShipmentById(int id){

        log.info("Fetching shipment with id: {}", id);

        Shipment sp = repo.findById(id).orElseThrow(() -> {
            log.error("Shipment not found with id: {}", id);
            return new ShipmentNotFoundException();
        });

        return new ApiResponse<>(
                ShipmentMapper.toDTO(sp),
                "Shipment fetched successfully by id"
        );
    }

    @CacheEvict(value = "shipments", key = "#id")
    public ApiResponse<ShipmentDTO> updateShipmentById(int id,ShipmentDTO spo) {

        log.info("Updating Shipment with id: {}",id);

        Shipment sp = repo.findById(id).orElseThrow(()-> {
            log.error("Shipment not found with id: {}",id);
            return new ShipmentNotFoundException();
        });
        sp.setSource(spo.getSource());
        sp.setDestination(spo.getDestination());
        sp.setUpdatedAt(LocalDateTime.now());

        Shipment update = repo.save(sp);

        auditLogService.logAction(
                AuditAction.UPDATE_SHIPMENT,
                "SHIPMENT",
                update.getId()
        );

        log.info("Shipment Updated Successfully with id: {}",id);
        ShipmentDTO response = ShipmentMapper.toDTO(update);
        return new ApiResponse<>(
                response,
                "Shipment updated successfully by id"
        );
    }



    @CacheEvict(value = "shipments", key = "#id")
    public void deleteShipmentById(int id) {

        log.info("Deleting Shipment for id: {}",id);

        Shipment sp = repo.findById(id).orElseThrow(()->{
            log.error("Shipment not found with id: {}",id);
            return new ShipmentNotFoundException();
        });
        repo.delete(sp);
        auditLogService.logAction(
                AuditAction.DELETE_SHIPMENT,
                "SHIPMENT",
                id
        );
        log.info("Shipment deleted successfully for id:{}",id);
    }



    public List<ShipmentDTO> searchBySource(String source){

        log.info("Fetching Shipment with source: {}",source);

        if(source==null || source.trim().isEmpty()){
            log.error("Source cannot be empty");
            throw new IllegalArgumentException("Source cannot be empty");
        }
        List<Shipment> list = repo.findBySourceContainingIgnoreCase(source);
        List<ShipmentDTO> dtoList = new ArrayList<>();
        for(Shipment sp:list){
            ShipmentDTO dto = ShipmentMapper.toDTO(sp);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public ApiResponse<ShipmentDTO> updateStatus(int id, String status){

        log.info("Updating shipment status for id: {} to {}",id,status);
        Shipment sp = repo.findById(id).orElseThrow(()->{
            log.error("Shipment not found with id: {}",id);
            return new ShipmentNotFoundException();
        });
        ShipmentStatus newStatus;
        try{
            newStatus = ShipmentStatus.valueOf(status.toUpperCase());
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid status value");
        }
        if(sp.getStatus() == newStatus){
            throw new IllegalStateException("Shipment already in this status");
        }

        if(sp.getStatus() == ShipmentStatus.DELIVERED){
            throw new IllegalStateException("Cannot update delivered Shipment");
        }

        if(sp.getStatus()==ShipmentStatus.CREATED && newStatus == ShipmentStatus.DELIVERED){
            throw new IllegalStateException("Invalid status transition");
        }

        sp.setStatus(newStatus);
        sp.setUpdatedAt(LocalDateTime.now());

        Shipment update = repo.save(sp);

        auditLogService.logAction(
                AuditAction.UPDATE_SHIPMENT_STATUS,
                "SHIPMENT",
                id
        );

        ShipmentTracking tracking = new ShipmentTracking();
        tracking.setShipmentId(update.getId());
        tracking.setStatus(update.getStatus());
        tracking.setTimestamp(LocalDateTime.now());

        trackingRepo.save(tracking);

        auditLogService.logAction(
                AuditAction.UPDATE_TRACKING,
                "SHIPMENT TRACKING",
                update.getId()
        );

        log.info("Shipment status updated successfully for id: {}",id);

        return new ApiResponse<>(
                ShipmentMapper.toDTO(update),
                "Status updated successfully"
        );
    }

    public ApiResponse<List<ShipmentTrackingDTO>> getTracking(int id) {
        List<ShipmentTracking> list =  trackingRepo.findByShipmentIdOrderByTimestampAsc(id);

        List<ShipmentTrackingDTO> dtoList = new ArrayList<>();
        for(ShipmentTracking tracking:list){
            ShipmentTrackingDTO dto = ShipmentMapper.toDTO(tracking);
            dtoList.add(dto);
        }
        return new ApiResponse<>(
                dtoList,
                "Tracking fetched successfully"
        );
    }
}