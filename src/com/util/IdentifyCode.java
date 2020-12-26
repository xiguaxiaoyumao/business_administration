package com.util;

import java.util.Random;

public class IdentifyCode {
    public StringBuffer getIdentifyCode() {
        StringBuffer identifyCode = new StringBuffer("");
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int value = random.nextInt(62);
            char ch = string.charAt(value);
            identifyCode.append(ch);
        }
        return identifyCode;
    }

    String string = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
}
