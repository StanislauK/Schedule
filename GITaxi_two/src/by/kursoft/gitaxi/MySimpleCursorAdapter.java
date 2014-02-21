package by.kursoft.gitaxi;

import by.kursoft.gitaxi.database.DB;
import stanislau.gitaxi_two.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {

	Cursor c;
	Context mContext;
	Activity activity;
	Typeface type;
	private int layout;
	private LayoutInflater layoutInflater;
	

	@SuppressWarnings("deprecation")
	public MySimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
		this.activity = (Activity) context;
		this.mContext = context;
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		type = Typeface.createFromAsset(mContext.getAssets(), Consts.FONT);

		if (view == null) {
			layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(layout, null);
			ViewHolder vh = new ViewHolder(view);
			view.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) view.getTag();

		//выровняй текст
		this.c.moveToPosition(position);
		vh.time.setText(this.c.getString(this.c.getColumnIndex(DB.COLUMN_TIME)));
			//цвет выноси в ресурсы
			vh.time.setTextColor(Color.parseColor("#177AF2"));
			vh.time.setTypeface(type);
		vh.owner.setText(this.c.getString(this.c
				.getColumnIndex(DB.COLUMN_OWNER)));
			vh.owner.setTextColor(Color.parseColor("#636363"));
			vh.owner.setTypeface(type);

		return view;
	}

	public class ViewHolder {

		public final TextView time;
		public final TextView owner;

		public ViewHolder(View view) {
			time = (TextView) view.findViewById(R.id.time);
			owner = (TextView) view.findViewById(R.id.owner);
		}

	}

}
