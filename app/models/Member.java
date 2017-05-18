package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


import play.db.jpa.Model;
import utils.Analytics;

@Entity
public class Member extends Model
{
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public double height;
    public double startingweight;
    public String gender;




    @OneToMany(cascade = CascadeType.ALL)
    public List<Assessment> assessments = new ArrayList<Assessment>();
  
  public Member(String firstname, String lastname, String email, String password, double height, double startingweight, String gender)
  {
	  this.firstname = firstname;
	  this.lastname = lastname;
	  this.email = email;
	  this.password = password;
	  this.height = height;
	  this.startingweight = startingweight;
	  this.gender = gender;

  }

    public double getHeight() {
        return height;
    }

    public double getStartingweight(){
        return startingweight;
    }

    public  String getMemberGender() { return gender; }


    public static Member findByEmail(String email)
    {
        return find("email", email).first();
    }

    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }





}