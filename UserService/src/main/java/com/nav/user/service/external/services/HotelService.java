package com.nav.user.service.external.services;


import com.nav.user.service.entites.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Name should be same as the service name
@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {


    @GetMapping("/hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") String HotelId);

}
