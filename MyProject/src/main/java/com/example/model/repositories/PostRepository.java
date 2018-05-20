package com.example.model.repositories;

import com.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Post p WHERE p.id = :postId AND p.user.userId = :userId")
    boolean existsReaction(@Param("postId") long postId, @Param("userId") long userId);
}
