package edu.neu.madcourse.stashbusters.model;

import java.util.Date;
import java.util.List;

import edu.neu.madcourse.stashbusters.enums.MaterialType;

/**
 * This class represents a Post object.
 */
public abstract class Post implements Comparable<Post>{
    public String id;
    public String title;
    public String description;
    public String authorId;
    public String photoUrl;
    public MaterialType materialType; //which material is this post about
    public List<Comment> comments;
    public List<User> likers;
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

    // order post by date
    @Override
    public int compareTo(Post o) {
        return Long.compare(this.getCreatedDate(), o.getCreatedDate());
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
