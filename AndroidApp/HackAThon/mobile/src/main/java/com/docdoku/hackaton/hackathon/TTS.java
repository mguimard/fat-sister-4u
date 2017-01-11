package com.docdoku.hackaton.hackathon;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * TTS helper
 * <p/>
 * Usage (as easy as abc):
 * <p/>
 * TTS tts = new TTS(getApplicationContext());
 * tts.say("Salut mes ptites beautés");
 */
public class TTS {

    private TextToSpeech t;

    public TTS(Context context) {
        t = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                System.out.println("onInit called with status = " + status);
                if (status != TextToSpeech.ERROR) {
                    t.setLanguage(Locale.FRENCH);
                }
            }
        });
    }

    public void say(String s) {
        t.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
