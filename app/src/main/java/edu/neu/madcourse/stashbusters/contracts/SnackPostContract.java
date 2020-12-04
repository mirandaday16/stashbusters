package edu.neu.madcourse.stashbusters.contracts;

import java.util.List;

import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.User;
import edu.neu.madcourse.stashbusters.views.SnackPostActivity;

/**
 * Defines the contract between the View {@link SnackPostActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.SnackPostPresenter}.
 * This is for adding a new stash panel post.
 */
public interface SnackPostContract {
    interface MvpView {
        void setPostView(List<SnackBustPost> postList);
        void setNewCard(User author);
    }

    interface Presenter {
        void loadSnackPosts();
        void loadSingleSnackPost(String userId, String postId);
        void loadAuthorData(String authorId);
    }
}
