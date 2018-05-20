package com.example.model.repositories;

import com.example.model.Multimedia;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {

    @Modifying
    @Query("Update User u SET u.password=:newPassword WHERE u.userId=:userId")
    void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
}
