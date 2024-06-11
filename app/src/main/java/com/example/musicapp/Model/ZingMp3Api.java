package com.example.musicapp.Model;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ZingMp3Api {

    private static final String VERSION = "1.6.34";
    private static final String URL = "https://zingmp3.vn";
    private static final String SECRET_KEY = "2aa2d1c561e809b267f3638c4a307aab";
    private static final String API_KEY = "88265e23d4284f25963e6eedac8fbfa3";
    private static final String CTIME = String.valueOf(System.currentTimeMillis() / 1000);

    private OkHttpClient client;
    private Gson gson;

    public ZingMp3Api() {
        client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
    }

    private String getHash256(String str) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(str.getBytes("UTF-8"));
        return bytesToHex(hash);
    }

    private String getHmac512(String str, String key) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512");
        hmac.init(secretKey);
        byte[] hash = hmac.doFinal(str.getBytes("UTF-8"));
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String hashParamNoId(String path) throws Exception {
        return getHmac512(
                path + getHash256("ctime=" + CTIME + "version=" + VERSION),
                SECRET_KEY
        );
    }

    private String hashParam(String path, String id) throws Exception {
        return getHmac512(
                path + getHash256("ctime=" + CTIME + "id=" + id + "version=" + VERSION),
                SECRET_KEY
        );
    }

    private String hashParamHome(String path) throws Exception {
        return getHmac512(
                path + getHash256("count=30ctime=" + CTIME + "page=1version=" + VERSION),
                SECRET_KEY
        );
    }

    private String hashCategoryMV(String path, String id, String type) throws Exception {
        return getHmac512(
                path + getHash256("ctime=" + CTIME + "id=" + id + "type=" + type + "version=" + VERSION),
                SECRET_KEY
        );
    }

    private String hashListMV(String path, String id, String type, String page, String count) throws Exception {
        return getHmac512(
                path + getHash256(
                        "count=" + count + "ctime=" + CTIME + "id=" + id + "page=" + page + "type=" + type + "version=" + VERSION
                ),
                SECRET_KEY
        );
    }

    private String getCookie() throws IOException {
        Request request = new Request.Builder().url(URL).build();
        Response response = client.newCall(request).execute();
        return response.header("Set-Cookie");
    }

    private String requestZingMp3(String path, Map<String, String> qs) throws Exception {
        String cookie = getCookie();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL + path).newBuilder();
        for (Map.Entry<String, String> entry : qs.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        urlBuilder.addQueryParameter("ctime", CTIME);
        urlBuilder.addQueryParameter("version", VERSION);
        urlBuilder.addQueryParameter("apiKey", API_KEY);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Cookie", cookie)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public JsonObject getSong(String songId) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", songId);
        qs.put("sig", hashParam("/api/v2/song/get/streaming", songId));

        String response = requestZingMp3("/api/v2/song/get/streaming", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getDetailPlaylist(String playlistId) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", playlistId);
        qs.put("sig", hashParam("/api/v2/page/get/playlist", playlistId));

        String response = requestZingMp3("/api/v2/page/get/playlist", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getHome() throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("page", "1");
        qs.put("segmentId", "-1");
        qs.put("count", "30");
        qs.put("sig", hashParamHome("/api/v2/page/get/home"));
        String response = requestZingMp3("/api/v2/page/get/home", qs);
        return gson.fromJson(response, JsonObject.class);
    }
    public JsonObject getListArtistPlaylist(String artistId, String page, String count) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", artistId);
        qs.put("type", "artist");
        qs.put("page", page);
        qs.put("count", count);
        qs.put("sort", "new");
        qs.put("sectionId", "aAlbum");
        qs.put("sig", hashListMV("/api/v2/playlist/get/list", artistId, "artist", page, count));
        String response = requestZingMp3("/api/v2/playlist/get/list", qs);
        return gson.fromJson(response, JsonObject.class);
    }
    public JsonObject getTop100() throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("sig", hashParamNoId("/api/v2/page/get/top-100"));

        String response = requestZingMp3("/api/v2/page/get/top-100", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getChartHome() throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("sig", hashParamNoId("/api/v2/page/get/chart-home"));

        String response = requestZingMp3("/api/v2/page/get/chart-home", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getNewReleaseChart() throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("sig", hashParamNoId("/api/v2/page/get/newrelease-chart"));

        String response = requestZingMp3("/api/v2/page/get/newrelease-chart", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getInfoSong(String songId) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", songId);
        qs.put("sig", hashParam("/api/v2/song/get/info", songId));

        String response = requestZingMp3("/api/v2/song/get/info", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getListArtistSong(String artistId, String page, String count) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", artistId);
        qs.put("type", "artist");
        qs.put("page", page);
        qs.put("count", count);
        qs.put("sort", "new");
        qs.put("sectionId", "aSong");
        qs.put("sig", hashListMV("/api/v2/song/get/list", artistId, "artist", page, count));

        String response = requestZingMp3("/api/v2/song/get/list", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getArtist(String name) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("alias", name);
        qs.put("sig", hashParamNoId("/api/v2/page/get/artist"));

        String response = requestZingMp3("/api/v2/page/get/artist", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getLyric(String songId) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", songId);
        qs.put("sig", hashParam("/api/v2/lyric/get/lyric", songId));

        String response = requestZingMp3("/api/v2/lyric/get/lyric", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject search(String name) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("q", name);
        qs.put("sig", hashParamNoId("/api/v2/search/multi"));

        String response = requestZingMp3("/api/v2/search/multi", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getListMV(String id, String type, String page, String count) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", id);
        qs.put("type", type);
        qs.put("page", page);
        qs.put("count", count);
        qs.put("sort", "new");
        qs.put("sig", hashListMV("/api/v2/video/get/list", id, type, page, count));

        String response = requestZingMp3("/api/v2/video/get/list", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getCategoryMV(String id, String type) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", id);
        qs.put("type", type);
        qs.put("sig", hashCategoryMV("/api/v2/genre/get/mv", id, type));

        String response = requestZingMp3("/api/v2/genre/get/mv", qs);
        return gson.fromJson(response, JsonObject.class);
    }

    public JsonObject getVideo(String videoId) throws Exception {
        Map<String, String> qs = new HashMap<>();
        qs.put("id", videoId);
        qs.put("sig", hashParam("/api/v2/video/get/streaming", videoId));

        String response = requestZingMp3("/api/v2/video/get/streaming", qs);
        return gson.fromJson(response, JsonObject.class);
    }

}