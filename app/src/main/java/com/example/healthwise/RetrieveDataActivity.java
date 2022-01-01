package com.example.healthwise;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class RetrieveDataActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    ListView categoryListView;
    List<String> categoryList;

    DatabaseReference categoriesDBRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_stories);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();

        //consider putting these things in the on start method?
        categoryListView = findViewById(R.id.categoryListView);

        categoryList = new ArrayList<String>();

        categoriesDBRef = FirebaseDatabase.getInstance().getReference("Categories");

        categoriesDBRef.orderByChild("categoryName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for(DataSnapshot storyDatasnap : dataSnapshot.getChildren()){
                    String stories = storyDatasnap.child("categoryName").getValue(String.class);
                    categoryList.add(stories);
                }
                ListAdapter adapter = new ListAdapter(RetrieveDataActivity.this, categoryList);
                categoryListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //open stories that have the category name clicked
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = categoryList.get(position);
                Intent intent = new Intent(RetrieveDataActivity.this, StoryContent.class);
                intent.putExtra("category_name", categoryName.toString());
                startActivity( intent);
            }
        });

        ImageButton fab = findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog( );
            }
        });
    }

    private void showUpdateDialog( ){
        AlertDialog mDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.add_category_dialog, null);
        mDialog.setView(mDialogView);

        //create views references
        EditText etUpdateCategory = mDialogView.findViewById(R.id.etUpdateCategory);
        Button btnUpdate = mDialogView.findViewById(R.id.btnAdd);
        ImageButton dialogButton = mDialogView.findViewById(R.id.btnCancelCategoryDialog);

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
                String newCategory = etUpdateCategory.getText().toString().trim();

                if (!TextUtils.isEmpty(newCategory)) {
                    //save category
                    DatabaseReference DbRefC = FirebaseDatabase.getInstance().getReference("Categories");
                    String categoryID = DbRefC.push().getKey();

                    //check if category exists before saving
                    DbRefC.orderByChild("categoryName").equalTo(newCategory)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        //category name exists, hence inform user with a toast message
                                        Toast.makeText(RetrieveDataActivity.this, "category already exists, input another one", Toast.LENGTH_LONG).show();
                                    } else {
                                        //category name does not exist, save it

                                        Categories category = new Categories(categoryID, newCategory);
                                        DbRefC.child(categoryID).setValue(category);

                                        //display a success toast
                                        Toast.makeText(RetrieveDataActivity.this, "Category Added Successfully", Toast.LENGTH_LONG).show();

                                        //kill dialog
                                        mDialog.dismiss();                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                }else {
                    //if the value is not given displaying a toast
                    Toast.makeText(RetrieveDataActivity.this, "Please enter the category you wish to add", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stories_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // User chose the "Settings" item, show the app settings UI...
                //setContentView(R.layout.categories_listed);
                startActivity(new Intent(RetrieveDataActivity.this, MainActivity.class));
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //setContentView(R.layout.activity_login);
                startActivity(new Intent(RetrieveDataActivity.this, LoginActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
