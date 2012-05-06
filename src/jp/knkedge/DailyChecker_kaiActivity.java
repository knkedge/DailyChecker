package jp.knkedge;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DailyChecker_kaiActivity extends Activity {
	SQLiteManager sqliteManager;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sqliteManager = new SQLiteManager(this);

        // View init
        ListView view = (ListView)this.findViewById(R.id.listView1);
        // int id = sqliteManager.addTask("test");
        sqliteManager.addRecord(1);
        ArrayList<DailyWork> list = sqliteManager.getAllTasks();
        // ArrayAdapter<DailyWork> adapter = new ArrayAdapter<DailyWork>(this, R.layout.listview, list);
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.listview, list);
        view.setAdapter(adapter);
    }
}