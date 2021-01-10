package be.henallux.ig3.smartcity.elbatapp.utils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputCheck {

    public static Boolean isUserNameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    public static Boolean isPasswordValid(String password){
        return password != null && password.trim().length() > 5;
    }

    public static Boolean isPasswordConfirm(String password, String passwordConfirm){
        return password.trim().equals(passwordConfirm.trim());
    }

    public static Boolean isWordValid(String word){
        return word != null && !word.trim().isEmpty();
    }

    public static Boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile("^(.+)@(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return isWordValid(email) && matcher.matches();
    }

    public static Boolean isPhoneValid(String phone){
        Pattern pattern = Pattern.compile("^\\d+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        return isWordValid(phone) && matcher.matches();
    }

    public static Boolean isDateValid(String date){
        return !date.isEmpty();
    }

    public static Boolean isAgeValid(String date){
        final Calendar calendar = Calendar.getInstance();
        return Integer.parseInt(date.split("/")[2]) <= calendar.get(Calendar.YEAR) - 18;
    }
}
