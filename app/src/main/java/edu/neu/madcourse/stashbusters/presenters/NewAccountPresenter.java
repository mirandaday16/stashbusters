package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;

import edu.neu.madcourse.stashbusters.NewAccountActivity;
import edu.neu.madcourse.stashbusters.contracts.NewAccountContract;

public class NewAccountPresenter implements NewAccountContract.Presenter {
    private static final String TAG = NewAccountActivity.class.getSimpleName();

    private NewAccountContract.MvpView mView;
    private Context mContext;

    public NewAccountPresenter(Context context) {
        this.mContext = context;
        this.mView = (NewAccountContract.MvpView) context;
    }


}
