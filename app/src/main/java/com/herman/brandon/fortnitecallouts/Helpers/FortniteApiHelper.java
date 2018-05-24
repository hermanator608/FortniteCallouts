package com.herman.brandon.fortnitecallouts.Helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.herman.brandon.fortnitecallouts.StatsFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herman.brandon.fortnitecallouts.MainActivity.requestQueue;

public class FortniteApiHelper {
    /**
     * Endpoints for www.reddit.com/r/jokes/
     */
    enum Endpoint{
        PLAYER ("/v2/player/"),
        SHOP ("/v2/shop");

        private final String ep;
        Endpoint(String ep) {
            this.ep = ep;
        }
    }

    private static final String BASE_URL = "https://fortnite.y3n.co";

    /**
     * Get user statistics for a player name
     * @param playerName - epic account name
     * @param callback - On success/failure callback
     */
    public static void fetchUserStats(final String playerName, final StatsFragment.VolleyCallback callback) {
        String url = BASE_URL + Endpoint.PLAYER.ep + playerName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(error);
                    }
                })
        {
            /** Passing some request headers* */
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("X-Key", "PxyNKPl3umzocuRoZoWK");
                return headers;
            }
        };

        // Access the RequestQueue using single request queue object
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Gets current store information
     * @param callback - On success/failure function
     */
    public static void fetchStoreInfo(final StatsFragment.VolleyCallback callback) {
        String url = BASE_URL + Endpoint.SHOP.ep;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(error);
                    }
                })
        {
            /** Passing some request headers* */
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("X-Key", "PxyNKPl3umzocuRoZoWK");
                return headers;
            }
        };

        // Access the RequestQueue using single request queue object
        requestQueue.add(jsonObjectRequest);
    }
}
