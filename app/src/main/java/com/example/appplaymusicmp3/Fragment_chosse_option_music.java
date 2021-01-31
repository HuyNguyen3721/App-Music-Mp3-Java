package com.example.appplaymusicmp3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appplaymusicmp3.databinding.LayoutChosseOptionMusicBinding;

public class Fragment_chosse_option_music extends Fragment {
    private LayoutChosseOptionMusicBinding binding ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutChosseOptionMusicBinding.inflate(inflater,container,false);
        binding.btnOnline.setOnClickListener(view->{
                ((MainActivity)getActivity()).fragmentmaninOnline();
        });
        binding.btnOffline.setOnClickListener(view -> {
            ((MainActivity)getActivity()).fragmentMainOffline();
        });

        return binding.getRoot();
    }
}
