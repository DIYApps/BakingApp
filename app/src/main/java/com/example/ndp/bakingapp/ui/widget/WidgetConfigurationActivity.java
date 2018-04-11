package com.example.ndp.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.HashSet;

public class WidgetConfigurationActivity extends AppCompatActivity {

    private static final String LOG_TAG = "_BAK_COnfigActivity";
    private RadioGroup radioGroup;
    int appWidgetId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the result to Cancel
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_configuration);
        Log.d(LOG_TAG, "onCreate():::");
        radioGroup = findViewById(R.id.recipeNameRadioGroup);
        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
        HashSet<String> recipeStrings = preferenceHelper.getRecipeNamesFromSharedPreference();

        //finish the activity if there are no recipe stored
        // this will stop the widget from creating.
        if(recipeStrings ==  null || recipeStrings.isEmpty()){

            //prompt the user
            Toast.makeText(this, R.string.widget_config_failed_toast_message
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        //set the radioButtons for every recipe.
        for (String recipeString : recipeStrings) {
            Log.d(LOG_TAG, recipeString);
            RadioButton radioButton = (RadioButton) layoutInflater
                    .inflate(R.layout.recipe_name_widget_layout,
                    null, false);
            String[] strings = recipeString.split(",");
            radioButton.setText(strings[1]);
            radioButton.setId(Integer.valueOf(strings[0]));
            radioGroup.addView(radioButton);
        }

        //get the widget id and validate it
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            //Abort the widget configuration if the not a valid widget id is found.
            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(LOG_TAG, "setOnCheckedChangeListener()::" + i +"---"+ appWidgetId);
                String recipeName = ((RadioButton)findViewById(i)).getText().toString();

                //update the SharedPreference for this app id to store the recipe string
                // This value will be used for updating the widget.
                String recipeString = i +","+recipeName;
                Log.d(LOG_TAG, "setOnCheckedChangeListener()::"+recipeName);

                PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
                preferenceHelper.setAppWidgetRecipePreferences(appWidgetId ,
                        recipeString);
                WidgetUpdateService.startDisplayIngredientsService(

                        WidgetConfigurationActivity.this, appWidgetId ,recipeName ,
                        String.valueOf(i)  );
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }
}
