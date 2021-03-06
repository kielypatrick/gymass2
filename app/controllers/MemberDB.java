package controllers;

import java.util.List;

import utils.Analytics;

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
        double BMI = Analytics.calculateBMI(member);
        String BMICategory = Analytics.determineBMICategory(BMI);
        double idealBodyWeight = Analytics.idealBodyWeight(member);

        render("member.html", member, assessments, BMI, BMICategory, idealBodyWeight);
    }



}