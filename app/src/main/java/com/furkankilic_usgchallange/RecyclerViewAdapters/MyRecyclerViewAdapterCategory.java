package com.furkankilic_usgchallange.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkankilic_usgchallange.Objects.Category;
import com.furkankilic_usgchallange.R;
import com.furkankilic_usgchallange.SecondActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapterCategory extends RecyclerView.Adapter<MyRecyclerViewAdapterCategory.MyRecyclerViewItemHolder> {
    private Context context;
    private ArrayList<Category> recyclerItemValues;
    Intent intent = null;

    public MyRecyclerViewAdapterCategory(Context context, ArrayList<Category> values){
        this.context = context;
        this.recyclerItemValues = values;
    }

    @NonNull
    @Override
    public MyRecyclerViewItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflator = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflator.inflate(R.layout.recycler_category, viewGroup, false);

        MyRecyclerViewItemHolder mViewHolder = new MyRecyclerViewItemHolder(itemView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewItemHolder myRecyclerViewItemHolder, int i) {

        final Category categoryItem = recyclerItemValues.get(i);
        myRecyclerViewItemHolder.tvCategoryName.setText(categoryItem.getStrCategory());
        String imagenameWithaddress= categoryItem.getStrCategoryThumb();
        Picasso.with(context)
                .load(imagenameWithaddress)
                .into(myRecyclerViewItemHolder.imgCategory);

        myRecyclerViewItemHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, SecondActivity.class);
                intent.putExtra("strCategory", categoryItem.getStrCategory());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recyclerItemValues.size();
    }

     class MyRecyclerViewItemHolder extends  RecyclerView.ViewHolder{
        TextView tvCategoryName;
        ImageView imgCategory;
        LinearLayout parentLayout;
        public MyRecyclerViewItemHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            parentLayout = itemView.findViewById(R.id.linearLayoutProduct);
        }
    }

}
