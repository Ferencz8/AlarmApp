package countingsheep.alarm.internaldi.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import countingsheep.alarm.AlarmApplication;
import countingsheep.alarm.util.Constants;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final AlarmApplication application;

    public ApplicationModule(AlarmApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    AlarmApplication provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(AlarmApplication application) {
        return application.getSharedPreferences(Constants.PrefFileName, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

//        //TODO:: this is for Testing

        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .callTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
//        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
//        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);

        return new retrofit2.Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
