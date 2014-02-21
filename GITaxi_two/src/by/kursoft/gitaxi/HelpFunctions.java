package by.kursoft.gitaxi;

import stanislau.gitaxi_two.R;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class HelpFunctions {

	public static void showAlertCrouton(Activity activity, String message) {
		Crouton.showText(activity, message, Style.ALERT, R.id.listMode);
	}

	public static void showConfirmCrouton(Activity activity, String message) {
		Crouton.showText(activity, message, Style.CONFIRM, R.id.listMode);
	}
	
	public static void dial(String tel, Activity activity) {
		activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(tel)));
	}
}
