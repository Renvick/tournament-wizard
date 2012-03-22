package ar.com.tlf.tournamentManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ar.com.tlf.tournamentManager.R;
import ar.com.tlf.tournamentManager.components.ColorPickerDialog;
import ar.com.tlf.tournamentManager.db.UsersDbAdapter;

public class NewUser extends Activity implements View.OnClickListener, ColorPickerDialog.OnColorChangedListener{

	private Paint color;
	private boolean edit = false;
	private long id = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user);
		
		color = new Paint();
		color.setColor(0xFFFF0000);

		Intent intent = getIntent();
		edit = false;
		id = intent.getLongExtra("ar.com.tlf.tournamentManager.Id", -1);
		if(id != -1){
			edit = true;
			UsersDbAdapter mDbHelper = new UsersDbAdapter(this);
			mDbHelper.open();
			Cursor c = mDbHelper.fetchUser(id);
			fillData(c);
		}
		
		Button bv = (Button)findViewById(R.id.color);
        bv.setOnClickListener(this);
        bv = (Button)findViewById(R.id.user_add);
        bv.setOnClickListener(this);
        bv = (Button)findViewById(R.id.user_cancel);
        bv.setOnClickListener(this);
	}
	
	private void fillData(Cursor c){
		EditText et = (EditText)findViewById(R.id.name);
		et.setText(c.getString(c.getColumnIndex(UsersDbAdapter.KEY_NAME)));
		colorChanged(c.getInt(c.getColumnIndex(UsersDbAdapter.KEY_COLOR)));
	}

	public void onClick(View v) {

		if(R.id.color == v.getId()){
			new ColorPickerDialog(v.getContext(), this, color.getColor()).show();
		}
		if(R.id.user_add == v.getId()){
			goToList(true);		
		}
		if(R.id.user_cancel == v.getId()){
			goToList(false);
		}
		
		
	}
	
	public void colorChanged(int col) {
        color.setColor(col);
        Button b = (Button)findViewById(R.id.color);
        b.setBackgroundColor(color.getColor());
    }

	private void goToList(boolean add){
		Intent listDataIntent = new Intent(this, ListUsers.class);
		if(add){
			EditText et = (EditText)findViewById(R.id.name);
			String inputText = et.getText().toString();
			
			UsersDbAdapter userDb = new UsersDbAdapter(this);
			
			userDb.open();
			if(edit){
				userDb.updateUser(id, inputText, null, color.getColor());
			}else{
				userDb.createUser(inputText, null, color.getColor());
			}
			
			et.setText("");
			color.setColor(0xFFFF0000);


		}
		startActivity(listDataIntent);
	}
}
