package com.example.controller;


import com.example.model.Comment;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.services.CommService;
import com.example.model.services.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Marina on 2.11.2017 г..
 */
@RestController
@Controller
public class LikeService {
	@Autowired
	PostService postService;
	@Autowired
	CommService commService;

	@RequestMapping(value = "/like/{postId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer reactToPost(HttpSession session, Model model, HttpServletResponse resp,
							   @PathVariable("postId") long postId) throws IOException {
		if(session.getAttribute("user") == null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Post post = postService.findOne(postId);
		User user = ((User) session.getAttribute("user"));
		post = postService.reactToPost(post, user);
//todo set logic for changing the like icon in the front-end and remove dislike frontend functionality
		//            resp.setStatus(200);
		// resp.setStatus(201);
		return post.getPeopleLiked().size();
	}

	// :::::Comment like/dislike functionality methods:::::

	@RequestMapping(value = "/likeComment/{commentId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer reactToComment(HttpSession session, Model model, HttpServletResponse resp,
								  @PathVariable("commentId") long commentId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Comment comment = commService.findOne(commentId);
		User user = ((User) session.getAttribute("user"));
		comment = commService.reactToComment(comment, user);
//      resp.setStatus(200);
//      resp.setStatus(201);
//todo set logic for changing the like icon in the front-end and remove dislike frontend functionality
		return comment.getPeopleLiked().size();
	}
}