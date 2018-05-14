package com.example.model.repositories;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
