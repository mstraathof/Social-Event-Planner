package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jack on 10/7/2017.
 */

public class logout extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_layout);
        
        Intent input = getIntent();
        String topLabel = input.getStringExtra("topLabel");
        final TextView tv = (TextView) findViewById(R.id.topLabel);
        tv.setText(topLabel);

        // The OK button gets the text from the input box and returns it to the calling activity
        Button bOk = (Button) findViewById(R.id.buttonOk);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
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
