package com.example.model;

public class Multimedia {
    private long id;
    private String url;
    private boolean isVideo;
    private Post post;

    public Multimedia() {
    }

    //constructor to be used when fetching from database
    public Multimedia(long id, String url, boolean isVideo, Post post) {
        this(url, isVideo, post);
        this.id = id;
        this.setPost(post);
    }

    //constructor to be used when putting object in database
    public Multimedia(String url, boolean isVideo, Post post) {
        this.setUrl(url);
        this.setVideo(isVideo);
        this.setPost(post);
    }

    public Multimedia(String url, boolean isVideo) {
        this.url=url;
        this.isVideo=isVideo;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


}
