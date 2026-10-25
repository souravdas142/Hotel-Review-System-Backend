package com.nav.hotel.services.impl;

import com.nav.hotel.entities.Hotel;
import com.nav.hotel.exceptions.ResourceNotFoundException;
import com.nav.hotel.repositories.HotelRepository;
import com.nav.hotel.services.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

   
    private HotelRepository hotelRepository;
    
    public HotelServiceImpl(HotelRepository hotelRepository) {
    	this.hotelRepository = hotelRepository;
    }

    @Override
    public Hotel create(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with given ID : " + id));
    }
}
