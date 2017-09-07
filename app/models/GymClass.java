package models;

import org.joda.time.LocalDate;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.*;
import java.util.List;
import play.db.jpa.Blob;


@Entity
public class GymClass extends Model
{



    public String name;
    public double duration;
    public String timeSlot;
    public int capacity;
    public int weeks;
    public String difficulty;
    public String date;
    public String endDate;
    public Blob image;


    @OneToMany(cascade=CascadeType.ALL)
    public List<Session> sessions = new ArrayList<Session>();




    public GymClass(String name, double duration, String timeSlot, int capacity, int weeks,
                    String difficulty, String date, String endDate){
        this.name = name;
        this.duration = duration;
        this.timeSlot = timeSlot;
        this.capacity = capacity;
        this.weeks = weeks;
        this.difficulty = difficulty;
        this.sessions = new ArrayList<Session>();


        this.date = date;
        this.endDate = endDate;


    }

    public void addSession(Session session) {
        sessions.add(session);
    }








    public List<Session> getSessions() {
        return sessions;
    }
}
