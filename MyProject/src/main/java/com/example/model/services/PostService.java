package com.example.model.services;

import com.example.model.*;
import com.example.model.repositories.PostRepository;

import com.example.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Marina on 12.5.2018 г..
 */
@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Autowired
    MultimediaService multimediaService;

    @Transactional
    public Post findOne(long postId) {
        return postRepository.findOne(postId);
    }

    @Transactional
    public boolean existsReaction(Post post, User user) {
        return postRepository.existsReaction(post.getId(), user);
    }

    @Transactional
    public Post reactToPost(Post post, User user) {
        user = userService.findById(user.getUserId());
        if (existsReaction(post, user)) {
            post.getPeopleLiked().remove(user);
        } else {
            post.getPeopleLiked().add(user);
        }
        return postRepository.saveAndFlush(post);
    }

    @Transactional
    public Post save(Post post, User user, String description, Multimedia video, Location location, Set<Category> categories, Set<Multimedia> multimedia, Set<User> taggedUsers, Set<Tag> tags) {
        if(multimedia != null && multimedia.size() > 0){
            for (Multimedia m : multimedia) {
                m.setPost(post);
                multimediaService.save(m);
            }        }
        if(video != null){
            video.setPost(post);
            multimediaService.save(video);
        }
        post.setUser(user);
        return postRepository.saveAndFlush(post);
    }

    @Transactional
    public Set<Post> getFilteredPosts(String searchFormDatatxt, ArrayList<String> categoriesIds) {
        ArrayList<Category> categories = getCategoriesFromIds(categoriesIds);
        if (searchFormDatatxt == null || searchFormDatatxt.isEmpty()) {
            if (categoriesIds == null || categories.size() == 0) {
                return Collections.emptySet();
            }else{
                return this.findPostsByCategory(categories);
            }
        }else{
            if (categoriesIds == null || categories.size() == 0) {
                return this.findPostsBySearchText(searchFormDatatxt);
            }else{
                return this.findPostsBySearchAndCategory(searchFormDatatxt,categories);
        }
    }
}

    @Transactional
    private TreeSet<Post> findPostsBySearchAndCategory(String searchFormDatatxt,
                                                   ArrayList<Category> categories) {
        Set<Post> filteredPosts = this.findPostsByCategory( categories);
        filteredPosts = filteredPosts.stream()
                .filter(p -> p.getDescription().contains(searchFormDatatxt))
                .collect(Collectors.toSet());
        return new TreeSet<>(filteredPosts);
        //todo marmalad mahai go toq method ujas prosto
    }

    /**
     * This is a method that searches in post's tags and description according to the passed text
     * @param searchFormDatatxt the passed text
     * @return
     */
    @Transactional
    private TreeSet<Post> findPostsBySearchText(String searchFormDatatxt) {
        return postRepository.findPostsBySearch(searchFormDatatxt);
    }

    private ArrayList<Category> getCategoriesFromIds(ArrayList<String> categoriesIds) {
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i < categoriesIds.size(); i++) {
            if (categoriesIds.get(i) != null && categoriesIds.get(i).equals("true")) {
                Category category = categoryService.findOne(new Long(i+1));
                categories.add(category);
            }
        }
        return categories;
    }

    @Transactional
    private TreeSet<Post> findPostsByCategory(ArrayList<Category> categories) {
        TreeSet<Post> postsByCategory = new TreeSet<>();
        for (Category category : categories) {
            postsByCategory.addAll(postRepository.findPostsByCategory(category));
        }
        return postsByCategory;
    }
    }
