package jp.knkedge;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteManager extends SQLiteOpenHelper {
	static class Table {
		static final String task = "daily_tasks";
		static final String records = "daily_records";
	}
	private static SQLiteDatabase db = null;

	/**
	 * コンストラクタ
	 * @param context
	 */
	public SQLiteManager(Context context) {
		this(context, "DailyChecker_kai", null, 1);
	}

	/**
	 * コンストラクタ
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public SQLiteManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		String create = "CREATE TABLE " + Table.task + " ( _id integer primary key, " +
				"name text unique not null);";
		String records = "CREATE TABLE " + Table.records + " ( _id integer primary key, " +
				"tid integer, date text, count integer, " +
				"FOREIGN KEY(tid) REFERENCES " + Table.task + "(_id) );";
		db.execSQL(create);
		db.execSQL(records);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * paramのIDの記録をCursorで返す
	 * @param id (タスクのID）
	 * @return Cursor
	 */
	public Cursor getRecords(int id) {
		getDb();
		Cursor c = db.query(Table.records, null, "tid="+id, null, null, null, "_id ASC");
		return c;
	}

	private DailyWork.Record getLastUpdated(int tid) {
		getDb();
		Cursor c = db.query(Table.records, null, "tid="+tid, null, null, null, "_id DESC", "1");
		if (c.moveToFirst()) {
			DailyWork.Record pair = new DailyWork.Record();
			pair._id = c.getInt(c.getColumnIndex("_id"));
			pair.date = c.getString(c.getColumnIndex("date"));
			pair.count = c.getInt(c.getColumnIndex("count"));
			return pair;
		} else {
			return null;
		}
	}

	public int addTask(String name) {
		getWritableDb();
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		int id = (int)db.insert(Table.task, "", cv);
		return id;
	}

	/**
	 * レコードを追加
	 * @param tid（タスクID）
	 * @return 追加に成功すればtrue, 失敗すればfalse
	 */
	public boolean addRecord(int tid) {
		getWritableDb();
		Calendar today = Calendar.getInstance();
		String date = today.get(Calendar.YEAR)+"/"+
				(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.DATE);
		/* 対象タスクの最終更新日調べる */
		DailyWork.Record p = getLastUpdated(tid);
		if (p != null) Log.v("DailyChecker", p.date);
		if (p != null && p.date.equals(date)) {
			ContentValues cv = new ContentValues();
			cv.put("count", ++p.count);
			int rows = db.update(Table.records, cv, "_id="+p._id, null);
			if (rows == 1) return true;
			else return false;
		/* 対象タスクの更新がなければ新たに当日のレコードを追加 */
		} else {
			ContentValues cv = new ContentValues();
			cv.put("tid", tid);
			cv.put("date", date);
			cv.put("count", 1);
			int recid = (int)db.insert(Table.records, "", cv);
			if (recid > 0) return true;
			else return false;
		}
	}

	public int getTaskId(String name) {
		// NameからIDを調べて返す
		getDb();
		Cursor c = db.query(Table.task, new String[]{"_id"}, "name=?", new String[]{name}, null, null, null);
		if (c.moveToFirst()) {
			int id = c.getInt(c.getColumnIndex("_id"));
			if (id > 0) {
				return id;
			}
		}
		return -1;
	}

	/**
	 * 全タスクのArrayListを返す
	 */
	public ArrayList<DailyWork> getAllTasks() {
		getDb();
		DailyWork.initialize(this);
		/* DBから全タスクを取得する */
		/* SELECT * FROM daily_tasks */
		Cursor c = db.query(Table.task, null, null, null, null, null, "_id ASC");
		/* foreach (Cursor c : ret) c.get -> DailyWork.construct(id, name) */
		ArrayList<DailyWork> list = new ArrayList<DailyWork>();
		if (c.moveToFirst()) {
			do {
				int id = c.getInt(c.getColumnIndex("_id"));
				String name = c.getString(c.getColumnIndex("name"));
				list.add(new DailyWork(id, name));
			} while (c.moveToNext());
		}
		return list;
	}

	private void getDb() {
		if ((db == null || !db.isOpen())) {
			db = this.getReadableDatabase();
		}
	}

	private void getWritableDb() {
		if (db != null && db.isOpen() && db.isReadOnly()) {
			db.close();
		}
		db = this.getWritableDatabase();
	}
}
