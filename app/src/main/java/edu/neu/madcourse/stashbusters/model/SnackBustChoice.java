package edu.neu.madcourse.stashbusters.model;

/**
 * This class represents a choice in a Snack Bust Post.
 */
public class SnackBustChoice {
    private String id;
    private String text;
    private Integer voteCount;

    public SnackBustChoice(){};
    public SnackBustChoice(String text) {
        this.text = text;
        this.voteCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}
