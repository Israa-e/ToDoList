package com.example.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.HomePageActivity;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> toDoModelList;
    private HomePageActivity activity;
    private FirebaseFirestore firebaseFirestore;

    public ToDoAdapter(HomePageActivity homePageActivity, List<ToDoModel> toDoModelList) {
        this.toDoModelList = toDoModelList;
        activity = homePageActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }

    public void deleteTask(int position) {
        ToDoModel toDoModel = toDoModelList.get(position);
        firebaseFirestore.collection("task").document(toDoModel.TaskId).delete();
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position) {
        ToDoModel toDoModel = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due", toDoModel.getDue());
        bundle.putString("uid", toDoModel.TaskId);
        bundle.putInt("status", toDoModel.getStatus());
        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
        notifyItemChanged(position);
    }

    public Context getContext() {
        return activity;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = toDoModelList.get(position);
        holder.text_task.setText(toDoModel.getTask());
        holder.dueDate.setText("Due On" + toDoModel.getDue());
        holder.checkBox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked() ) {
                    firebaseFirestore.collection("task").document(toDoModel.TaskId).update("status", 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(activity, "Task Completed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(activity, "Task Uncompleted", Toast.LENGTH_SHORT).show();
                    firebaseFirestore.collection("task").document(toDoModel.TaskId).update("status", 0);
                }
            }
        });


    }


    private boolean toBoolean(int Status) {
        return Status != 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dueDate;
        TextView text_task;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View v) {
            super(v);
            dueDate = v.findViewById(R.id.due_date_tv);
            text_task= v.findViewById(R.id.text_task);
            checkBox = v.findViewById(R.id.mCheckBox);
        }
    }


}
