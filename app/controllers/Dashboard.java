package controllers;

import java.util.List;


import models.Trainer;
import models.Member;
import play.Logger;
import play.mvc.Controller;


public class Dashboard extends Controller
{
  //public static void index()
  //{
    //Logger.info("Rendering Dashboard");

    //List<Member> members = Member.findAll();
    //render ("dashboard.html", members);
  //}

  public static void index(Member member)
  {
    Logger.info("Rendering Dashboard");
    Trainer trainer = Accounts.getLoggedInTrainer();
    List<Member> members = trainer.members;

    render("dashboard.html", trainer, members);
  }

  /**
   * This method adds a member to the app. All Members are ties to trainers, so logged in trainer must be referenced
   */
  public static void addMember(String firstname, String lastname, String email, String password, double height, double startingweight, String gender)
  {
    Trainer trainer = Accounts.getLoggedInTrainer();
    Member member = new Member(firstname, lastname, email, password, height, startingweight, gender);
    trainer.members.add(member);
    member.save();
    trainer.save();
    Logger.info("Adding Member" + firstname + lastname);
    redirect("/dashboard");
  }

  /**
   * This method deletes a member from the app. All Members are ties to trainers, so logged in trainer must be referenced
   */
  public static void deleteMember (Long id)
{
  Trainer trainer = Accounts.getLoggedInTrainer();
  Member member = Member.findById(id);
  trainer.members.remove(member);
  Logger.info ("Removing" + member.firstname + member.lastname);
  trainer.save();
  member.delete();
  redirect ("/dashboard");
}

}