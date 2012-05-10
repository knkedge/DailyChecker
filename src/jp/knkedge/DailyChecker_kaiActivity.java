package jp.knkedge;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class DailyChecker_kaiActivity extends Activity {
	SQLiteManager sqliteManager;
	ListView view;
	private static final int MENU_CREATE = (Menu.FIRST+1);
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sqliteManager = new SQLiteManager(this);

        // View init
        view = (ListView)this.findViewById(R.id.listView1);
        ArrayList<DailyWork> list = sqliteManager.getAllTasks();
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.listview, list);
        view.setAdapter(adapter);
    }

    @Override
    public void onResume() {
    	if (sqliteManager == null) {
    		sqliteManager = new SQLiteManager(this);
    	}
    	if (view == null) {
    		view = (ListView)this.findViewById(R.id.listView1);
    	}
    	ArrayList<DailyWork> list = sqliteManager.getAllTasks();
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.listview, list);
        view.setAdapter(adapter);
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, MENU_CREATE, Menu.NONE, "Dailyタスクを追加");
    	return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == MENU_CREATE) {
    		Intent intent = new Intent(this, AddTaskActivity.class);
    		startActivity(intent);

    		return true;
    	}
    	return false;
    }

    @Override
    public void onDestroy() {
    	sqliteManager = null;
    	view = null;
    	super.onDestroy();
    }
}