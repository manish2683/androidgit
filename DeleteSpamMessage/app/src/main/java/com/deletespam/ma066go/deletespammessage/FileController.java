package com.deletespam.ma066go.deletespammessage;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by MA066GO on 6/13/2015.
 */
public class FileController {

    private Context context;

    public FileController(Context _context){
        context = _context;
    }

    public String ReadContent(File fileToRead) {
        //Read text from file
        StringBuilder spamFilterContent = new StringBuilder();
        String fileContent = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                spamFilterContent.append(line);
            }
            bufferedReader.close();
            fileContent = spamFilterContent.toString();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return fileContent;
    }

    public void WriteCotent(File fileToWrite, String content, boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(fileToWrite, append);
            if (append)
                fileWriter.append(content);
            else
                fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void DeleteContent(File file){
        WriteCotent(file,"",false);
    }

    public File GetFileObject(String directoryName, String fileName){
        String state = Environment.getExternalStorageState();
        File folder;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            folder = new File(Environment.getExternalStorageDirectory(), directoryName);
        } else {
            folder = new File(context.getFilesDir(), directoryName);
        }
        return new File(folder, fileName);
    }
}
