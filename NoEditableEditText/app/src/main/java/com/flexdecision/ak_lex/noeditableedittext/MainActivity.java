package com.flexdecision.ak_lex.noeditableedittext;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NoEditableEditText someNoEditET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        someNoEditET = findViewById(R.id.someNoEditET);
        someNoEditET.setText(getResources().getString(R.string.mascot));
        someNoEditET.setCursorVisible(false);
        someNoEditET.setTextIsSelectable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            someNoEditET.setShowSoftInputOnFocus(false);
        }else{
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        someNoEditET.setOnClickListener(v-> showToast());

    }

    private void showToast() {
        Toast.makeText(this, "Clicked: " + someNoEditET.getSelectionStart() + ", " +
                someNoEditET.getSelectionEnd(), Toast.LENGTH_SHORT).show();
    }
}
