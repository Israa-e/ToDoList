package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements OnDialogCloseListener {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private FirebaseFirestore firebaseFirestore;
    private ToDoAdapter toDoAdapter;
    Button mBtnSave;
    EditText task;
    private List<ToDoModel> toDoModels;
    private Query query;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        task = findViewById(R.id.task_ed);
        mBtnSave = findViewById(R.id.save);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        toDoModels = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(this, toDoModels);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(toDoAdapter);
        ShowData();

    }

    private void ShowData() {
        query = firebaseFirestore.collection("task").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        ToDoModel Model = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                        toDoModels.add(Model);
                        toDoAdapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            }
        });
    }

    @Override
    public void onDialogCloseListener(DialogInterface dialogInterface) {
        toDoModels.clear();
        ShowData();
        toDoAdapter.notifyDataSetChanged();
    }


}