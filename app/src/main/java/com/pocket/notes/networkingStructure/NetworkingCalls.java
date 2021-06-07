package com.pocket.notes.networkingStructure;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pocket.notes.database.NotesDataDB;
import com.pocket.notes.database.NotesDataDao;
import com.pocket.notes.databaseEntity.NotesData;
import com.pocket.notes.entryStructure.OnBoardingFragment;
import com.pocket.notes.homeStructure.MainFragment;
import com.pocket.notes.models.LoginModel;
import com.pocket.notes.models.NotesModel;
import com.pocket.notes.prefrences.LoginPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NetworkingCalls {
    private static final String TAG = "NetworkingCalls";
    private Context context;
    Activity activity;
    private NetworkingCallBacks networkingCallBacks;
    private NotesURL notesURL = new NotesURL();
    private RequestQueue requestQueue;
    LoginPreference loginPreference;

    public NetworkingCalls(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        loginPreference = new LoginPreference(context);
    }

    public NetworkingCalls(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(context);
        loginPreference = new LoginPreference(context);
    }

    public NetworkingCalls(Context context, NetworkingCallBacks networkingCallBacks) {
        this.context = context;
        this.networkingCallBacks = networkingCallBacks;
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(context);
        loginPreference = new LoginPreference(context);
    }

    public void loginRequest(final String email, final String name) {
        StringRequest request = new StringRequest(Request.Method.POST, notesURL.getLOGIN(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse login"+response );
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Type listType = new TypeToken<LoginModel>(){}.getType();
                        LoginModel loginModel = gson.fromJson(String.valueOf(jsonObject.getJSONArray("data").getJSONObject(0)), listType);
                        getNotes(loginModel);
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, true, null);
                    }
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, null);
                } catch (Exception e) {
                    Log.e("exception",e.getMessage());
//                    networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                return params;
            }

        };
        addToQueue(request);
    }

    public void getNotes(final LoginModel loginModel) {
        StringRequest request = new StringRequest(Request.Method.GET, notesURL.getGET_NOTES() + loginModel.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse notes"+response );
                NotesDataDao notesDataDao = NotesDataDB.getInstance(context).notesDataDao();
                try {
                    notesDataDao.removeNotesData();
                } catch (Exception e) {
//                    networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.fetchUserData, false, null);
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Type listType = new TypeToken<List<NotesModel>>(){}.getType();
                        List<NotesModel> notesModels = gson.fromJson(String.valueOf(jsonObject.getJSONArray("data")), listType);
                        for (int j = 0 ; j < notesModels.size() ; j++)
                        {
                            NotesData notesData = new NotesData(notesModels.get(j).getId(), notesModels.get(j).getTitle(),notesModels.get(j).getDescription(), notesModels.get(j).getImage());
                            notesDataDao.insertNotesData(notesData);
                        }
                        OnBoardingFragment.onBoardingViewModel.getLoginLiveData().setValue(loginModel);
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, true, null);
                    }
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, null);
                } catch (Exception e) {
                    Log.e("exception",e.getMessage());
//                    networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, error);
            }
        });

        addToQueue(request);
    }

    public void getNotes(final int id, int flag) {
        StringRequest request = new StringRequest(Request.Method.GET, notesURL.getGET_NOTES() + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse notes"+response );
                NotesDataDao notesDataDao = NotesDataDB.getInstance(context).notesDataDao();
                try {
                    notesDataDao.removeNotesData();
                } catch (Exception e) {
//                    networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.fetchUserData, false, null);
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Type listType = new TypeToken<ArrayList<NotesModel>>(){}.getType();
                        ArrayList<NotesModel> notesModels = gson.fromJson(String.valueOf(jsonObject.getJSONArray("data")), listType);
                        for (int j = 0 ; j < notesModels.size() ; j++)
                        {
                            NotesData notesData = new NotesData(notesModels.get(j).getId(), notesModels.get(j).getTitle(),notesModels.get(j).getDescription(), notesModels.get(j).getImage());
                            notesDataDao.insertNotesData(notesData);
                        }
                        List<NotesData> notesDataList = notesDataDao.getNotes();
                        ArrayList<NotesData> notesData = new ArrayList<>(notesDataList);

                        if (flag == 1)
                            MainFragment.notesViewModel.getNotesLiveData().setValue(notesData);
                        else if (flag == 0)
                            networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.addNote,true,null);
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, true, null);
                    }
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, null);
                } catch (Exception e) {
                    Log.e("exception",e.getMessage());
//                    networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, error);
            }
        });

        addToQueue(request);
    }


    public void deleteNotes(final int id) {
        StringRequest request = new StringRequest(Request.Method.GET, notesURL.getDELETE_NOTES() + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse notes"+response );
                NotesDataDao notesDataDao = NotesDataDB.getInstance(context).notesDataDao();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_SHORT).show();
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, true, null);
                    }
//                        networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, null);
                } catch (Exception e) {
                    Log.e("exception",e.getMessage());
//                    networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.login, false, error);
            }
        });

        addToQueue(request);
    }


    public void addNote(String title,String description){

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Adding Note");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, notesURL.getADD_NOTES(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("onResponseAddNote", ""+response);
                getNotes(loginPreference.getId(), 0);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResAddNote", ""+error);
                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.addNote,false,error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title",title );
                params.put("description",description );
                params.put("userId",""+loginPreference.getId());
                return params;
            }
        };
        addToQueue(stringRequest);
    }

    public void updateNotes(String title,String description,String id){

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Updating Note");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, notesURL.getEDIT_NOTE(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("onResponseEditNote", ""+response);
                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.editNotes,true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResEditNote", ""+error);
                networkingCallBacks.networkingRequestPerformed(NetworkingCallBacks.MethodType.editNotes,false,error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title",title );
                params.put("description",description );
                params.put("id",id);
                return params;
            }
        };
        addToQueue(stringRequest);

    }

    private void addToQueue(StringRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(30),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}
