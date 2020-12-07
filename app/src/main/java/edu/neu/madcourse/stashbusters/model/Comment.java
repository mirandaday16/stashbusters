package edu.neu.madcourse.stashbusters.model;

import java.util.Date;

/**
 * This class represents a Comment in a Post.
 */
public class Comment {
    private String id;
    private String text;
    private long createdDate;
    private String authorId;

    public Comment () {
        // for Datasnapshot 
    }

    public Comment(String text) {
        this.text = text;
        this.createdDate = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
