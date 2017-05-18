package controllers;


import models.Assessment;
import models.Member;

import play.Logger;
import play.mvc.Controller;




public class MemberCtrl extends Controller {
    public static void index(Long id) {
        Member member = Member.findById(id);
        Logger.info("Member id = " + id);
        render("memberupdate.html", member);
    }

    public static void addAssessment(Long id, String date, double weight, double chest, double upperArm, double waist, double hips, String comment)
    {
        Assessment assessment = new Assessment(date, weight, chest, upperArm, waist, hips, comment);
        Member member = Member.findById(id);
        member.assessments.add(assessment);
        member.save();
        redirect("/dashboard");
    }

    public static void deleteassessment (Long id, Long assessmentid)
    {
        Member member = Member.findById(id);
        Assessment assessment = Assessment.findById(assessmentid);
        Logger.info ("Removing" + assessment.date);
        member.assessments.remove(assessment);
        member.save();
        assessment.delete();
        render("member.html", member);
    }

}