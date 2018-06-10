package com.example.model.repositories;

import com.example.model.Location;
import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean existsUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u.visitedLocations FROM User u WHERE u.userId = :userId")
    Set<Location> findAllVisitedLocations(@Param("userId") Long userId);

    @Query("SELECT u.posts FROM User u WHERE u.userId = :userId")
    Set<Post> findAllPosts(@Param("userId") Long userId);

    @Query("SELECT u.username FROM User u")
    Set<String> findAllUsernames();

    @Query("SELECT u.following FROM User u WHERE u.userId = :userId")
    Set<User> findAllFollowing(@Param("userId") Long userId);

    @Query("SELECT DISTINCT u FROM User u WHERE u.username LIKE CONCAT('%',:searchFormDataTxt,'%') OR u.description LIKE CONCAT('%',:searchFormDataTxt,'%')")
    Set<User> findFilteredUsers(@Param("searchFormDataTxt") String searchFormDataTxt);

    @Query("SELECT u.followers FROM User u WHERE u.userId = :userId")
    Set<User> findAllFollowers(@Param("userId") long userId);

    @Query("SELECT u.profilePic FROM User u WHERE u = :user")
    Multimedia findPoriflePic(@Param("user") User user);
}
