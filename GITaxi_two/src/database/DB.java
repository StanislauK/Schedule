package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

	public DBHelper dbHelper;

	private static final String DB_NAME = "data_of_app";
	private static final int DB_VERSION = 1;
	public static final String DB_TABLE_TEMP = "sheldule";

	public static final String ARRAY_UZDA_MINSK = "Uzda-Minsk";
	public static final String ARRAY_MINSK_UZDA = "Minsk-Uzda";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_OWNER = "owner";
	public static final String COLUMN_DIRECTION = "direction";


	public static final String SAVE_VERSION = "SAVE_VERSION";

	//выровняй чтобы все удобно читалось
	private static final String DB_MAIN = "create table " + DB_TABLE_TEMP
			+ " ( " + COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_DAY + " text," + COLUMN_TIME + " text, " + COLUMN_OWNER
			+ " text, " + COLUMN_DIRECTION + " integer " + ");";

	public DBHelper dbhelper;
	public SQLiteDatabase db;

	//не надо тут контекст хранить
	private Context mCtx;

	public DB(Context ctx) {
		mCtx = ctx;
	}

	public void open() {
		dbhelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
		db = dbhelper.getWritableDatabase();
	}

	public void close() {
		if (dbhelper != null)
			dbhelper.close();
	}

	// get All data from table

	public Cursor getAllData(String name_of_the_table) {
		return db.query(name_of_the_table, null, null, null, null, null, null);
	}

	public Cursor getData(String[] massArgs) {
		
		String whereClause = "day =? AND direction =?";
		String[] whereArgs = massArgs;
		
		return db.query(DB_TABLE_TEMP, null, whereClause, whereArgs, null,
				null, null);
	}

	public void addRec(String day, String time, String owner, int direction) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DAY, day);
		cv.put(COLUMN_TIME, time);
		cv.put(COLUMN_OWNER, owner);
		cv.put(COLUMN_DIRECTION, direction);
		db.insert(DB_TABLE_TEMP, null, cv);
	}

	public void delRec(String table, long id) {
		db.delete(table, COLUMN_ID + " = " + id, null);

	}

	public class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context, String name, CursorFactory factory,
				int versio) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// db.execSQL(DB_CREATE1);
			// db.execSQL(DB_CREATE2);
			db.execSQL(DB_MAIN);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}

}
