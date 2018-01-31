package cct.ansteph.moodrecorder.model;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class DiaryUser {

    int id;
    String firstname, surname ;

    int age;

    String motive;

    public DiaryUser() {
    }

    public DiaryUser(int id, String firstname, String surname, int age) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.age = age;
    }

    public DiaryUser(String firstname, String surname, int age) {
        this.firstname = firstname;
        this.surname = surname;
        this.age = age;
    }

    public DiaryUser(String firstname, String surname, int age, String motive) {
        this.firstname = firstname;
        this.surname = surname;
        this.age = age;
        this.motive = motive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }
}
