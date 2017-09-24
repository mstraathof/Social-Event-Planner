package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateBuzzActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Get top label from the calling activity, and put it in TextView
        Intent input = getIntent();
        String topLabel = input.getStringExtra("topLabel");
        final TextView tv = (TextView) findViewById(R.id.topLabel);
        tv.setText(topLabel);

        final EditText et = (EditText) findViewById(R.id.enterSubject);
        final EditText em = (EditText) findViewById(R.id.enterMessage);

        // The OK button gets the text from the input box and returns it to the calling activity
        Button bOk = (Button) findViewById(R.id.buttonOk);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent only when the user entered text in both fields
                if (!et.getText().toString().equals("") && !em.getText().toString().equals("")) {
                    Intent i = new Intent();
                    i.putExtra("resultSubject", et.getText().toString());
                    i.putExtra("resultMessage", em.getText().toString());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });

        // The Cancel button returns to the caller without sending it any data
        Button bCancel = (Button) findViewById(R.id.buttonCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
