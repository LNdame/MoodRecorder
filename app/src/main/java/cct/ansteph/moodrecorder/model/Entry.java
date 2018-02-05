package cct.ansteph.moodrecorder.model;

import java.util.ArrayList;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class Entry {

    int id;

    Emoji emoji;

    String mood, recordTime, recordDate, addedNote;

    DiaryUser diaryUser;

    ArrayList<Activity> activityList;

    public Entry() {

    }

    public Entry(String mood, String recordTime, String recordDate) {
        this.mood = mood;
        this.recordTime = recordTime;
        this.recordDate = recordDate;
    }

    public Entry(int id, Emoji emoji, String mood, String recordTime, String recordDate, DiaryUser diaryUser) {
        this.id = id;
        this.emoji = emoji;
        this.mood = mood;
        this.recordTime = recordTime;
        this.recordDate = recordDate;
        this.diaryUser = diaryUser;
    }


    public Entry(Emoji emoji, String mood, String recordTime, String recordDate, DiaryUser diaryUser) {
        this.emoji = emoji;
        this.mood = mood;
        this.recordTime = recordTime;
        this.recordDate = recordDate;
        this.diaryUser = diaryUser;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public DiaryUser getDiaryUser() {
        return diaryUser;
    }

    public void setDiaryUser(DiaryUser diaryUser) {
        this.diaryUser = diaryUser;
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<Activity> activityList) {
        this.activityList = activityList;
    }

    public String getAddedNote() {
        return addedNote;
    }

    public void setAddedNote(String addedNote) {
        this.addedNote = addedNote;
    }
}
