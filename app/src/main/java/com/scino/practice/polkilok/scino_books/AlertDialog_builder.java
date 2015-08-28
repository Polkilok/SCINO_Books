package com.scino.practice.polkilok.scino_books;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialog_builder {
	static public final int DIALOG_ACCEPT_REMOVE = 1;
	static public final int DIALOG_WARNING_CATEGORY_IS_EXIST = 2;
	static public final int DIALOG_SUCCESSFULLY = 3;
	static public final int DIALOG_INCORRECT_DATA = 4;
	static public final int DIALOG_ACTION_IMPOSSIBLE = 5;
	static private final int CRITIC_FAIL_COUNT = 5;
	static private int fail_counter = 0;

	public static AlertDialog getDialog(Activity activity, int ID, String item_name, final extra_action act) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		switch (ID) {
			case DIALOG_ACCEPT_REMOVE:
				builder.setTitle(R.string.warning_accept_remove);
				builder.setMessage(item_name);
				builder.setCancelable(true);
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						act.action();
					}
				});
				builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // Кнопка ОК
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				return builder.create();
			case DIALOG_WARNING_CATEGORY_IS_EXIST:
				fail_counter++;
				builder.setTitle(R.string.warning_is_exist);
				builder.setMessage(item_name);
				builder.setCancelable(true);
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				return builder.create();
			case DIALOG_SUCCESSFULLY:
				builder.setTitle(R.string.warning_is_successful);
				builder.setCancelable(true);
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				return builder.create();
			case DIALOG_INCORRECT_DATA:
				fail_counter++;
				builder.setTitle(R.string.warning_incorrect_data);
				builder.setCancelable(true);
				if (fail_counter > CRITIC_FAIL_COUNT)
					builder.setMessage(R.string.warning_you_are_tester);
				if (item_name != null)
					builder.setMessage(item_name);
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				return builder.create();
			case DIALOG_ACTION_IMPOSSIBLE:
				fail_counter++;
				builder.setTitle(R.string.warning_mission_is_impossible);
				builder.setCancelable(true);
				if (fail_counter > CRITIC_FAIL_COUNT)
					builder.setMessage(R.string.warning_you_are_tester);
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				return builder.create();
		}
		return builder.create();
	}

	interface extra_action {
		void action();
	}
}
