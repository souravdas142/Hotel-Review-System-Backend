package com.nav.rating.services.impl;

import com.nav.rating.entities.Rating;
import com.nav.rating.repositories.RatingRepository;
import com.nav.rating.services.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RatingServiceImpl implements RatingService {

    
    private RatingRepository ratingRepository;
    
    public RatingServiceImpl(RatingRepository ratingRepository) {
    	this.ratingRepository = ratingRepository;
    }
    
    @Override
    public Rating create(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getRatingByUserId(String userId) {
        return ratingRepository.findByUserId(userId);
    }

    @Override
    public List<Rating> getRatingByHotelId(String hotelId) {
        return ratingRepository.findByHotelId(hotelId);
    }
}
