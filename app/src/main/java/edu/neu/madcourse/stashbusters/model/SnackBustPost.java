package edu.neu.madcourse.stashbusters.model;

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

    public SnackBustPost(String title, String photoUrl) {
        this.title = title;
        this.photoUrl = photoUrl;
    }

    public SnackBustPost(String title, String photoUrl, List<SnackBustChoice> choices) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.choiceList = choices;
    }
}
