package by.barsnik96.HeadHunter_JSP.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "https://api.hh.ru/";
    private Retrofit mRetrofit;
    private HttpLoggingInterceptor mLogging;
    private OkHttpClient mClient;

    private NetworkService() {
        // Настройка уровня логирования
        mLogging = new HttpLoggingInterceptor();
        mLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Установка перехватчика для клиента OkHttp
        mClient = new OkHttpClient.Builder()
                .addInterceptor(mLogging)
                .build();
        // Создание билдера Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient)
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public HeadHunterAPI getHeadHunterApi() {
        return mRetrofit.create(HeadHunterAPI.class);
    }
}