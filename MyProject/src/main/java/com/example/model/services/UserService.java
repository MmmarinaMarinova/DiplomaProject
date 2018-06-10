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

import javax.servlet.ServletContext;
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
    @Autowired
    ServletContext servletContext;

    @Transactional
    public User save(User user){
        //TODO VALIDATIONS HERE
        if(null != user){
            user.setProfilePic((Multimedia) servletContext.getAttribute("profilePic"));
            user = userRepository.saveAndFlush(user);
        }
        return user;
    }

    @Transactional
    public boolean existsUsername(String username){
        return userRepository.existsUsername(username);
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
    public User updatePassword(User user, String newPassword) throws UserException {
        user.setPassword(newPassword);
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateEmail(User user, String newEmail) throws UserException {
        user.setEmail(newEmail);
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateDescription(User user, String description){
        if(description == null){
            user.setDescription("");
        }else{
            user.setDescription(description);
        }
        userRepository.saveAndFlush(user);
    }

    /**
     * This is a method for following a new user
     * It uses bidirectional addition in the follower's and followed's lists
     * @param follower
     * @param followed
     */
    @Transactional
    public void follow(User follower, User followed){
        this.addFollower(follower, followed);
        this.addFollowed(follower, followed);
        userRepository.saveAndFlush(follower);
    }

    private void addFollower(User follower, User followed) {
        if(null != follower && null != followed){
            followed.getFollowers().add(follower);
        }
    }

    private void addFollowed(User follower, User followed) {
        if(null != follower && null != followed) {
            follower.getFollowing().add(followed);
        }
    }

    @Transactional
    public void unfollow(User unfollower, User unfollowed) {
        this.removeFollower(unfollower, unfollowed);
        this.removeFollowed(unfollowed, unfollower);
        userRepository.saveAndFlush(unfollower);
    }

    private void removeFollowed(User unfollowed, User unfollower) {
        if(null != unfollower && null != unfollowed) {
            unfollower.getFollowing().remove(unfollowed);
        }
    }

    private void removeFollower(User unfollower, User unfollowed) {
        if(null != unfollower && null != unfollowed){
            unfollowed.getFollowers().remove(unfollower);
        }
    }

    @Transactional
    public Set<String> findAllUsernames() {
        return userRepository.findAllUsernames();
    }

    @Transactional
    public User updateProfilePic(User user, Multimedia profilePic) {
        if( null != user && null != profilePic){
            user.setProfilePic(profilePic);
            user = userRepository.saveAndFlush(user);
        }
        return user;
    }

    @Transactional
    public Set<User> findAllFollowing(User currentUser) {
        return userRepository.findAllFollowing(currentUser.getUserId());
    }

    @Transactional
    private Set<User> findAllFollowers(User user) {
        return userRepository.findAllFollowers(user.getUserId());
    }

    @Transactional
    public Set<Post> findNewsFeed(User currentUser, NewsfeedType type){
        Set<User> following = findAllFollowing(currentUser);
        TreeSet<Post> newsfeed = new TreeSet<>(type.getComparator());
        for (User user : following) {
            for (Post post : findAllPosts(user.getUserId())) {
                newsfeed.add(post);
            }
        }
//
//        following.stream().forEach(u -> {
//            findAllPosts(u.getUserId()).stream().forEach(p -> newsfeed.add(p));
//        });
        return newsfeed;
    }

    @Transactional
    public Collection<Location> findVisitedLocations(long userId) {
        return userRepository.findAllVisitedLocations(userId);
    }

    public Set<User> getTaggedUsers(String taggedPeople) {
        Set<User> users = new HashSet<>();
        if (!"".equals(taggedPeople)) {
            String[] splitUsernames = taggedPeople.split(",");
            for (int i = 0; i < splitUsernames.length; i++) {
                String currentUsername = splitUsernames[i];
                currentUsername = currentUsername.replace("]", "");
                currentUsername = currentUsername.replace("[", "");
                currentUsername = currentUsername.trim();
                if (!"".equals(currentUsername)) {
                    if (userRepository.existsUsername(currentUsername)) {
                        User user = userRepository.findByUsername(currentUsername);
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    /**
     * This is a method that returns a set of users according to input search text
     * The users are returned if they have the input text in their description or their username
     * @param searchFormDataTxt input text
     * @return Set<User>
     */
    @Transactional
    public Set<User> getFilteredUsers(String searchFormDataTxt) {
        if(searchFormDataTxt != null && !searchFormDataTxt.isEmpty()){
            return userRepository.findFilteredUsers(searchFormDataTxt);
        }
        return Collections.emptySet();
    }


    @Transactional
    public User findByIdAndGetFollowing(long id) {
        User user = this.findById(id);
        user.setFollowing(this.findAllFollowing(user));
        user.setFollowers(this.findAllFollowers(user));
        return user;
    }


    @Transactional
    public Multimedia findProfilePic(User user) {
        return userRepository.findPoriflePic(user);
    }
}
