package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.Utilities.NewsfeedType;
import com.example.model.services.LocationService;
import com.example.model.services.MultimediaService;
import com.example.model.services.PostService;
import com.example.model.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.WebInitializer;
import com.example.model.Location;
import com.example.model.Post;
import com.example.model.User;

@Controller
public class ExploreController {
	@Autowired
	UserService userService;
	@Autowired
	LocationService locationService;
	@Autowired
	PostService postService;
	@Autowired
	MultimediaService multimediaService;

	@RequestMapping(value = "/searchAdventurers", method = RequestMethod.POST)
	public String searchAdventurers(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		//todo put them in model instead of session
		session.setAttribute("browsedAdventurers",
				userService.getFilteredUsers(request.getParameter("searchFormDataTxt")));
		return "exploreAdventurers";
	}

	@RequestMapping(value = "/showMostPopular", method = RequestMethod.POST)
	public String showMostPopularFirst(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		User currentUser = (User) session.getAttribute("user");
		Set<Post> newsfeedPosts = userService.findNewsFeed(currentUser, NewsfeedType.BY_MOST_POPULAR);
		session.setAttribute("newsfeedPosts", newsfeedPosts);
		return "newsfeed";
	}

	@RequestMapping(value = "/searchDestinations", method = RequestMethod.POST)
	public String searchDestinations(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		//todo put them in model
		session.setAttribute("browsedLocations",
				locationService.getFilteredLocations(request.getParameter("searchFormDataTxt")));
		return "exploreDestinations";
	}

	@RequestMapping(value = "/searchAdventures", method = RequestMethod.POST)
	public String searchAdventures(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		Set<Post> browsedAdventures = new TreeSet<Post>();
		ArrayList<String> checkBoxValues = new ArrayList<String>();
		checkBoxValues.add(request.getParameter("natureCheckBox")); // category_id must be '1' in db
		checkBoxValues.add(request.getParameter("seaCheckBox")); // category_id must be '2' in db
		checkBoxValues.add(request.getParameter("mountainsCheckBox")); // category_id must be '3' in db
		checkBoxValues.add(request.getParameter("dessertCheckBox")); // category_id must be '4' in db
		checkBoxValues.add(request.getParameter("landmarkCheckBox")); // category_id must be '5' in db
		checkBoxValues.add(request.getParameter("resortCheckBox")); // category_id must be '6' in db
		checkBoxValues.add(request.getParameter("cityCheckBox")); // category_id must be '7' in db
		String searchFormData = request.getParameter("searchFormDataTxt");
		browsedAdventures = postService.getFilteredPosts(searchFormData, checkBoxValues);
		//todo put them in model
		session.setAttribute("browsedAdventures", browsedAdventures);
		return "exploreAdventures";
	}

	@RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
	public String getLocationPage(@PathVariable("id") long id, HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		Location selectedLocation = locationService.findById(id);
		session.setAttribute("location", selectedLocation);
		return "location";
	}

	@RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
	public String getPostPage(@PathVariable("id") long id, HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		Post selectedPost = postService.findOne(id);
		session.setAttribute("post", selectedPost);
		return "post";
	}

	@RequestMapping(value = "/showPassport/{id}", method = RequestMethod.GET)
	public String getPassportPage(@PathVariable("id") long id, HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		User current = (User) session.getAttribute("user");
		current.setFollowing(userService.findAllFollowing(current));
		User selectedUser = userService.findById(id);
		session.setAttribute("selectedUser", selectedUser);
		request.setAttribute("thisFollowsSelected", current.follows(selectedUser));
		request.setAttribute("isMyPassport", current.equals(selectedUser));
		return "passport";
	}

	@RequestMapping(value = "/location/getMainPic/{id}", method = RequestMethod.GET)
	public void getLocationMainPic(HttpSession session,@PathVariable("id") long id, HttpServletResponse response) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
		try {
			Location selectedLocation = locationService.findById(id);
			String locationMainPicUrl = selectedLocation.getMainPic().getUrl();
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
					+ WebInitializer.LOCATIONS_PICTURES_LOCATION + File.separator + locationMainPicUrl);
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/post/getMainPic/{id}", method = RequestMethod.GET)
	public void getPostMainPic(HttpSession session,@PathVariable("id") long id, HttpServletResponse response) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
		try {
			Post selectedPost = postService.findOne(id);
			if (selectedPost.getMainPic() != null) {
				String postMainPicUrl = selectedPost.getMainPic().getUrl();
				File myFile = new File(
						WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION + File.separator + postMainPicUrl);
				OutputStream out = response.getOutputStream();
				Path path = myFile.toPath();
				Files.copy(path, out);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/location/picture/{id}", method = RequestMethod.GET)
	public void getLocationPicture(@PathVariable("id") long id,HttpSession session, HttpServletResponse response) throws IOException {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
					+ WebInitializer.LOCATIONS_PICTURES_LOCATION + File.separator
					+ multimediaService.findOne(id).getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/post/multimedia/{id}", method = RequestMethod.GET)
	public void getPostMultimediaFile(@PathVariable("id") long id, HttpSession session,HttpServletResponse response) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION + File.separator
					+ multimediaService.findOne(id).getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/user/picture/{id}", method = RequestMethod.GET)
	public void getUserPicture(@PathVariable("id") long id, HttpSession session,HttpServletResponse response) throws IOException {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.AVATAR_LOCATION + File.separator
					+ userService.findById(id).getProfilePic().getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}