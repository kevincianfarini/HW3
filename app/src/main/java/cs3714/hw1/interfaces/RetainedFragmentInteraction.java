package cs3714.hw1.interfaces;

/**
 * Created by Andrey on 2/17/2017.
 */

public interface RetainedFragmentInteraction {
    String getActiveFragmentTag();
    void setActiveFragmentTag(String s);
    void checkIfLoggedIn();
    void loginResult(String result);
}
