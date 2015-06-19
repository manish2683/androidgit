package com.deletespam.ma066go.deletespammessage;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;


public class SpamSMS extends ActionBarActivity {

    private TextView blockedSMS;
    private Button btnClean;
    FileController fileController;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam_sms);
        fileController = new FileController(this);
       DisplayBlockedSMS();
        btnClean = (Button)findViewById(R.id.btnClear);
        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File discardedMsgFile = fileController.GetFileObject(getString(R.string.spam_filter_folder),getString(R.string.discardedMessage_file));
                fileController.DeleteContent(discardedMsgFile);
                DisplayBlockedSMS();
            }
        });

        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayBlockedSMS();
            }
        });

    }

    private void DisplayBlockedSMS(){
        blockedSMS = (TextView)findViewById(R.id.blockedSMS);
        File discardedMsgFile = fileController.GetFileObject(getString(R.string.spam_filter_folder),getString(R.string.discardedMessage_file));
        blockedSMS.setText(fileController.ReadContent(discardedMsgFile).replace("\\n", "\n"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spam_sm, menu);
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
