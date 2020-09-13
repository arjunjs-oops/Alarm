package com.arjun.alaram.RV;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arjun.alaram.POJO.Data;
import com.arjun.alaram.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
   private List<Data> arrayList = new ArrayList<>();
   onClickIO clickIO;

    public Adapter(onClickIO fromActivity) {
        this.clickIO = fromActivity;
    }


    public void setDynamically(List<Data> dataList) {
            arrayList.clear();
            arrayList.addAll(dataList);
            notifyDataSetChanged();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new ViewHolder(view,clickIO) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.selectedTime.setText(arrayList.get(position).getCalender());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView selectedTime,title;
        SwitchMaterial switchMaterial;
        ImageView imageView;
        onClickIO onClickIO;
        public ViewHolder(@NonNull View itemView ,onClickIO io ) {
            super(itemView);
            selectedTime =  itemView.findViewById(R.id.selectedTime);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.open);
            this.onClickIO = clickIO;
            switchMaterial = itemView.findViewById(R.id.set);
            switchMaterial.setChecked(true);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickIO.setImageOnClick(switchMaterial,getAdapterPosition()); }
    }

  public  interface onClickIO{
        void setImageOnClick(SwitchMaterial data,int position);
    }
}
