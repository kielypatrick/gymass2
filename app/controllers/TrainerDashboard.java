package controllers;

import models.*;
import org.joda.time.LocalDate;
import play.Logger;
import play.db.jpa.Blob;
import play.mvc.Controller;
import sun.rmi.runtime.Log;
import utils.Analytics;
import utils.MemberStats;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

public class TrainerDashboard extends Controller {
  public static void index() {

    Trainer trainer = Accounts.getLoggedInTrainer();
    List<Member> members = Member.findAll();
    List<GymClass> gymclasses = GymClass.findAll();
    List<Appointment> appointments = trainer.appointments;
    Logger.info("Rendering Trainer Dashboard for " + trainer);
    render("trainerdashboard.html", trainer, members, gymclasses, appointments);
  }

  public static void trainerAssessment(Long id) throws ParseException {
    Member member = Member.findById(id);
    Trainer trainer = Accounts.getLoggedInTrainer();
    List<Appointment> appointments = trainer.appointments;
    List<Assessment> assessments = member.assessments;
    List<Goal> goals = member.goals;

    MemberStats memberStats = Analytics.generateMemberStats(member);
    Collections.reverse(assessments);

    render("trainerassessment.html", member, assessments, memberStats, goals, appointments);
  }

  public static void editComment(Long id, String comment) {
    Logger.info("Comment " + comment);
    Assessment assessment = Assessment.findById(id);
    assessment.comment = comment;
    assessment.save();
    redirect("/trainerdashboard");
  }

  public static void deleteMember(Long id) {
    Member member = Member.findById(id);
    if (member != null) {
      member.delete();
    }
    redirect("/trainerdashboard");
  }

  public static void deleteGymClass(Long id) {
    GymClass gymClass = GymClass.findById(id);
    if (gymClass != null) {
      gymClass.delete();
    }
    Logger.info("Deleting " + gymClass.name);
    redirect("/trainerdashboard");
  }


  public static void deleteSession(Long gymClassId, Long sessionId)
  {
    GymClass gymClass = GymClass.findById(gymClassId);
    Session session = Session.findById(sessionId);
    gymClass.sessions.remove(session);
    gymClass.save();
    session.delete();
    Logger.info("Deleting " + session.name + " from " + gymClass.name);
    redirect("/trainerdashboard");
  }

  public static void gymClassDetails(Long id) {
    GymClass gymclass = GymClass.findById(id);
    Logger.info("Rendering details for "  + gymclass.name);

    List<Session> sessions = gymclass.sessions;
    for (Session session : sessions) {
      Logger.info("Session attendance: " + session.members.size());

    }
    render("gymclassdetails.html", gymclass);
  }

  public static void editGymClassDetails(Long id, GymClass gymclass1) {
    Logger.info("id: ", id);
    Logger.info("gymclass1: ", gymclass1);
    GymClass gymclass = GymClass.findById(id);

    if (gymclass1.date.contains("20")) {
      gymclass.date = gymclass1.date;}
    if (gymclass1.endDate.contains("20")){
      gymclass.endDate = gymclass1.endDate;}
      gymclass.difficulty = gymclass1.difficulty;
      gymclass.duration = gymclass1.duration;
      gymclass.weeks = gymclass1.weeks;
      gymclass.capacity = gymclass1.capacity;
      gymclass.timeSlot = gymclass1.timeSlot;
//carry changes from the block of classes down to each session
    List<Session> sessions = gymclass.sessions;
    for (Session session : sessions) {
      session.timeSlot = gymclass1.timeSlot;
      session.capacity = gymclass1.capacity;
      session.timeSlot = gymclass1.timeSlot;
      session.save();
      Logger.info("session: " + session.name + "edited");

    }
    gymclass.save();
    Logger.info("Updating gym class " + gymclass.name);
    redirect("/trainerdashboard");
  }

  public static void createGymClass() {
    render(new Object[]{"addgymclass.html"});
  }

  public static void addGymClass(String name, double duration, String timeSlot, int capacity,
                                 int weeks, String difficulty, String date, String endDate, Blob image) throws ParseException {
    Logger.info("Creating Gym Class " + name);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    GymClass gymclass = new GymClass(name, duration, timeSlot, capacity, weeks, difficulty, date, endDate);
    gymclass.image = image;


//Generate a weekly class from the startDate to the endDate
    Date date1 = sdf.parse(date);
    Date endDate1 = sdf.parse((endDate));
    Calendar calendar = new GregorianCalendar((date1.getYear() + 1900), date1.getMonth(), date1.getDate());
    Calendar calendar1 = new GregorianCalendar((endDate1.getYear() + 1900), endDate1.getMonth(), endDate1.getDate());
    calendar1.add(Calendar.DATE, 1);

    for (Calendar i = calendar; i.before(calendar1); i.add(Calendar.DATE, 7))
    {
      Date date2 = (calendar.getTime());

      Session newSession = new Session(sdf.format(date2), name, capacity, timeSlot);
      gymclass.sessions.add(newSession);
    }

    gymclass.save();

    index();
    redirect("/trainerdashboard");
  }

  public static void getImage(Long id)
  {
    GymClass gymClass = GymClass.findById(id);
    Blob image = gymClass.image;
    if (image.exists())
    {
      response.setContentTypeIfNotSet(image.type());
      renderBinary(image.get());
    }
  }


  public static void addSession(Long id, String date) throws ParseException {

    Logger.info("gymClassId: " + id);
    Logger.info("sessionDate: " + date);
    GymClass gymClass = GymClass.findById(id);

    Logger.info("gymClass: " + gymClass.name);
    Session newSession = new Session(date, gymClass.name, gymClass.capacity, gymClass.timeSlot);
    gymClass.addSession(newSession);
    gymClass.save();
    redirect("/trainerdashboard");
  }

  public static void editAppointment(Long id) {
    Appointment appointment = Appointment.findById(id);


    Logger.info("Rendering details for "  + appointment.member);
    render("editappointment.html", appointment);
  }

  public static void addAppointment(Long id, Appointment appointment1) {
    Logger.info("id: ", id);
    Logger.info("appointment1: ", appointment1);
    Appointment appointment = Appointment.findById(id);

    appointment.date = appointment1.date;
    appointment.time = appointment1.time;

    appointment.status = appointment1.status;


    appointment.save();
    Logger.info("Updating appointment " + appointment.status);
    redirect("/trainerdashboard");
  }

  public static void viewGoal(Long id) {
    Goal goal = Goal.findById(id);


    Logger.info( "Rendering class details for " + goal.name);
    render("trainerviewgoal.html", goal);

  }

  public static void addGoal(Long id) {
    Member member = Member.findById(id);

    render("traineraddgoal.html", member);

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

    redirect("/trainerdashboard");

  }

  public static void deleteGoal(Long memberId, Long goalId)
  {
    Goal goal = Goal.findById(goalId);
    Member member = Member.findById(memberId);
    Logger.info("Removing " + member.name  + "'s goal " + goal.name);
    member.goals.remove(goal);
    member.save();

    goal.delete();
    redirect("/trainerassessment/" + memberId);
  }

  public static void createGoal(Long id, String name, String description, String date, String target, int targetInt) throws ParseException {
    Logger.info("Creating Goal");
    Member member = Member.findById(id);
    Goal goal = new Goal(name, description, target, targetInt, date);

    member.goals.add(goal);
    member.save();
    redirect("/trainerassessment/" + id);

  }


}