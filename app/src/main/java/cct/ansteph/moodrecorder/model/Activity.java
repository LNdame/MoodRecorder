package cct.ansteph.moodrecorder.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class Activity implements Serializable{

    int id;
    String activityName, status;

    byte [] ImageByte;

    public Activity() {
    }

    public Activity(int id, String activityName, byte[] imageByte) {
        this.id = id;
        this.activityName = activityName;
        ImageByte = imageByte;
    }

    public Activity(int id, String activityName, String status) {
        this.id = id;
        this.activityName = activityName;
        this.status = status;
    }

    public Activity(String activityName, String status) {
        this.activityName = activityName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getImageByte() {
        return ImageByte;
    }

    public void setImageByte(byte[] imageByte) {
        ImageByte = imageByte;
    }
}
