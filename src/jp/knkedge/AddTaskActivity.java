package jp.knkedge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskActivity extends Activity implements OnClickListener{
	SQLiteManager sqliteManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addtask);
		Button addButton = (Button)findViewById(R.id.button1);
		addButton.setOnClickListener(this);

		if (sqliteManager == null) {
			sqliteManager = new SQLiteManager(getApplicationContext());
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			EditText edit =(EditText) findViewById(R.id.editText1);
			if (sqliteManager != null) {
				sqliteManager.addTask(edit.getText().toString());
				Toast.makeText(getApplicationContext(), "追加したタスク: "+edit.getText().toString(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		sqliteManager = null;
		super.onDestroy();
	}

}
