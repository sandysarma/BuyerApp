package com.buyer.buyerApp.Adapter;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.Activity.ExploreProjectActivity;
import com.buyer.buyerApp.BuildConfig;
import com.buyer.buyerApp.ModelResult.ExploreProjectresult;
import com.buyer.buyerApp.R;
import com.buyer.buyerApp.RetrofitApi.UrlClass;

import java.util.List;

public class ExploreProjectAdapter extends RecyclerView.Adapter<ExploreProjectAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<ExploreProjectresult> projectresultList;

    public ExploreProjectAdapter(@NonNull Context context, List<ExploreProjectresult> projectresultList) {
        this.context = context;
        this.projectresultList = projectresultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_project_adapter,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.occasionTxt.setText(projectresultList.get(position).getCategoryName());

        String strImage=projectresultList.get(position).getImage();
        String image_url = BuildConfig.API_URL+ UrlClass.projectImageUrl + strImage;
        Glide.with(context).load(image_url)
                .thumbnail(0.5f)
                .into(holder.exploreImage);

holder.occasionLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ExploreProjectActivity.class);
        String projectName= projectresultList.get(position).getProjectName();
        String startDate= projectresultList.get(position).getWorkStartDate();
        String completeDate= projectresultList.get(position).getCompleteDate();
        String priceTxt= String.valueOf("$" +projectresultList.get(position).getPrice());
        String descriptionTxt=projectresultList.get(position).getShortDescription();
        String statustxt = String.valueOf(projectresultList.get(position).getStatus());
        String locationtxt = projectresultList.get(position).getLocation();
        String strImagetxt=projectresultList.get(position).getImage();
        intent.putExtra("exploreprojectName",projectName);
        intent.putExtra("startDate",startDate);
        intent.putExtra("completeDate",completeDate);
        intent.putExtra("priceTxt",priceTxt);
        intent.putExtra("descriptionTxt",descriptionTxt);
        intent.putExtra("status",statustxt);
        intent.putExtra("locationtxt",locationtxt);
        intent.putExtra("explore_image",strImagetxt);
        context.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {
        return projectresultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView exploreImage;
        TextView occasionTxt, projectNameTxt, exploreStatus, priceTxt, descriptionTxt, startDateTxt, locationtxt;
        LinearLayout occasionLayout;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            exploreImage = itemView.findViewById(R.id.exploreImage);
            occasionTxt = itemView.findViewById(R.id.occasionTxt);
            projectNameTxt = itemView.findViewById(R.id.projectNameTxt);
            exploreStatus = itemView.findViewById(R.id.exploreStatus);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            startDateTxt = itemView.findViewById(R.id.startDateTxt);
            locationtxt = itemView.findViewById(R.id.locationtxt);
            occasionLayout = itemView.findViewById(R.id.occasionLayout);
        }
    }
}
