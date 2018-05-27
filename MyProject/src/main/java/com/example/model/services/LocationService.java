package com.example.model.services;

import com.example.model.Location;
import com.example.model.exceptions.LocationException;
import com.example.model.repositories.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    @Transactional
    public Set<String> findAllLocationNames() {
        return locationRepository.findAllLocationNames();
    }

    @Transactional
    public Set<Location> findAll() {
        return new HashSet<>(locationRepository.findAll());
    }

    @Transactional
    public Location findById(long id) {
        return locationRepository.findOne(id);
    }

    @Transactional
    public Location findOne(String locationName, String latitude, String longtitude) throws LocationException {
        if(locationName == null && latitude == null && longtitude ==null){
            return null;
        }
        Location location = locationRepository.findByLocationData(locationName, latitude, longtitude);
        if(location == null){
            return new Location(latitude, longtitude, "", locationName);
        }else {
            return location;
        }
    }

    @Transactional
    public Object getFilteredLocations(String searchFormDataTxt) {
        if (searchFormDataTxt != null && !searchFormDataTxt.isEmpty()) {
            return locationRepository.findFilteredLocations(searchFormDataTxt);
        }
        return Collections.emptySet();
        //todo not sure if should return this or null
    }
}
