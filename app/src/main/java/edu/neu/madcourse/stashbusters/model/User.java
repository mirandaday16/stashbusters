package edu.neu.madcourse.stashbusters.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents user.
 */
@IgnoreExtraProperties
public class User {
    private String id;
    private String username;
    private String email;
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

    public User(String username, String email, String photoUrl, String bio) {
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
        this.bio = bio;
    }

    public User(String username, String email, String photoUrl, String bio, String registrationToken) {
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
        this.bio = bio;
        this.registrationToken = registrationToken;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("username", username);
        result.put("bio", bio);
        result.put("email", email);
        result.put("photoUrl", photoUrl);

        return result;
    }
}
