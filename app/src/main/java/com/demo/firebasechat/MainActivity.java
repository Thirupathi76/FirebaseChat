package com.demo.firebasechat;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Varun John on 4 Dec, 2018
 * Github : https://github.com/varunjohn
 */
public class MainActivity extends AppCompatActivity implements AudioRecordView.RecordingListener {

    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private long time;

    private MediaRecorder mRecorder;
    private String fileName = "";
    File root = android.os.Environment.getExternalStorageDirectory();
    File file = new File(root.getAbsolutePath() + "/Audio_Example/Audios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       /* outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"_"+System.currentTimeMillis() + "/recording.3gp";
        //audio
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);*/


        audioRecordView = findViewById(R.id.recordingView);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        audioRecordView.setRecordingListener(this);

        messageAdapter = new MessageAdapter();

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewMessages.setHasFixedSize(false);

        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());

        setListener();
    }

    private void setListener() {

        audioRecordView.getAttachmentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attachment");
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString();
                audioRecordView.getMessageView().setText("");
                messageAdapter.add(new Message(msg, fileName));
            }
        });
    }

    @Override
    public void onRecordingStarted() {
        showToast("started");
        debug("started");
        try {
            /*myAudioRecorder.prepare();

        myAudioRecorder.start();*/
            startRecording();
        time = System.currentTimeMillis() / (1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordingLocked() {
        showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
        showToast("completed");
        debug("completed");
        /*myAudioRecorder.stop();
        myAudioRecorder.release();*/
        stopRecording();

//        myAudioRecorder = null;
        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 2) {
            messageAdapter.add(new Message(recordTime, fileName));
        }
    }

    @Override
    public void onRecordingCanceled() {
        showToast("canceled");
        debug("canceled");
    }


    private void prepareforRecording() {
        /*TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);*/
    }


    private void startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory named VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/

        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/Audio_Example/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();*/
        //starting the chronometer
       /* chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();*/
    }


    private void stopPlaying() {
        /*try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;*/

        //showing the play button
       /* imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();*/
    }


    private void prepareforStop() {
       /* TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlay.setVisibility(View.VISIBLE);*/
    }

    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        /*chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());*/
        //showing the play button
        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }



    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void debug(String log) {
        Log.d("VarunJohn", log);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.code:
                showDialog();
                break;
        }
        return true;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Created by:\nVarun John\nvarunjohn1990@gmail.com\n\nCheck code on Github :\nhttps://github.com/varunjohn/Audio-Recording-Animation")
                .setCancelable(false)
                .setPositiveButton("Github", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = "https://github.com/varunjohn/Audio-Recording-Animation";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            i.setPackage(null);
                            try {
                                startActivity(i);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
