package com.example.practicaltouch.ui.main;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.R;
import com.example.practicaltouch.database.AppSet;
import com.example.practicaltouch.database.AppSetViewModel;

import java.util.List;
import java.util.Objects;

public class FirstFragment extends Fragment {
    private AppSetViewModel appSetViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createdsets_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        LayoutInflater layoutInflater = getLayoutInflater();

        final AppSetAdapter appSetAdapter =
                new AppSetAdapter(packageManager, layoutInflater, getActivity());
        recyclerView.setAdapter(appSetAdapter);

        appSetViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getActivity().getApplication()).create(AppSetViewModel.class);
        appSetViewModel.getAllAppSets().observe(getViewLifecycleOwner(), new Observer<List<AppSet>>() {
            @Override
            public void onChanged(List<AppSet> appSets) {
                appSetAdapter.submitList(appSets);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                appSetViewModel.delete(appSetAdapter.getAppSetAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "AppSet deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
