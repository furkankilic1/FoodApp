package com.furkankilic_usgchallange.DatabaseOperations;

import android.content.ContentValues;
import android.database.Cursor;

import com.furkankilic_usgchallange.Objects.Product;

import java.util.ArrayList;

public class FavoriteDB {
    public static String TABLE_NAME="favorites";
    public static String FIELD_IDMEAL = "idMeal";
    public static String FIELD_MEAL = "strMeal";
    public static String FIELD_IMAGE = "strMealThumb";
    public static String CREATE_TABLE_SQL="CREATE TABLE "+TABLE_NAME+" ("+FIELD_IDMEAL+" TEXT, "+FIELD_MEAL+" TEXT, "+FIELD_IMAGE+" TEXT)";
    public static String DROP_TABLE_SQL = "DROP TABLE if exists "+TABLE_NAME;

    public static ArrayList<Product> getAllProduct(DatabaseHelper dbHelper){
        Product anItem;
        ArrayList<Product> data = new ArrayList<>();
        Cursor cursor = dbHelper.getAllRecords(TABLE_NAME, null);
        while(cursor.moveToNext()){
            String idMeal = cursor.getString(0);
            String strMeal= cursor.getString(1);
            String strMealThumb= cursor.getString(2);
            anItem = new Product(idMeal, strMeal, strMealThumb);
            data.add(anItem);

        }
        return data;
    }

    public static ArrayList<Product> findProduct(DatabaseHelper dbHelper, String key) {
        Product anItem;
        ArrayList<Product> data = new ArrayList<>();
        String where = FIELD_IDMEAL+" like '%"+key+"%'";

        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);
        while(cursor.moveToNext()){
            String idMeal = cursor.getString(0);
            String strMeal= cursor.getString(1);
            String strMealThumb= cursor.getString(2);
            anItem = new Product(idMeal, strMeal, strMealThumb);
            data.add(anItem);
        }
        return data;
    }

    public static boolean insertProduct(DatabaseHelper dbHelper, String idMeal, String strMeal, String strMealThumb){
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_IDMEAL, idMeal);
        contentValues.put(FIELD_MEAL, strMeal);
        contentValues.put(FIELD_IMAGE, strMealThumb);

        boolean res = dbHelper.insert(TABLE_NAME,contentValues);
        return res;
    }
    public static boolean insertProduct(DatabaseHelper dbHelper, Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_IDMEAL, product.getIdMeal());
        contentValues.put(FIELD_MEAL, product.getStrMeal());
        contentValues.put(FIELD_IMAGE, product.getStrMealThumb());

        boolean res = dbHelper.insert(TABLE_NAME,contentValues);
        return res;
    }

    public static boolean insertProduct(DatabaseHelper dbHelper, String idMeal, String strMeal){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_IDMEAL, idMeal);
        contentValues.put(FIELD_MEAL, strMeal);

        boolean res = dbHelper.insert(TABLE_NAME,contentValues);
        return res;
    }

    public static boolean delete(DatabaseHelper dbHelper, String id){
        String where = FIELD_IDMEAL + " = "+id;
        boolean res =  dbHelper.delete(TABLE_NAME, where);
        return  res;
    }

}
