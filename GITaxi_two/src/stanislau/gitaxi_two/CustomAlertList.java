package stanislau.gitaxi_two;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAlertList extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] operators;
	private final Integer[] imageId;
	Typeface type;
	private static String FONT = "HelveticaNeueCyr-Light.otf";

	public CustomAlertList(Activity context, String[] operators, Integer[] imageId) {
		super(context, stanislau.gitaxi_two.R.layout.item, operators);
		this.context = context;
		this.operators = operators;
		this.imageId = imageId;

	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(stanislau.gitaxi_two.R.layout.item,
				null);
		type = Typeface.createFromAsset(context.getAssets(), FONT);
		TextView oper = (TextView) rowView
				.findViewById(stanislau.gitaxi_two.R.id.operator);
		ImageView im_op = (ImageView) rowView   
				.findViewById(stanislau.gitaxi_two.R.id.image_op);
		oper.setText(operators[pos]);
		oper.setTypeface(type);
		im_op.setImageResource(imageId[pos]);

		return rowView;
	}
}
