package com.herman.brandon.fortnitecallouts.Helpers;

import java.util.List;
import java.util.regex.Pattern;

public class FortniteMapHelper {
    // Matches patterns like "A1", "B3", "H5", etc
    private static Pattern p = Pattern.compile("([a-zA-Z][0-9])");
    private static final double ONE_UNIT_TIME = 45;

    /**
     * Using google's Speech Recognizer list of possible words from user, calculate travel time
     * @param voiceCommandOptions - list of possible commands from user
     * @return - travel time in seconds
     */
    public static double parseVoiceCommandResults(List<String> voiceCommandOptions) {
        for (String voiceCommand : voiceCommandOptions) {
            String[] values = voiceCommand.split("\\s+");

            String matchedValue1 = null;
            String matchedValue2 = null;

            if(values.length >= 2) {
                for (String val : values) {
                    if (p.matcher(val).matches()) {
                        if(matchedValue1 == null){
                            matchedValue1 = val;
                        } else if (matchedValue2 == null) {
                            matchedValue2 = val;
                        }
                    }
                }

                if (matchedValue1 != null && matchedValue2 != null) {
                    int x1 = convertCharToX(matchedValue1.toUpperCase().charAt(0));
                    int y1 = Integer.parseInt(matchedValue1.substring(1)) - 1;

                    int x2 = convertCharToX(matchedValue2.toUpperCase().charAt(0));
                    int y2 = Integer.parseInt(matchedValue2.substring(1)) - 1;

                    if (x1 >= 0 && y1 >= 0 && x2 >= 0 && y2 >= 0) {
                        return calculateTravelTime(x1, y1, x2, y2);
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Calculate travel time using geometry
     * @param x1 - 1st x coordinate
     * @param y1 - 1st y coordinate
     * @param x2 - 2nd x coordinate
     * @param y2 - 2nd y coordinate
     * @return - time in seconds
     */
    public static double calculateTravelTime(int x1, int y1, int x2, int y2){
        double val = Math.hypot(x1-x2, y1-y2);
        return val * ONE_UNIT_TIME;
    }

    /**
     * Convert A-J coordinate to 0-9 coordinate
     * @param c - character to convert
     * @return - 0-9 coordinate representation
     */
    private static int convertCharToX(Character c) {
        switch (c){
            case 'A':
                return 0;
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 6;
            case 'H':
                return 7;
            case 'I':
                return 8;
            case 'J':
                return 9;
            default:
                return -1;
        }
    }
}
