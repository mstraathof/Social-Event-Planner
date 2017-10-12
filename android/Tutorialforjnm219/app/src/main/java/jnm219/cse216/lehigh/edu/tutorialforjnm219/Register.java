package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jack on 10/3/2017.
 */

public class Register extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        //Get top label from the calling activity, and put it in a TextView
        Intent input = getIntent();
        String topLabel = input.getStringExtra("topLabel");
        String tvUsername = input.getStringExtra("username");
        String tvPassword = input.getStringExtra("password");
        final TextView tv = (TextView) findViewById(R.id.topLabel);
        final TextView us = (TextView) findViewById(R.id.userName);
        final TextView ps = (TextView) findViewById(R.id.password);
        tv.setText(topLabel);
        us.setText(tvUsername);
        ps.setText(tvPassword);
        //Creates EditText input elements for the username and passsword variables
        final EditText username = (EditText) findViewById(R.id.enterUserName);
        final EditText password = (EditText) findViewById(R.id.enterPassword);
        final EditText realName = (EditText) findViewById(R.id.enterRealName);
        final EditText email = (EditText) findViewById(R.id.enterEmail);

        //The OK Button gets the text from the inut box and returns it to the calling activity
        Button bOK = (Button) findViewById(R.id.buttonOk);
        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Create an intent only when the user entered text in both fields
                if(!password.getText().toString().equals("") && !username.getText().toString().equals("") && !realName.getText().toString().equals("") && !email.getText().toString().equals("")){
                    Intent i = new Intent();
                    i.putExtra("resultUserName", username.getText().toString());
                    i.putExtra("resultPassword", password.getText().toString());
                    i.putExtra("resultRealName", realName.getText().toString());
                    i.putExtra("resultEmail", email.getText().toString());

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
