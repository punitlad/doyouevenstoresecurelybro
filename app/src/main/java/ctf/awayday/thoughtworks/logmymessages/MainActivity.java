package ctf.awayday.thoughtworks.logmymessages;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.os.Build.*;
import static ctf.awayday.thoughtworks.logmymessages.LogcatTags.*;
import static ctf.awayday.thoughtworks.logmymessages.LogcatTags.EXTRA_DATA;

public class MainActivity extends AppCompatActivity {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String FLAG_FOR_CTF = "The Flag is: " + "way_to_leak_data_bro";
    private static final String APP_DIRECTORY = "/logmymessages/";

    private static int entryNum = 0;
    private File logFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            logFile = createLogFile();
        } catch (IOException e) {
            Log.d(MAIN_ACTIVITY, "Cannot create log file");
        }
    }

    @NonNull
    private File createLogFile() throws IOException {
        File sdcard = Environment.getExternalStorageDirectory();
        File directory = new File(sdcard.getAbsolutePath() + APP_DIRECTORY);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.i(MAIN_ACTIVITY, "did create successfully");
            } else {
                Log.i(MAIN_ACTIVITY, "did not create successfully");
            }
        } else {
            Log.i(MAIN_ACTIVITY, "directory already exists");
        }
        File logFile = new File(directory, "secure.log");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        return logFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void sendMessage(View view) throws IOException {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        Intent displayMessageIntent = new Intent(this, DisplayMessageActivity.class);
        displayMessageIntent.putExtra(EXTRA_DATA, message);
        startActivity(displayMessageIntent);

        if (isExternalStorageWritable()) {
            writeToLog(message, logFile);
        }
    }

    private void writeToLog(String message, File logFile) throws IOException {
        FileOutputStream os = new FileOutputStream(logFile, true);
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.append(makeLogCatMessage());
        osw.append(LINE_SEPARATOR);
        osw.append(makeLog(message));
        osw.append(LINE_SEPARATOR);
        osw.flush();
        osw.close();
        os.close();
    }

    @NonNull
    private String makeLogCatMessage() {
        Log.i(CTF_FLAG_TAG, FLAG_FOR_CTF);
        return "Logging Tag -" + CTF_FLAG_TAG;
    }

    private String makeLog(String message) {
        entryNum++;
        String fullMessage = "DEVICE NAME: " + DEVICE + " " +
                "VERSION: " + VERSION.RELEASE + " " +
                "NO:" + entryNum + " " +
                "MESSAGE: " + message;
        return fullMessage;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
