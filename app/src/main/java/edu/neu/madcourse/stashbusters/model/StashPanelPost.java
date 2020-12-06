package edu.neu.madcourse.stashbusters.model;

/**
 * Stash Panel Post is a type of Post whose content is specifically about
 * searching for project suggestions from available materials that user has.
 */
public class StashPanelPost extends Post {

    public StashPanelPost() {
        // For DataSnapshot
        super();
    }

    public StashPanelPost(String title, String description) {
        super(title, description);
    }

    public StashPanelPost(String title, String description, String photoUrl) {
        super(title, description, photoUrl);
    }
}
