package com.example.healthwise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoryContent extends AppCompatActivity {

    FirebaseAuth mAuth;

    ListView storiesListView;
    List<String> storiesList;

    DatabaseReference storiesDBRef, categoriesDBRef;
    ArrayList<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_content);

        //get the category name to pass on later
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("category_name");

        Toolbar myToolbar = findViewById(R.id.my_toolbar_stories);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();

        storiesListView = findViewById(R.id.storiesListView);
        storiesList = new ArrayList<String>();


        storiesDBRef = FirebaseDatabase.getInstance().getReference("Stories");

        //here we use order by and equal to to get all stories under a particular category
        storiesDBRef.orderByChild("storyCategory").equalTo(categoryName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    storiesList.clear();
                    for(DataSnapshot storyDatasnap : dataSnapshot.getChildren()){
                        String stories = storyDatasnap.child("storyContent").getValue(String.class);
                        storiesList.add(stories);
                    }
                    StoryContentListAdapter adapter = new StoryContentListAdapter(StoryContent.this, storiesList);
                    storiesListView.setAdapter(adapter);
                } else{
                    Toast.makeText(StoryContent.this, "No stories under this category", Toast.LENGTH_LONG).show();
                    //set text to an imaginary textview
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton fab = findViewById(R.id.fabAddStory);
        fab.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateDialog( );
                // return false;
            }
        });


    }

    private void showUpdateDialog( ){
        AlertDialog mDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.add_story_dialog, null);
        mDialog.setView(mDialogView);

        //create views references
        EditText etUpdateStory = mDialogView.findViewById(R.id.etUpdateStory);
        Spinner categorySpinner = mDialogView.findViewById(R.id.categorySpinner);
        Button btnUpdate = mDialogView.findViewById(R.id.btnAdd);
        ImageButton dialogButton = mDialogView.findViewById(R.id.btnCancelStoryDialog);

        categoriesDBRef = FirebaseDatabase.getInstance().getReference("Categories");

        categoriesDBRef.orderByChild("categoryName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();

                for(DataSnapshot storyDatasnap : dataSnapshot.getChildren()){

                    String categories = storyDatasnap.child("categoryName").getValue(String.class);
                    categoryList.add(categories);
                }

                //call the spinner method

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(StoryContent.this, android.R.layout.simple_spinner_item, categoryList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDialog.show();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we will update data in database
                String newStory = etUpdateStory.getText().toString().trim();
                String newCategory = categorySpinner.getSelectedItem().toString().trim();

                if (!TextUtils.isEmpty(newStory)) {
                    //save stories
                    DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Stories");
                    String storyId = DbRef.push().getKey();
                    Stories stories = new Stories(storyId, newCategory, newStory);
                    DbRef.child(storyId).setValue(stories);

                    //display a success toast
                    Toast.makeText(StoryContent.this, "Story Added Successfully", Toast.LENGTH_LONG).show();

                    //kill dialog
                    mDialog.dismiss();
                }else {
                    //if the value is not given displaying a toast
                    Toast.makeText(StoryContent.this, "Please enter  the story content", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.stories_content_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // User chose the "Settings" item, show the app settings UI...
                //setContentView(R.layout.categories_listed);
                startActivity(new Intent(StoryContent.this, MainActivity.class));
                return true;

            case R.id.action_show_stories:
                // User chose the "Settings" item, show the app settings UI...
                //setContentView(R.layout.categories_listed);
                startActivity(new Intent(StoryContent.this, RetrieveDataActivity.class));
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //setContentView(R.layout.activity_login);
                startActivity(new Intent(StoryContent.this, LoginActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}