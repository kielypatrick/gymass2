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

  public static void index()
  {
    Logger.info("Rendering Dashboard");
    Trainer trainer = Accounts.getLoggedInTrainer();
    List<Member> members = trainer.members;
    render("dashboard.html", trainer, members);
  }

  public static void addMember(String firstname, String lastname, String email, String password)
  {
    Trainer trainer = Accounts.getLoggedInTrainer();
    Member member = new Member(firstname, lastname, email, password);
    trainer.members.add(member);
    member.save();
    trainer.save();
    Logger.info("Adding Member" + firstname + lastname);
    redirect("/dashboard");
  }

}