package com.example.todolist;


import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";
    private TextView setDueDate;
    private EditText mTaskEdit;
    private Context context;
    private ImageButton speechBtn;
    private String dueDate = "";
    private String dueDateUpdate = "";
    private String id = "";
    private int status;
    private Button mBtnSave;
    private FirebaseFirestore firebaseFirestore;
    private static final int RECOGNIZER_CODE = 10;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task, container, false);
    }

    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_tv);
        mTaskEdit = view.findViewById(R.id.task_ed);
        mBtnSave = view.findViewById(R.id.save);
        speechBtn = view.findViewById(R.id.speech_btn);

        mBtnSave.setEnabled(false);
        mBtnSave.setBackgroundColor(Color.GRAY);

        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                try {
//                    if(intent.resolveActivity(context.getPackageManager())!=null){
                    startActivityForResult(intent, RECOGNIZER_CODE);
//                    }else {
//                        Toast.makeText(context, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();

//                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Error Of Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("uid");
            dueDateUpdate = bundle.getString("due");
            status = bundle.getInt("status");
            mTaskEdit.setText(task);
            setDueDate.setText(dueDateUpdate);
        }

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    mBtnSave.setEnabled(false);
                    mBtnSave.setBackgroundColor(Color.GRAY);
                } else {
                    mBtnSave.setEnabled(true);
                    mBtnSave.setBackgroundColor(getResources().getColor(R.color.green_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        setDueDate.setText(i2 + "/" + i1 + "/" + i);
                        dueDate = i2 + "/" + i1 + "/" + i;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        boolean finalIsUpdate = isUpdate;

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String task = mTaskEdit.getText().toString();
                String due = setDueDate.getText().toString();
                if (finalIsUpdate) {

                    mBtnSave.setEnabled(true);
                    mBtnSave.setText("UPDATE");
                    mBtnSave.setBackgroundColor(getResources().getColor(R.color.green_blue));
//                    Map<String, Object> taskMap = new HashMap<>();
//                    taskMap.put("task", task);
//                    taskMap.put("due", dueDateUpdate);
//                    taskMap.put("status", status);
//                    taskMap.put("time", FieldValue.serverTimestamp());
                    firebaseFirestore.collection("task").document(id).update("task", task, "due", due);
                    Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show();

                } else if (task.isEmpty()) {
                    Toast.makeText(context, "Empty Task Not Allowed ?", Toast.LENGTH_SHORT).show();

                } else {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("task", task);
                    taskMap.put("due", dueDate);
                    taskMap.put("status", 0);
                    taskMap.put("time", FieldValue.serverTimestamp());
                    firebaseFirestore.collection("task").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                dismiss();
            }
        });

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Toast.makeText(context, "Meo"+data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0), Toast.LENGTH_SHORT).show();
//        if (requestCode == RECOGNIZER_CODE && requestCode == RESULT_OK) {
//            ArrayList<String> taskText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            String raw =taskText.get(0).toString();
//            String[] arr= raw.split(" ");
//            mTaskEdit.setText(raw);
////            Toast.makeText(context, taskText.get(0).toString(), Toast.LENGTH_SHORT).show();
//        }
//        switch (requestCode){
//            case 1000:
//
//                if(requestCode ==RESULT_OK && data!=null){
//                   ArrayList<String> result=  ;
        mTaskEdit.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
//                }
//                break;
//        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogCloseListener(dialog);
        }
    }
}
