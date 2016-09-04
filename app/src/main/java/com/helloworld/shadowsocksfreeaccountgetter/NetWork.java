package com.helloworld.shadowsocksfreeaccountgetter;

import android.accounts.NetworkErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PinkD on 2016/9/4.
 * NetWork
 */
public class NetWork {

    public static void getResult(final Parse parse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Config.URL).openConnection();
                    httpURLConnection.connect();
                    int code = httpURLConnection.getResponseCode();
                    if (code >= 200 && code < 300) {
                        parse.parse(responseToString(httpURLConnection.getInputStream()));
                    } else if (code >= 300 && code < 400) {
                        parse.onFail(new NetworkErrorException(code + httpURLConnection.getResponseMessage() + "\n" + responseToString(httpURLConnection.getInputStream())));
                    } else {
                        parse.onFail(new NetworkErrorException(code + httpURLConnection.getResponseMessage() + "\n" + responseToString(httpURLConnection.getErrorStream())));
                    }
                } catch (IOException e) {
                    parse.onFail(e);
                }
            }
        }).start();
    }

    /**
     * @param inputStream inputStream
     * @return String content
     */
    public static String responseToString(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String tmp;
        StringBuilder stringBuilder = new StringBuilder();
        while ((tmp = bufferedReader.readLine()) != null) {
            stringBuilder.append(tmp).append("\n");
        }
        return stringBuilder.toString();
    }
}
