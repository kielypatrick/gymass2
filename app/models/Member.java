package models;

import play.Logger;
import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static controllers.Accounts.login;


@Entity
public class Member extends Model
{
  public String email;
  public String name;
  public String password;
  public String address;
  public String gender;
  public double height;
  public double startingweight;
  public Blob image;

  @OneToMany(cascade = CascadeType.ALL)
  public List<Assessment> assessments = new ArrayList<Assessment>();

  @OneToOne(cascade = CascadeType.ALL)
  public Appointment appointment;

  @OneToMany(cascade = CascadeType.ALL)
  public List<Goal> goals = new ArrayList<Goal>();



  @ManyToMany
  public List<Session> sessions;

  public Member(String email, String name, String password, String address, String gender, double height, double startingweight)
  {
    this.email = email;
    this.name = name;
    this.password = password;
    this.address = address;
    this.gender = gender;
    this.height = height;
    this.startingweight = startingweight;
  }

  public static Member findByEmail(String email)
  {
    return find("email", email).first();
  }

  public boolean checkPassword(String password)
  {
    return this.password.equals(password);
  }



  public void addSession(Session session, Member member) {
    sessions.add(session);
    Logger.info("Adding " + member.name + " to " + session.name);
  }


}
