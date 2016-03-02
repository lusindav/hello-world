package pilot.org;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDB {

	static public Context context;
	static public boolean tabelExists;
	private PilotAssistanceDb DBHelper;
	private SQLiteDatabase db;
	public static Thread thread2 ;
	static final String states = "States";
	static final String openedStates = "OpenedStatesNames";

	static final String stateID = "_id";
	static final String stateCode = "code";
	static final String stateName = "state";
	static final String cityName = "city";

	public void CreateTable() {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		db.execSQL("create table if not exists " + states + " (" + stateID
				+ " INTEGER PRIMARY KEY , " + stateName
				+ " TEXT," + cityName + " TEXT," + stateCode + " TEXT)");
		db.execSQL("create table if not exists " + openedStates + " ("
				+ stateID + " INTEGER PRIMARY KEY , " + stateName
				+ " TEXT)");
		
		Cursor c = db.rawQuery("SELECT * from " + openedStates, new String[] {});
		if (c.getCount() == 0) {
			tabelExists = false;
		} else {
			tabelExists = true;	
		}
	}

	public WeatherDB(final Context ctx) {
		this.context = ctx;
		DBHelper = new PilotAssistanceDb(context);
        CreateTable();
	}

	public void fillState(String state, String city, String code) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(stateCode, code);
		initialValues.put(stateName, state);
		initialValues.put(cityName, city);
		db.insert(states, null, initialValues);
	}

	public void fillOpenedState(String state) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(stateName, state);
		db.insert(openedStates, null, initialValues);
	}

	public Cursor fetchOpenedStateByCode(String code) {
		SQLiteDatabase db1 = DBHelper.getReadableDatabase();
		Cursor cur = db1.rawQuery("SELECT * from " + states + " WHERE "
				+ stateCode + " LIKE \"" + code + "%\"", new String[] {});
		return cur;
	}

	public Cursor fetchStates() {
		SQLiteDatabase db1 = DBHelper.getReadableDatabase();
		Cursor cur = db1.rawQuery("SELECT * from " + states, new String[] {});
		return cur;
	}

	public Cursor fetchOpenedStates() {
		SQLiteDatabase db1 = DBHelper.getReadableDatabase();
		Cursor cur = db1.rawQuery("SELECT * from " + openedStates, new String[] {});
		return cur;
	}

	public void removeOpenedStateByCode(String code) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		db.delete(openedStates, "state = \"" + code + "\"", new String[] {});
	}

public void close(){
	db.close();
}
}
