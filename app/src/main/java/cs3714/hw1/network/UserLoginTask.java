package cs3714.hw1.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import cs3714.hw1.Constants.Constants;
import cs3714.hw1.MainActivity;
import cs3714.hw1.R;
import cs3714.hw1.interfaces.LogInScreenInteraction;

/**
 * Created by kevincianfarini on 3/15/17.
 */

public class UserLoginTask extends AsyncTask<String, Void, String> {

    private final String username, password;
    private Context context;
    private LogInScreenInteraction activity;

    public UserLoginTask(String username, String password, Context context) {
        this.username = username;
        this.password = password;
        this.context = context;
        this.activity = (LogInScreenInteraction) context;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = Constants.STATUS_OFFLINE;
        try {
            publishProgress();
            Thread.sleep(5000);
            result = this.attemptLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        activity.NetworkingFlagUpdate(true);
    }

    @Override
    protected void onPostExecute(String s) {
        activity.NetworkingFlagUpdate(false);
        activity.LoginStatus(s);
    }

    protected void onCancelled() {

    }

    private void saveInSharedPreferences(String result) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sessionid", result);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private String attemptLogin() throws IOException {
        InputStream stream = null;
        String cookie = "empty cookie";
        String result = "relogin";

        try {
            System.setProperty("http.keepAlive", "false");
            HttpURLConnection connection = (HttpURLConnection) (new URL(
                    context.getString(R.string.login_url)
            )).openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(MainActivity.READ_TIMEOUT_MS);
            connection.setConnectTimeout(MainActivity.CONNECT_TIMEOUT_MS);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.connect();
            JSONObject credentials = new JSONObject();
            credentials.put("username", username);
            credentials.put("password", password);
            Writer osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(credentials.toString());
            osw.flush();
            osw.close();

            final int HttpResultCode = connection.getResponseCode();
            stream = HttpResultCode >= 400 ? connection.getErrorStream() : connection.getInputStream();

            if (HttpResultCode == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookieHeader = headerFields.get(context.getString(R.string.cookies_header));
                cookie = cookieHeader.get(0).substring(0, cookieHeader.get(0).indexOf(";"));
                saveInSharedPreferences(cookie);
                result = Constants.STATUS_LOGGED_IN;
            } else if (HttpResultCode == 401) {
                result = Constants.STATUS_RELOGIN;
            } else {
                result = Constants.STATUS_OFFLINE;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return result;
    }
}
