package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

@RestController
@Controller
public class FollowService {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/follow/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] followUser(HttpSession session, HttpServletResponse resp, @PathVariable("userId") long userId)
			throws IOException {
		if (session.getAttribute("user") == null || session.getAttribute("logged").equals(false)) {
			resp.sendRedirect("login");
		}
		User follower = (User) session.getAttribute("user");
		User followed = userService.findById(userId);
		userService.follow(follower, followed);
		resp.setStatus(200);
		Integer[] sizes = new Integer[2];
		sizes[0] = followed.getFollowers().size();
		sizes[1] = followed.getFollowing().size();
		return sizes;
	}

	@RequestMapping(value = "/unfollow/{userId}", method = RequestMethod.POST)
	public Integer[] unfollowUser(HttpSession session, HttpServletResponse resp, @PathVariable("userId") long userId)
			throws IOException {
		if (session.getAttribute("user") == null || session.getAttribute("logged").equals(false)) {
			resp.sendRedirect("login");
		}
		User unfollower = (User) session.getAttribute("user");
		User unfollowed = userService.findById(userId);
		userService.unfollow(unfollower, unfollowed);
		resp.setStatus(200);
		Integer[] sizes = new Integer[2];
		sizes[0] = unfollowed.getFollowers().size();
		sizes[1] = unfollowed.getFollowing().size();
		return sizes;
	}

}