package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import backend.sleepguard.vibhuyadav.example.com.responseEndpoint.model.Response;


/**
 * Created by WeiHuang on 3/4/2015.
 */
public class NoiseAwakeRunnable implements Runnable {
    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 16000;
    static final short THRESHOLD = 4000;
    static final int WINDOW_WIDTH = 5;//second

    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetAudio = true;
    long requestTime=0;
    List<long[]> recordList=new ArrayList<>();
    static final int LISTSIZE=(SAMPLE_RATE_IN_HZ/BUFFER_SIZE+1)*30;//30 seconds
    //AutomaticGainControl ACG;
    Object mLock;
    String regId;
    Context context;
    UserPreferences mUserPreferences;
    public NoiseAwakeRunnable(Context ctxt,String regId){
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
            Thread.sleep(1000);
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
        while (isGetAudio && mUserPreferences.getMySleepStatus()==false) {
            int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
            long approximateTime = System.currentTimeMillis();
            for (int i = 0; i < r; i++) {
                audioWindow.push(buffer[i], approximateTime);
            }
//            Log.d("Num over threshold", Long.toString(audioWindow.getNumOverThreshold()));
//            Log.d("Average",Long.toString(audioWindow.getAverageAmplitude()));
            if (audioWindow.isFull()) {
                long startTime=audioWindow.getTimeStamp();
                int numOverThreshold=audioWindow.getNumOverThreshold();
                int average=audioWindow.getAverageAmplitude();
                long[] strs={startTime,numOverThreshold,average};
                recordList.add(strs);
                if(recordList.size()>LISTSIZE)
                    recordList.remove(0);
            }
            if(mUserPreferences.getRequestStatus()){
                requestTime=mUserPreferences.getRequestingTimeStamp();
                long startTime=0;
                int numOverThreshold=0;
                int average=0;
                for(long[] strs:recordList){
                    if(Math.abs(startTime-requestTime)>Math.abs(strs[0]-requestTime)){
                        startTime=strs[0];
                        numOverThreshold=(int)strs[1];
                        average=(int)strs[2];
                    }
                }
//                Log.d("Start time",Long.toString(startTime));
//                Log.d("Num over Threshold",Long.toString(numOverThreshold));
//                Log.d("Average",Long.toString(average));
                mUserPreferences.setmAverage((double)average);

                Response response = new Response();
                response.setMDeviceId(mUserPreferences.getMyDeviceId());
                response.setRequestId(mUserPreferences.getRequestingDeviceId());//Is it right?
                response.setAverage(mUserPreferences.getmAverage());
                new ResponseEndpointsAsyncTask(context).execute(response);
                mUserPreferences.setRequestStatus(false);
                isGetAudio=false;// Temporary Test
            }
       /*     synchronized (mLock) {
                try {
                    mLock.wait(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }
        Log.d("report", "finished");
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;


    }

}
