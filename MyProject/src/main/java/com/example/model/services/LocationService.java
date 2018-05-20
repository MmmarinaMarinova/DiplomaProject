package com.example.model.services;

import com.example.model.Location;
import com.example.model.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    @Transactional
    public HashSet<String> findAllLocationNames() {
        return locationRepository.findAllLocationNames();
    }

    @Transactional
    public HashSet<Location> findAll() {
        return new HashSet<>(locationRepository.findAll());
    }

    @Transactional
    public Location findById(long id) {
        return locationRepository.findOne(id);
    }
}
