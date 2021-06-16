package by.barsnik96.HeadHunter_JSP.utils;

import java.util.ArrayList;

public class LoadingParameters
{
    public static ArrayList<String> prof_areas_names = new ArrayList<String>();
    public static ArrayList<String> specializations_names = new ArrayList<String>();
    public static ArrayList<String> company_industries_names = new ArrayList<String>();
    public static ArrayList<String> company_scopes_names = new ArrayList<String>();
    public static ArrayList<String> areas_names = new ArrayList<String>();
    public static ArrayList<String> metro_lines_names = new ArrayList<String>();
    public static ArrayList<String> metro_stations_names = new ArrayList<String>();

    public static void SetLoadingParametersArrayListsToNull(int prof_areas_count, int specializations_count,
                                                            int company_industries_count, int company_scopes_count,
                                                            int areas_count,
                                                            int metro_lines_count, int metro_stations_count)
    {
        for (int i = 0; i < prof_areas_count; i++)
        {
            LoadingParameters.prof_areas_names.add(i, null);
        }
        for (int i = 0; i < specializations_count; i++)
        {
            LoadingParameters.specializations_names.add(i, null);
        }
        //
        //
        for (int i = 0; i < company_industries_count; i++)
        {
            LoadingParameters.company_industries_names.add(i, null);
        }
        for (int i = 0; i < company_scopes_count; i++)
        {
            LoadingParameters.company_scopes_names.add(i, null);
        }
        //
        //
        for (int i = 0; i < areas_count; i++)
        {
            LoadingParameters.areas_names.add(i, null);
        }
        //
        //
        for (int i = 0; i < metro_lines_count; i++)
        {
            LoadingParameters.metro_lines_names.add(i, null);
        }
        for (int i = 0; i < metro_stations_count; i++)
        {
            LoadingParameters.metro_stations_names.add(i, null);
        }
    }
}