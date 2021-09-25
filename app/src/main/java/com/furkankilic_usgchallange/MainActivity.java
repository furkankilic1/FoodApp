package com.furkankilic_usgchallange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.furkankilic_usgchallange.Objects.Category;
import com.furkankilic_usgchallange.Objects.Country;
import com.furkankilic_usgchallange.RecyclerViewAdapters.MyRecyclerViewAdapter;
import com.furkankilic_usgchallange.RecyclerViewAdapters.MyRecyclerViewAdapterCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerCountry;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mRecyclerAdapter;
    private RecyclerView recyclerCategory;
    private RecyclerView.LayoutManager cLayoutManager;
    private MyRecyclerViewAdapterCategory cRecyclerAdapter;
    private RequestQueue mRequestQueue;
    private JSONArray countries;
    private JSONObject countryJSONObject;
    private JSONArray categories;
    private JSONObject categoryJSONObject;
    private static String COUNTRY_URL = "https://www.themealdb.com/api/json/v1/1/list.php?a=list";
    private ArrayList<Country> mArrayList;
    private ArrayList<Category> cArrayList;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Loading...");

        mRequestQueue = Volley.newRequestQueue(this);

        recyclerCountry = (RecyclerView) findViewById(R.id.recyclerViewCountries);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCountry.setLayoutManager(mLayoutManager);

        recyclerCategory = (RecyclerView) findViewById(R.id.recyclerViewCategories);
        cLayoutManager = new LinearLayoutManager(this);
        recyclerCategory.setLayoutManager(cLayoutManager);

        mArrayList = new ArrayList<Country>();
        cArrayList = new ArrayList<Category>();

        JSONObject obj = new JSONObject();
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.DEPRECATED_GET_OR_POST, COUNTRY_URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        countryJSONObject = response;
                        new GetCountries().execute();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Result", "ERROR" + error.toString());
            }
        });

        JSONObject catObj = new JSONObject();
        String CATEGORY_URL = "https://www.themealdb.com/api/json/v1/1/categories.php";
        JsonObjectRequest cJsonObjectRequest = new JsonObjectRequest(
                Request.Method.DEPRECATED_GET_OR_POST, CATEGORY_URL, catObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        categoryJSONObject = response;
                        new GetCategories().execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Result", "ERROR JSONObject request" + error.toString());
            }
        });

        mRequestQueue.getCache().clear();
        mRequestQueue.add(mJsonObjectRequest);
        mRequestQueue.add(cJsonObjectRequest);
    }

    private class GetCountries extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                countries = countryJSONObject.getJSONArray("meals");
                for (int i = 0; i < countries.length(); i++) {
                    JSONObject jsonObj = countries.getJSONObject(i);
                    String strArea = jsonObj.getString("strArea");
                    Country c = new Country(strArea);

                    mArrayList.add(c);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mArrayList != null) {
                mRecyclerAdapter = new MyRecyclerViewAdapter(MainActivity.this, mArrayList);
                recyclerCountry.setAdapter(mRecyclerAdapter);
            }
            else
                Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_LONG).show();

            mDialog.dismiss();
        }
    }

    private class GetCategories extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                categories = categoryJSONObject.getJSONArray("categories");
                for (int i = 0; i < categories.length(); i++) {

                    JSONObject jsonObj = categories.getJSONObject(i);

                    String idCategory = jsonObj.getString("idCategory");
                    String strCategory = jsonObj.getString("strCategory");
                    String strCategoryThumb = jsonObj.getString("strCategoryThumb");
                    String strCategoryDescription = jsonObj.getString("strCategoryDescription");

                    Category c = new Category(idCategory, strCategory, strCategoryThumb, strCategoryDescription );

                    cArrayList.add(c);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (cArrayList != null) {
                cRecyclerAdapter = new MyRecyclerViewAdapterCategory(MainActivity.this, cArrayList);
                recyclerCategory.setAdapter(cRecyclerAdapter);
            }
            else
                Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_LONG).show();

            mDialog.dismiss();
        }
    }

}