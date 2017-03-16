package cs3714.hw1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import cs3714.hw1.fragments.HomeScreenFragment;
import cs3714.hw1.fragments.MyStepsFragment;
import cs3714.hw1.fragments.TaskFragment;
import cs3714.hw1.fragments.TeamFragment;
import cs3714.hw1.interfaces.ActivityInteraction;
import cs3714.hw1.interfaces.HomeScreenInteraction;
import cs3714.hw1.interfaces.RetainedFragmentInteraction;

public class MainActivity extends AppCompatActivity implements HomeScreenInteraction, ActivityInteraction {


    private Fragment homeScreenFragment, taskFragment, myStepsFragment, teamFragment;
    public static final int READ_TIMEOUT_MS = 20000;
    public static final int CONNECT_TIMEOUT_MS = 20000;
    private SharedPreferences prefs;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); //TODO change this back?

        if(prefs.getString("loggedin","").equals("yes") || getIntent().getBooleanExtra("loggedin", false)) {
            prefs.edit().putString("loggedin", "yes");
            prefs.edit().commit();

        } else {
            Intent intent = new Intent(this, LoginScreen.class);
            this.startActivity(intent);
            finish();

        }



        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);

        if (taskFragment == null) {

            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {

            homeScreenFragment = new HomeScreenFragment();
            ((RetainedFragmentInteraction) taskFragment).setActiveFragmentTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            fragmentManager.beginTransaction().replace(R.id.frame, homeScreenFragment ).commit();

        } else {

            myStepsFragment = fragmentManager.findFragmentByTag(MyStepsFragment.TAG_MY_STEPS_FRAGMENT);
            ((RetainedFragmentInteraction) taskFragment).setActiveFragmentTag(TeamFragment.TAG_TEAM_FRAGMENT);
            teamFragment = fragmentManager.findFragmentByTag(TeamFragment.TAG_TEAM_FRAGMENT);

        }
    }

    @Override
    public void changeFragment(String fragment_name) {
        Fragment fragment;
        Class fragmentClass = null;

        if(fragment_name.equals(TeamFragment.TAG_TEAM_FRAGMENT)){
            fragmentClass = TeamFragment.class;
        }
        else if(fragment_name.equals(MyStepsFragment.TAG_MY_STEPS_FRAGMENT)){
            fragmentClass = MyStepsFragment.class;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();

                if (fragment.getClass().equals(TeamFragment.class)) {
                    teamFragment = fragment;
                }
                else if (fragment.getClass().equals(MyStepsFragment.class)) {
                    myStepsFragment = fragment;
                }


                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.frame, fragment,
                        ((RetainedFragmentInteraction)taskFragment).getActiveFragmentTag());
                ft.addToBackStack(null);
                ft.commit();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void InitiateLoginActivity() {

    }
}
