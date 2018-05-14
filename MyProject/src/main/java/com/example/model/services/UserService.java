package com.example.model.services;

import com.example.model.Location;
import com.example.model.User;
import com.example.model.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Transactional
    public User save(User user){
        user = userRepository.save(user);
        return user;
    }

    public Map<Timestamp,Location> getSortedVisitedLocations(Long userId) {
       // return UserRepository.findAllVisitedLocations
        return null;
    }
}
