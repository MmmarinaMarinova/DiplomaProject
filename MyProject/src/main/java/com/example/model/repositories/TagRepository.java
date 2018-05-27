package com.example.model.repositories;

import com.example.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

    @Query("SELECT t FROM Tag t WHERE t.tag_name = :tagName")
    Tag findByName(@Param("tagName") String tagName);
}
