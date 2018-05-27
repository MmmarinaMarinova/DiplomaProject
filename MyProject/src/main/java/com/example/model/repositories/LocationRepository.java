package com.example.model.repositories;

import com.example.model.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l.locationName FROM Location l")
    Set<String> findAllLocationNames();

    @Query("SELECT l from Location l WHERE l.locationName = :locationName AND " +
            "l.latitude = :latitude AND l.longtitude = :longtitude")
    Location findByLocationData(@Param("locationName") String locationName,
                                @Param("latitude") String latitude,
                                @Param("longtitude") String longtitude);

    @Query("SELECT l FROM Location l WHERE l.locationName LIKE :searchFormDataTxt OR l.description LIKE :searchFormDataTxt")
    Set<Location> findFilteredLocations(@Param("searchFormDataTxt") String searchFormDataTxt);
}
