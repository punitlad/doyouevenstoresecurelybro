package ctf.awayday.thoughtworks.doyouevenstoresecurelybro;

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

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "ctf.awayday.tw.MESSAGE";
    public static final String CTF_FLAG_TAG = "ctf.awayday.tw.CTF_FLAG";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String FLAG_FOR_CTF = "The Flag is: way_to_leak_data_bro";
    private File logFile;
    private static int entryNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            logFile = createLogFile();
        } catch (IOException e) {
            Log.d("MainActivity", "Cannot Create Log File");
        }
    }

    @NonNull
    private File createLogFile() throws IOException {
        File sdcard = Environment.getExternalStorageDirectory();
        File directory = new File(sdcard.getAbsolutePath() + "/doyouevenlogsecurelybro/");
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.i("MainActivity", "did create successfully");
            } else {
                Log.i("MainActivity", "did not create successfully");
            }
        } else {
            Log.i("MainActivity", "directory already exists");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearch() {
        Intent displayMessageIntent = new Intent(this, DisplayMessageActivity.class);
        displayMessageIntent.putExtra(EXTRA_DATA, "search");
        startActivity(displayMessageIntent);
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
        return "Sent Message to LOGCAT";
    }

    private String makeLog(String message) {
        entryNum++;
        String fullMessage =
                "DEVICE NAME: " + DEVICE + " " +
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
