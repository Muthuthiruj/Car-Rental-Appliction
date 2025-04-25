package com.example.carrental.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carrental.R;
import java.util.List;

public class CarLogoAdapter extends RecyclerView.Adapter<CarLogoAdapter.ViewHolder> {

    private List<Integer> carLogoList;

    public CarLogoAdapter(List<Integer> carLogoList) {
        this.carLogoList = carLogoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_logo_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.carImage.setImageResource(carLogoList.get(position));
    }

    @Override
    public int getItemCount() {
        return carLogoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
        }
    }
}
