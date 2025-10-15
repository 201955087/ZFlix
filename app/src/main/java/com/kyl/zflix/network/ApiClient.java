package com.kyl.zflix.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://withcorn.store:2909/";
    public static Retrofit getClient() {
        if (retrofit == null) {
            // 1. HttpLoggingInterceptor 객체를 생성하고 로그 레벨을 BODY로 설정합니다.
            //    BODY는 HTTP 요청과 응답의 헤더, 본문을 모두 보여줍니다.
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2. OkHttpClient.Builder를 사용해 클라이언트를 빌드하고 인터셉터를 추가합니다.
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS) // 연결 시간 초과 설정 (선택 사항)
                    .readTimeout(30, TimeUnit.SECONDS)    // 읽기 시간 초과 설정 (선택 사항)
                    .build();

            // 3. Retrofit 객체를 생성할 때, 위에서 만든 OkHttpClient를 연결합니다.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}