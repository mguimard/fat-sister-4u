package com.docdoku.hackaton.hackathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class MainActivity extends Activity {

    private Button buttonCommand;

    private TextView speechCommand;
    private Socket mSocket;
    private boolean isPolite = false;
    private TTS textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket("http://192.168.1.15:3000").connect();
            attemptConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        displaySpeechRecognizer();
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                textToSpeech = new TTS(getApplicationContext());
                speechCommand = (TextView) stub.findViewById(R.id.speechTextView);
            }
        });
    }

    private void sendServerResponse(String msg) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        textToSpeech.say(msg);
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    private void attemptConnection() {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Auth auth = new Auth("1", "password");
        try {
            String json = ow.writeValueAsString(auth);
            mSocket.emit("auth", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            if (!isPolite && spokenText.contains("ok ma gueule")) {
                sendServerResponse("Salut ma gueule, que puis-je faire pour toi aujourd'hui ?");
                isPolite = true;
                displaySpeechRecognizer();
                return;
            }
            if (!isPolite) {
                sendServerResponse("Et bah alors on est pas poli ma gueule ?!");
                displaySpeechRecognizer();
                return;
            }
            if (spokenText.contains("mettre à jour mon projet")) {
                sendServerResponse("Git pull lancé");
                displaySpeechRecognizer();
                return;
            }
            sendServerResponse("J'ai rien compris ma gueule !");
            displaySpeechRecognizer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
