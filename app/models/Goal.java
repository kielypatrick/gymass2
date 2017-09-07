package models;

import controllers.Accounts;
import org.omg.PortableInterceptor.ACTIVE;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Patrick on 30/08/2017.
 */
@Entity
public class Goal extends Model {


    public String name;
    public String description;
    public String target;
    public int targetInt;
    public boolean achieved;
    public String date;
    public boolean open;
    public boolean assessed;



    public Goal(String name, String description, String target, int targetInt, String date) throws ParseException {
        this.name = name;
        this.description = description;
        this.date = date;
        this.target = target;
        this.targetInt = targetInt;
        this.open = open();
        this.achieved = achieved();
        this.assessed = assessed();

    }


    public boolean open() throws ParseException {


            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 3);

            //sdf has to be part of the method. It was in the constructor and this caused crashing
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = sdf.parse(date);
            Date date2 = cal.getTime();


            return ((date1.after(date2)));

        }

    public boolean achieved() throws ParseException {

        Member member = Accounts.getLoggedInMember();
        List<Assessment> assessments = member.assessments;
        if (assessments.size() != 0) {
            Assessment assessment = assessments.get(assessments.size() - 1);
            if (target == "weight") {
                return (targetInt >= assessment.weight);
            } else {
                return (targetInt >= assessment.waist);
            }
        }
        else {
            return false;
        }

    }

    public boolean assessed() throws ParseException {


        Member member = Accounts.getLoggedInMember();
        List<Assessment> assessments = member.assessments;
        if (assessments.size() != 0) {
            Assessment assessment = assessments.get(assessments.size() - 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(date);
            Calendar calendar = new GregorianCalendar((date1.getYear() + 1900), date1.getMonth(), date1.getDate());
            calendar.add(Calendar.DATE, 3);
            Date goalDate = calendar.getTime();

            return ((assessment.date).before(date1));
        }
        else {
            return false;
        }

    }



}
