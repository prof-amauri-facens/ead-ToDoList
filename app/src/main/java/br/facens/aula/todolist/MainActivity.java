package br.facens.aula.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.facens.aula.todolist.model.Task;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTaskTitle;
    private EditText editTextTaskDescription;
    private Spinner spinnerPriority;
    private Button buttonAddTask;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // Configurar o RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskTitle = editTextTaskTitle.getText().toString().trim();
                String taskDescription = editTextTaskDescription.getText().toString().trim();
                String taskPriority = spinnerPriority.getSelectedItem().toString();
                if (!taskTitle.isEmpty()) {
                    Task task = new Task();
                    task.setTitle(taskTitle);
                    task.setDescription(taskDescription);
                    task.setPriority(taskPriority);
                    tasks.add(task);
                    taskAdapter.notifyDataSetChanged();
                    editTextTaskTitle.setText("");
                    editTextTaskDescription.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Digite um título para a tarefa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewTitle;
            private TextView textViewDescription;
            private TextView textViewPriority;
            private CheckBox checkBoxCompleted;

            public TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
                textViewPriority = itemView.findViewById(R.id.textViewPriority);
                checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            }

            public void bind(Task task) {
                textViewTitle.setText("Título: " + task.getTitle());
                textViewDescription.setText("Descrição: " + task.getDescription());
                textViewPriority.setText("Prioridade: " + task.getPriority());
                checkBoxCompleted.setChecked(task.isCompleted());
            }
        }
    }
}
