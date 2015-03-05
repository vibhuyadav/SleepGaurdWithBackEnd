package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WeiHuang on 3/4/2015.
 */
public class NoiseAwakeRunnable implements Runnable {
    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 16000;
    static final short THRESHOLD = 4000;
    static final int WINDOW_WIDTH = 5;//second
    static final int RECORD_LENGTH = 25;//second
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetAudio = true;
    boolean isGetRequest = false;
    long timeStamp=0;
    List<String[]> recordList=new ArrayList<>();
    //AutomaticGainControl ACG;
    Object mLock;
    String regId;
    Context context;
    public NoiseAwakeRunnable(Context ctxt,String regId){
        context=ctxt;
        this.regId=regId;
    }

    public void terminate(){
        isGetAudio=false;
    }
    public void reportRequest(long time){
        timeStamp=time;
        isGetRequest=true;
    }
    @Override
    public void run(){
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        mLock = new Object();

        mAudioRecord.startRecording();
        short[] buffer = new short[BUFFER_SIZE];

        AudioWindow audioWindow = new AudioWindow(THRESHOLD, WINDOW_WIDTH, SAMPLE_RATE_IN_HZ);
        while (isGetAudio) {
            int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
            long approximateTime = System.currentTimeMillis();
            for (int i = 0; i < r; i++) {
                String[] strs=audioWindow.push(buffer[i], approximateTime);
                if(strs!=null)
                    updateRecordWindow(strs);
            }
            Log.d("Num over threshold", Long.toString(audioWindow.num_over_threshold));

            if(isGetRequest){
                long timePoint=0;
                int numOverThreshold=0;
                long averagePeakValue=0;
                for(String[] strs:recordList){
                    long tempTimePoint=Long.parseLong(strs[0]);
                    if(Math.abs(timePoint-timeStamp)>Math.abs(tempTimePoint-timeStamp)){
                        timePoint=tempTimePoint;
                        numOverThreshold=Integer.parseInt(strs[1]);
                        averagePeakValue=Long.parseLong(strs[2]);
                    }
                }
                Log.d("Report",Long.toString(timePoint));
                Log.d("Report",Integer.toString(numOverThreshold));
                Log.d("Report",Long.toString(averagePeakValue));
                isGetRequest=false;
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

    public void updateRecordWindow(String[] strs){
        if(recordList.size()<=0){
            recordList.add(strs);
        }else{
            String[] lastRecord=recordList.get(recordList.size()-1);
            if(!lastRecord[0].equals(strs[0])){
                recordList.add(strs);
                if(recordList.size()>RECORD_LENGTH*8){
                    recordList.remove(0);
                }
            }
        }
    }
}
