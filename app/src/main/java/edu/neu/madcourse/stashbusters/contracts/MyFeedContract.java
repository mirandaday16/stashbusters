package edu.neu.madcourse.stashbusters.contracts;

import java.util.List;

import edu.neu.madcourse.stashbusters.model.Post;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.MyFeedActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.MyFeedPresenter}.
 * This is for displaying posts on MyFeed (which displays posts of users the user is following)
 */
public interface MyFeedContract extends PostContract {
    interface MvpView {
        /**
         * Called by the Presenter (from within setPosts) to initialize the RecyclerView with all
         * of the posts. If the posts list is null or empty, then it displays a message advising the
         * user to follow other users.
         */
        void setPosts(List<Post> posts);
    }

    interface Presenter{
        /**
         * Called by the MvpView to gather posts from Firebase. It gathers all panel/swap posts
         * from users the user is currently following. Then, it sends the gathered posts to
         * MvpView by calling setPosts(posts).
         */
        void loadPosts();
    }
}
