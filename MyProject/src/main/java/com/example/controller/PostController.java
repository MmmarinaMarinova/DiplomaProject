package com.example.controller;

import com.example.model.*;
import com.example.model.exceptions.*;
import com.example.model.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.util.*;

/**
 * Created by Marina on 12.05.2018 ?..
 */
@Controller
public class PostController {
    @Autowired
    ServletContext servletContext;
    @Autowired
    PostService postService;
    @Autowired
    TagService tagService;
    @Autowired
    LocationService locationService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    MultimediaService multimediaService;

    @RequestMapping(value = "/uploadPost", method = RequestMethod.GET)
    public String getUploadPostForm(HttpSession session,HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
            return "login";
        }
        return "uploadPost";
    }

    @RequestMapping(value = "/uploadPost", method = RequestMethod.POST)
    public String uploadPost(@RequestParam("description") String description, @RequestParam("locationName") String locationName,
                    @RequestParam("latitude") String latitude, @RequestParam("longtitude") String longtitude,
                    @RequestParam("taggedPeople") String taggedPeople, @RequestParam("tags") String tagNames,
                    @RequestParam("categories") String categoryNames, @RequestParam("image1") MultipartFile image1,
                    @RequestParam("image2") MultipartFile image2, @RequestParam("image3") MultipartFile image3,
                    @RequestParam("video") MultipartFile video1, HttpSession session){

        if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
            return "login";
        }
        if(parametersAreNull(description, locationName, latitude, longtitude, taggedPeople, tagNames, categoryNames,
                image1, image2, image3, video1)){
            return "redirect:/myPassport";
        }

        Post post = null;
        try {
            User user=(User)session.getAttribute("user");
            Location location = locationService.findOne(locationName, latitude, longtitude);
            Set<User> taggedUsers = userService.getTaggedUsers(taggedPeople);
            Set<Tag> tags = tagService.getTags(tagNames);
            Set<Category> categories = categoryService.getCategories(categoryNames);
            Set<Multimedia> multimedia = multimediaService.getImages(image1, image2, image3);
            Multimedia video = multimediaService.getVideo(video1);

            if(location == null && taggedUsers.size() == 0 && categories.size() == 0 && tags.size() == 0
                    && multimedia.size() == 0 && video == null){
                return "redirect:/myPassport";
            }

            post = new Post(user, description, video, location, categories, multimedia, taggedUsers, tags);
            postService.save(post, user,description, video, location, categories, multimedia, taggedUsers, tags);
        } catch (PostException | LocationException | CategoryException e) {
            e.printStackTrace();
        }
        return "redirect:/myPassport";
    }

    private boolean parametersAreNull(String description, String locationName, String latitude,
                                      String longtitude, String taggedPeople, String tagNames, String categoryNames,
                                      MultipartFile image1, MultipartFile image2, MultipartFile image3,
                                      MultipartFile video1){
        return ("".equals(description) && "".equals(locationName) && "".equals(latitude) && "".equals(longtitude)
                && "".equals(taggedPeople) && "".equals(tagNames) && "".equals(categoryNames)
                && image1.getSize() == 0 && image2.getSize() == 0 && image3.getSize() == 0 && video1.getSize() == 0);
    }


}
