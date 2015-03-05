package com.example.vibhuyadav.sleepguard;

/**
 * Created by WeiHuang on 3/3/2015.
 */
public class AudioWindow {
    long num_over_threshold;
    short threshold;
    int width;
    int num_of_nodes;
    long cumulate;
    SampleNode top;
    SampleNode bottom;
    SampleNode iterator;

    public AudioWindow (short threshold,int seconds,int sampleRate){
        top=null;
        bottom=null;
        num_of_nodes=0;
        num_over_threshold=0;
        cumulate=0;
        this.threshold=threshold;
        width=seconds*sampleRate;
    }

    public String[] push(short a,long t){
        SampleNode tempNode=new SampleNode(a,t);
        if(Math.abs(a)>threshold)
            //Log.d("a",Short.toString(a));
            num_over_threshold++;
            long s2l=a+0L;
            cumulate=cumulate+s2l*s2l;
            //Log.d("cumulate",Long.toString(cumulate));
        if(num_of_nodes==0){
            top=bottom=tempNode;
        }else{
            bottom.next=tempNode;
            bottom=tempNode;
        }
        num_of_nodes++;
        if(num_of_nodes>width)
            return pop();
        else
            return null;
    }

    public boolean isEmpty(){
        if(num_of_nodes==0)
            return true;
        else
            return false;
    }

    public String[] pop(){
        if(!isEmpty()) {
            short temp = top.value;
            long timeStamp=top.timeMills;
            top = top.next;
            num_of_nodes--;
            if (Math.abs(temp) > threshold){
                long s2l=temp+0L;
                cumulate=cumulate-temp*temp;
                num_over_threshold--;
            }
            String[] strs={Long.toString(timeStamp),Long.toString(num_over_threshold),"0"};
            if(num_over_threshold!=0)
                strs[2]=Long.toString(cumulate/num_over_threshold);
            return strs;
        }else {
            return null;
        }
    }

    public boolean isFull(){
        if(num_of_nodes==width){
            return true;
        }
        else
            return false;
    }

    public void setIterator(){
        iterator=new SampleNode((short)0,(long)0);
        iterator.next=top;
    }
    public boolean hasNext(){
        if(iterator.next!=null)
            return true;
        else
            return false;
    }
    public String[] getNext(){
        iterator=iterator.next;
        String[] strs={Short.toString(iterator.value),Long.toString(iterator.timeMills)};
        return strs;
    }

    public long getTimeStamp(){
        return top.timeMills;
    }
    private class SampleNode{
        short value;
        long timeMills;
        SampleNode next;
        public SampleNode(short v,long t){
            timeMills=t;
            value=v;
        }
    }
}
