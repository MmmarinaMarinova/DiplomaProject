package com.example.model.repositories;

import com.example.model.Location;
import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    User findByUsername(@Param("username") String username);

    boolean existsUsername(String username);

    //todo not the correct return type here
    @Query("SELECT u.visitedLocations FROM User u")
    Set<Location> findAllVisitedLocations(Long userId);

    @Query("SELECT u.posts FROM User u WHERE u.userId = :userId")
    Set<Post> findAllPosts(@Param("userId") Long userId);

    @Modifying
    @Query("Update User u SET u.password=:newPassword WHERE u.userId=:userId")
    void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    @Modifying
    @Query("Update User u SET u.password=:newPassword WHERE u.userId=:userId")
    void updateEmail(@Param("userId") Long userId, @Param("newEmail") String newEmail);

    @Modifying
    @Query("Update User u SET u.profilePic=:profilePic WHERE u.userId=:userId")
    void updateProfilePicId(@Param("userId") Long userId, @Param("profilePic") Multimedia profilePic);
    //TODO NOT SURE IF THIS WOULD WORK

    void updateDescription(long userId, String description);

    @Modifying
    @Query("Update User u INSERT INTO  u.profilePic=:profilePic WHERE u.userId=:userId")
    void insertFollower(User follower, User followed);

    @Modifying
    @Query("Update User u INSERT INTO  u.profilePic=:profilePic WHERE u.userId=:userId")
    void deleteFollower(User unfollower, User unfollowed);

    @Query("SELECT u.username FROM User u")
    HashSet<String> findAllUsernames();

    @Query("SELECT u.following FROM User u WHERE u.userId = :userId")
    HashSet<User> findAllFollowing(@Param("userId") Long userId);

}
