package com.example.model.services;

import com.example.model.Location;
import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.Utilities.NewsfeedType;
import com.example.model.exceptions.UserException;
import com.example.model.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    LocationService locationService;
    @Autowired
    MultimediaService multimediaService;

    @Transactional
    public User save(User user){
        //TODO VALIDATIONS HERE
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public boolean existsUsername(String username){
        return userRepository.existsUsername(username);
    }

    @Transactional
    public Map<Timestamp,Location> getSortedVisitedLocations(Long userId) {
            //return userRepository.findAllVisitedLocations(userId);
        return null;
    }

    @Transactional
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User findById(Long userId){
        return userRepository.findOne(userId);
    }

    @Transactional
    public Set<Post> findAllPosts(Long userId){
        return userRepository.findAllPosts(userId);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) throws UserException {
        //TODO VALIDATIONS HERE
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(User user, String newEmail) throws UserException {
        user.setEmail(newEmail);
        userRepository.save(user);
        //TODO VALIDATIONS HERE
    }

    @Transactional
    public void updateDescription(User user, String description){
        userRepository.updateDescription(user.getUserId(), description);
    }

    @Transactional
    public void follow(User follower, User followed){
        this.addFollower(follower, followed);
        this.addFollowed(follower, followed);
        userRepository.save(follower);
    }

    private void addFollower(User follower, User followed) {
        followed.getFollowers().add(follower);
    }

    private void addFollowed(User follower, User followed) {
        follower.getFollowing().add(followed);
    }

    @Transactional
    public void unfollow(User unfollower, User unfollowed) {
        this.removeFollower(unfollower, unfollowed);
        this.removeFollowed(unfollowed, unfollower);
        userRepository.save(unfollower);
    }

    private void removeFollowed(User unfollowed, User unfollower) {
        unfollower.getFollowing().remove(unfollowed);
    }

    private void removeFollower(User unfollower, User unfollowed) {
        unfollowed.getFollowers().remove(unfollower);
    }

    @Transactional
    public Set<String> findAllUsernames() {
        return userRepository.findAllUsernames();
    }
    @Transactional
    public void updateProfilePic(User user, Multimedia profilePic) {
        //todo transaction needed here
        profilePic = multimediaService.save(profilePic);
        userRepository.updateProfilePicId(user.getUserId(), profilePic);
    }

    @Transactional
    public Set<User> findAllFollowing(User currentUser) {
        return userRepository.findAllFollowing(currentUser.getUserId());
    }

    @Transactional
    public Set<Post> findNewsFeed(User currentUser, NewsfeedType type){
        Set<User> following = findAllFollowing(currentUser);
        TreeSet<Post> newsfeed = new TreeSet<>(type.getComparator());
        for (User user : following) {
            newsfeed.addAll(findAllPosts(user.getUserId()));
        }
        return newsfeed;
    }

//    @Transactional
//    public TreeSet<Post> findNewsFeedByTime(User currentUser) {
//        HashSet<User> following = findAllFollowing(currentUser);
//        TreeSet<Post> newsfeed = new TreeSet<>(new Comparator<Post>() {
//            @Override
//            public int compare(Post o1, Post o2) {
//                return o1.getDateTime().compareTo(o2.getDateTime());
//            }
//        });
//        for (User user : following) {
//            newsfeed.addAll(findAllPosts(user.getUserId()));
//        }
//        return newsfeed;
//    }

    @Transactional
    public Collection<Location> findVisitedLocations(long userId) {
        return userRepository.findAllVisitedLocations(userId);
    }

}
