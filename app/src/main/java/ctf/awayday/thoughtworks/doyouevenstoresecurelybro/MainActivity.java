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

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "ctf.awayday.thoughtworks.doyouevenstoresecurelybro.MESSAGE";
    private File logFile;

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
        File directory = new File(sdcard.getAbsolutePath() + "/doyouevenstoresecurelybro/");
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.d("MainActivity", "did create successfully");
            } else {
                Log.d("MainActivity", "did not create successfully");
            }
        }
        File logFile = new File(directory, "secure.log");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        return logFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        os.write(message.getBytes());
        os.close();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
