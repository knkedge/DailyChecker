package jp.knkedge;

import java.util.Calendar;
import android.database.Cursor;
import jp.knkedge.SQLiteManager;

public class DailyWork {
	// private static final SimpleDateFormat sdft = new SimpleDateFormat("yyyy/mm/dd");
	private int id = 0;
	private String name;
	private int exp;
	private int level;
	private String prevDate;
	private int continuingDay;
	private static SQLiteManager sqliteManager;

	/** コンストラクタ **/
	public DailyWork (int id, String name) {
		this.id = id;
		this.name = name;

		initialize();
	}

	public static void initialize (SQLiteManager manager) {
		if (sqliteManager == null) {
			sqliteManager = manager;
		}
	}

	private void initialize () {
		Cursor c = sqliteManager.getRecords(id);
		this.exp = 0;
		this.level = 0;
		this.continuingDay = 0;
		int continueCount = 0;
		String befDate = "2000/0/1";

		if (c.moveToFirst()) {
			do {
				String date = c.getString(c.getColumnIndex("date"));
				int count = c.getInt(c.getColumnIndex("count"));
				/* calculate exp */
				if (diffDays(befDate, date) < 2) {
					continueCount++;
				} else {
					continueCount = 0;
				}
				this.exp += count + continueCount;
				/* update level */
				if (this.exp >= getNeccessaryExp(this.level)) {
					this.exp = this.exp - getNeccessaryExp(this.level);
					this.level++;	// level up!!
				}
				befDate = date;
			} while (c.moveToNext());
		}
		c.close();

		this.prevDate = befDate;
		this.continuingDay = continueCount;
	}

	private int diffDays (String bef, String aft) {
		Calendar date1 = strToDate(bef);
		Calendar date2 = strToDate(aft);
		return diffDays(date1, date2);
	}

	private int diffDays (Calendar date1, Calendar date2) {
		if (date1 == null || date2 == null) {
			return -1;
		}
		long diffmills = date2.getTimeInMillis() - date1.getTimeInMillis();
		long diffDays = diffmills / 1000 / 60 / 60 / 24;
		return (int)diffDays;
	}

	private Calendar strToDate (String str) {
		String[] val = str.split("/");
		// 形式がおかしかったらNullを返す
		if (val.length < 3) return null;

		int year = Integer.parseInt(val[0]);
		if (year < 0) return null;
		int month = Integer.parseInt(val[1]);
		if (month < 1 || month > 12) return null;
		int date = Integer.parseInt(val[2]);
		if (date < 1 || date > 31) return null;


		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, date);
		return cal;
	}

	public String getName () {
		return this.name;
	}

	public int getExp () {
		return this.exp;
	}

	public int getLevel () {
		return this.level;
	}

	public int getContinuingDay () {
		if (this.continuingDay < 0) {
			Calendar cal = Calendar.getInstance();
			return diffDays(cal, strToDate(this.prevDate));
		} else {
			return this.continuingDay;
		}
	}

	public static int getNeccessaryExp (int level) {
		return 50+level*10;
	}

	public String getDate () {
		return this.prevDate;
	}

//	public void update () {
//		int plusValue = 1;
//		/* 前回の更新日取得 */
//		Calendar prev = Calendar.getInstance(TimeZone.getTimeZone("JST"));
//		prev.set(prevDate.getYear(), prevDate.getMonth(), prevDate.getDate());
//		prev.add(Calendar.DATE, 1);
//		// 前回の更新日の翌日
//
//		/* 連続してるか確認 */
//		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("JST"));
//		if (today.get(Calendar.YEAR) == prev.get(Calendar.YEAR)
//				&& today.get(Calendar.MONTH) == prev.get(Calendar.MONTH)
//				&& today.get(Calendar.DATE) == prev.get(Calendar.DATE)) {
//			plusValue += this.continueingDay;
//			// 継続日数追加
//			this.continueingDay++;
//		} else {
//			this.continueingDay = 0;
//		}
//
//		/* 経験値更新 */
//		this.exp += plusValue;
//		// レベルアップ
//		if (this.exp >= getNeccessaryExp(this.level)) {
//			this.exp -= getNeccessaryExp(this.level);
//			this.level++;
//		}
//
//		// java.util.Date today = new java.util.Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
//
//		/* 日付更新 */
//		this.prevDate.setYear(today.get(Calendar.YEAR));
//		this.prevDate.setMonth(today.get(Calendar.MONTH));
//		this.prevDate.setDate(today.get(Calendar.DATE));
//	}

	/*
	public class Day {
		int year;
		int month;
		int date;
		@Override
		public String toString () {
			return year+"/"+month+"/"+date;
		}
	}*/
}
