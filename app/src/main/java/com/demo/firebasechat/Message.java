package com.demo.firebasechat;

/**
 * Created by Varun John on 4 Dec, 2018
 * Github : https://github.com/varunjohn
 */
public class Message {

    public static int TYPE_TEXT = 1;
    public static int TYPE_AUDIO = 21;

    public String text;
    public int type;
    public int time;
    public String audioPath;

    public Message(String text, String audioPath) {
        this.text = text;
        this.audioPath = audioPath;
        this.type = TYPE_TEXT;
    }

    public Message(int time, String audioPath) {
        this.time = time;
        this.audioPath = audioPath;
        this.type = TYPE_AUDIO;
    }

}
