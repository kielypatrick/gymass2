package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Assessment extends Model
{

    public String date;
    public String weight;
    public String chest;
    public String upperArm;
    public String waist;
    public String hips;



    public Assessment(String date, String weight, String chest, String upperArm, String waist, String hips)
    {
        this.date = date;
        this.weight = weight;
        this.chest = chest;
        this.upperArm = upperArm;
        this.waist = waist;
        this.hips = hips;
    }
}
