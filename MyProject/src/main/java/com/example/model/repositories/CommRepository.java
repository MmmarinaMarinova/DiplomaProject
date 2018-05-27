package com.example.model.repositories;

import com.example.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface CommRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Comment c WHERE c.id = :commentId AND c.sentBy.userId = :userId")
    boolean existsReaction(@Param("commentId") long commentId, @Param("userId") long userId);
}
