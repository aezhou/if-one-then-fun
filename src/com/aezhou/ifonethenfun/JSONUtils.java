package com.aezhou.ifonethenfun;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class JSONUtils {

    private static JSONArray readJsonArrayFromInputStream(InputStream is) throws IOException, JSONException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return new JSONArray(readAll(rd));
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        return readJsonArrayFromInputStream(new URL(url).openStream());
    }

    public static JSONArray readJsonArrayFromFile(File file) throws IOException, JSONException {
        return readJsonArrayFromInputStream(new FileInputStream(file));
    }
}
