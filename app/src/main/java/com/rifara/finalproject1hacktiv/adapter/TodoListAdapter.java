package com.rifara.finalproject1hacktiv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rifara.finalproject1hacktiv.R;
import com.rifara.finalproject1hacktiv.entity.TodoList;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {
    private final OnItemClickCallback onItemClickCallback;

    public TodoListAdapter(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    private final ArrayList<TodoList> listTodoList = new ArrayList<>();

    public ArrayList<TodoList> getListNotes() {
        return listTodoList;
    }

    public void setListNotes(ArrayList<TodoList> listTodoList) {
        if (listTodoList.size() > 0) {
            this.listTodoList.clear();
        }
        this.listTodoList.addAll(listTodoList);
    }


    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_list, parent, false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoListViewHolder holder, int position) {
        holder.tvTitle.setText(listTodoList.get(position).getTitle());
        holder.tvDate.setText(listTodoList.get(position).getDate());
        holder.tvDescription.setText(listTodoList.get(position).getDescription());
        holder.cvNote.setOnClickListener(v -> onItemClickCallback.onItemClicked(listTodoList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return listTodoList.size();
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription, tvDate;
        final CardView cvNote;

        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);

        }
    }


    public interface OnItemClickCallback {
        void onItemClicked(TodoList selectedNote, Integer position);
    }


    // metode untuk menambahkan, memperbarui dan menghapus Item di RecyclerView.
    public void addItem(TodoList todoList) {
        this.listTodoList.add(todoList);
        notifyItemInserted(listTodoList.size() - 1);
    }
    public void updateItem(int position, TodoList todoList) {
        this.listTodoList.set(position, todoList);
        notifyItemChanged(position, todoList);
    }
    public void removeItem(int position) {
        this.listTodoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listTodoList.size());
    }
}
