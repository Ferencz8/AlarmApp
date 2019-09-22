package countingsheep.alarm.core.services;

import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firestore.v1.TargetOrBuilder;

import countingsheep.alarm.core.contracts.OnResult;
import countingsheep.alarm.core.domain.Comment;
import countingsheep.alarm.core.domain.enums.CommentType;
import countingsheep.alarm.core.services.interfaces.UserService;
import countingsheep.alarm.db.SharedPreferencesContainer;
import countingsheep.alarm.infrastructure.NetworkStateInteractor;
import countingsheep.alarm.network.retrofit.UserAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserServiceImpl implements UserService {
    private String TAG = this.getClass().getName();
    private Retrofit retrofit;
    private SharedPreferencesContainer sharedPreferencesContainer;
    private NetworkStateInteractor networkStateInteractor;
    private TimeService timeService;


    public UserServiceImpl(Retrofit retrofit, SharedPreferencesContainer sharedPreferencesContainer, NetworkStateInteractor networkStateInteractor, TimeService timeService) {
        this.retrofit = retrofit;
        this.sharedPreferencesContainer = sharedPreferencesContainer;
        this.networkStateInteractor = networkStateInteractor;
        this.timeService = timeService;
    }

    @Override
    public void sendRoastFeedback(int messageId, String content, OnResult onResult) {
        if (this.networkStateInteractor.isNetworkAvailable()) {

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setType(CommentType.RoastResponse);
            comment.setValue(Integer.toString(messageId));
            comment.setDateCreated(timeService.getUTCDateNow());
            comment.setUserId(sharedPreferencesContainer.getCurrentUserId());
            retrofit.create(UserAPI.class).addComment(comment).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(onResult!=null){
                        onResult.onSuccess(response);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Crashlytics.logException(t);
                }
            });
        }
        else {
            if(onResult!=null){
                onResult.onFailure("No Internet connection detected.");
            }
        }
    }

    @Override
    public void sendGeneralFeedback(String content, OnResult onResult) {
        if (this.networkStateInteractor.isNetworkAvailable()) {

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setType(CommentType.Feedback);
            comment.setDateCreated(timeService.getUTCDateNow());
            comment.setUserId(sharedPreferencesContainer.getCurrentUserId());
            retrofit.create(UserAPI.class).addComment(comment).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(onResult!=null){
                        onResult.onSuccess(response);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Crashlytics.logException(t);
                }
            });
        }
        else {
            if(onResult!=null){
                onResult.onFailure("No Internet connection detected.");
            }
        }
    }
}
