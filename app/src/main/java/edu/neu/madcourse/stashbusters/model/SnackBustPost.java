package edu.neu.madcourse.stashbusters.model;

import java.util.Date;
import java.util.List;

/**
 * This class represents a Snack Bust Post.
 */
public class SnackBustPost {
    private String id;
    private String title;
    private String photoUrl;
    private String authorId;
    private List<SnackBustChoice> choiceList;
    protected long createdDate;

    public SnackBustPost(){}

    public SnackBustPost(String title, String photoUrl) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.createdDate = new Date().getTime();
    }

    public SnackBustPost(String title, String photoUrl, List<SnackBustChoice> choices) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.choiceList = choices;
        this.createdDate = new Date().getTime();
    }

    public SnackBustPost(String title, String photoUrl, String authorId, List<SnackBustChoice> choices) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.choiceList = choices;
        this.authorId = authorId;
        this.createdDate = new Date().getTime();
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getId() {
        return id;
    }

    public List<SnackBustChoice> getChoiceList() {
        return choiceList;
    }
}
