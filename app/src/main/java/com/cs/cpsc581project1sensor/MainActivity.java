package com.cs.cpsc581project1sensor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs.cpsc581project1sensor.audio.record.AmplitudeClipListener;
import com.cs.cpsc581project1sensor.audio.util.AudioUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Toast toasty;
    ImageButton ibDoor;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;

    int doorSelected = 0;               //Door to use as image  0 = no door,    1 = tardis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);

        checkPermission();
        linkScreenElements();
        createListeners();
        Log.d("zzz", "listenign for speech");
        createSpeechRecognizer();
        Log.d("zzz", "end speech");
    }

    private class startClapper extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(final Void... voids) {
            try{
                Clapper clapper = new Clapper();
                boolean clapDetected = clapper.recordClap();

                Log.d("zzz", "Clapper started");
                return(clapDetected);
            }
            catch (java.io.IOException e){ Log.d("test", "failed to prepare recorder "); }

            return (false);
        }

        @Override
        protected void onPostExecute(Boolean clapDetected) {
            if (clapDetected && doorSelected == 1){
                Log.d("zzz", "Clap!!!!!!!!!!!!!!!");
                ibDoor.setImageResource(R.drawable.tardisafter);
                new unlockPhone().execute();
            }
            if (clapDetected && doorSelected == 2){
                Log.d("zzz", "Clap!!!!!!!!!!!!!!!");
                ibDoor.setImageResource(R.drawable.blondeafter);
                new unlockPhone().execute();
            }
            if (clapDetected && doorSelected == 3){
                Log.d("zzz", "Clap!!!!!!!!!!!!!!!");
                ibDoor.setImageResource(R.drawable.krispiesafter);
                new unlockPhone().execute();
            }
            if (clapDetected && doorSelected == 4){
                Log.d("zzz", "Clap!!!!!!!!!!!!!!!");
                ibDoor.setImageResource(R.drawable.thanosafter);
                new unlockPhone().execute();
            }
            if (!clapDetected){
                ibDoor.setImageResource(R.drawable.mist);
            }
        }
    }


    private class unlockPhone extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(final Void... voids) {
            try {
                MainActivity.this.wait(500);
            }
            catch (InterruptedException e) { }

            return (voids[0]);
        }

        @Override
        protected void onPostExecute(Void voids) {
            ibDoor.setImageResource(R.drawable.home_screen_1);
        }
    }



    private void unlockPhone(){
        int i = 0;
        while (i < 20000){
            i++;
        }
    }

    private void createListeners(){
        findViewById(R.id.ibDoor).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        toasty = Toast.makeText(getApplicationContext(),
                                "Listening...",
                                Toast.LENGTH_SHORT);

                        toasty.show();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;

                }
                return false;
            }
        });
    }


   private void linkScreenElements(){
        //editText = findViewById(R.id.editText);
        ibDoor = findViewById(R.id.ibDoor);
    }


    private void createSpeechRecognizer(){
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matches != null) {
                    String speech = matches.get(0);
                    Log.d("zzz", "You said: " + speech);

                    if(speech.equals("1") || speech.equals("Alonzi")){
                        doorSelected = 1;
                        Log.d("zzz", "door selected");
                        ibDoor.setImageResource(R.drawable.tardis);
                        Log.d("zzz", "image set");
                        new startClapper().execute();
                    } else if(speech.equals("2") || speech.equals("Elle")){
                        doorSelected = 2;
                        Log.d("zzz", "door selected");
                        ibDoor.setImageResource(R.drawable.blonde);
                        Log.d("zzz", "image set");
                        new startClapper().execute();
                    } else if(speech.equals("3") || speech.equals("Crackle")){
                        doorSelected = 3;
                        Log.d("zzz", "door selected");
                        ibDoor.setImageResource(R.drawable.krispies);
                        Log.d("zzz", "image set");
                        new startClapper().execute();
                    } else if(speech.equals("4") || speech.equals("Stark")){
                        doorSelected = 4;
                        Log.d("zzz", "door selected");
                        ibDoor.setImageResource(R.drawable.thanos);
                        Log.d("zzz", "image set");
                        new startClapper().execute();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Word is invalid, please try again!",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }


    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public class Clapper
    {
        private static final String TAG = "Clapper";

        private static final long DEFAULT_CLIP_TIME = 1000;
        private long clipTime = DEFAULT_CLIP_TIME;
        private AmplitudeClipListener clipListener;

        private boolean continueRecording;

        /**
         * how much louder is required to hear a clap 10000, 18000, 25000 are good
         * values
         */
        private int amplitudeThreshold;

        /**
         * requires a little of noise by the user to trigger, background noise may
         * trigger it
         */
        public static final int AMPLITUDE_DIFF_LOW = 10000;
        public static final int AMPLITUDE_DIFF_MED = 30000;
        /**
         * requires a lot of noise by the user to trigger. background noise isn't
         * likely to be this loud
         */
        public static final int AMPLITUDE_DIFF_HIGH = 30000;

        private static final int DEFAULT_AMPLITUDE_DIFF = AMPLITUDE_DIFF_MED;

        private MediaRecorder recorder;

        private String tmpAudioFile;

        public Clapper() throws IOException
        {
            this(DEFAULT_CLIP_TIME, "tmp.3gp", DEFAULT_AMPLITUDE_DIFF, null, null);
        }

        public Clapper(long snipTime, String tmpAudioFile,
                       int amplitudeDifference, Context context, AmplitudeClipListener clipListener)
                throws IOException
        {
            this.clipTime = snipTime;
            this.clipListener = clipListener;
            this.amplitudeThreshold = amplitudeDifference;
            this.tmpAudioFile = tmpAudioFile;
        }

        public boolean recordClap()
        {
            Log.d(TAG, "record clap");
            boolean clapDetected = false;

            try
            {
                File file = new File(getApplicationContext().getFilesDir(), "tmp.3gp");
                recorder = AudioUtil.prepareRecorder(file);//tmpAudioFile);
            }
            catch (IOException io)
            {
                Log.d("zzz", "failed to prepare recorder ", io);
                //throw new RecordingFailedException("failed to create recorder", io);
            }

            recorder.start();
            int startAmplitude = recorder.getMaxAmplitude();
            Log.d(TAG, "starting amplitude: " + startAmplitude);

            do
            {
                Log.d(TAG, "waiting while recording...");
                waitSome();
                int finishAmplitude = recorder.getMaxAmplitude();
                if (clipListener != null)
                {
                    clipListener.heard(finishAmplitude);
                }

                int ampDifference = finishAmplitude - startAmplitude;
                if (ampDifference >= amplitudeThreshold)
                {
                    Log.d(TAG, "heard a clap!");
                    clapDetected = true;
                }
                Log.d(TAG, "finishing amplitude: " + finishAmplitude + " diff: "
                        + ampDifference);
            } while (continueRecording || !clapDetected);

            Log.d(TAG, "stopped recording");
            done();

            return clapDetected;
        }

        private void waitSome()
        {
            try
            {
                // wait a while
                Thread.sleep(clipTime);
            } catch (InterruptedException e)
            {
                Log.d(TAG, "interrupted");
            }
        }

        /**
         * need to call this when completely done with recording
         */
        public void done()
        {
            Log.d(TAG, "stop recording");
            if (recorder != null)
            {
                if (isRecording())
                {
                    stopRecording();
                }
                //now stop the media player
                recorder.stop();
                recorder.release();
            }
        }

        public boolean isRecording()
        {
            return continueRecording;
        }

        public void stopRecording()
        {
            continueRecording = false;
        }
    }





}
