package by.barsnik96.HeadHunter_JSP.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface HeadHunterAPI
{
    @GET("vacancies/{vacancy_id}")
    Call<JsonObject> getVacancyById(@Path("vacancy_id") String vacancyId);

    @GET
    Call<JsonElement> getVacancyByUrl(@Url String url);

    @GET("vacancies/")
    Call<JsonObject> getVacancies(@Query("text") String text,
                                  @Query("specialization") ArrayList<Double> specialization, @Query("industry") ArrayList<Double> industry,
                                  @Query("area") ArrayList<Integer> area, @Query("metro") ArrayList<Double> metro,
                                  @Query("salary") String salary, @Query("currency_code") String currency_code, @Query("only_with_salary") String only_with_salary,
                                  @Query("experience") String experience,
                                  @Query("employment") ArrayList<String> employment,
                                  @Query("schedule") ArrayList<String> schedule,
                                  @Query("order_by") String order_by, @Query("search_period") String time,
                                  @Query("per_page") String per_page, @Query("page") String page);

    @GET
    Call<JsonObject> getEmployerByUrl(@Url String url);

    @GET("dictionaries")
    Call<JsonObject> getDictionaries();

    @GET("specializations")
    Call<JsonArray> getSpecializations();

    @GET("areas")
    Call<JsonArray> getAreas();

    @GET("metro")
    Call<JsonArray> getMetro();

    @GET("industries")
    Call<JsonArray> getIndustries();
}