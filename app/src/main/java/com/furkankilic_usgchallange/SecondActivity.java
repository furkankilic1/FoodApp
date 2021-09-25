package com.furkankilic_usgchallange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.furkankilic_usgchallange.Objects.Product;
import com.furkankilic_usgchallange.RecyclerViewAdapters.MyRecyclerViewAdapterProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {
    private RecyclerView recyclerProduct;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyRecyclerViewAdapterProduct mRecyclerAdapter;
    private RequestQueue mRequestQueue;
    private JSONArray products;
    private JSONObject productJSONObject;
    private ArrayList<Product> pArrayList;
    private ProgressDialog mDialog;

    Intent intent = null;
    String key;
    TextView tvSecondAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().hide();

        tvSecondAct = findViewById(R.id.tvSecondAct);

        mDialog = new ProgressDialog(SecondActivity.this);
        mDialog.setMessage("Loading...");

        mRequestQueue = Volley.newRequestQueue(SecondActivity.this);

        recyclerProduct = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        mLayoutManager = new LinearLayoutManager(SecondActivity.this);
        recyclerProduct.setLayoutManager(mLayoutManager);

        pArrayList = new ArrayList<Product>();

        intent = getIntent();

        JSONObject obj = new JSONObject();
        String PRODUCT_URL = "https://www.themealdb.com/api/json/v1/1/filter.php";
        if (intent.hasExtra("strCategory")) {
            key = intent.getStringExtra("strCategory");
            PRODUCT_URL += "?c="+key;
        } else if (intent.hasExtra("strArea")) {
            key = intent.getStringExtra("strArea");
            PRODUCT_URL += "?a="+key;
        }
        tvSecondAct.setText(key+ " Category");
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.DEPRECATED_GET_OR_POST, PRODUCT_URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        productJSONObject = response;
                        new GetProducts().execute();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Result",
                        "ERROR JSONObject request" + error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                if (intent.hasExtra("strCategory")) {
                    key = intent.getStringExtra("strCategory");
                    params.put("c", key);
                } else if (intent.hasExtra("strArea")) {
                    key = intent.getStringExtra("strArea");
                    params.put("a", key);
                }
                return params;
            }
        };

        mRequestQueue.getCache().clear();
        mRequestQueue.add(mJsonObjectRequest);

    }

    private class GetProducts extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                products = productJSONObject.getJSONArray("meals");

                for (int i = 0; i < products.length(); i++) {

                    JSONObject jsonObj = products.getJSONObject(i);
                    String strMeal = jsonObj.getString("strMeal");
                    String strMealThumb = jsonObj.getString("strMealThumb");
                    String idMeal = jsonObj.getString("idMeal");

                    Product p = new Product(strMeal, strMealThumb, idMeal);

                    pArrayList.add(p);
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
            if (pArrayList != null) {
                mRecyclerAdapter = new MyRecyclerViewAdapterProduct(SecondActivity.this, pArrayList);
                recyclerProduct.setAdapter(mRecyclerAdapter);
            }
            else
                Toast.makeText(SecondActivity.this, "Not Found", Toast.LENGTH_LONG).show();

            mDialog.dismiss();
        }
    }
}