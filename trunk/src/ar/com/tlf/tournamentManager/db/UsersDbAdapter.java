package ar.com.tlf.tournamentManager.db;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsersDbAdapter {

	public static final String KEY_NAME = "name";
	public static final String KEY_ICON = "icon";
	public static final String KEY_COLOR = "color";
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "UsuariosDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "user_data";
	private static final String DATABASE_TABLE = "user";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + "(" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_NAME
			+ " text not null, " + KEY_ICON + " blob, " + KEY_COLOR + " integer);";

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
	}
	
	public UsersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
	
	public UsersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    public long createUser(String name, HttpEntity icon, int color){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_COLOR, color);
        try{
        	if(icon != null)
        		initialValues.put(KEY_ICON, EntityUtils.toByteArray(icon));
        }catch (IOException e) {
			// TODO: handle exception
		}

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean deleteUser(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor fetchAllUsers() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_ICON, KEY_COLOR}, null, null, null, null, null);
    }
    
    public Cursor fetchUser(long rowId) throws SQLException {

        Cursor mCursor =
            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_NAME, KEY_ICON, KEY_COLOR}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public boolean updateUser(long rowId, String name, String icon, int color) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_ICON, icon);
        args.put(KEY_COLOR, color);
        
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
