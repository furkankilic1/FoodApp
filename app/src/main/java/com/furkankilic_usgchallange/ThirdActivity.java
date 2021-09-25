package com.furkankilic_usgchallange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.furkankilic_usgchallange.DatabaseOperations.DatabaseHelper;
import com.furkankilic_usgchallange.DatabaseOperations.FavoriteDB;
import com.furkankilic_usgchallange.Objects.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    Intent intent = null;
    private JSONArray productDetails;
    private JSONObject productDetailJSONObject;
    private ArrayList<Product> pdArrayList;
    private ArrayList<Product> checkExist;
    private String key = "";
    private ProgressDialog mDialog;
    private String MEALID = "";

    ImageView imgProductDetail, imgProductFav;
    TextView tvProductDetailName, tvProductDetailCategory, tvProductDerailDesc, tvHiddenProductId;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        getSupportActionBar().hide();

        imgProductDetail = findViewById(R.id.imgProductDetail);
        imgProductFav = findViewById(R.id.imgProductFav);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        tvProductDetailCategory = findViewById(R.id.tvProductDetailCategory);
        tvProductDerailDesc = findViewById(R.id.tvProductDerailDesc);
        tvHiddenProductId = findViewById(R.id.tvHiddenProductId);

        dbHelper = new DatabaseHelper(this);

        mDialog = new ProgressDialog(ThirdActivity.this);
        mDialog.setMessage("Loading...");

        mRequestQueue = Volley.newRequestQueue(ThirdActivity.this);

        intent = getIntent();
        String keyId = intent.getStringExtra("idMeal");
        pdArrayList = new ArrayList<Product>();

        String DETAIL_URL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
        key = intent.getStringExtra("idMeal");
        DETAIL_URL += key;

        JSONObject obj = new JSONObject();
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.DEPRECATED_GET_OR_POST, DETAIL_URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        productDetailJSONObject = response;
                        new GetProductDetails().execute();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Result",
                        "ERROR JSONObject request" + error.toString());
            }
        });

        mRequestQueue.getCache().clear();
        mRequestQueue.add(mJsonObjectRequest);

        imgProductFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExist = new ArrayList<>();
                checkExist = FavoriteDB.findProduct(dbHelper, MEALID);
                if (!checkExist.isEmpty() ){
                    FavoriteDB.delete(dbHelper, MEALID);
                    imgProductFav.setImageResource(R.drawable.unfav);
                    Toast.makeText(ThirdActivity.this, "Deleted from Favorite List", Toast.LENGTH_LONG).show();
                } else {
                    String DetailName = tvProductDetailName.getText().toString();
                    FavoriteDB.insertProduct(dbHelper, MEALID, DetailName );
                    imgProductFav.setImageResource(R.drawable.fav);
                    Toast.makeText(ThirdActivity.this, "Added to Favorite List", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private class GetProductDetails extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                // Getting JSON Array
                productDetails = productDetailJSONObject.getJSONArray("meals");

                // looping through all countries
                for (int i = 0; i < productDetails.length(); i++) {

                    JSONObject jsonObj = productDetails.getJSONObject(i);
                    String strMeal = jsonObj.getString("strMeal");
                    String strMealThumb = jsonObj.getString("strMealThumb");
                    String idMeal = jsonObj.getString("idMeal");
                    String strCategory = jsonObj.getString("strCategory");
                    String strInstructions = jsonObj.getString("strInstructions");
                    MEALID = idMeal;

                    Product p = new Product(strMeal, strMealThumb, idMeal, strCategory, strInstructions);

                    pdArrayList.add(p);
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
            for(int i = 0; i < pdArrayList.size(); i++) {
                Product p = pdArrayList.get(i);
                if(p.getIdMeal().equalsIgnoreCase(key)) {
                    tvProductDetailName.setText(p.getStrMeal());
                    tvProductDetailCategory.setText(p.getStrCategory());
                    tvProductDerailDesc.setText(p.getStrInstructions());
                    tvProductDerailDesc.setMovementMethod(new ScrollingMovementMethod());
                    tvHiddenProductId.setText(p.getIdMeal());
                    String imagenameWithaddress= p.getStrMealThumb();
                    Picasso.with(ThirdActivity.this)
                            .load(imagenameWithaddress)
                            .into(imgProductDetail);

                    checkExist = new ArrayList<>();
                    checkExist = FavoriteDB.findProduct(dbHelper, p.getIdMeal());
                    if (!checkExist.isEmpty() ){
                        imgProductFav.setImageResource(R.drawable.fav);
                    } else {
                        imgProductFav.setImageResource(R.drawable.unfav);
                    }
                }

            }

            mDialog.dismiss();
        }
    }

}