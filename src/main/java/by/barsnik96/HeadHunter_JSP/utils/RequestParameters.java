package by.barsnik96.HeadHunter_JSP.utils;

import java.util.ArrayList;

public class RequestParameters
{
    public static ArrayList<Double> specializations_ids = new ArrayList<Double>();
    public static ArrayList<Double> company_industries_ids = new ArrayList<Double>();
    public static ArrayList<Integer> areas_ids = new ArrayList<Integer>();
    public static ArrayList<Double> metro_ids = new ArrayList<Double>();
    public static String text;
    public static String currency_code;
    public static String salary;
    public static String only_with_salary;
    public static String experience;
    public static ArrayList<String> employment = new ArrayList<String>();
    public static ArrayList<String> schedule = new ArrayList<String>();
    public static String time;

    public static void SetRequestParametersArrayListsToNull(int sum_specializations_count,
                                                            int sum_company_industries_count,
                                                            int areas_count,
                                                            int sum_metro_count)
    {
        for (int i = 0; i < sum_specializations_count; i++)
        {
            RequestParameters.specializations_ids.add(i, null);
        }
        //
        //
        for (int i = 0; i < sum_company_industries_count; i++)
        {
            RequestParameters.company_industries_ids.add(i, null);
        }
        //
        //
        for (int i = 0; i < areas_count; i++)
        {
            RequestParameters.areas_ids.add(i, null);
        }
        //
        //
        for (int i = 0; i < sum_metro_count; i++)
        {
            RequestParameters.metro_ids.add(i, null);
        }
        //
        //
        RequestParameters.text = "";
        RequestParameters.currency_code = "RUR";
        RequestParameters.salary = "";
        RequestParameters.only_with_salary = "false";
        RequestParameters.experience = "doesNotMatter";
        for (int i = 0; i < 5; i++)
        {
            RequestParameters.employment.add(i, null);
        }
        for (int i = 0; i < 5; i++)
        {
            RequestParameters.schedule.add(i, null);
        }
        RequestParameters.time = "0";
    }
}