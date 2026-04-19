
package com.example.logichain.service;

import com.example.logichain.ShipmentNotFoundException;
import com.example.logichain.dto.ShipmentDTO;
import com.example.logichain.mapper.ShipmentMapper;
import com.example.logichain.model.Shipment;
import com.example.logichain.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository repo;

    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    public List<ShipmentDTO> getAllShipments(int page, int size, String sortBy, String source){

        log.info("Fetching shipment - page: {}, size: {}, sortBy: {}, source: {}", page, size, sortBy, source);

        page = Math.max(page,0);
        size = Math.max(size,1);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Shipment> shipmentPage;

        if(source == null || source.trim().isEmpty()){
            shipmentPage = repo.findAll(pageable);
        } else {
            shipmentPage = repo.findBySourceContainingIgnoreCase(source, pageable);
        }

        List<ShipmentDTO> list = new ArrayList<>();

        for(Shipment sp : shipmentPage.getContent()){
            list.add(ShipmentMapper.toDTO(sp));
        }

        return list;
    }

    public ShipmentDTO saveShipment(ShipmentDTO dto){

        log.info("Creating shipment with source: {} and destination: {}", dto.getSource(), dto.getDestination());

        Shipment sp = ShipmentMapper.toEntity(dto);
        sp.setStatus("CREATED");

        Shipment saved = repo.save(sp);

        log.info("Shipment created successfully with id: {}", saved.getId());

        return ShipmentMapper.toDTO(saved);
    }

    public ShipmentDTO getShipmentById(int id){

        log.info("Fetching shipment with id: {}", id);

        Shipment sp = repo.findById(id).orElseThrow(() -> {
            log.error("Shipment not found with id: {}", id);
            return new ShipmentNotFoundException();
        });

        return ShipmentMapper.toDTO(sp);
    }

    public ShipmentDTO updateShipmentById(int id,ShipmentDTO spo) {

        log.info("Updating Shipment with id: {}",id);

        Shipment sp = repo.findById(id).orElseThrow(()-> {
            log.error("Shipment not found with id: {}",id);
            return new ShipmentNotFoundException();
        });
        sp.setSource(spo.getSource());
        sp.setDestination(spo.getDestination());
        Shipment update = repo.save(sp);
        log.info("Shipment Updated Successfully with id: {}",id);
        ShipmentDTO response = ShipmentMapper.toDTO(update);
        return response;
    }



    public void deleteShipmentById(int id) {

        log.info("Deleting Shipment for id: {}",id);

        Shipment sp = repo.findById(id).orElseThrow(()->{
            log.error("Shipment not found with id: {}",id);
            return new ShipmentNotFoundException();
        });
        repo.delete(sp);
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

}