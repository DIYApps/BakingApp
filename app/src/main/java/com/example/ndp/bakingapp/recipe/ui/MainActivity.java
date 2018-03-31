package com.example.ndp.bakingapp.recipe.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.recipe.presenter.MainPresenter;
import com.example.ndp.bakingapp.recipe.view.MainView;

import java.util.ArrayList;

/**JAI MAHAKAAL
 * HAR HAR MAHADEV*/
public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity::";
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
