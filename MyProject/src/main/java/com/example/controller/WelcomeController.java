package com.example.controller;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.model.Utilities.NewsfeedType;
import com.example.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.Post;
import com.example.model.User;
/**
 * Created by Marina on 27.10.2017 г..
 */

@Controller
public class WelcomeController {
	@Autowired
	UserService userService;

	// TODO - INDEX PAGE MUST BE SHOWN FIRST
	@RequestMapping(value = "/wanderlust", method = RequestMethod.GET)
	public String getWelcomePage(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "index";
	}

	@RequestMapping(value = "/myPassport", method = RequestMethod.GET)
	public String getMyPassport(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		long userId = ((User) session.getAttribute("user")).getUserId();
		return "redirect:/showPassport/" + userId;
	}

	@RequestMapping(value = "/newsfeed", method = RequestMethod.GET)
	public String showNewsfeed(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		User currentUser = (User) session.getAttribute("user");
		TreeSet<Post> newsfeedPosts = userService.findNewsFeed(currentUser, NewsfeedType.BY_TIME);
		session.setAttribute("newsfeedPosts", newsfeedPosts);
		return "newsfeed";
	}

}