package cct.ansteph.moodrecorder.model;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class Emoji {

    int id;
    String moodName;
    byte [] ImageByte;

    String status;

    public Emoji() {
    }

    public Emoji(int id, String moodName, byte[] imageByte) {
        this.id = id;
        this.moodName = moodName;
        ImageByte = imageByte;
    }


    public Emoji(int id, String moodName) {
        this.id = id;
        this.moodName = moodName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    public byte[] getImageByte() {
        return ImageByte;
    }

    public void setImageByte(byte[] imageByte) {
        ImageByte = imageByte;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
