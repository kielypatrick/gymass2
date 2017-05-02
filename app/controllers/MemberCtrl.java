package controllers;


import models.Assessment;
import models.Member;
import play.Logger;
import play.mvc.Controller;


public class MemberCtrl extends Controller
{
  public static void index(Long id)
  {
	    Member member = Member.findById(id);
	    Logger.info ("Member id = " + id);
        render("memberupdate.html", member);
  }

    public static void addAssessment(Long id, String date, String weight, String chest, String upperArm, String waist, String hips)
    {
        Assessment assessment = new Assessment(date, weight, chest, upperArm, waist, hips);
        Member member = Member.findById(id);
        member.assessments.add(assessment);
        member.save();
        redirect ("/dashboard");
    }

  
}