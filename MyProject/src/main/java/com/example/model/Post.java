package com.example.model;

import com.example.model.exceptions.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "posts")
public class Post implements Comparable<Post> {
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String description;

    private int likesCount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Multimedia video;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    private String dateTimeString;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="POSTS_CATEGORIES",
            joinColumns={@JoinColumn(name="POST_ID")},
            inverseJoinColumns={@JoinColumn(name="CATEGORY_ID")})
    private Set<Category> categories;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="POSTS_TAGS",
            joinColumns={@JoinColumn(name="POST_ID")},
            inverseJoinColumns={@JoinColumn(name="TAG_ID")})
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Set<Multimedia> multimedia;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="TAGGED_USERS",
            joinColumns={@JoinColumn(name="POST_ID")},
            inverseJoinColumns={@JoinColumn(name="USER_ID")})
    private Set<User> taggedPeople;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<Comment> comments = new TreeSet<>();

    @ManyToMany(cascade ={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(name="POSTS_LIKES",
            joinColumns={@JoinColumn(name="POST_ID")},
            inverseJoinColumns={@JoinColumn(name="USER_ID")})
    private Set<User> peopleLiked;

    public Post() {
    }

    // constructor to be used when putting object in database
    public Post(User user, String description,Multimedia video, Location location, Set<Category> categories,
                Set<Multimedia> multimedia, Set<User> taggedPeople, Set<Tag> tags) throws PostException {
        this.user = user;
        this.description = description;
        this.setDateTime(new Date(System.currentTimeMillis()));
        this.video = video;
        this.location = location;
        this.categories = categories;
        this.multimedia = multimedia;
        this.taggedPeople = taggedPeople;
        this.tags = tags;
        this.likesCount = 0;
        this.comments = new TreeSet<>();
        this.peopleLiked = new HashSet<>();
    }

    // constructor to be used when fetching from database
    public Post(long id, String description, int likesCount, int dislikesCount, Date dateTime) throws PostException {
        this.id = id;
        this.likesCount = likesCount;
        this.description = description;
        this.dateTime = dateTime;
        this.dateTimeString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(dateTime);
    }

    //not used constructor
    public Post(User user, String description, int likes_count, Date date_time,
                long location_id) throws PostException {
        this.dateTimeString= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(date_time);
    }

    public Multimedia getVideo() {
        return this.video;
    }

    public void setVideo(Multimedia video) {
        if(null != video){
            this.video = video;
        }
    }

    public Set<User> getPeopleLiked() {
        if(this.peopleLiked == null){
            this.peopleLiked = new HashSet<>();
        }

        return this.peopleLiked;
    }

    public void setPeopleLiked(Set<User> peopleLiked) {
        this.peopleLiked = peopleLiked;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(HashSet<Tag> tags) {
        if(tags != null){
            this.tags = tags;
        }
    }

    public void setDescription(String description) throws PostException {
        if(description.length() <= MAX_LENGTH){
            this.description = description;
        }
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        this.dateTimeString= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(dateTime);
    }

    public String getDateTimeString() {
        return  this.dateTimeString;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }

    public Set<Multimedia> getMultimedia() {
        return  this.multimedia;
    }

    public void setMultimedia(HashSet<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

    public Set<User> getTaggedPeople() {
        return this.taggedPeople;
    }
    
	public Multimedia getMainPic() {
		return this.multimedia != null && this.multimedia.iterator().hasNext() ? this.multimedia.iterator().next() : null;
	}

    public void setTaggedPeople(Set<User> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(TreeSet<Comment> treeSet) {
        this.comments = treeSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public int compareTo(Post o) {
        return o.dateTime.compareTo(this.dateTime);
    }

    ///////////////TODO METHODS TO BE MOVED TO SERVICE CLASS
    public void deleteMultimedia(Multimedia multimedia) {
        if (multimedia != null) {
            this.multimedia.remove(multimedia);
        }
    }

    public void tagUser(User user) {
        if (user != null && !this.taggedPeople.contains(user)) {
            this.taggedPeople.add(user);
        }
    }

    public void addCategory(Category category) {
        if (category != null && !this.categories.contains(category)) {
            this.categories.add(category);
        }
    }

    public void addComment(Comment c) {
        if (this.comments!=null && c!=null) {
        this.comments.add(c);
    }
    }

    public void deleteComment(Comment c) {
        if (c != null && this.comments.contains(c)) {
            this.comments.remove(c);
        }
    }

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
}