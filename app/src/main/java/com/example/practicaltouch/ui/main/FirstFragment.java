package com.example.practicaltouch.ui.main;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.MainActivity;
import com.example.practicaltouch.database.AppSetViewModel;
import com.example.practicaltouch.databinding.FragmentCreatedsetsTabBinding;

import java.util.Objects;

public class FirstFragment extends Fragment {

    private FragmentCreatedsetsTabBinding binding;
    private AppSetViewModel appSetViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatedsetsTabBinding.inflate(inflater, container, false);
        appSetViewModel = ((MainActivity) Objects.requireNonNull(getActivity())).getAppSetViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        LayoutInflater layoutInflater = getLayoutInflater();

        final AppSetAdapter appSetAdapter =
                new AppSetAdapter(packageManager, layoutInflater, getActivity());
        recyclerView.setAdapter(appSetAdapter);

        appSetViewModel.getAllAppSets().observe(getViewLifecycleOwner(), appSetAdapter::submitList);

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

        appSetViewModel.getScrollBool().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Objects.requireNonNull(binding.recyclerView.getLayoutManager()).smoothScrollToPosition(binding.recyclerView, null, 0);
                appSetViewModel.setScrollUpFalse();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
