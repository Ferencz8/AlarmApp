package countingsheep.alarm.dataaccess.httpservices;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.domain.User;
import countingsheep.alarm.core.contracts.api.ApiAuthenticationService;
import countingsheep.alarm.util.HttpRequester;

@Singleton
public class ApiAuthenticationServiceImpl implements ApiAuthenticationService {

    private Activity activity;

    @Inject
    public ApiAuthenticationServiceImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void register(User user) {
        String url = "http://my-json-feed";
        JSONObject parameters = null;
        try {
            Gson gson = new Gson();
            parameters = new JSONObject(gson.toJson(user));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(ApiAuthenticationServiceImpl.class.getSimpleName(),"Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Access the RequestQueue through your singleton class.
        HttpRequester.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }
}
