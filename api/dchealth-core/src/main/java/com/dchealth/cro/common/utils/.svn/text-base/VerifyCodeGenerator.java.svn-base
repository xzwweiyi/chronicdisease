package com.dchealth.cro.common.utils;

import java.util.Random;

public class VerifyCodeGenerator {
    private static final String VERIFY_CODES = "8205349761";

    private static final int VERIRY_SIZE = 6;

    public static String gen(int... length) {
        int len = VERIRY_SIZE;
        if (length != null && length.length > 0)
            len = length[0];

        int codesLen = VERIFY_CODES.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            verifyCode.append(VERIFY_CODES.charAt(rand.nextInt(
                    codesLen - ((int) Math.random() * 8))));
        }

        return verifyCode.toString();
    }
}
