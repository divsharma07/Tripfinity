package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.app.tripfinity.R;
import com.app.tripfinity.model.User;
import com.app.tripfinity.model.UserBio;
import com.app.tripfinity.viewmodel.InviteViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class InviteActivity extends AppCompatActivity {

    private InviteViewModel inviteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        initInviteViewModel();
        Button nextButton = findViewById(R.id.invite_next_button);
        nextButton.setOnClickListener(this::onNextClicked);
        Bundle bundle;
        if(getIntent().getSerializableExtra("users") != null){
            bundle = new Bundle();
            bundle.putSerializable("users",getIntent().getSerializableExtra("users"));
        }
        else {
            bundle = null;
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.invite_fragment_container_view, InviteFragment.class, bundle)
                    .commit();
        }

        // Objects.requireNonNull(getSupportActionBar()).setTitle("Invite Friends to Trip");

    }

    private void onNextClicked(View view){
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        ArrayList<UserBio> users = new ArrayList<>();
        for (User user: Objects.requireNonNull(inviteViewModel.getUsers().getValue())
             ) {
            users.add(new UserBio(user.getUid(), user.getName(), user.getEmail()));
        }
        bundle.putSerializable("users",  users);
        data.putExtra("users", bundle);
        setResult(RESULT_OK, data);
        finish();
    }

    private void initInviteViewModel() {
        inviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);
    }


}