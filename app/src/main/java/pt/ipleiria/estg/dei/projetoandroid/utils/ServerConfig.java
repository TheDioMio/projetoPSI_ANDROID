package pt.ipleiria.estg.dei.projetoandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class ServerConfig {

    // ---------------- API CONFIG ----------------
    private static final String API_PREFS = "API_CONFIG";
    private static final String API_KEY_HOST = "HOST";
    private static final String API_KEY_PORT = "PORT";

    private static final String PREFS = "API_CONFIG";
    private static final String KEY_API_BASE = "API_BASE";
    private static final String DEFAULT_API_BASE ="http://127.0.0.1";

//    // defaults da API (ajusta se precisares)
//    private static final String DEFAULT_API_HOST = "172.20.10.2";
//    private static final int DEFAULT_API_PORT = 80;

//    public static String getApiHost(Context ctx) {
//        SharedPreferences sp = ctx.getSharedPreferences(API_PREFS, Context.MODE_PRIVATE);
//        return sp.getString(API_KEY_HOST, DEFAULT_API_HOST);
//    }
//
//    public static int getApiPort(Context ctx) {
//        SharedPreferences sp = ctx.getSharedPreferences(API_PREFS, Context.MODE_PRIVATE);
//        return sp.getInt(API_KEY_PORT, DEFAULT_API_PORT);
//    }

    public static String getApiBase(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Toast.makeText(ctx, sp.getString(KEY_API_BASE, DEFAULT_API_BASE), Toast.LENGTH_SHORT).show();
        return sp.getString(KEY_API_BASE, DEFAULT_API_BASE);
    }

//    public static String getFrontendBase(Context ctx) {
//        return "http://" + getApiHost(ctx) + ":" + getApiPort(ctx)
//                + "/PSI/projetoPSI_WEB/frontend/web";
//    }

    public static void saveApiBase(Context ctx, String apiBase) {
        AppSingleton.getInstance(ctx.getApplicationContext()).setEndereco(apiBase);
        Toast.makeText(ctx, apiBase, Toast.LENGTH_SHORT).show();
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_API_BASE, apiBase)
                .apply();
    }

//    public static void saveFrontendBase(Context ctx, String frontendBase) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
//        sp.edit().putString(KEY_FRONTEND_BASE, frontendBase).apply();
//    }

    // ---------------- MQTT CONFIG ----------------
    private static final String MQTT_PREFS = "MQTT_CONFIG";
    private static final String MQTT_KEY_HOST = "HOST";
    private static final String MQTT_KEY_PORT = "PORT";

    private static final String DEFAULT_MQTT_HOST = "172.20.10.2";
    private static final int DEFAULT_MQTT_PORT = 1883;

    public static String getMqttHost(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(MQTT_PREFS, Context.MODE_PRIVATE);
        return sp.getString(MQTT_KEY_HOST, DEFAULT_MQTT_HOST);
    }

    public static int getMqttPort(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(MQTT_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(MQTT_KEY_PORT, DEFAULT_MQTT_PORT);
    }

    public static String getMqttUri(Context ctx) {
        return "tcp://" + getMqttHost(ctx) + ":" + getMqttPort(ctx);
    }

    public static void saveMqtt(Context ctx, String host, int port) {
        SharedPreferences sp = ctx.getSharedPreferences(MQTT_PREFS, Context.MODE_PRIVATE);
        sp.edit()
                .putString(MQTT_KEY_HOST, host)
                .putInt(MQTT_KEY_PORT, port)
                .apply();
    }


}
