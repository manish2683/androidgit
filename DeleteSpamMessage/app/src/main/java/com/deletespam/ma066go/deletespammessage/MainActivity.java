package com.deletespam.ma066go.deletespammessage;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private Button btnSubmit;
    private Button btnShowBlockedSMS;
    private FileController fileController;
    private TextView txtViewSpamFilter;
    private TextView txtViewWhiteList;
    private Button btnModify;
    private Button btnCancel;
    private EditText spamFilter;
    private EditText whiteListFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeControls();
        CreateFolder();
        fileController = new FileController(this);

        LoadViewMode();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSpamFilterData();
                LoadViewMode();
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadEditMode();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadViewMode();
            }
        });

        btnShowBlockedSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SpamSMS.class);
                startActivity(intent);
            }
        });
    }

    private void InitializeControls(){
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnShowBlockedSMS = (Button)findViewById(R.id.btnShowBlockedSMS);
        spamFilter = (EditText) findViewById(R.id.spamFilter);
        whiteListFilter = (EditText) findViewById(R.id.whitelistFilter);
        txtViewSpamFilter = (TextView)findViewById(R.id.txtViewSpamFilter);
        txtViewWhiteList = (TextView)findViewById(R.id.txtViewWhiteListNumber);
        btnModify = (Button)findViewById(R.id.btnModify);
        btnCancel = (Button)findViewById(R.id.btnCancel);

    }
    private void ViewMode(){
        btnSubmit.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        spamFilter.setVisibility(View.INVISIBLE);
        whiteListFilter.setVisibility(View.INVISIBLE);

        btnShowBlockedSMS.setVisibility(View.VISIBLE);
        txtViewSpamFilter.setVisibility(View.VISIBLE);
        txtViewWhiteList.setVisibility(View.VISIBLE);
        btnModify.setVisibility(View.VISIBLE);
    }

    private void EditMode(){
        btnSubmit.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        spamFilter.setVisibility(View.VISIBLE);
        whiteListFilter.setVisibility(View.VISIBLE);

        btnShowBlockedSMS.setVisibility(View.INVISIBLE);
        txtViewSpamFilter.setVisibility(View.INVISIBLE);
        txtViewWhiteList.setVisibility(View.INVISIBLE);
        btnModify.setVisibility(View.INVISIBLE);
    }

    private void LoadEditMode(){

        File spamFilterFile = fileController.GetFileObject(getString(R.string.spam_filter_folder), getString(R.string.spam_filter_file));
        spamFilter.setText(fileController.ReadContent(spamFilterFile));

        File whiteListFilterFile = fileController.GetFileObject(getString(R.string.spam_filter_folder),getString(R.string.whitelist_file));
        whiteListFilter.setText(fileController.ReadContent(whiteListFilterFile));

        EditMode();
    }

    private void LoadViewMode(){

        File spamFilterFile = fileController.GetFileObject(getString(R.string.spam_filter_folder), getString(R.string.spam_filter_file));
        txtViewSpamFilter.setText(fileController.ReadContent(spamFilterFile));

        File whiteListFilterFile = fileController.GetFileObject(getString(R.string.spam_filter_folder),getString(R.string.whitelist_file));
        txtViewWhiteList.setText(fileController.ReadContent(whiteListFilterFile));

        ViewMode();
    }

    private void CreateFolder(){
        try {

            String state = Environment.getExternalStorageState();
            File spamFilterFolder;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                spamFilterFolder = new File(Environment.getExternalStorageDirectory(), getString(R.string.spam_filter_folder));
            }
            else
            {
                spamFilterFolder = new File(getFilesDir(), getString(R.string.spam_filter_folder));
            }

            if (!spamFilterFolder.exists()) {
                spamFilterFolder.mkdir();
            }
            try {
                File spamFilterFile = new File(spamFilterFolder, getString(R.string.spam_filter_file));
                if (!spamFilterFile.exists())
                    spamFilterFile.createNewFile();
                File whiteListFilterFile = new File(spamFilterFolder, getString(R.string.whitelist_file));
                if (!whiteListFilterFile.exists())
                    whiteListFilterFile.createNewFile();
            } catch (Exception ex) {
            }

        } catch (Exception e) {
        }
    }

    private void SaveSpamFilterData() {
        File spamFilterFile = fileController.GetFileObject(getString(R.string.spam_filter_folder), getString(R.string.spam_filter_file));
        fileController.WriteCotent(spamFilterFile, spamFilter.getText().toString(), false );

        File whiteListFilterFile = fileController.GetFileObject(getString(R.string.spam_filter_folder),getString(R.string.whitelist_file));
        fileController.WriteCotent(whiteListFilterFile, whiteListFilter.getText().toString(), false );
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
