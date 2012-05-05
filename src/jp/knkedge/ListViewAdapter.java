package jp.knkedge;

import java.util.ArrayList;
import java.util.List;

import jp.knkedge.SQLiteManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends ArrayAdapter<DailyWork> implements OnClickListener{
	// SQLiteManager sqliteManager = null;

	// private Context context = null;
	private LayoutInflater inflater = null;
	// private SQLiteManager sqliteManager = null;

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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		DailyWork work = (DailyWork)getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview, null);

			holder = new ViewHolder();
			holder.button = (Button) convertView.findViewById(R.id.imageButton1);
			// update clicklistener initialize
			holder.button.setOnClickListener(this);
			holder.dayView = (TextView) convertView.findViewById(R.id.textDay);
			holder.expView = (TextView) convertView.findViewById(R.id.textExp);
			holder.levelView = (TextView) convertView.findViewById(R.id.textLevel);
			holder.expBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 値設定
		holder.dayView.setText(work.getContinuingDay()+"日");
		holder.button.setText(work.getName());
		holder.expView.setText(""+work.getExp());
		holder.levelView.setText("Lv "+work.getLevel());
		holder.expBar.setProgress(work.getExp()-DailyWork.getNeccessaryExp(work.getLevel()));

		return convertView;
	}

	private class ViewHolder {
		Button button;
		TextView dayView;
		TextView expView;
		TextView levelView;
		ProgressBar expBar;
	}

	public void onClick(View view) {
		// TODO 自動生成されたメソッド・スタブ
		// ボタンが押されたとき
		if (view.getId() == R.id.imageButton1) {
			Toast.makeText(getContext(), "Pushed", Toast.LENGTH_LONG).show();
			/*
			if (sqliteManager == null) {
				sqliteManager = new SQLiteManager(getContext());
			}
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
