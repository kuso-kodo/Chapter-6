package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

public class NoteActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private TodoDbHelper dbHelper;

    private EditText editText;
    private Button addBtn;
    private AppCompatRadioButton lowBtn;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        dbHelper = new TodoDbHelper(this);
        db = dbHelper.getWritableDatabase();

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        radioGroup = findViewById(R.id.radio_group);

        lowBtn = findViewById(R.id.btn_low);
        lowBtn.setChecked(true);

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim(), getPriority());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }

    private Priority getPriority() {
        int i = this.radioGroup.getCheckedRadioButtonId();
        return (i == R.id.btn_high) ? Priority.HIGH :
                ((i == R.id.btn_medium) ? Priority.MEDIUM : Priority.LOW);
    }

    private boolean saveNote2Database(String content, Priority priority) {
        if (db != null && !TextUtils.isEmpty(content)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TodoContract.TodoNote.COLUMN_CONTENT, content);
            contentValues.put(TodoContract.TodoNote.COLUMN_STATE, State.TODO.intValue);
            contentValues.put(TodoContract.TodoNote.COLUMN_DATE, System.currentTimeMillis());
            contentValues.put(TodoContract.TodoNote.COLUMN_PRIORITY, priority.intValue);
            return db.insert("note", null, contentValues) != -1L;
        }
        return false;
    }
}
