package cs490.purdueparkingassistant;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Craig on 10/2/2015.
 */
public class AccountInfoValidator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public AccountInfoValidator() {}

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateUsername(String username) {
        if (username.length() < 4 || username.length() > 20) {
            Log.d("Validator","Username length incorrect");
            return false;
        }
        return true;
    }

    public boolean validatePassword(String password) {
        if (password.length() < 3 || password.length() > 20) {
            Log.d("Validator","Password length incorrect");
            return false;
        }
        return true;
    }

    public boolean validatePhoneNumber(String number) {
        Log.d("VALIDATOR", number);
        //number = number.replace("(", "");
        //number = number.replace(")", "");
        //number = number.replace("-", "");
        //number = number.replace(" ", "");
        Log.d("VALIDATOR", number);
        if (number.length() != 12) {
            Log.d("Validator", "Number length wrong");
            return false;
        }
        return true;
    }

}
