package edu.neu.madcourse.stashbusters.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {
    public String username;
    public List<Post> posts;
    public List<Post> likedPosts;
    public List<User> followers;
    public String deviceToken;

    public User(String username, String deviceToken) {
        this.username = username;
        this.deviceToken = deviceToken;
    }
}
