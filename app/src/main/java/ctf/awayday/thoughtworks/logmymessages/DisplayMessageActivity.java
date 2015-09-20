package ctf.awayday.thoughtworks.logmymessages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import static ctf.awayday.thoughtworks.logmymessages.LogcatTags.EXTRA_DATA;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_DATA);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
    }
}
