package ar.com.tlf.tournamentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ar.com.tlf.tournamentManager.R;
import ar.com.tlf.tournamentManager.R.id;
import ar.com.tlf.tournamentManager.R.layout;

public class MainMenu extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bv = (Button)findViewById(R.id.users);
        bv.setOnClickListener(this);
    }
    
    public void onClick(View v) {
		switch ( v.getId() ) {
		case R.id.users:
			Intent listDataIntent = new Intent(this, ListUsers.class);
			startActivity(listDataIntent);
		}
	}
}