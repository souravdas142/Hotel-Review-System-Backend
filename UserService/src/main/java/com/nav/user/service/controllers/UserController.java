package com.nav.user.service.controllers;

import com.nav.user.service.entites.User;
import com.nav.user.service.services.UserService;
import com.nav.user.service.services.impl.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

   
    private UserService userService;

    private static Logger logger;

    //create
    
    public UserController(UserService userService) {
    	this.userService = userService;
    	logger = LoggerFactory.getLogger(UserController.class);
    }

    @PostMapping
    public ResponseEntity<User> createUser( @RequestBody User user) {
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount = 1;
//    Get Single user
    @GetMapping("/{userId}")
//    @CircuitBreaker(name = "ratingHotelBreaker" , fallbackMethod = "ratingHotelFallback")
//    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        logger.info("Get Single User Handler : UserController");
        logger.info("Retry count : {} " + retryCount);
        retryCount++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }


    // creating fallback method for circuit breakers
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
//        logger.info("Fallback is executed bcoz service is down : ", ex.getMessage());

        User user = User.builder()
                .email("dummy@gmail.com")
                .name("dummy")
                .about("This user is created dummy bcoz some service is down")
                .userId("00000")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

}
