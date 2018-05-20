package com.example.model.repositories;

import com.example.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l.locationName FROM Location l")
    HashSet<String> findAllLocationNames();
}
