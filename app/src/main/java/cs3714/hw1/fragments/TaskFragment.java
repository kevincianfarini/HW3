package cs3714.hw1.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import cs3714.hw1.Constants.Constants;
import cs3714.hw1.LoginScreen;
import cs3714.hw1.R;
import cs3714.hw1.interfaces.ActivityInteraction;
import cs3714.hw1.interfaces.RetainedFragmentInteraction;
import cs3714.hw1.network.IsUserLoggedInAsyncTask;

/**
 * Created by Andrey on 2/16/2017.
 */

public class TaskFragment extends Fragment implements RetainedFragmentInteraction {

    public static final String TAG_TASK_FRAGMENT = "task_fragment";
    private String mActiveFragmentTag;
    private String loginResult;
    private ActivityInteraction activityInteraction;

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }
    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityInteraction = (ActivityInteraction) context;
        checkIfLoggedIn();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public String getActiveFragmentTag() {
        return mActiveFragmentTag;
    }

    public void setActiveFragmentTag(String s) {
        mActiveFragmentTag = s;
    }

    @Override
    public void checkIfLoggedIn() {
        new IsUserLoggedInAsyncTask(this, this.getContext()).execute();
    }

    @Override
    public void loginResult(String result) {
        if (!result.equals(Constants.STATUS_LOGGED_IN)) {
            this.startActivity(new Intent(this.getContext(), LoginScreen.class));
        }
    }


}
