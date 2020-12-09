package edu.neu.madcourse.stashbusters.contracts;

import java.util.List;

import edu.neu.madcourse.stashbusters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.model.Post;

public interface MyFeedContract extends PostContract {
    interface MvpView {
        void setPosts(List<Post> posts);
    }

    interface Presenter{
        void loadPosts();
    }
}
