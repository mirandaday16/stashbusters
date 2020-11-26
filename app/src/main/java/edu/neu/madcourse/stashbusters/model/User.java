package edu.neu.madcourse.stashbusters.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * This class represents user.
 */
@IgnoreExtraProperties
public class User {
    private String id;
    private String username;
    private String photoUrl;
    private String bio;
    private List<Post> posts;
    private List<Post> likedPosts;
    private List<User> followers;
    private Integer followerCount;
    private String registrationToken;

    public User(String username, String registrationToken) {
        this.username = username;
        this.registrationToken = registrationToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBio() {
        return bio;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}
