package com.sopan.checkbox.select.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sopan.checkbox.select.R;
import com.sopan.checkbox.select.adapter.StudentAdapter;
import com.sopan.checkbox.select.model.StudentModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class StudentActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.cbSelectAll)
    CheckBox cbSelectAll;

    @BindView(R.id.tvSelect)
    TextView tvSelect;

    StudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setNestedScrollingEnabled(false);
        mAdapter = new StudentAdapter(this, prepareData());
        //mAdapter.notifyDataSetChanged();
        recycler_view.setAdapter(mAdapter);

    }

    @OnCheckedChanged(R.id.cbSelectAll)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mAdapter != null) {
            mAdapter.toggleSelection(isChecked);
            tvSelect.setText(isChecked ? "Deselect All" : "Select All");
        }

    }

    public List<StudentModel> prepareData() {
        try {
            List<StudentModel> studentList = new ArrayList<>();
            InputStream stream = getAssets().open("Student.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String data = builder.toString();
            studentList = new Gson().fromJson(data, new TypeToken<List<StudentModel>>() {
            }.getType());
            return studentList;
        } catch (Exception e) {
            //java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $
            //dont keep root key...
            e.printStackTrace();
        }
        return null;
    }
}