package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

import java.util.Date;

@Entity
public class Assessment extends Model
{

    public String date;
    public double weight;
    public double chest;
    public double upperArm;
    public double waist;
    public double hips;
    public String comment;



    public Assessment(String date, double weight, double chest, double upperArm, double waist, double hips, String comment)
    {
        this.date = date;
        this.weight = weight;
        this.chest = chest;
        this.upperArm = upperArm;
        this.waist = waist;
        this.hips = hips;
        this.comment = comment;

    }

    public double getWeight() {
        return weight;
    }





}

