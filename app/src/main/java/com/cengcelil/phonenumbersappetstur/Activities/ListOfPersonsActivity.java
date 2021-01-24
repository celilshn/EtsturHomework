package com.cengcelil.phonenumbersappetstur.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cengcelil.phonenumbersappetstur.Adapters.PersonAdapter;
import com.cengcelil.phonenumbersappetstur.Helper.DatabaseHelper;

import com.cengcelil.phonenumbersappetstur.Helper.RecyclerClickListener;
import com.cengcelil.phonenumbersappetstur.Models.PersonModel;
import com.cengcelil.phonenumbersappetstur.R;
import com.cengcelil.phonenumbersappetstur.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ListOfPersonsActivity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "ListOfPersonsActivity";

    private RecyclerView personRecyclerView;
    private ArrayList<PersonModel> personModels;
    private PersonAdapter personAdapter;
    private RelativeLayout add_person_button;
    private DatabaseHelper databaseHelper;
    private EditText
            et_search;
    private ProgressBar progressBar;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_persons);
        bindViews();
        initComponents();
        setupRecyclerViewWithAdapter();
        add_person_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showProgressBar(progressBar);
                startActivity(new Intent(ListOfPersonsActivity.this, AddOrEditPersonActivity.class));
            }
        });
    }


    private void setupRecyclerViewWithAdapter() {
        String filtered = et_search.getText().toString();
        personModels = databaseHelper.getPersonsOrderedWithFilter(filtered);
        personAdapter = new PersonAdapter(personModels,progressBar,listener);
        personRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personRecyclerView.setHasFixedSize(true);
        personRecyclerView.setAdapter(personAdapter);
    }

    private void initComponents() {
        personModels = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        et_search.addTextChangedListener(this);
        listener = new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                int id = personAdapter.getPersonModels().get(position).getId();
                long result = databaseHelper.deletePersonWithId(id);
                if (result ==1)
                {
                    personAdapter.getPersonModels().remove(position);
                    personAdapter.notifyItemRemoved(position);
                    personAdapter.notifyItemRangeChanged(position, personAdapter.getItemCount() - position);


                }
                else
                    Toast.makeText(ListOfPersonsActivity.this, getString(R.string.error_delete_message), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void bindViews() {
        personRecyclerView = findViewById(R.id.layout_bottom_person_items_recyclerview);
        add_person_button = findViewById(R.id.layout_top_add_button);
        et_search = findViewById(R.id.search_edittext);

        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Utils.showProgressBar(progressBar);
        String filtered = charSequence.toString();

        personAdapter.setPersonModels(databaseHelper.getPersonsOrderedWithFilter(filtered));
        personAdapter.notifyDataSetChanged();
        Utils.hideProgressBar(progressBar);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        personRecyclerView.setItemViewCacheSize(0);
        et_search.setText(et_search.getText());
    }
}