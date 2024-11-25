package dev.mrsluffy.exam.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/11/2024
 **/

public class Utilities {

    public static Map<String, String> message(String param, String msg) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put(param != null ? param : "msg", msg);
        return msgMap;
    }

    public static Map<String, String> error(String msg) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("err_msg", msg);
        return errorMap;
    }

    public static Map<String, String> errorTraceId(String msg) {
        Map<String, String> errorMap = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        errorMap.put("trace-id", uuid.toString());
        // Store to db for trace-id
        // msg
        return errorMap;
    }
}
