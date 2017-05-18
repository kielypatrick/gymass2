package utils;

import models.*;


/**
 * Created by Patrick on 18/05/2017.
 */
public class Analytics {


    private static double toTwoDecimalPlaces(double num) {
        return (int) (num * 100) / 100.0;
    }


    /**
     * This method calculates the BMI value for the member.
     * <p>
     * The formula used for BMI is weight divided by the square of the height.
     *
     * @return the BMI value for the member.  The number returned is truncated to two decimal places.
     **/
    public static double calculateBMI(Member member) {
        if (member.assessments.size() > 0) {
            return toTwoDecimalPlaces(member.assessments.get(member.assessments.size() - 1).getWeight() / (member.getHeight() * member.getHeight()));
        } else {
            return toTwoDecimalPlaces(member.getStartingweight() / (member.getHeight() * member.getHeight()));
        }
    }

    /**
     * This method determines the BMI category that the member belongs to.
     * <p>
     * <pre>
     *
     * The category is determined by the magnitude of the members BMI according to the following:
     *
     *     BMI less than    15   (exclusive)                      is "VERY SEVERELY UNDERWEIGHT"
     *     BMI between      15   (inclusive) and 16   (exclusive) is "SEVERELY UNDERWEIGHT"
     *     BMI between      16   (inclusive) and 18.5 (exclusive) is "UNDERWEIGHT"
     *     BMI between      18.5 (inclusive) and 25   (exclusive) is "NORMAL"
     *     BMI between      25   (inclusive) and 30   (exclusive) is "OVERWEIGHT"
     *     BMI between      30   (inclusive) and 35   (exclusive) is "MODERATELY OBESE"
     *     BMI between      35   (inclusive) and 40   (exclusive) is "SEVERELY OBESE"
     *     BMI greater than 40   (inclusive)                      is "VERY SEVERELY OBESE"
     *
     * </pre>
     *
     * @return <pre>
     * The format of the String is similar to this (note the double quotes around the category):
     *     "NORMAL".
     * </pre>
     */
    public static String determineBMICategory(double bmi) {
        String BMICategory = "";

        if (bmi < 15) {
            BMICategory = "VERY SEVERELY UNDERWEIGHT";
        } else if (bmi < 16) {
            BMICategory = "SEVERELY UNDERWEIGHT";
        } else if (bmi < 18.5) {
            BMICategory = "UNDERWEIGHT";
        } else if (bmi < 25) {
            BMICategory = "NORMAL";
        } else if (bmi < 30) {
            BMICategory = "OVERWEIGHT";
        } else if (bmi < 35) {
            BMICategory = "MODERATELY OBESE";
        } else if (bmi < 40) {
            BMICategory = "SEVERELY OBESE";
        } else {
            BMICategory = "VERY SEVERELY OBESE";
        }
        return BMICategory;
    }

    /**
     * This method returns the member height converted from metres to inches.
     * @return member height converted from metres to inches using the formula: meters
     * multiplied by 39.37.  The number returned is truncated to two decimal places.
     **/
    public static double convertHeightMetresToInches(Member member){
        return toTwoDecimalPlaces(member.getHeight() * 39.37);
    }


    /**
     * This method returns a boolean to indicate if the member has an ideal
     * body weight based on the Devine formula.
     *
     * <pre>
     * For males, an ideal body weight is:   50 kg + 2.3 kg for each inch over 5 feet.
     * For females, an ideal body weight is: 45.5 kg + 2.3 kg for each inch over 5 feet.
     *
     * Note:  if no gender is specified, return the result of the female calculation.
     *
     * </pre>
     *
     * @return Returns true if the result of the devine formula is within 2 kgs (inclusive) of the
     *         starting weight; false if it is outside this range.
     */
    public static double idealBodyWeight(Member member)
    {
        double fiveFeet = 60.0;

        double inches = convertHeightMetresToInches(member);
        if (inches <= fiveFeet){
            if (member.getMemberGender().equals("M")){
                return 50;
            }
            else{
                return 45.5;
            }
        }
        else{
            if (member.getMemberGender().equals("M") || member.getMemberGender().equals(("m"))){
                return toTwoDecimalPlaces(50 + ((inches - fiveFeet) * 2.3));
            }
            else{
                return toTwoDecimalPlaces(45.5 + ((inches - fiveFeet) * 2.3));
            }
        }


    }




}
