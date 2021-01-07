package com.se.focusclock.utils;

import java.util.regex.Pattern;

public class ValidateUtils {

    public static boolean checkPass(String password) {
        if (password == null) {
            return false;
        }
        return password.matches(".*\\d+.*") && password.matches(".*[a-zA-Z]+.*") && password.length() >= 8;
    }

    public static boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public static boolean checkMoblie(String phone){
        return phone.matches("^1[35-9]\\d{9}$");
    }
}
