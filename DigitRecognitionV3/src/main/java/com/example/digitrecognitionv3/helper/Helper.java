package com.example.digitrecognitionv3.helper;

public class Helper {

    public static String shapeBase64(String base64) {

        StringBuffer sb = new StringBuffer(base64);
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-2);
        for (int i = 0; i < base64.length(); i++) {
            if (base64.charAt(i) == '%' && base64.charAt(i + 1) == '2' &&
                    base64.charAt(i + 2) == 'F') {
                base64 = base64.substring(0, i) + '/' +
                        base64.substring(i + 3, base64.length());
            }
            if (base64.charAt(i) == '=')
                base64 = base64.substring(0, i);
        }

        return base64;
    }
}
