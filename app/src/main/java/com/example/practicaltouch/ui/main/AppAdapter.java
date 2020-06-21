package com.example.practicaltouch.ui.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.databinding.AppDrawerItemBinding;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<AppDrawerItem> appDrawerItemsList;
    private LayoutInflater layoutInflater;
    private OnAppListener mOnAppListener;

    AppAdapter(Activity context, List<AppDrawerItem> appDrawerItemList, OnAppListener mOnAppListener) {
        this.appDrawerItemsList = appDrawerItemList;
        this.layoutInflater = context.getLayoutInflater();
        this.mOnAppListener = mOnAppListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppDrawerItemBinding binding;
        private OnAppListener onAppListener;

        public ViewHolder(@NonNull AppDrawerItemBinding binding, OnAppListener onAppListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onAppListener = onAppListener;
            itemView.setOnClickListener(this);
        }
        public void bind(AppDrawerItem appDrawerItem) {
            binding.setAppDrawerItem(appDrawerItem);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            onAppListener.onAppClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppDrawerItemBinding binding = AppDrawerItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding, mOnAppListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppDrawerItem appDrawerItem = appDrawerItemsList.get(position);
        holder.bind(appDrawerItem);
    }

    @Override
    public int getItemCount() {
        return appDrawerItemsList.size();
    }

    public interface OnAppListener {
        void onAppClick(int position);
    }

}
