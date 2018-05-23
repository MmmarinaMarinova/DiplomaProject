package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.Post;
import com.example.model.services.CommService;
import com.example.model.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Comment;
import com.example.model.User;
import com.example.model.DBManagement.CommentDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.CommentException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

@Controller
@RestController
public class CommentService {
	@Autowired
	PostService postService;
	@Autowired
	CommService commService;

	@RequestMapping(value = "/postComment/{postId}/{content}", method = RequestMethod.POST)
	@ResponseBody
	public Comment postComment(HttpSession session, HttpServletResponse resp, @PathVariable("postId") long postId,
							   @PathVariable("content") String content) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		User sentBy = (User) session.getAttribute("user");
		Post post = postService.findOne(postId);
		Comment comment = new Comment(content, post, sentBy);
		comment = commService.saveComment(comment);
		resp.setStatus(200);
		return comment;
	}

}