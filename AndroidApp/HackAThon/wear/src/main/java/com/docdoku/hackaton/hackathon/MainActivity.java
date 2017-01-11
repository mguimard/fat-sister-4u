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
    private final String GIT_PULL_PROJECT_KEYWORD = "PULL_REPO";
    private final String LAUNCH_IDE_KEYWORD = "IDE";
    private final String LAUNCH_CHROME_KEYWORD = "WEB";
    private final String LAUNCH_SHELL_KEWORD = "TERMINAL";
    private final String SHUTDOWN_COMPUTER_KEYWORD = "HALT";
    private final String RESTART_COMPUTER_KEYWORD = "REBOOT";
    private final String WORK_KEYWORD = "WORK";
    private final String SLACK_KEYWORD = "SLACK";
    private Button buttonCommand;

    private TextView speechCommand;
    private Button buttonTerminal;
    private Button buttonOff;
    private Button buttonReboot;
    private Button buttonWeb;
    private Button buttonIde;
    private Button buttonPull;
    private Button buttonWork;
    private Button buttonSlack;

    private Socket mSocket;
    private boolean isPolite = false;
    private AvailableCommand availableCommand;
    //private TTS textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket("http://192.168.1.15:3000").connect();
            attemptConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        availableCommand = new AvailableCommand();
        //displaySpeechRecognizer();
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //textToSpeech = new TTS(getApplicationContext());
                buttonTerminal = (Button) stub.findViewById(R.id.buttonTerminal);
                buttonIde = (Button) stub.findViewById(R.id.buttonIde);
                buttonOff = (Button) stub.findViewById(R.id.buttonOff);
                buttonPull = (Button) stub.findViewById(R.id.buttonPull);
                buttonReboot = (Button) stub.findViewById(R.id.buttonReboot);
                buttonWeb = (Button) stub.findViewById(R.id.buttonWeb);
                buttonWork = (Button) stub.findViewById(R.id.buttonWork);
                buttonSlack = (Button) stub.findViewById(R.id.buttonSlack);


                buttonTerminal.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(LAUNCH_SHELL_KEWORD);
                        sendRequest(args);
                    }
                });
                buttonIde.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(LAUNCH_IDE_KEYWORD);
                        sendRequest(args);
                    }
                });
                buttonOff.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(SHUTDOWN_COMPUTER_KEYWORD);
                        sendRequest(args);
                    }
                });
                buttonPull.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(GIT_PULL_PROJECT_KEYWORD);
                        sendRequest(args);
                    }
                });
                buttonReboot.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(RESTART_COMPUTER_KEYWORD);
                        sendRequest(args);
                    }
                });
                buttonWeb.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(LAUNCH_CHROME_KEYWORD);
                        sendRequest(args);
                    }
                });

                buttonWork.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(WORK_KEYWORD);
                        sendRequest(args);
                    }
                });
                buttonSlack.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Args args = new Args(SLACK_KEYWORD);
                        sendRequest(args);
                    }
                });
            }
        });
    }

    private void sendRequest(Args command) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        try {
            mSocket.emit("command", command.toJson());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't send event :" + command.getCommand());
        }
    }

    private void showResponse(String respnse) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, respnse, duration);
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
                showResponse("Salut ma gueule, que puis-je faire pour toi aujourd'hui ?");
                isPolite = true;
                displaySpeechRecognizer();
                return;
            }
            if (!isPolite) {
                showResponse("Et bah alors on est pas poli ma gueule ?!");
                displaySpeechRecognizer();
                return;
            }

            Args command = availableCommand.foundCommand(spokenText);
            if (command != null) {
                sendRequest(command);
            } else {
                showResponse("J'ai rien compris ma gueule !");
            }
            displaySpeechRecognizer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
