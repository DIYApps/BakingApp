package com.example.ndp.bakingapp.ui.recipelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.ndp.bakingapp.R;

/**JAI MAHAKAAL
 * HAR HAR MAHADEV*/
public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.recipe_menu, menu );
        return true;
    }
}
