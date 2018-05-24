package com.herman.brandon.fortnitecallouts.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Helper for statistics content
 */
public class StatsContent {
    public static final List<StatItem> ITEMS = new ArrayList<>();

    private static final HashMap<String, String> titles = new HashMap<>();

    // Create hashmap items
    static {
        titles.put("kills", "Kills");
        titles.put("matchesPlayed", "Matches Played");
        titles.put("minutesPlayed", "Minutes Played");
        titles.put("wins", "Wins");
        titles.put("top3", "Top 3");
        titles.put("top5", "Top 5");
        titles.put("top6", "Top 6");
        titles.put("top10", "Top 10");
        titles.put("top12", "Top 12");
        titles.put("top25", "Top 25");
        titles.put("deaths", "Deaths");
        titles.put("kpd", "Kills Per Death");
        titles.put("kpm", "Kills Per Min");
        titles.put("tpm", "Time Per Match");
        titles.put("spm", "Score Per Match");
        titles.put("score", "Score");
        titles.put("winRate", "Win Rate");
        titles.put("solo", "Solo");
        titles.put("duo", "Duo");
        titles.put("squad", "Squad");
        titles.put("all", "Total");
    }

    /**
     * Generates statistics for all game modes
     * @param stats
     */
    public static void generateAllGameModeStats(JSONObject stats) {
        ITEMS.clear();
        generateGameModeStats(stats, "solo");
        generateGameModeStats(stats, "duo");
        generateGameModeStats(stats, "squad");
        generateGameModeStats(stats, "all");
    }

    /**
     * Generate stats for a player for one specific gamemode
     * @param playerStats - JSONObject from REST API
     * @param gameMode - string for game mode ("solo", "duo", "squad", "all")
     */
    public static void generateGameModeStats(JSONObject playerStats, String gameMode){
        JSONObject stats;
        try {
            stats = playerStats.getJSONObject("br").getJSONObject("stats");
        }catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JSONObject xbox = null;
        JSONObject ps4 = null;
        JSONObject pc = null;
        JSONObject mobile = null;

        // Try catches for each to continue processing if there are errors

        try {
            xbox = stats.getJSONObject("xb1");
        } catch (JSONException e) {
            // Continue
        }

        try {
            ps4 = stats.getJSONObject("ps4");
        } catch (JSONException e) {
            // Continue
        }

        try {
            pc = stats.getJSONObject("pc");
        } catch (JSONException e) {
            // Continue
        }

        try {
            mobile = stats.getJSONObject("mobile");
        } catch (JSONException e) {
            // Continue
        }

        try {
            Iterator<String> keys = null;

            JSONObject xboxSolo = null;
            if (xbox != null) {
                xboxSolo = xbox.getJSONObject(gameMode);
                keys = xboxSolo.keys();
            }

            JSONObject ps4Solo = null;
            if (ps4 != null) {
                ps4Solo = ps4.getJSONObject(gameMode);
                keys = ps4Solo.keys();
            }

            JSONObject pcSolo = null;
            if (pc != null) {
                pcSolo = pc.getJSONObject(gameMode);
                keys = pcSolo.keys();
            }

            JSONObject mobileSolo = null;
            if (mobile != null) {
                mobileSolo = mobile.getJSONObject(gameMode);
                keys = mobileSolo.keys();
            }

            if (keys != null) {
                createItem(xboxSolo, ps4Solo, pcSolo, mobileSolo, keys, gameMode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a statistic item to go into ITEMS list
     * @param xbox - xbox stat
     * @param ps4 - ps4 stat
     * @param pc - pc stat
     * @param mobile - mobile stat
     * @param keys - Keys to filter through
     * @param gameMode - name of game mode
     */
    private static void createItem(JSONObject xbox, JSONObject ps4, JSONObject pc, JSONObject mobile, Iterator<String>keys, String gameMode) {
        while (keys.hasNext()) {
            String key = keys.next();
            if(key.equals("lastMatch") && keys.hasNext()) {
                key = keys.next();
            }

            double xboxVal = 0;
            double ps4Val = 0;
            double pcVal = 0;
            double mobileVal = 0;

            try {
                if(xbox != null) {
                    xboxVal = xbox.getDouble(key);
                }
                if(ps4 != null) {
                    ps4Val = ps4.getDouble(key);
                }
                if(pc != null) {
                    pcVal = pc.getDouble(key);
                }
                if(mobile != null) {
                    mobileVal = mobile.getDouble(key);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ITEMS.add(new StatItem(titles.get(gameMode) + " " + titles.get(key), (float) xboxVal, (float) ps4Val, (float) pcVal, (float) mobileVal));
        }
    }

    /**
     * A statistic item representing a piece of content.
     */
    public static class StatItem {
        public final String title;
        public final float xbox;
        public final float ps4;
        public final float pc;
        public final float mobile;

        public StatItem(String title, float xbox, float ps4, float pc, float mobile) {
            this.title = title;
            this.xbox = xbox;
            this.ps4 = ps4;
            this.pc = pc;
            this.mobile = mobile;
        }
    }
}
