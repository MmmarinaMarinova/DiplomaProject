package com.example.model.repositories;

import com.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
