package com.herman.brandon.fortnitecallouts.database;

import android.provider.BaseColumns;

public final class Player {
    // To prevent someone from accidentally instantiating the Player class,
    // make the constructor private.
    private Player() {}

    /* Inner class that defines the table contents */
    public static class PlayerEntry implements BaseColumns {
        public static final String TABLE_NAME = "player_entry";
        public static final String COLUMN_NAME_EPIC_ACCOUNT_NAME = "epic_account";
        public static final String COLUMN_NAME_IS_FRIEND = "is_friend";
    }
}
