package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import dartmouth.edu.sleepguard.util.Constants;

/**
 * Created by WeiHuang on 3/3/2015a.
 */
public class NoiseSleepRunnable implements Runnable {
    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 16000;
    static final short THRESHOLD = 2000;
    static final int NUM_OVER_THRESHOLD = 1000;
    static final int WINDOW_WIDTH = 5;//second
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetAudio = true;
    long lastRequest=0;
    //AutomaticGainControl ACG;
    Object mLock;
    String regId;
    Context context;
    UserPreferences mUserPreferences;

    public NoiseSleepRunnable(Context ctxt,String regId){
        context=ctxt;
        mUserPreferences= new UserPreferences(context);
        this.regId=regId;
    }

    public void terminate(){
        isGetAudio=false;
    }
    @Override
    public void run(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        mLock = new Object();
        mAudioRecord.startRecording();
        short[] buffer = new short[BUFFER_SIZE];

        AudioWindow audioWindow = new AudioWindow(THRESHOLD, WINDOW_WIDTH, SAMPLE_RATE_IN_HZ);
        while (isGetAudio && mUserPreferences.getMySleepStatus()==true) {
            int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
            long approximateTime = System.currentTimeMillis();
            for (int i = 0; i < r; i++) {
                audioWindow.push(buffer[i], approximateTime);
            }
            //Log.d("Num over threshold", Integer.toString(audioWindow.num_over_threshold));Nm
            if (audioWindow.num_over_threshold > NUM_OVER_THRESHOLD && audioWindow.isFull()) {
                if((System.currentTimeMillis()-lastRequest)>15000) {
                    lastRequest=System.currentTimeMillis();
                    Intent localIntent = new Intent(Constants.NOISE_ALERT);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
//                Log.d("Start Time", Long.toString(audioWindow.getTimeStamp()));
                    String[] params = {regId
                            , Long.toString(audioWindow.getTimeStamp())
                            , Integer.toString(audioWindow.getNumOverThreshold())
                            , Integer.toString(audioWindow.getAverageAmplitude())};
                    mUserPreferences.setmAverage((double)audioWindow.getAverageAmplitude());
                    new NoiseSleepAsyncTask(context).execute(params);
                    isGetAudio = false;// Temporary Test
                }
            }
            synchronized (mLock) {
                try {
                    mLock.wait(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("report", "finished");
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;

    }
}
