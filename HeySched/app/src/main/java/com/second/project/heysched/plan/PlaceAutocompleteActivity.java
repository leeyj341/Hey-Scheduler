package com.second.project.heysched.plan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.second.project.heysched.R;

public class PlaceAutocompleteActivity extends AppCompatActivity {

    private TextView txtSelectedPlaceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        txtSelectedPlaceName = (TextView) this.findViewById(R.id.txtSelectedPlaceName);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.fragment_autocomplete1);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                Log.i("auto", "Place: " + place.getName());
                txtSelectedPlaceName.setText(String.format("Selected places : %s  - %s" , place.getName(), place.getAddress()));
            }

            @Override
            public void onError(Status status) {
                Log.i("auto", "An error occurred: " + status);
                Toast.makeText(PlaceAutocompleteActivity.this, "Place cannot be selected!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
