package com.sana.timetable;

public class DataModel {

    String subject, start_time, end_time, room, id;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataModel(String subject, String start_time, String end_time, String room, String id) {
        this.subject = subject;
        this.start_time = start_time;
        this.end_time = end_time;
        this.room = room;
        this.id = id;
    }

}