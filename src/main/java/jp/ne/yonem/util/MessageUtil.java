package jp.ne.yonem.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageUtil {

    public static final String E_000 = "E_000";
    public static final String E_001 = "E_001";
    public static final String E_101 = "E_101";
    public static final String E_102 = "E_102";
    public static final String E_201 = "E_201";
    public static final String I_001 = "I_001";
    public static final String I_101 = "I_101";

    public static String getMessage(MessageSource source, String code, Object... params) {
        return source.getMessage(code, params, Locale.JAPAN);
    }
}
