package com.example.model.repositories;

import com.example.model.Comment;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface CommRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Comment c JOIN c.peopleLiked pl WHERE pl = :user AND c.id = :commentId")
    boolean existsReaction(@Param("commentId") long commentId, @Param("user") User user);
}
