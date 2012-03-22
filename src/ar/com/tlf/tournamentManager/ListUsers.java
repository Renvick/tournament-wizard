package ar.com.tlf.tournamentManager;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import ar.com.tlf.tournamentManager.R;
import ar.com.tlf.tournamentManager.db.UsersDbAdapter;

public class ListUsers extends ListActivity {

	private UsersDbAdapter mDbHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users);
		mDbHelper = new UsersDbAdapter(this);
		mDbHelper.open();
		fillData();
	}

	private void fillData() {
		Cursor c = mDbHelper.fetchAllUsers();
		startManagingCursor(c);
		UsersAdapter notes = new UsersAdapter(this, c);
		setListAdapter(notes);
		registerForContextMenu(this.getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.users_menu, menu);

		return result;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.users_context, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_user:
			createUser();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		if(item.getItemId() == R.id.edit_user_context){
			editUser(info.id);
			return true;
		}
		if(item.getItemId() == R.id.delete_user_context){
			deleteUser(info.id);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createUser() {
		Intent newUserIntent = new Intent(this, NewUser.class);
		startActivity(newUserIntent);
	}
	
	private void editUser(long id){
		Intent newUserIntent = new Intent(this, NewUser.class);
		newUserIntent.putExtra("ar.com.tlf.tournamentManager.Id", id);
		startActivity(newUserIntent);
	}
	
	private void deleteUser(long id){
		mDbHelper.deleteUser(id);
		fillData();
	}

	private class UsersAdapter extends CursorAdapter {
		private Cursor mCursor;
		private Context mContext;
		private final LayoutInflater mInflater;

		public UsersAdapter(Context context, Cursor cursor) {
			super(context, cursor, true);
			mInflater = LayoutInflater.from(context);
			mContext = context;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView t = (TextView) view.findViewById(R.id.name1);
			t.setText(cursor.getString(cursor
					.getColumnIndex(UsersDbAdapter.KEY_NAME)));

			ImageView myImage = (ImageView) findViewById(R.id.icon1);
			byte[] bb = cursor.getBlob(cursor
					.getColumnIndex(UsersDbAdapter.KEY_ICON));
			if (bb != null) {
				myImage.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0,
						bb.length));
			}

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final View view = mInflater.inflate(R.layout.user_row, parent,
					false);
			return view;
		}
		
		
	}
}
