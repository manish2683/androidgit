package com.readpermission.ma066go.readpermission;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReadPErmission();
    }

    private void ReadPErmission(){
        StringBuffer appNameAndPermissions=new StringBuffer();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : packages) {
            Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);
            try {
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
                    appNameAndPermissions.append(packageInfo.packageName + "*:\n");
                    //Get Permissions
                    String[] requestedPermissions = packageInfo.requestedPermissions;
                    if (requestedPermissions != null) {
                        for (int i = 0; i < requestedPermissions.length; i++) {
                            Log.d("test", requestedPermissions[i]);
                            appNameAndPermissions.append(requestedPermissions[i] + "\n");
                        }
                        appNameAndPermissions.append("\n");
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }}
        TextView permission = (TextView)findViewById(R.id.textView);
        permission.setText(appNameAndPermissions.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
