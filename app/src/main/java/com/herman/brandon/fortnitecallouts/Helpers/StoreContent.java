package com.herman.brandon.fortnitecallouts.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for store content
 */
public class StoreContent {
    public static final List<StoreItem> ITEMS = new ArrayList<>();

    /**
     * Generates store items from JSON from REST API
     * @param storeInfo - json that has store content
     */
    public static void generateStoreItems(JSONObject storeInfo) {
        ITEMS.clear();
        try {
            JSONArray brWeekly = storeInfo.getJSONObject("br").getJSONArray("weekly");
            JSONArray brDaily = storeInfo.getJSONObject("br").getJSONArray("daily");

            for (int i = 0; i < brWeekly.length(); i++) {
                JSONObject jsonObject = brWeekly.getJSONObject(i);

                ITEMS.add(new StoreItem("Weekly - " + jsonObject.getString("name"),
                        jsonObject.getInt("price"),
                        jsonObject.getString("rarity"),
                        jsonObject.getString("cosmeticType"),
                        jsonObject.getString("imgPNGURL")));
            }

            for (int i = 0; i < brDaily.length(); i++) {
                JSONObject jsonObject = brDaily.getJSONObject(i);

                ITEMS.add(new StoreItem("Daily - " + jsonObject.getString("name"),
                        jsonObject.getInt("price"),
                        jsonObject.getString("rarity"),
                        jsonObject.getString("cosmeticType"),
                        jsonObject.getString("imgPNGURL")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A store item representing a piece of content.
     */
    public static class StoreItem {
        public final String title;
        public final int price;
        public final String rarity;
        public final String type;
        public final String imgUrl;

        public StoreItem(String title, int price, String rarity, String type, String imgUrl) {
            this.title = title;
            this.price = price;
            this.rarity = rarity;
            this.type = type;
            this.imgUrl = imgUrl;
        }
    }
}
