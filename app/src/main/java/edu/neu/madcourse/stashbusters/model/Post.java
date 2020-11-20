package edu.neu.madcourse.stashbusters.model;

import android.location.Location;

import java.util.Date;
import java.util.List;

import edu.neu.madcourse.stashbusters.enums.MaterialType;

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
    protected MaterialType materialType; //which material is this post about
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

    protected Post(String title, String description, String photoUrl) {
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
        this.createdDate = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Location getLocation() {
        return location;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<User> getLikers() {
        return likers;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setLikers(List<User> likers) {
        this.likers = likers;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}
