package pl.idedyk.android.japaneselearnhelper.problem;

import android.content.Intent;

public class ReportProblem {
	
	public static Intent createReportProblemIntent(String mailSubject, String mailBody, String versionName, int versionCode) {
		
		Intent email = new Intent(Intent.ACTION_SEND);
		
		mailBody += "---\nVersion code: " + versionCode + "\nVersion name: " + versionName + "\n";
		
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { "fryderyk.mazurek@gmail.com" } );
		
		email.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
		email.putExtra(Intent.EXTRA_TEXT, mailBody);
		
		email.setType("message/rfc822");
		
		return email;
	}
}
