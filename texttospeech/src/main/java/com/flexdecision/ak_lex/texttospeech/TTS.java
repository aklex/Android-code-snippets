package com.flexdecision.ak_lex.texttospeech;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by a_Lex on 3/7/2018.
 */

public class TTS {
    private TextToSpeech tts;
    private List<Locale> ttsLocales = new ArrayList<>();
    public static final String TAG = TTS.class.getSimpleName();
    public static final String delimiter = "_";

    public interface OnTTSLaunched{
        void onInitializeData();
    }
    private OnTTSLaunched onTTSLaunched;

    public void onInitializeData(){
        this.onTTSLaunched.onInitializeData();
    }


    public void setOnTTSLaunched(OnTTSLaunched onTTSLaunched){
        this.onTTSLaunched = onTTSLaunched;
    }

    public TTS(final Context context){
        this.tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    Log.d(TAG, "TTS initialized");
                    printLocalInfo("Current TTS", getCurrentTTSLocale());

                    Log.d(TAG, getCurrentKeyboardLocale(context));

                    //printLocalInfo("Keyboard", locale);

                    //changeTTSLanguage(locale);

                    setAllTTSLocales();
                    //printAllTTSLocales();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Log.d(TAG, "Max length of String: " + tts.getMaxSpeechInputLength());
                    }


                    printLocalInfo("Current TTS", getCurrentTTSLocale());
                    Log.d(TAG, "Hash: " + tts.hashCode());

                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            Log.d(TAG, "onStart: " + utteranceId);
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.d(TAG, "onDone: " + utteranceId);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.d(TAG, "onError: " + utteranceId);
                        }

                        @Override
                        public void onStop(String utteranceId, boolean interrupted) {
                            super.onStop(utteranceId, interrupted);
                            Log.d(TAG, "onStop: " + utteranceId);
                        }
                    });

                    onInitializeData();
                }else{
                    Log.e(TAG, "TTS fault");
                }
            }
        });
    }

    public boolean changeTTSLanguage(Locale locale) {
        int result = tts.setLanguage(locale);
        Log.d(TAG, "Result TTS: " + result);
        if (result == TextToSpeech.LANG_AVAILABLE){
            Log.d(TAG, "Language is available for the language by the locale but not the country and variant: "
                    + locale.toString() +" : " + locale.getLanguage() + "_" + locale.getCountry());
            return true;
        }
        if (result == TextToSpeech.LANG_COUNTRY_AVAILABLE ){
            Log.d(TAG, "Language is available for the language and country specified by the locale, but not the variant.: "
                    + locale.toString() +" : " + locale.getLanguage() + "_" + locale.getCountry());
            return true;
        }
        if (result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE ){
            Log.d(TAG, "Language is available exactly as specified by the locale.: "
                    + locale.toString() +" : " + locale.getLanguage() + "_" + locale.getCountry());
            return true;
        }
        if (result == TextToSpeech.LANG_NOT_SUPPORTED){
            Log.d(TAG, "Language is not supported: " + locale.toString() +" : " +
                    locale.getLanguage() + "_" + locale.getCountry());
            return false;
        }
        if (result == TextToSpeech.LANG_MISSING_DATA){
            Log.d(TAG, "Language data is missing.: " + locale.toString() +" : " +
                    locale.getLanguage() + "_" + locale.getCountry());
            return false;
        }
        return false;
    }

    private void printLocalInfo(String title, Locale locale){
        Log.d(TAG, title + ": " +locale.toString() +" : "+ locale.getLanguage() +"_"+ locale.getCountry());
    }


    /*public boolean isTTSLangAvailable(Locale newLocale){
        if (tts.isLanguageAvailable(newLocale) == TextToSpeech.LANG_AVAILABLE){
            *//*Log.d(TAG, "Language is available for the language by the locale, but not the country and variant: "
                    + newLocale.toString());*//*
            return true;
        }else if (tts.isLanguageAvailable(newLocale) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
            *//*Log.d(TAG, "Language is available for the language and country specified by the locale, but not the variant: "
                    + newLocale.toString());*//*
            return true;
        }else if (tts.isLanguageAvailable(newLocale) == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
            *//*Log.d(TAG, "Language is available exactly as specified by the locale: "
                    + newLocale.toString());*//*
            return true;
        }else {
            *//*Log.d(TAG, "GoogleLanguage doesn't available: " + newLocale.toString());*//*
            return false;
        }
    }*/

    public boolean isTTSLangAvailable(Locale newLocale){
        if (tts.isLanguageAvailable(newLocale) == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
            /*Log.d(TAG, "Language is available exactly as specified by the locale: "
                    + newLocale.toString());*/
            return true;
        }else if (tts.isLanguageAvailable(newLocale) == TextToSpeech.LANG_COUNTRY_AVAILABLE && newLocale.getVariant().isEmpty()) {
            /*Log.d(TAG, "Language is available for the language and country specified by the locale, but not the variant: "
            + newLocale.toString());*/
            return true;
        }else if (tts.isLanguageAvailable(newLocale) == TextToSpeech.LANG_AVAILABLE && newLocale.getCountry().isEmpty() && newLocale.getVariant().isEmpty()) {
            /*Log.d(TAG, "Language is available exactly as specified by the locale: "
            + newLocale.toString());*/
            return true;
        }else {
            //Log.d(TAG, "GoogleLanguage doesn't available: " + newLocale.toString());
            return false;
        }
    }

    public void setAllTTSLocales(){
        if (ttsLocales.isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ttsLocales.addAll(tts.getAvailableLanguages());
            } else {
                List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());

                for(Locale i: locales){
                    if (tts.isLanguageAvailable(i) >= 0){
                        ttsLocales.add(i);
                    }
                }

                /*for (Locale i : locales) {
                    if (tts.isLanguageAvailable(i) == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                            tts.isLanguageAvailable(i) == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
                        if (i.getVariant().isEmpty() && !i.getCountry().isEmpty()) {
                            ttsLocales.add(i);
                        }
                    }*//*else if (tts.isLanguageAvailable(i) == TextToSpeech.LANG_AVAILABLE) {
                        if (i.getVariant().isEmpty() && i.getCountry().isEmpty()) {
                            ttsLocales.add(i);
                        }
                    }*//*
                }*/
            }
        }
    }

    public List<Locale> getTTSLocales(){
        return ttsLocales;
    }

    public void printAllTTSLocales(){
        List<Locale> locales = getTTSLocales();
        for (Locale i: locales){
            Log.d(TAG, i.toString() + " : " + i.getLanguage() + "_" + i.getCountry() + "_" + i.getVariant());
        }
    }

/*    public Set<Locale> getSupportedTTSLocales(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return tts.getAvailableLanguages();
        }else{
            return tts.getVoices();
        }
    }*/

    public static String getCurrentKeyboardLocale(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
        String localeString;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            localeString = changeDelimiters(ims.getLanguageTag());
            if (localeString == null || localeString.isEmpty()){
                //getLanguageTag() has a bug, doesn't show language, using deprecated method
                localeString = ims.getLocale();
                Log.d(TAG, "with variants: " + ims.getLocale() + " , " + ims.getLanguageTag());
            }
        }else{
            localeString = ims.getLocale();
        }

        Log.d(TAG, "current keyboard lang is: " + localeString);
        return localeString;
    }

    private static String changeDelimiters(String str) {
        return str.replace('-', '_');
    }

    public void speak(String str){
        if(!str.isEmpty()){
            Log.d(TAG, "Speaking..." + str);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ttsSpeak21(str);
            else
                ttsSpeakUnder21(str);
        }
    }
    @SuppressWarnings("deprecation")
    private void ttsSpeakUnder21(String text){
        HashMap<String,String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        int result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        if (result == TextToSpeech.ERROR){
            Log.d(TAG, "Can't say");
        }else {
            Log.d(TAG, tts.getLanguage().toString() +": " + tts.getLanguage().getLanguage() + "_" + tts.getLanguage().getCountry() + " ...speaking!");
        }
    }

    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsSpeak21(String text){

        String utteranceId = this.hashCode() + "";
        int result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        if (result == TextToSpeech.ERROR){
            Log.d(TAG, "Can't say");
        }else {
            String lang = tts.getVoice().getLocale().getLanguage();
            String country = tts.getVoice().getLocale().getCountry();
            String variant = tts.getVoice().getLocale().getVariant();
            String voice = tts.getVoice().getName();
            Log.d(TAG, tts.getVoice().getLocale().toString() +": " +lang + "_" + country + ", voice: "+ voice+" ...speaking!");
        }
    }*/

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsSpeak21(String text){

        String utteranceId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        int result = tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
        if (result == TextToSpeech.ERROR){
            Log.d(TAG, "Can't say");
        }else {

            Log.d(TAG, tts.getVoice().getLocale().toString() +": " + " ...speaking!");
        }
    }

    /*public boolean compareLocales(Context context){
        String keyboardLocaleStr = getCurrentKeyboardLocale(context).toString();
        String currentTTSLocalStr = getCurrentTTSLocale().toString();

        return currentTTSLocalStr.equals(keyboardLocaleStr);
    }*/

    public Locale getCurrentTTSLocale(){
        Locale currentTTSLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            currentTTSLocale = tts.getVoice().getLocale();
        }else{
            currentTTSLocale = tts.getLanguage();
        }
        return currentTTSLocale;
    }



    public void destroy() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }


    public static Locale createLocale(String str){
        List<String> elements = Arrays.asList(str.split(delimiter));

        /*for (String i: elements){
            Log.d(TAG, i);
        }*/

        if (elements.size() == 1 && (elements.get(0).length() == 2 || elements.get(0).length() == 3)){
            //Log.d(TAG, "Only language: " + str);
            return new Locale(elements.get(0).toLowerCase());
        }
        else if (elements.size() == 2 && (elements.get(0).length() == 2 || elements.get(0).length() == 3) &&
                ((elements.get(1).length() == 2 || elements.get(1).length() == 3))){
            //Log.d(TAG, "language and country " + str);
            return new Locale(elements.get(0).toLowerCase(), elements.get(1).toUpperCase());
        }
        else if (elements.size() > 2) {
            //Log.d(TAG, "language, country, variants: " + str);
            return new Locale(elements.get(0).toLowerCase(), elements.get(1).toUpperCase(), elements.get(2));
        }
        else {
            //Log.d(TAG, "null");
            return null;
        }

    }

    public static String printLocale(Locale locale){
        if (locale == null){
            Log.d(TAG, "null");
            return "";
        }
        return locale.toString() + " : " + locale.getLanguage() + "_" + locale.getCountry() +
                "_" + locale.getVariant();
    }

    public void stopSpeech(){
        if (tts.isSpeaking())
            tts.stop();
    }




}

