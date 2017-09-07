package controllers;

import models.Member;
import models.Trainer;
import play.Logger;
import play.db.jpa.Blob;
import play.mvc.Controller;

import java.text.ParseException;

public class Accounts extends Controller
{
  public static void index()
  {
    render("about.html");
  }

  public static void signup()
  {
    render("signup.html");
  }

  public static void login()
  {
    render("login.html");
  }

  public static void settings()
  {
    Member member = getLoggedInMember();
    render("settings.html", member);
  }

  public static void updateSettings(Member member, Blob image)
  {
    Member loggedInMember = getLoggedInMember();

    loggedInMember.email = member.email;
    loggedInMember.name = member.name;
    loggedInMember.password = member.password;
    loggedInMember.address = member.address;
    loggedInMember.gender = member.gender;
    loggedInMember.height = member.height;
    loggedInMember.startingweight = member.startingweight;
    loggedInMember.image = image;

    loggedInMember.save();
    Logger.info("Updating " + loggedInMember.name + " with " + loggedInMember.image);
    redirect("/settings");
  }


  public static void logout()
  {
    session.clear();
    index();
  }

  public static Member getLoggedInMember()
  {
    Member member = null;
    if (session.contains("logged_in_Memberid")) {
      String memberId = session.get("logged_in_Memberid");
      member = Member.findById(Long.parseLong(memberId));
    } else {
      login();
    }
    return member;
  }

  public static Trainer getLoggedInTrainer()
  {
    Trainer trainer = null;
    if (session.contains("logged_in_Trainerid")) {
      String trainerid = session.get("logged_in_Trainerid");
      trainer = Trainer.findById(Long.parseLong(trainerid));
    } else {
      login();
    }
    return trainer;
  }

  public static void register(String email, String name, String password, String address, String gender, double height, double startingweight)
  {
    Logger.info(name + " " + email);
    Member member = new Member(email, name, password, address, gender, height, startingweight);
    member.save();
    index();
  }

  public static void authenticate(String email, String password) throws ParseException {
    Logger.info("Attempting to authenticate with " + email + ":" + password);

    Member member = Member.findByEmail(email);
    if ((member != null) && (member.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_Memberid", member.id);
      Dashboard.index();
    } else {
      Trainer trainer = Trainer.findByEmail(email);
      if ((trainer != null) && (trainer.checkPassword(password) == true)) {
        Logger.info("Authentication successful");
        session.put("logged_in_Trainerid", trainer.id);
        TrainerDashboard.index();
      } else {
        Logger.info("Authentication failed");
        login();
      }
    }
  }
}