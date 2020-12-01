package edu.neu.madcourse.stashbusters.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.AbstractList;
import java.util.List;

/**
 * This class represents user.
 */
@IgnoreExtraProperties
public class User {
    private String emailAddress;
    private String id;
    private String username;
    private String bio;
    private String photoUrl;
    private List<Post> posts;
    private List<Post> likedPosts;
    private Integer followerCount;
    private String deviceToken; // user's device token

    public User(String username, String deviceToken) {
        this.username = username;
        this.deviceToken = deviceToken;
        this.followerCount = 0;
    }

    public User(String emailAddress,
                String username,
                String bio,
                String photoUrl,
                String deviceToken) {
        this.emailAddress = emailAddress;
        this.username = username;
        this.bio = bio;
        this.photoUrl = photoUrl;
        this.deviceToken = deviceToken;
        this.followerCount = 0;
    }

    public String getUsername() {
        return username;
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


    public Integer getFollowerCount() {
        return followerCount;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setUsername(String username) {
        this.username = username;
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


    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl;
    }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress;}
}
