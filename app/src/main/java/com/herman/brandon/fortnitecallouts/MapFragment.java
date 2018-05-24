package com.herman.brandon.fortnitecallouts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.herman.brandon.fortnitecallouts.Helpers.FortniteMapHelper;

import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private TextToSpeech tts;
    private TextView travelResult;
    private static final int SPEECH_REQUEST_CODE = 0;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create text to speech object
        tts = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });

        tts.setPitch(1.15f);
        tts.setSpeechRate(1.25f);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ImageView map = view.findViewById(R.id.map_image);
        Glide.with(this)
                .load(R.drawable.map_season_4)
                .into(map);

        travelResult = view.findViewById(R.id.travel_time);

        FloatingActionButton fab = view.findViewById(R.id.callout_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

        return view;
    }

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
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Double time = FortniteMapHelper.parseVoiceCommandResults(results);

            if (time == -1) {
                String response = "Error parsing request";
                speak(response);
                travelResult.setText(response);
            } else {
                speakTravelTime(time);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Uses text to speech to announce travel time
    public void speakTravelTime(Double time) {
        String res = "";
        if (time < 60) {
            speak("Travel time is " + String.format(Locale.ENGLISH,"%.2f", time) + " seconds");
             res = String.format(Locale.ENGLISH,"%.2f", time) + "s";
        } else if (time > 60) {
            int minutes = (int)(time / 60);
            double seconds = time % 60;

            res = minutes + "m " + String.format(Locale.ENGLISH,"%.2f", seconds) + "s";
            if(minutes == 1) {
                speak("Travel time is " + minutes + " minute and " + String.format(Locale.ENGLISH,"%.2f", seconds) + " seconds");
            } else {
                speak("Travel time is " + minutes + " minutes and " + String.format(Locale.ENGLISH,"%.2f", seconds) + " seconds");
            }
        }

        travelResult.setText(res);
    }

    // Actually call tts method
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onDetach() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDetach();
    }
}
