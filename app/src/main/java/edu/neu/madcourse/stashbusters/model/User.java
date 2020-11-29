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
    public String emailAddress;
    public String id;
    public String username;
    public String bio;
    public List<Post> posts;
    public List<Post> likedPosts;
    public List<User> followers;
    public String registrationToken; // user's device token
    public Bitmap profilePic;

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

    public void setProfilePicture(Bitmap profilePic) { this.profilePic = profilePic;
    }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress;}
}
