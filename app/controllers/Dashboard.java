package controllers;

import models.*;
import play.Logger;
import play.db.jpa.Blob;
import play.mvc.Controller;
import utils.Analytics;
import utils.MemberStats;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Dashboard extends Controller
{
  public static void index() throws ParseException {
    Logger.info("Rendering Dashboard");
    Member member = Accounts.getLoggedInMember();

    List<Assessment> assessments = member.assessments;

    List<GymClass> gymclasses = GymClass.findAll();
    List<Goal> goals = member.goals;
    //run Goal methods on login to update for the current date
    for (Goal goal:goals){
      goal.open = goal.open();
      goal.achieved = goal.achieved();
      goal.save();
    }
    List<Session> sessions = member.sessions;
//sort members upcoming classes by date
    Collections.sort(sessions, new Comparator<Session>() {
      @Override
      public int compare(Session o1, Session o2) {
        if (o1.date.after(o2.date)){
          return 1;
        }
        else if (o1.date.before(o2.date)){
          return -1;
        }
        else {
          return 0;
        }
      }
    });
    MemberStats memberStats = Analytics.generateMemberStats(member);
    Collections.reverse(assessments);

    render("dashboard.html", member, assessments, memberStats, gymclasses, goals);
  }

  public static void addAssessment(double weight, double chest, double thigh, double upperarm, double waist, double hips) throws ParseException {
    Logger.info("Creating Assessment");
    Member member = Accounts.getLoggedInMember();
    Assessment assessment = new Assessment(weight, chest, thigh, upperarm, waist, hips);
    MemberStats memberStats = Analytics.generateMemberStats(member);
    assessment.trend = memberStats.trend;
    member.assessments.add(assessment);
    List<Goal> goals = member.goals;
    for (Goal goal:goals){
      if (goal.assessed == false) {
        goal.assessed = goal.assessed();
        goal.achieved = goal.achieved();
        goal.save();
      }
    }
    member.save();
    redirect("/dashboard");
  }

  public static void bookAssessment()
  {

    List<Trainer> trainers = Trainer.findAll();
    render("bookassessment.html", trainers);
  }



  public static void deleteAssessment(Long memberid, Long assessmentid)
  {
    Member member = Member.findById(memberid);
    Assessment assessment = Assessment.findById(assessmentid);
    member.assessments.remove(assessment);
    member.save();
    assessment.delete();
    redirect("/dashboard");
  }



  public static void gymClassDetails(Long id) throws ParseException {
    GymClass gymclass = GymClass.findById(id);
    Logger.info("sessions: " + gymclass.getSessions());
    List<Session> sessions = gymclass.sessions;
    for (Session session:sessions){
      session.inFuture = session.inFuture();
      session.save();
    }

    Logger.info( "Rendering class details for " + gymclass.name);
    render("membergymclassdetails.html", gymclass, sessions);

  }

  public static void addGymClass(Long id) {

    GymClass gymclass = GymClass.findById(id);
    List<Session> sessions = gymclass.sessions;
    Member member = Accounts.getLoggedInMember();

    for (Session session : sessions) {
      if ((session.inFuture == true) && (session.members.size() < session.capacity)) {
        //this is included to avoid duplicate enrolment. Member is removed and re-enrolled later
        if (member.sessions.contains(session)) {
          Logger.info(member.name + " in " + session.name + "already duuuuh!");
          member.sessions.remove(session);
        }
        member.sessions.add(session);
        member.save();

      }
    }
    Logger.info("Enrollin " + member.name + " in " + gymclass.name + sessions);
    redirect("/dashboard");


  }

  public static void addSession(Long id)
  {
    Session session = Session.findById(id);

    Member member = Accounts.getLoggedInMember();
    //this is included to avoid duplicate enrolment. Member is redirected to dashboard
    if (member.sessions.contains(session)) {
      Logger.info(member.name + " in " + session.name + "already duuuuh!");
      redirect("/dashboard");
    }
      member.addSession(session, member);
    member.save();
    session.save();
    Logger.info("Enrollin " + member.name + " in " + session.gymClassName + " for "
            + session.name + ". Total of " + session.members.size() + " attending");
    redirect("/dashboard");
  }

  public static void quitSession(Long sessionid, Long memberid)
  {
    Member member = Member.findById(memberid);
    Session session = Session.findById(sessionid);
    member.sessions.remove(session);
    member.save();
    Logger.info("Removing " + member.name + " from " + session.gymClassName + " for " + session.name);
    redirect("/dashboard");


  }

  public static void requestAppointment(Long trainerid, String date, String time) throws ParseException {
    Member member = Accounts.getLoggedInMember();
    Trainer trainer = Trainer.findById(trainerid);
    String status = "pending...";
    Appointment appointment = new Appointment(date, time, trainer.name, member.name, status, trainer.email);

    if(member.appointment != null) {
      Logger.info("Removing existing appointment for" + member.name);
      cancelAppointment(member.appointment.getId(), member.getId());
    }

    trainer.appointments.add(appointment);
    member.appointment = appointment;
    member.save();
    trainer.save();
    Logger.info("Requesting appointment for " + member.name + " with " + trainer.name + " for "
            + appointment.date + " at " + appointment.time);
    redirect("/dashboard");
  }

  public static void cancelAppointment(Long appointmentid, Long memberid)
  {
    Member member = Member.findById(memberid);
    Appointment appointment = Appointment.findById(appointmentid);
    Trainer trainer = Trainer.findByEmail(appointment.trainerEmail);
    Logger.info("Removing " + member.name  + "'s appointment booking ");
    member.appointment = null;
    trainer.appointments.remove(appointment);

    member.save();
    trainer.save();
    appointment.delete();
    redirect("/dashboard");
  }

  public static void addGoal() {

    render("addgoal.html");

  }

  public static void createGoal(String name, String description, String date, String target, int targetInt) throws ParseException {
    Member member = Accounts.getLoggedInMember();
    Logger.info("Creating Goal for " + member.name);

    Goal goal = new Goal(name, description, target, targetInt, date);

    member.goals.add(goal);
    member.save();
    redirect("/dashboard");

  }

  public static void deleteGoal(Long goalId)
  {
    Goal goal = Goal.findById(goalId);
    Member member = Accounts.getLoggedInMember();
    Logger.info("Removing " + member.name  + "'s goal " + goal.name);
    member.goals.remove(goal);
    member.save();

    goal.delete();
    redirect("/dashboard");
  }

  public static void viewGoal(Long id) {
    Goal goal = Goal.findById(id);


    Logger.info( "Rendering class details for " + goal.name);
    render("viewgoal.html", goal);

  }

  public static void editGoal(Long id, Goal goal1) throws ParseException {

    Goal goal = Goal.findById(id);

    if (goal1.date.contains("20")) {
      goal.date = goal1.date;
    }
    goal.description = goal1.description;
    if (goal1.target.contains("aist")) {
      goal.target = "waist";
    }
    else if (goal1.target.contains("ght")) {
      goal.target = "weight";
    }

    goal.targetInt = goal1.targetInt;
    goal.open = goal.open();

    goal.save();
    Logger.info("Updating goall " + goal.name);

      redirect("/dashboard");

  }

  public static void getPic(Long id)
  {
    Member member = Member.findById(id);
    Blob image = member.image;
    if (image.exists())
    {
      response.setContentTypeIfNotSet(image.type());
      renderBinary(image.get());
    }
  }


}
