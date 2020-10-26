package com.smartpc.chiyun.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestUtil {

    public static String receiveData(HttpServletRequest request) throws IOException {
        StringBuffer data = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String line;
        while((line = br.readLine()) != null) {
            data.append(line);
        }
        br.close();
        return data.toString();
    }

}
