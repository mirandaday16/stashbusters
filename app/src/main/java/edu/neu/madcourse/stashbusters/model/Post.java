package edu.neu.madcourse.stashbusters.model;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import edu.neu.madcourse.stashbusters.enums.MaterialType;

/**
 * This class represents a Post object.
 */
public abstract class Post {
    public String id;
    public String title;
    public String description;
    public String authorId;
    public String photoUrl;
    public Location location;
    public MaterialType materialType; //which material is this post about
    public List<Comment> comments;
    public List<User> likers; //TODO: maybe a better name?
    public long likeCount;
    public long createdDate;

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

    public long getLikeCount() {
        return likeCount;
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

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }
    /**
     * Return type of post - StashSwapPost or StashPanelPost
     * @return
     */
    public String getPostType() {
        return this.getClass().getSimpleName();
    }
}
