package pl.idedyk.android.japaneselearnhelper.info;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class InfoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);

        JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.info);
        
        JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_info));

        TextView titleVersion = (TextView)findViewById(R.id.info_title_version);
        
        String versionName = "";
        int versionCode = 0;
        
        try {
        	PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        	
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

        } catch (NameNotFoundException e) {        	
        }
        
        titleVersion.setText(getString(R.string.info_version, versionCode, versionName));
        
        TextView infoBody = (TextView)findViewById(R.id.info_body);
        
        infoBody.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
