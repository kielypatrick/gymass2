package controllers;

import java.util.List;

import models.Assessment;
import models.Trainer;
import models.Member;
import play.Logger;
import play.mvc.Controller;

public class MemberDB extends Controller
{
    //public static void index()
    //{
    //Logger.info("Rendering Dashboard");

    //List<Member> members = Member.findAll();
    //render ("dashboard.html", members);
    //}

    public static void index()
    {
        Logger.info("Rendering MemberDB");
        Member member = Accounts.getLoggedInMember();
        List<Assessment> assessments = member.assessments;
        render("member.html", member, assessments);
    }



}