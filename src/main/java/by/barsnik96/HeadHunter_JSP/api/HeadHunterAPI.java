package by.barsnik96.HeadHunter_JSP.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.Map;

public interface HeadHunterAPI
{
    @GET("vacancies/{vacancy_id}")
    Call<JsonObject> getVacancyById(@Path("vacancy_id") int vacancyId);

    @GET
    Call<JsonObject> getVacancyByUrl(@Url String url);

    @GET("vacancies/")
    Call<JsonObject> getVacancies(@Query("text") String text, @Query("per_page") String per_page, @Query("page") String page,
                                  @Query("specialization") ArrayList<Double> specializations, @Query("area") String area,
                                  @Query("currency_code") String currency_code, @Query("salary") String salary, @Query("only_with_salary") String only_with_salary,
                                  @Query("experience") String experience, @Query("employment") String employment, @Query("schedule") String schedule);

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