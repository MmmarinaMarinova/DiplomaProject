package com.example.controller;

import com.amdelamar.jhash.Hash;
import com.amdelamar.jhash.algorithms.Type;
import com.amdelamar.jhash.exception.BadOperationException;
import com.amdelamar.jhash.exception.InvalidHashException;
import com.example.WebInitializer;

import com.example.model.*;
import com.example.model.exceptions.*;
import com.example.model.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 26.10.2017 ?..
 */
@Controller
public class UserController {
	@Autowired
	ServletContext servletContext;
	@Autowired
	MultimediaService multimediaService;
	@Autowired
	TagService tagService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	LocationService locationService;
	@Autowired
	UserService userService;

	@RequestMapping(value = "*", method = RequestMethod.GET)
		public String getIndex(HttpSession session) {
			return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login2(HttpSession session) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String logUser(HttpSession session, HttpServletRequest request) {
		String username = request.getParameter("user");
		username=username.trim();
		String password = request.getParameter("pass");
		password=password.trim();

		if("".equals(username) || "".equals(password)){
			request.setAttribute("isValidData",false);
			return "login";
		}
		try {
			User user = userService.findByUsername(username);
			if (user != null) {
				if (Hash.verify(password, user.getPassword())) {
					session.setAttribute("user", user);
					session.setAttribute("logged", true);
					request.setAttribute("isValidData", true);
					Set<String> usernames = userService.findAllUsernames();
					Set<Tag> tags = tagService.findAll();
					Set<Category> categories = categoryService.findAll();
					Set<Location> locations = locationService.findAll();
					servletContext.setAttribute("locations", locations);
					servletContext.setAttribute("usernames", usernames);
					servletContext.setAttribute("tags", tags);
					servletContext.setAttribute("categories", categories);
					servletContext.setAttribute("categoryNames", categories);
					return "redirect:/showPassport/" + user.getUserId();
				}
			} else {
				request.setAttribute("isValidData", false);
				return "login";
			}
		} catch ( NoSuchAlgorithmException | BadOperationException | InvalidHashException e) {
			e.printStackTrace();
			return "login";
		}
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegister() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("user");
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");
		String email = request.getParameter("email");
		try {
			User test = new User(username, pass, email); //test if given data is correct
			if (pass != null && pass.equals(pass2)) {
					if (!userService.existsUsername(username)) {
						User user = new User(username, Hash.create(pass, Type.BCRYPT), email);
						userService.save(user);
						session.setAttribute("user", user);
						session.setAttribute("logged", true);
						return "redirect:/showPassport/" + user.getUserId();
					} else {
						return "register";
					}
			} else {
				request.setAttribute("doPasswordsMatch", false);
				return "register";
			}
		} catch ( NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "register";
		} catch (UserException | BadOperationException e) {
			if(e.getMessage().contains("Username")) {
				request.setAttribute("isValidUsername", false);
			}
			if(e.getMessage().contains("e-mail")) {
				request.setAttribute("isValidEmail", false);
			}
			if(e.getMessage().contains("Password")) {
				request.setAttribute("isValidPassword", false);
			}
			return "register";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		session.setAttribute("logged", false);
		session.invalidate();
		return "login";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String arrangeSettings(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeDescription", method = RequestMethod.GET)
	public String getChangeDescriptionForm(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeDescription", method = RequestMethod.POST)
	public String changeDescription(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		String newDescription = request.getParameter("descriptionTxt");
		userService.updateDescription((User) session.getAttribute("user"), newDescription);
		return "settings";
	}

	@RequestMapping(value = "/settings/changeEmail", method = RequestMethod.GET)
	public String getChangeEmailForm(HttpSession session, Model model) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		model.addAttribute("email", ((User) session.getAttribute("user")).getEmail());
		return "settings";
	}

	@RequestMapping(value = "/settings/changeEmail", method = RequestMethod.POST)
	public String changeEmail(HttpSession session, HttpServletRequest request,
			@Valid @ModelAttribute("email") String email, BindingResult result) throws UserException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		if (result.hasErrors()) {
			return "settings";
		} else {
			userService.updateEmail((User) session.getAttribute("user"), email);
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeAvatar", method = RequestMethod.GET)
	public String getAvatar(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/getAvatar", method = RequestMethod.GET)
	public void getChangeAvatar(HttpSession session, HttpServletResponse resp, Model model) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		User u = (User) session.getAttribute("user");
		String avatarUrl = u.getProfilePic().getUrl();
		try {
			File newAvatar = new File(
					WebInitializer.LOCATION + WebInitializer.AVATAR_LOCATION + File.separator + avatarUrl);
			OutputStream out = resp.getOutputStream();
			Path path = newAvatar.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/settings/changeAvatar", method = RequestMethod.POST)
	public String changeAvatar(HttpSession session, @RequestParam("avatar") MultipartFile file, Model model) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		User user = (User) session.getAttribute("user");
		String avatarUrl = user.getUsername();
		try {
			if (file.isEmpty()) {
				// TODO NOT SURE HERE
				return "settings";
			}
			File f = new File(WebInitializer.LOCATION + WebInitializer.AVATAR_LOCATION + File.separator + avatarUrl);
			file.transferTo(f);
			Multimedia newAvatar = new Multimedia(avatarUrl, false);
			userService.updateProfilePic(user, newAvatar); // insert in multimedia table and UPDATE USER HAVE THE NEWLY
			// INSERTED AVATAR
			// insert in user the new avatar
			session.setAttribute("avatar", avatarUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changePassword", method = RequestMethod.POST)
	public String changePassword(HttpSession session, HttpServletRequest request,@ModelAttribute("oldPassword") String oldPassword,
							  @Valid @ModelAttribute("newPassword") String newPassword,@ModelAttribute("confirmPassword") String confirmPassword,
								 BindingResult result) throws SQLException, BadOperationException, NoSuchAlgorithmException, InvalidHashException, UserException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		// TODO AJAX
		if (result.hasErrors()) {
			//TODO RETURN ERROR MESSAGE
			return "settings";
		} else {
			User user=(User)session.getAttribute("user");
			if(Hash.verify(oldPassword,user.getPassword())){
				if(newPassword.equals(confirmPassword)){
					String newPass=Hash.create(newPassword, Type.BCRYPT);
					userService.updatePassword(user, newPass);
				}
			}
		}
		return "settings";
	}

}