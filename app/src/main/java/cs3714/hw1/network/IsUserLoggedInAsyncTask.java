package cs3714.hw1.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cs3714.hw1.Constants.Constants;
import cs3714.hw1.MainActivity;
import cs3714.hw1.R;
import cs3714.hw1.fragments.TaskFragment;
import cs3714.hw1.interfaces.RetainedFragmentInteraction;

/**
 * Created by kevincianfarini on 3/15/17.
 */

public class IsUserLoggedInAsyncTask extends AsyncTask<Void, Void, String> {


    private RetainedFragmentInteraction taskFragment;
    private Context context;

    public IsUserLoggedInAsyncTask(TaskFragment taskFragment, Context context) {
        this.taskFragment = (RetainedFragmentInteraction) taskFragment;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getString("status", "").equals(Constants.STATUS_LOGGED_IN)) {
            String username = prefs.getString("username", "");
            if (username.equals("")) {
                return Constants.STATUS_RELOGIN;
            }
            try {
                return this.isUserLoggedIn(username);
            } catch (Exception e) {

            }
            return Constants.STATUS_OFFLINE;
        }
        return Constants.STATUS_LOGGED_IN;
    }

    @Override
    protected void onPostExecute(String s) {
        taskFragment.loginResult(s);
    }


    private String isUserLoggedIn(String user) throws IOException, JSONException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String cookie = prefs.getString("sessionid", "");
        if (cookie.equals("")) {
            return Constants.STATUS_RELOGIN;
        }
        InputStream stream = null;

        try {
            System.setProperty("http.keepAlive", "false");
            HttpURLConnection connection = (HttpURLConnection) (new URL(
                    context.getString(R.string.logged_in_url) + "?" + user
            )).openConnection();
            connection.setReadTimeout(MainActivity.READ_TIMEOUT_MS);
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();

            final int HttpResultCode = connection.getResponseCode();

            stream = HttpResultCode >= 400 ? connection.getErrorStream() : connection.getInputStream();
            String response = this.readInputStream(stream, 2).substring(0, 1);

            //TODO get username and email and display in navigation drawer

            if (!response.contains("0")) {
                connection.disconnect();
                return Constants.STATUS_LOGGED_IN;
            } else if (HttpResultCode > 200) {
                connection.disconnect();
                return Constants.STATUS_RELOGIN;
            } else {
                connection.disconnect();
                return Constants.STATUS_OFFLINE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
            return Constants.STATUS_OFFLINE;
        }
    }


    public String readInputStream(InputStream stream, int len) {
        return "";
    }


}
