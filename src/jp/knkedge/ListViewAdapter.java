package jp.knkedge;

import java.util.ArrayList;

import jp.knkedge.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends ArrayAdapter<DailyWork> implements OnClickListener{
	// SQLiteManager sqliteManager = null;

	// private Context context = null;
	private LayoutInflater inflater = null;
	private SQLiteManager sqliteManager = null;

	public ListViewAdapter (Context context, int listview, ArrayList<DailyWork> items) {
		super(context, listview, items);
		// this.context = context;

		this.inflater = LayoutInflater.from(context);
//		this.add(new DailyWork("テスト", 0, 0, 0));

        // SQLite initialize
       // sqliteManager = new SQLiteManager(getContext());
	}

//	public void addAll (List<DailyWork> list) {
//		this.list.addAll(list);
//	}

//	public void add (DailyWork work) {
//		this.list.add(work);
//	}

	private class ViewHandler extends Handler{
		View retView;
		public ViewHandler() {
			super();
		}
		public View returnView (Message msg) {
			this.handleMessage(msg);
			return this.retView;
		}
		public void handleMessage (Message msg) {
			if (msg.what == 1) {
				this.retView = inflater.inflate(R.layout.listview, null);
			}
			super.handleMessage(msg);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		DailyWork work = (DailyWork)getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			// convertView = inflater.inflate(R.layout.listview, null);
			// TODO inflater.inflate はハンドラでしか使えない
			ViewHandler handle = new ViewHandler();
			convertView = handle.returnView(handle.obtainMessage(1));
			holder = new ViewHolder();
			holder.button = (Button) convertView.findViewById(R.id.imageButton1);
			// update clicklistener initialize
			holder.button.setOnClickListener(this);
			holder.dayView = (TextView) convertView.findViewById(R.id.textDay);
			holder.expView = (TextView) convertView.findViewById(R.id.textExp);
			holder.levelView = (TextView) convertView.findViewById(R.id.textLevel);
			holder.expBar = (BarView)convertView.findViewById(R.id.barview);
			Log.v("DailyChecker", holder.expBar.toString());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 値設定
		holder.dayView.setText(work.getContinuingDay()+"日");
		holder.button.setText(work.getName());
		// holder.expView.setText(""+work.getExp());
		holder.levelView.setText("Lv "+work.getLevel());
		holder.expBar.setItem(work);

		// holder.expBar.setMax(DailyWork.getNeccessaryExp(work.getLevel()+1));
		// holder.expBar.setProgress(work.getExp());

		return convertView;
	}

	private class ViewHolder {
		Button button;
		TextView dayView;
		TextView expView;
		TextView levelView;
		BarView expBar;
	}

	public void onClick(View view) {
		// TODO 自動生成されたメソッド・スタブ
		// ボタンが押されたとき
		if (view.getId() == R.id.imageButton1) {
			if (sqliteManager == null) {
				sqliteManager = new SQLiteManager(getContext());
			}
			// タスクのアップデート処理
			Button b = (Button)view;
			int id = sqliteManager.getTaskId(b.getText().toString());
			// Toast.makeText(getContext(), "Pushed: "+id, Toast.LENGTH_LONG).show();
			if (id > 0) {
				sqliteManager.addRecord(id);
				Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), "error!", Toast.LENGTH_LONG).show();
			}
			/*
			Button b = (Button) view;
			String taskName = b.getText().toString();
			DailyWork work = sqliteManager.getDailyWork(taskName);
			if (work != null) {
				work.update();
				sqliteManager.setChangedState(work);
			}
			*/
		}
	}
}
