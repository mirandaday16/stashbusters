package edu.neu.madcourse.stashbusters.model;

import android.graphics.Bitmap;

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
    private List<Post> posts;
    private List<Post> likedPosts;
    private List<User> followers;
    private String registrationToken; // user's device token
    private Bitmap profilePic;
    private String photoUrl;

    public User(String username, String registrationToken) {
        this.username = username;
        this.registrationToken = registrationToken;
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

    public List<User> getFollowers() {
        return followers;
    }

    public String getPhotoUrl() { return photoUrl; }

    public String getRegistrationToken() {
        return registrationToken;
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

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }


    public void setPhotoUrl(String url) {
        this.photoUrl = url;
    }

    public void setProfilePicture(Bitmap profilePic) { this.profilePic = profilePic;
    }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress;}
}
