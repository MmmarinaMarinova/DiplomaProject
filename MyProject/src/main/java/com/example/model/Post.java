package com.example.model;

import com.example.model.exceptions.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
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
    @OneToOne
    private User user;
    private String description;
    private int likesCount;
    private int dislikesCount;
    @OneToOne
    private Multimedia video;
    private Timestamp dateTime;
    private String dateTimeString;
    @OneToOne
    private Location location;
    @OneToMany(targetEntity = Category.class)
    private Set<Category> categories;
    @OneToMany(targetEntity = Tag.class)
    private Set<Tag> tags;
    @OneToMany(targetEntity = Multimedia.class)
    private Set<Multimedia> multimedia;
    @OneToMany(targetEntity = User.class)
    private Set<User> taggedPeople;
    @OneToMany(targetEntity = Comment.class)
    private Set<Comment> comments; //treeset
    @OneToMany
    private Set<Long> peopleLiked;
    private Set<Long> peopleDisliked;
    //TODO Set<Long>

    public Post() {
    }

    // constructor to be used when putting object in database
    public Post(User user, String description,Multimedia video, Location location, HashSet<Category> categories,
                HashSet<Multimedia> multimedia, HashSet<User> taggedPeople, HashSet<Tag> tags) throws PostException {
        this.user = user;
        this.description=description;
        this.video=video;
        this.location = location;
        this.categories = categories;
        this.multimedia = multimedia;
        this.taggedPeople = taggedPeople;
        this.tags=tags;
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.comments=new TreeSet<>();
        this.peopleDisliked=new HashSet<>();
        this.peopleLiked=new HashSet<>();
    }

    // constructor to be used when fetching from database
    public Post(long id, String description, int likesCount, int dislikesCount, Timestamp dateTime) throws PostException {
        this.id = id;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.description=description;
        this.dateTime = dateTime;
        this.dateTimeString= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(dateTime);
    }

    public Post(User user, long user_id, String description, int likes_count, int dislikes_count, Timestamp date_time,
                long location_id) throws PostException {
        this.dateTimeString= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(date_time);
    }

    public Multimedia getVideo() {
        return this.video;
    }

    public void setVideo(Multimedia video) {
        if(null!=video){
            this.video = video;
        }
    }

    public Set<Long> getPeopleLiked() {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }

        return Collections.unmodifiableSet(this.peopleLiked);
    }

    public void setPeopleLiked(HashSet<Long> peopleLiked) {
        this.peopleLiked = peopleLiked;
    }

    public Set<Long> getPeopleDisliked() {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        return Collections.unmodifiableSet(this.peopleDisliked);
    }

    public void setPeopleDisliked(HashSet<Long> peopleDisliked) {
        this.peopleDisliked = peopleDisliked;
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
        return Collections.unmodifiableSet(this.tags);
    }

    public void setTags(HashSet<Tag> tags) {
        if(tags!=null){
            this.tags = tags;
        }
    }

    public void setDescription(String description) throws PostException {
        if(description.length()<=MAX_LENGTH){
            this.description = description;
        }
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return this.dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public Timestamp getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
        this.dateTimeString= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(dateTime);
    }

    public String getDateTimeAsString() {
        return  this.dateTimeString;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(this.categories);
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
        return Collections.unmodifiableSet(this.taggedPeople);
    }
    
	public Multimedia getMainPic() {
		return this.multimedia != null && this.multimedia.iterator().hasNext() ? this.multimedia.iterator().next() : null;
	}

    public void setTaggedPeople(HashSet<User> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }

    public Set<Comment> getComments() {
        return Collections.unmodifiableSet(this.comments);
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

    @Override
    public int compareTo(Post o) {
        return o.dateTime.compareTo(this.dateTime);
    }

    public void addPersonLiked(long userId) {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }
        this.peopleLiked.add(userId);
    }

    public void removePersonLiked(long userId) {
        if(this.peopleLiked==null){
            this.peopleLiked=new HashSet<>();
        }
        this.peopleLiked.remove(userId);
    }

    public void removePersonDisliked(long userId) {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        this.peopleDisliked.remove(userId);
    }

    public void addPersonDisliked(long userId) {
        if(this.peopleDisliked==null){
            this.peopleDisliked=new HashSet<>();
        }
        this.peopleDisliked.add(userId);
    }
}