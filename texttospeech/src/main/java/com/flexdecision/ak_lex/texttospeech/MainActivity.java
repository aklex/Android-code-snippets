package com.flexdecision.ak_lex.texttospeech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private TTS tts;
    private EditText textET;
    private Spinner kSpinner;
    private ArrayAdapter<Locale> sAdapter;

    private ImageButton speechIB;

    private ImageButton stopIB;


    private List<Locale> keyboardLocales = new ArrayList<>();

    private ButtonState playBtnState;

    class ButtonState{
        private boolean state;
        public ButtonState(boolean state){
            this.state = state;
        }

        public boolean isSpeaking(){
            return state;
        }

        public void setSpeakingState(boolean state){
            this.state = state;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kSpinner = findViewById(R.id.keySpinner);
        sAdapter = new ArrayAdapter<Locale>(this, R.layout.support_simple_spinner_dropdown_item, keyboardLocales);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kSpinner.setAdapter(sAdapter);

        textET = findViewById(R.id.textET);

        tts = new TTS(this);
        tts.setOnTTSLaunched(new TTS.OnTTSLaunched() {
            @Override
            public void onInitializeData() {
                postTTSUIInitialization();
            }

        });

        speechIB = findViewById(R.id.speechIB);
        speechIB.setOnClickListener(v-> speechText());

        stopIB = findViewById(R.id.stopIB);
        stopIB.setOnClickListener(v -> stopSpeech());

        playBtnState = new ButtonState(false);
    }

    private void postTTSUIInitialization() {
        Log.d(TAG, "Start action");
        Locale locales[] = Locale.getAvailableLocales();
        for(Locale i: locales){
            //Log.d(TAG, "Keyboard: " + TTS.printLocale(i) + " == New Locale: " + TTS.printLocale(newLocale));
            if (tts.isTTSLangAvailable(i)){
                //Log.d(TAG, "Keyboard: " + TTS.printLocale(i) + " - Available");
                keyboardLocales.add(i);
            }
        }
        sAdapter.notifyDataSetChanged();
    }

    private void speechText() {
        if (tts == null){
            Log.d(TAG, "Wrong tts initialization");
            return;
        }
        Locale selectedLocale = (Locale) kSpinner.getSelectedItem();
        if (!tts.getCurrentTTSLocale().toString().toLowerCase().equals(selectedLocale.toString().toLowerCase())) {
            Log.d(TAG, "Other locale, have to change");
            if (tts.isTTSLangAvailable(selectedLocale)){
                tts.changeTTSLanguage(selectedLocale);
                Log.d(TAG, "Was changed successful!!!!!!!!!");
            }else{
                Log.d(TAG, "Locale wasn't found, something wrong in logic: " + selectedLocale);
                return;
            }
        }else{
            Log.d(TAG, "The same local!!!!");
        }

        tts.speak(textET.getText().toString().trim());
    }



    private void stopSpeech() {
        Log.d(TAG, "Stop speech");
        tts.stopSpeech();
        speechIB.setImageResource(R.drawable.ic_action_play);
        playBtnState.setSpeakingState(false);
    }


    @Override
    protected void onDestroy() {
        tts.destroy();
        super.onDestroy();
    }

    /*private void speechTextCurrentKeyboard(String text) {
        if (tts == null){
            Log.d(TAG, "Wrong tts initialization");
            return;
        }
        String keyboardLocale = TTS.getCurrentKeyboardLocale(this);
        if (!tts.getCurrentTTSLocale().toString().toLowerCase().equals(keyboardLocale.toLowerCase())) {

            Locale newLocale = TTS.createLocale(keyboardLocale);
            if (newLocale == null){
                Log.d(TAG, "Wrong keyboard local: " + keyboardLocale);
                return;
            }
            Log.d(TAG, "Keyboard: " + keyboardLocale + " ; newLocale: " + newLocale.toString());

            Log.d(TAG, "Other locale, have to change");
            if (tts.isTTSLangAvailable(newLocale)){
                tts.changeTTSLanguage(newLocale);
                Log.d(TAG, "Was changed successful!!!!!!!!!");
            }else{
                Log.d(TAG, "Locale wasn't found, something wrong in logic: " + newLocale);
                return;
            }
        }else{
            Log.d(TAG, "The same local!!!!");
        }
        Log.d(TAG, "saying ..." + text);
        tts.prepareNextSentence(text);
    }*/
}
