package controllers;

import java.util.List;

import models.Assessment;
import play.Logger;
import play.mvc.Controller;

public class Admin extends Controller
{
  public static void index() 
  {
    Logger.info("Rendering Admin");
    
    List<Assessment> assessments = Assessment.findAll();
    
    

    // TODO - get list of all assessments, and send them to the view
    render ("admin.html", assessments);
  }
}