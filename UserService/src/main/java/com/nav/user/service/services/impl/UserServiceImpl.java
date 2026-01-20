package com.nav.user.service.services.impl;

import com.nav.user.service.entites.Hotel;
import com.nav.user.service.entites.Rating;
import com.nav.user.service.entites.User;
import com.nav.user.service.exceptions.ResourceNotFoundException;
import com.nav.user.service.external.services.HotelService;
import com.nav.user.service.repositories.UserRepository;
import com.nav.user.service.services.UserService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private static  Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public User saveUser(User user) {
        //generate  unique userid
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        //implement RATING SERVICE CALL: USING REST TEMPLATE
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {

//        Get user from database with the help of user repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given ID : " + userId));

//        fetch rating of the above user from RATING SERVICE

        logger.info("Fetching user details for userId: {}", userId);


        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);

        List<Rating> ratings= Arrays.stream(ratingsOfUser).toList();

        logger.info("{}", ratingsOfUser);

        List<Rating> ratingList = ratings.stream().map(rating -> {

            // api call to hotel service to get hotel details
            // http://localhost:8082/hotels/37f71683-8e1e-4f2b-affe-a37cdc774864
            // ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            // Hotel hotel = forEntity.getBody();

            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            // logger.info("Response Status Code: {}",forEntity.getStatusCode().toString());
            // set hotel details in rating object
            rating.setHotel(hotel);

            //return rating
            return rating;

        }).collect(Collectors.toList());

        user.setRatings(ratingList);

//        user.setRatings(ratingsOfUser);

        return user;
    }
}
