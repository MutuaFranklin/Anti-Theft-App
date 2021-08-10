package com.track.function;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.track.R;
import com.track.activity.HomeActivity;
import com.track.model.ResponseModel;
import com.track.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NetworkingFunction {

    private Context tContext;
    public static String domain = "http://jmobi.org";// "192.168.88.50:5555"";
    public static String domain_url = domain + "/track/mobile"; //173.255.224.47
    private ProgressDialog loadingDialog;

    public NetworkingFunction(Context context) {
        tContext = context;
    }

    public void loginUser(final String email, final String password) {
        loadingDialog = new FeaturesFunction(tContext).progressDialog(null, "Authenticating...");
        final String url = domain_url + "/login_user.php";
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String results = new String(responseBody);
//                new FeaturesFunction(tContext).alertDialog(null, results);
                try {
                    JSONObject jsonObject = new JSONObject(results);
                    String user = jsonObject.getString("user");
                    UserModel userModel = new Gson().fromJson(user, UserModel.class);

                    if (TextUtils.equals(userModel.email, email) && TextUtils.equals(userModel.password, password)) {
                        new PreferenceFunction(tContext).setUserId(userModel.user_id);
                        new PreferenceFunction(tContext).setUserName(userModel.user_name);
                        new PreferenceFunction(tContext).setEmail(email);
                        new PreferenceFunction(tContext).setPhoneNumber(userModel.phone_number);
                        new PreferenceFunction(tContext).setPassword(password);
                        new PreferenceFunction(tContext).setIntroDone(true);
                        new FeaturesFunction(tContext).toaster("Welcome back, " + userModel.user_name);
                        tContext.startActivity(new Intent(tContext, HomeActivity.class));
                        ((Activity) tContext).finish();
                    } else {
                        new FeaturesFunction(tContext).toaster("Please contact " + tContext.getString(R.string.app_name) + " for more information.");
                    }

                } catch (JSONException e) {
                    new FeaturesFunction(tContext).toaster("Login error : " + e.getMessage());
                    e.printStackTrace();
                }

                loadingDialog.dismiss();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                loadingDialog.dismiss();
            }
        });
    }

    public void registerUser(final String userName, final String email, final String password) {
        loadingDialog = new FeaturesFunction(tContext).progressDialog(null, "Please wait...");
        final String url = domain_url + "/register_user.php";
        RequestParams params = new RequestParams();
        params.put("userName", userName);
        params.put("phoneNumber", new FeaturesFunction(tContext).getSimPhoneNumber());
        params.put("email", email);
        params.put("password", password);
        new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String results = new String(responseBody);
                try {
                    ResponseModel userModel = new Gson().fromJson(results.trim(), ResponseModel.class);

                    if (TextUtils.equals(userModel.code, "200")) {
                        new FeaturesFunction(tContext).toaster("please login, " + userName);
                        tContext.startActivity(new Intent(tContext, HomeActivity.class));
                        ((Activity) tContext).finish();
                    } else if (TextUtils.equals(userModel.code, "300")) {
                        new FeaturesFunction(tContext).toaster("You already exist, " + userName);
                        tContext.startActivity(new Intent(tContext, HomeActivity.class));
                        ((Activity) tContext).finish();
                    } else {
                        new FeaturesFunction(tContext).toaster("Please contact " + tContext.getString(R.string.app_name) + " for more information.");
                    }

                } catch (Exception e) {
                    new FeaturesFunction(tContext).toaster("Register error : " + e.getMessage());
                    e.printStackTrace();
                }

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                loadingDialog.dismiss();
            }
        });
    }

    public void postChanges(String locationId, String descriptions) {
        final String url = domain_url + "/add_location.php";
        RequestParams params = new RequestParams();
        String simNumber = new FeaturesFunction(tContext).getSimPhoneNumber();
        params.put("locationId", locationId);
        params.put("userId", new PreferenceFunction(tContext).getUserId());
        params.put("latitude", new PreferenceFunction(tContext).getLatitude());
        params.put("longitude", new PreferenceFunction(tContext).getLongitude());
        params.put("location", new PreferenceFunction(tContext).getLocation());
        params.put("phone", simNumber);
        params.put("description", descriptions);
        new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String results = new String(responseBody);
                try {
                    ResponseModel responseModel = new Gson().fromJson(results.trim(), ResponseModel.class);

                    if (TextUtils.equals(responseModel.code, "200")) {
                        new FeaturesFunction(tContext).toaster("Added successful.");
                        new LocationFunction(tContext).deleteLocation(responseModel.results);
                    } else {
                        new FeaturesFunction(tContext).toaster("Please contact " + tContext.getString(R.string.app_name) + " for more information.");
                    }

                } catch (Exception e) {
                    new FeaturesFunction(tContext).toaster("Posting location error : \n" + e.getMessage());
                    e.printStackTrace();
                }

                new FeaturesFunction(tContext).toaster(results);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public void fetchLocation() {
        final String url = domain_url + "/fetch_location.php";
        RequestParams params = new RequestParams();
        params.put("userId", new PreferenceFunction(tContext).getUserId());
        new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String results = new String(responseBody);
//                new FeaturesFunction(tContext).toaster(results);
                try {
                    JSONObject jsonObject = new JSONObject(results);
                    String code = jsonObject.getString("code");
                    JSONArray jsonArray = jsonObject.getJSONArray(UtilityFunction.locations);
//                    ResponseModel responseModel = new Gson().fromJson(results.trim(), ResponseModel.class);
//
                    if (TextUtils.equals(code, "200")) {
                        new PreferenceFunction(tContext).setArrays(UtilityFunction.locations , results.trim());
                    } else {
                        new FeaturesFunction(tContext).toaster("Please contact " + tContext.getString(R.string.app_name) + " for more information.");
                    }
                    new PreferenceFunction(tContext).setArrays(UtilityFunction.locations , results.trim());
                } catch (Exception e) {
                    new FeaturesFunction(tContext).toaster("Location error : \n" + e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

}
