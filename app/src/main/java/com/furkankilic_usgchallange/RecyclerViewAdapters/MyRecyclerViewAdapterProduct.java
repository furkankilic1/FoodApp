package com.furkankilic_usgchallange.RecyclerViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkankilic_usgchallange.DatabaseOperations.DatabaseHelper;
import com.furkankilic_usgchallange.DatabaseOperations.FavoriteDB;
import com.furkankilic_usgchallange.Objects.Product;
import com.furkankilic_usgchallange.R;
import com.furkankilic_usgchallange.ThirdActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapterProduct extends RecyclerView.Adapter<MyRecyclerViewAdapterProduct.MyRecyclerViewItemHolder> {
    private Context context;
    private ArrayList<Product> recyclerItemValues;
    Intent intent = null;
    ArrayList<Product> checkExist;
    ArrayList<Product> allFavProducts;
    DatabaseHelper dbHelper;
    private ProgressDialog mDialog;

    public MyRecyclerViewAdapterProduct(Context context, ArrayList<Product> values){
        this.context = context;
        this.recyclerItemValues = values;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyRecyclerViewItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflator = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflator.inflate(R.layout.recycler_product, viewGroup, false);

        MyRecyclerViewItemHolder mViewHolder = new MyRecyclerViewItemHolder(itemView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewItemHolder myRecyclerViewItemHolder, int i) {
        final Product productItem = recyclerItemValues.get(i);

        myRecyclerViewItemHolder.tvProductName.setText(productItem.getStrMeal());

        String imagenameWithaddress= productItem.getStrMealThumb();
        Picasso.with(context)
                .load(imagenameWithaddress)
                .into(myRecyclerViewItemHolder.imgProduct);

        checkExist = new ArrayList<>();
        checkExist = FavoriteDB.findProduct(dbHelper, productItem.getIdMeal());
        if (!checkExist.isEmpty() ){
            myRecyclerViewItemHolder.imgFavIcon.setImageResource(R.drawable.fav);
        } else {
            myRecyclerViewItemHolder.imgFavIcon.setImageResource(R.drawable.unfav);
        }

        myRecyclerViewItemHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, ThirdActivity.class);
                intent.putExtra("idMeal", productItem.getIdMeal());
                context.startActivity(intent);
            }
        });

        myRecyclerViewItemHolder.imgFavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExist = new ArrayList<>();
                checkExist = FavoriteDB.findProduct(dbHelper, productItem.getIdMeal());
                if (!checkExist.isEmpty() ){
                    FavoriteDB.delete(dbHelper, productItem.getIdMeal());
                    myRecyclerViewItemHolder.imgFavIcon.setImageResource(R.drawable.unfav);
                    Toast.makeText(context, "Deleted from Favorite List", Toast.LENGTH_LONG).show();
                } else {
                    FavoriteDB.insertProduct(dbHelper, productItem);
                    myRecyclerViewItemHolder.imgFavIcon.setImageResource(R.drawable.fav);
                    Toast.makeText(context, "Added to Favorite List", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerItemValues.size();
    }

     class MyRecyclerViewItemHolder extends  RecyclerView.ViewHolder{
        TextView tvProductName;
        ImageView imgProduct, imgFavIcon;
        LinearLayout parentLayout;
        public MyRecyclerViewItemHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgFavIcon = itemView.findViewById(R.id.imgFavIcon);
            parentLayout = itemView.findViewById(R.id.linearLayoutProduct);
        }
    }

}
