package com.nav.user.service.repositories;


import com.nav.user.service.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
//    if you want to write custom queries you can write
}
