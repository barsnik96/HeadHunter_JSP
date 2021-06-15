package by.barsnik96.HeadHunter_JSP.utils;

import java.util.ArrayList;

public class RequestParameters
{
    public static ArrayList<Double> specializations_ids = new ArrayList<Double>();
    public static ArrayList<Double> company_industries_ids = new ArrayList<Double>();
    public static ArrayList<Double> areas_ids = new ArrayList<Double>();

    public static void SetRequestParametersArrayListsToNull(int sum_specializations_count,
                                                            int sum_company_industries_count,
                                                            int areas_count)
    {
        for (int i = 0; i < sum_specializations_count; i++)
        {
            RequestParameters.specializations_ids.add(i, null);
        }
        for (int i = 0; i < sum_company_industries_count; i++)
        {
            RequestParameters.company_industries_ids.add(i, null);
        }
        for (int i = 0; i < areas_count; i++)
        {
            RequestParameters.areas_ids.add(i, null);
        }
    }
}