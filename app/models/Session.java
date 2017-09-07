package models;

import org.joda.time.LocalDate;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.*;
import java.util.List;

@Entity
public class Session extends Model
{
    @ManyToMany(mappedBy = "sessions")
    public List<Member> members = new ArrayList<Member>();

    public String name;
    public boolean inFuture;
    public String gymClassName;
    public int capacity;
    public String timeSlot;
    public Date date;







    public Session(String name, String gymClassName, int capacity, String timeSlot) throws ParseException {
        this.name = name;
        this.members = new ArrayList<Member>();
        this.inFuture = inFuture();
        this.gymClassName = gymClassName;
        this.capacity = capacity;
        this.timeSlot = timeSlot;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.parse(name);



    }

    public boolean inFuture() throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(name);
        Date date2 = cal.getTime();

        return ( (date1.after(date2)) );
    }




}
