package com.docdoku.hackaton.hackathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
                speechCommand = (TextView) stub.findViewById(R.id.speechTextView);
                buttonCommand = (Button) stub.findViewById(R.id.launchCommandButton);
                buttonCommand.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        onClickEvent();
                    }
                });
            }
        });
    }

    private void onClickEvent() {
        Context context = getApplicationContext();
        CharSequence text = "Send auth";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
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
            speechCommand.setText(spokenText);
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
