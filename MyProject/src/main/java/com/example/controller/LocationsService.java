package com.example.controller;

import com.example.model.DBManagement.UserDao;
import com.example.model.Location;
import com.example.model.User;
import com.example.model.exceptions.LocationException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;
import com.example.model.repositories.LocationRepository;
import com.example.model.repositories.UserRepository;
import com.example.model.services.LocationService;
import com.example.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Marina on 5.11.2017 г..
 */
@RestController
@Controller
public class LocationsService {
    @Autowired
	UserService userService;

    @RequestMapping(value = "/getVisitedPlaces/{userId}",method = RequestMethod.GET)
    @ResponseBody
    public Collection<Location> getVisitedPlaces(HttpSession session,HttpServletResponse response,
                                                 @PathVariable("userId") long userId) throws IOException {

		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
		Collection<Location> locations = userService.findVisitedLocations(userId);
		response.setStatus(200);
        return locations;
    }


}