package com.rifara.finalproject1hacktiv;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rifara.finalproject1hacktiv.adapter.TodoListAdapter;
import com.rifara.finalproject1hacktiv.db.TodoListHelper;
import com.rifara.finalproject1hacktiv.entity.TodoList;
import com.rifara.finalproject1hacktiv.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback {

    private ProgressBar progressBar;
    private RecyclerView rvTodo;
    private TodoListAdapter adapter;

    private static final String EXTRA_STATE = "EXTRA_STATE";

    final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    // Akan dipanggil jika request codenya ADD
                    if (result.getResultCode() == NoteAddUpdateActivity.RESULT_ADD) {
                        TodoList note = result.getData().getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);

                        adapter.addItem(note);
                        rvTodo.smoothScrollToPosition(adapter.getItemCount() - 1);

                        showSnackbarMessage("Satu item berhasil ditambahkan");
                    } else if (result.getResultCode() == NoteAddUpdateActivity.RESULT_UPDATE) {

                        TodoList note = result.getData().getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                        int position = result.getData().getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                        adapter.updateItem(position, note);
                        rvTodo.smoothScrollToPosition(position);

                        showSnackbarMessage("Satu item berhasil diubah");
                    } else if (result.getResultCode() == NoteAddUpdateActivity.RESULT_DELETE) {
                        int position = result.getData().getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                        adapter.removeItem(position);

                        showSnackbarMessage("Satu item berhasil terselesaikan");
                    }
                }
            });


    // =====
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Todo List");
        }

        progressBar = findViewById(R.id.progressbar);
        rvTodo = findViewById(R.id.rv_notes);
        rvTodo.setLayoutManager(new LinearLayoutManager(this));
        rvTodo.setHasFixedSize(true);

        adapter = new TodoListAdapter((selectedNote, position) -> {
            Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
            intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, selectedNote);
            intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position);
            resultLauncher.launch(intent);
        });
        rvTodo.setAdapter(adapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
            resultLauncher.launch(intent);
        });


        // Proses ambil data
        if (savedInstanceState == null) {
            new LoadNotesAsync(this, this).execute();
        } else {
            ArrayList<TodoList> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListNotes(list);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListNotes());
    }

    // =====
    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<TodoList> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        if (notes.size() > 0) {
            adapter.setListNotes(notes);
        } else {
            adapter.setListNotes(new ArrayList<>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }


    private static class LoadNotesAsync {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;

        private LoadNotesAsync(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            weakCallback.get().preExecute();
            executor.execute(() -> {
                Context context = weakContext.get();

                TodoListHelper noteHelper = TodoListHelper.getInstance(context);
                noteHelper.open();
                Cursor dataCursor = noteHelper.queryAll();
                ArrayList<TodoList> notes = MappingHelper.mapCursorToArrayList(dataCursor);
                noteHelper.close();

                handler.post(() -> weakCallback.get().postExecute(notes));
            });
        }

    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvTodo, message, Snackbar.LENGTH_SHORT).show();
    }

}


interface LoadNotesCallback {
    void preExecute();

    void postExecute(ArrayList<TodoList> notes);
}
