package com.example.model;

import com.example.model.exceptions.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name="USERS")
public class User {
	// ::::::::: additional object characteristics :::::::::
	private static final int MIN_USERNAME_LENGTH = 5;
	private static final int MAX_USERNAME_LENGTH = 45;
	private static final int MIN_PASSWORD_LENGTH = 6;
	private static final int MAX_PASSWORD_LENGTH = 255;
	private static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*([0-9]|[\\W])).+$";
	private static final String EMAIL_VALIDATION_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	private static final String USERNAME_VALIDATION_REGEX = "([A-Za-z0-9-_]+)";
	public static Multimedia AVATAR=new Multimedia(0,"avatar.png",false, null);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;

	@Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
	@Pattern(regexp = USERNAME_VALIDATION_REGEX, message = "Username must be at least " + MIN_USERNAME_LENGTH
			+ " characters long and must contain only letters, digits, hyphens and underscores! ")
	private String username;

	@Pattern(regexp = PASSWORD_VALIDATION_REGEX, message = "Password must be at least " + MIN_PASSWORD_LENGTH
			+ " characters long and must contain at least one lowercase character, at least one uppercase character" +
			" and at least one non-alphabetic character!")
	@Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
	private String password;

	@Pattern(regexp = EMAIL_VALIDATION_REGEX, message = "Invalid email address")
	private String email;

	private String description = "";

	@OneToOne(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	private Multimedia profilePic;

	@ManyToMany(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(name = "USERS_FOLLOWERS",
            joinColumns = { @JoinColumn(name = "FOLLOWER_ID") },
			inverseJoinColumns = { @JoinColumn(name = "FOLLOWED_ID") } )
	private Set<User> followers = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(name = "USERS_FOLLOWERS",
            joinColumns = { @JoinColumn(name = "FOLLOWED_ID") },
			inverseJoinColumns = { @JoinColumn(name = "FOLLOWER_ID") } )
	private Set<User> following = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(name="VISITED_LOCATIONS",
			joinColumns={@JoinColumn(name="USER_ID")},
			inverseJoinColumns={@JoinColumn(name="LOCATION_ID")})
	private Set<Location> visitedLocations = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(name="WISHLISTS",
			joinColumns={@JoinColumn(name="USER_ID")},
			inverseJoinColumns={@JoinColumn(name="LOCATION_ID")})
	private Set<Location> wishlist = new HashSet<>();

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private Set<Post> posts = new TreeSet<>();

	public User() {
	}

	// ::::::::: constructor to be used for user registration :::::::::
	public User(String username, String password, String email) throws UserException {
		this.username = username;
		this.password = password;
		this.email = email;
//		this.setUsername(username);
//		this.setPassword(password);
//		this.setEmail(email);
		this.setProfilePic(AVATAR);
		//todo remove the avatar from here
	}

	public User(long userId, String username, Multimedia profilePic, String description) throws UserException {
		this.userId = userId;
		this.username = username;
		this.profilePic = profilePic;
		this.description = description;
//		this.setUserId(userId);
//		this.setUsername(username);
//		this.setProfilePic(profilePic);
//		this.setDescription(description);
	}

	// ::::::::: constructor to be used when loading an existing user from db
	public User(long userId, String username, String password, String email, Multimedia profilePic, String description)
			throws UserException {
		this(username, password, email);
		this.userId = userId;
		this.profilePic = profilePic;
		this.description = description;
//		this.setUserId(userId);
//		this.setProfilePic(profilePic);
//		this.setDescription(description);
	}

	public long getUserId() {
		return this.userId;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getDescription() {
		return this.description;
	}

	public Multimedia getProfilePic() {
		return this.profilePic;
	}

	public String getEmail() {
		return this.email;
	}

	public Set<User> getFollowers() {
		return this.followers;
	}

	public Set<User> getFollowing() {
		return this.following;
	}

	public Set<Location> getWishlist() {
		return this.wishlist;
	}

	public Set<Post> getPosts() {
		return this.posts;
	}

	public void setUserId(long userId) throws UserException {
		if (userId > 0) {
			this.userId = userId;
		} else {
			throw new UserException("Invalid user id!");
		}
	}

	public void setUsername(String username) throws UserException {
		if (username.length() >= MIN_USERNAME_LENGTH && username.matches(USERNAME_VALIDATION_REGEX)) {
			if (username.length() <= MAX_USERNAME_LENGTH) {
				this.username = username;
			} else {
				throw new UserException("Username too long!");
			}
		} else {
			throw new UserException("Username must be at least " + MIN_USERNAME_LENGTH
		+ " characters long and must contain only letters, digits, hyphens and underscores! ");
		}
	}

	public boolean setPassword(String password) throws UserException {
		if(validPassword(password)){
			this.password = password; //hashing required
			return true;
		}else{
			throw new UserException("Password too long!");
		}
	}

	private boolean validPassword(String password) throws UserException {
		if (password != null && password.length() >= MIN_PASSWORD_LENGTH
				&& (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])$")
				|| password.matches(PASSWORD_VALIDATION_REGEX))) {
			if (password.length() <= MAX_PASSWORD_LENGTH) {
				return true;
			} else {
				throw new UserException("Password too long!");
			}
		} else {
			throw new UserException("Password must be at least " + MIN_PASSWORD_LENGTH
					+ " characters long and must contain at least one lowercase character, at least one uppercase character and at least one non-alphabetic character!");
		}
	}

	public boolean setEmail(String email) throws UserException {
		if (email != null && email.matches(EMAIL_VALIDATION_REGEX)) {
			this.email = email;
			return true;
		} else {
			throw new UserException("Invalid e-mail address!");
		}
	}

	public void setDescription(String description) {
		this.description = description != null ? description : "";
	}

	public void setProfilePic(Multimedia profilePic) {
		this.profilePic = profilePic;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public void setFollowing(Set<User> following) {
		this.following = following;
	}

	public void setVisitedLocations(Set<Location> visitedLocations) {
		this.visitedLocations = visitedLocations;
	}

	public void setWishlist(Set<Location> wishlist) {
		this.wishlist = wishlist;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

    public boolean follows(User u) {
        return this.following != null && this.following.contains(u);
    }

	public void follow(User followed) {
		if (this.following == null) {
			this.following = new HashSet<>();
		}
		this.following.add(followed);
		if (followed.followers == null) {
			followed.followers = new HashSet<>();
		}
		followed.followers.add(this);
	}

	public void unfollow(User followed) {
		this.following.remove(followed);
		if (followed.followers != null) {
			followed.followers.remove(this);
		}
	}

	public void addVisitedLocation(Location location) {
		if (this.visitedLocations == null) {
			this.visitedLocations = new HashSet<>();
		}
		if(null != location){
			this.visitedLocations.add(location);
		}
	}

	public void removeVisitedLocation(Location location) {
	    if(null != location){
            this.visitedLocations.remove(location);
        }
	}

	public void addToWishlist(Location location) {
		if (this.wishlist == null) {
			this.wishlist = new HashSet<>();
		}
		this.wishlist.add(location);
	}

	public void removeFromWihslist(Location location) {
	    if( null != location){
            this.wishlist.remove(location);
        }
	}

	// ::::::::: add/remove from posts :::::::::
	public void addPost(Post p) {
		if (this.posts == null) {
			this.posts = new TreeSet<>();
		}
		if(null != p){
            this.posts.add(p);
        }
	}

	public void removePost(Post p) {
	    if(null != p){
            this.posts.remove(p);
        }
	}

    /**
     * Only "UserId" field is used for user distinction
     * duplicate usernames and emails must not be assigned
     * @return
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}
}