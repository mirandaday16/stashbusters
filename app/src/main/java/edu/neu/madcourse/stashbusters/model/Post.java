package edu.neu.madcourse.stashbusters.model;

import android.location.Location;

import java.util.Date;
import java.util.List;

/**
 * This class represents a Post object.
 */
public abstract class Post {
    protected String id;
    protected String title;
    protected String description;
    protected String authorId;
    protected String photoUrl;
    protected Location location;
    protected String materialType;
    protected List<Comment> comments;
    protected List<User> likers; //TODO: maybe a better name?
    protected long createdDate;

    protected Post() {
        this.createdDate = new Date().getTime();
    }

    protected Post(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdDate = new Date().getTime();
    }

}
