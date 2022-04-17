package com.app.tripfinity.view;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.zip.Inflater;

public class TripFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        Toast.makeText(getActivity(), "User- -- -- " + user, Toast.LENGTH_SHORT).show();

        db = FirebaseFirestore.getInstance();


        
        return  view;
    }
}
