package com.example.model;

import com.example.model.exceptions.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment implements Comparable<Comment> {
	private static final int MAX_CONTENT_LENGTH = 500;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String content;
	//todo maybe remove likes and dislikes count -> can take them from people liked.size
	private int likesCount = 0;
	private int dislikesCount = 0;
	@ManyToOne
	@JoinColumn(name = "POST_ID")
	private Post post;
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User sentBy;
	//private long userId = 0;
	private Timestamp datetime;
	//todo string is not needed since we can have method in service class that returns it
	private String datetimeString;
	@ManyToMany
	@JoinTable(name="COMMENTS_REACTIONS",
			joinColumns={@JoinColumn(name="COMMENT_ID")},
			inverseJoinColumns={@JoinColumn(name="USER_ID")}) //LIKE REACTION = TRUE
    private Set<User> peopleLiked;
	@ManyToMany
	@JoinTable(name="COMMENTS_REACTIONS",
			joinColumns={@JoinColumn(name="COMMENT_ID")},
			inverseJoinColumns={@JoinColumn(name="USER_ID")}) //DISLIKE REACTION = FALSE
    private Set<User> peopleDisliked;

	public Comment() {
	}

	// ::::::::: constructors to be used when posting a new comment :::::::::
	public Comment(String content, Post post, User sentBy){
		this.setContent(content);
		this.setPost(post);
		//this.setUserId(userId);
		this.setSentBy(sentBy);
	}

	public Comment(long id, String content, int likesCount, int dislikesCount, Post post, long userId,
			Timestamp datetime){
		this.setContent(content);
		this.setPost(post);
		//this.setUserId(userId);
		this.setId(id);
		this.setLikesCount(likesCount);
		this.setDislikesCount(dislikesCount);
		this.setDatetime(datetime);
		this.datetimeString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(datetime);
	}

	public String getDatetimeString() {
		return this.datetimeString;
	}

	public void setDatetimeString(String datetimeString) {
		this.datetimeString = datetimeString;
	}

	// ::::::::: constructor to be used when loading an existing comment from db
	public Comment(long id, String content, int likesCount, int dislikesCount, Post post, long userId,
			Timestamp datetime, User sentBy){
		this(content, post, sentBy);
		this.setId(id);
		this.setLikesCount(likesCount);
		this.setDislikesCount(dislikesCount);
		this.setDatetime(datetime);
		this.datetimeString=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(datetime);
		this.setSentBy(sentBy);
	}

	// ::::::::: accessors :::::::::
	public long getId() {
		return this.id;
	}

	public String getContent() {
		return this.content;
	}

	public int getLikesCount() {
		return this.likesCount;
	}

	public int getDislikesCount() {
		return this.dislikesCount;
	}

	public Post getPost() {
		return this.post;
	}

	public Timestamp getDatetime() {
		return this.datetime;
	}

	public User getSentBy() {
		return this.sentBy;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setContent(String content) {
		if (content != null) {
			if (content.length() <= MAX_CONTENT_LENGTH) {
				this.content = content;
			}
		}
	}

	public void setLikesCount(int likesCount)  {
		if (likesCount >= 0) {
			this.likesCount = likesCount;
		}
	}

	public void setDislikesCount(int dislikesCount){
		if (dislikesCount >= 0) {
			this.dislikesCount = dislikesCount;
		}
	}

	public void setPost(Post post) {
		if (post!=null) {
			this.post = post;
		}
	}

	public void setDatetime(Timestamp datetime){
		if (datetime != null) {
			this.datetime = datetime;
			this.datetimeString= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(datetime);
		}
	}

	public void setSentBy(User sentBy) {
		if (sentBy != null) {
			this.sentBy = sentBy;
		}
	}

	public void incrementLikes() {
		this.setLikesCount(this.likesCount + 1);
	}

	public void incrementDislikes() {
		this.setDislikesCount(this.dislikesCount + 1);
	}

	@Override
	public int compareTo(Comment c) {
		return c.getDatetime().compareTo(this.getDatetime());
	}

	//::::::::: like/dislike functionality :::::::::
	public void addPersonLiked(User user) {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }
       this.peopleLiked.add(user);
    }

    public void removePersonLiked(User user) {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }
        this.peopleLiked.remove(user);
    }

    public void removePersonDisliked(User user) {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        this.peopleDisliked.remove(user);
    }

    public void addPersonDisliked(User user) {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        this.peopleDisliked.add(user);
    }
	
    public Set<User> getPeopleLiked() {
        return Collections.unmodifiableSet(this.peopleLiked);
    }

    public void setPeopleLiked(HashSet<User> peopleLiked) {
        this.peopleLiked = peopleLiked;
    }

    public Set<User> getPeopleDisliked() {
        return Collections.unmodifiableSet(this.peopleDisliked);
    }

    public void setPeopleDisliked(HashSet<User> peopleDisliked) {
        this.peopleDisliked = peopleDisliked;
    }
    
}