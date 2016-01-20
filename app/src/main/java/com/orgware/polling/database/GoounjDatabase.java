package com.orgware.polling.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.orgware.polling.interfaces.Appinterface;
import com.orgware.polling.pojo.CityCountry;
import com.orgware.polling.pojo.CountryItem;
import com.orgware.polling.pojo.CurrentPollItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nandagopal on 24/10/15.
 */
public class GoounjDatabase extends SQLiteOpenHelper implements Appinterface {

    String COUNTRY_TABLE = "CREATE TABLE IF NOT EXISTS " + COUNTRY_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COUNTRY_NAMES + " VARCHAR," +
            COUNTRY_CODE + " VARCHAR)";

    String CURRENT_POLL_TABLE = "CREATE TABLE IF NOT EXISTS " + CURRENT_POLL_TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + CURRENT_POLL_ID + " INTEGER," + CURRENT_POLL_START_DATE + " VARCHAR," + CURRENT_POLL_END_DATE + " VARCHAR," + CURRENT_POLL_NAME + " VARCHAR," + CURRENT_ISBOOST + " INTEGER," + CURRENT_CREATED_USER_NAME + " VARCHAR)";

    String DBDIR = "GOOUNJDB", DBPATH = Environment.getExternalStorageDirectory() + File.separator + DBDIR, DBNAME = "countrydb";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context          to use to open or create the database
     * @param database_name    of the database file, or null for an in-memory database
     * @param factory          to use for creating cursor objects, or null for the default
     * @param database_version number of the database (starting at 1); if the database is older,
     *                         {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                         newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public GoounjDatabase(Context context, String database_name, SQLiteDatabase.CursorFactory factory, int database_version) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public GoounjDatabase(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COUNTRY_TABLE);
        db.execSQL(CURRENT_POLL_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + COUNTRY_TABLE);
        db.execSQL("drop table if exists " + CURRENT_POLL_TABLE);
    }

    public void insertCurrentPoll(CurrentPollItem currentPollItem) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put(CURRENT_POLL_ID, currentPollItem.currentPollId);
            insertValues.put(CURRENT_POLL_START_DATE, currentPollItem.mCurrentPollStart);
            insertValues.put(CURRENT_POLL_END_DATE, currentPollItem.mCurrentPollEnd);
            insertValues.put(CURRENT_POLL_NAME, currentPollItem.mCurrentPollTitle);
            insertValues.put(CURRENT_ISBOOST, currentPollItem.mCurrentBoost);
            insertValues.put(CURRENT_CREATED_USER_NAME, currentPollItem.mCreatedUserName);
            db.insert(CURRENT_POLL_TABLE_NAME, null, insertValues);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public ArrayList<CountryItem> getCountrycityValues() {

        SQLiteDatabase dbGoounj = SQLiteDatabase.openDatabase(
                DBPATH + File.separator + DBNAME, null,
                SQLiteDatabase.OPEN_READWRITE);

        Cursor countryCursor = dbGoounj.rawQuery("select country from table_country_city", null);
        // Cursor medicineCursor = dbPregnancy.rawQuery("select * from "
        // + TABLE_CALENDAR + " where " + EVENT_TYPE + " = 0 " + " and "
        // + EVENT_DATE + " = " + "'" + selectedDate + "'", null);
        ArrayList<CountryItem> itemList = new ArrayList<CountryItem>();
        if (countryCursor.moveToFirst()) {
            do {

                CountryItem items = new CountryItem();
                items.mCountryName = countryCursor.getString(countryCursor.getColumnIndex("country"));
//                items.mCityName = countryCursor.getString(countryCursor.getColumnIndex("city"));

                itemList.add(items);
            } while (countryCursor.moveToNext());
        }
        countryCursor.close();
        dbGoounj.close();
        return itemList;
    }

    public void insertCountryAndCodeValues(String countryNames, String countryCodes) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put(COUNTRY_NAMES, countryNames);
            insertValues.put(COUNTRY_CODE, countryCodes);
            db.insert(COUNTRY_TABLE_NAME, null, insertValues);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public ArrayList<CountryItem> getCountryData() {
        SQLiteDatabase db = null;
        ArrayList<CountryItem> itemList = null;
        try {
            db = getReadableDatabase();
            Cursor countryCursor = db.rawQuery("select * from " + COUNTRY_TABLE_NAME, null);
            itemList = new ArrayList<>();
            itemList.clear();
            if (countryCursor.moveToFirst()) {
                do {
                    CountryItem items = new CountryItem();
                    items.mCountryName = countryCursor.getString(countryCursor
                            .getColumnIndex(COUNTRY_NAMES));
                    items.mCountryCode = countryCursor.getString(countryCursor
                            .getColumnIndex(COUNTRY_CODE));
                    itemList.add(items);
                } while (countryCursor.moveToNext());
            }
            countryCursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return itemList;
    }

    public ArrayList<CurrentPollItem> getCurrentPollData() {
        SQLiteDatabase db = null;
        ArrayList<CurrentPollItem> itemList = null;
        try {
            db = getReadableDatabase();
            Cursor countryCursor = db.rawQuery("select * from " + CURRENT_POLL_TABLE_NAME, null);
            itemList = new ArrayList<>();
            itemList.clear();
            if (countryCursor.moveToFirst()) {
                do {
                    CurrentPollItem items = new CurrentPollItem();
                    items.currentPollId = countryCursor.getInt(countryCursor
                            .getColumnIndex(CURRENT_POLL_ID));
                    items.mCurrentPollStart = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_POLL_START_DATE));
                    items.mCurrentPollEnd = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_POLL_END_DATE));
                    items.mCurrentPollTitle = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_POLL_NAME));
                    items.mCurrentBoost = countryCursor.getInt(countryCursor
                            .getColumnIndex(CURRENT_ISBOOST));
                    items.mCreatedUserName = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_CREATED_USER_NAME));
                    itemList.add(items);
                } while (countryCursor.moveToNext());
            }
            countryCursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return itemList;
    }

    public ArrayList<CurrentPollItem> getCurrentPollSingleData(int position) {
        SQLiteDatabase db = null;
        ArrayList<CurrentPollItem> itemList = null;
        try {
            db = getReadableDatabase();
            Cursor countryCursor = db.rawQuery("select * from " + CURRENT_POLL_TABLE_NAME + " where id = " + position, null);
            itemList = new ArrayList<>();
            itemList.clear();
            if (countryCursor.moveToFirst()) {
                do {
                    CurrentPollItem items = new CurrentPollItem();
                    items.currentPollId = countryCursor.getInt(countryCursor
                            .getColumnIndex(CURRENT_POLL_ID));
                    items.mCurrentPollStart = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_POLL_START_DATE));
                    items.mCurrentPollEnd = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_POLL_END_DATE));
                    items.mCurrentPollTitle = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_POLL_NAME));
                    items.mCurrentBoost = countryCursor.getInt(countryCursor
                            .getColumnIndex(CURRENT_ISBOOST));
                    items.mCreatedUserName = countryCursor.getString(countryCursor
                            .getColumnIndex(CURRENT_CREATED_USER_NAME));
                    itemList.add(items);
                } while (countryCursor.moveToNext());
            }
            countryCursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return itemList;
    }

}
