package br.facens.aula.todolist;

import android.content.Intent;
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

    // Declaração dos elementos de interface do usuário
    private EditText editTextTaskTitle;
    private EditText editTextTaskDescription;
    private Spinner spinnerPriority;
    private Button buttonAddTask;
    private Button buttonSendTasks;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos elementos de interface do usuário
        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        buttonSendTasks = findViewById(R.id.buttonSendTasks);

        // Configuração do RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(tasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        // Configuração do Spinner para as prioridades das tarefas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        // Configuração do botão para adicionar uma nova tarefa
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenção dos dados da nova tarefa
                String taskTitle = editTextTaskTitle.getText().toString().trim();
                String taskDescription = editTextTaskDescription.getText().toString().trim();
                String taskPriority = spinnerPriority.getSelectedItem().toString();

                // Verificação se o título da tarefa não está vazio
                if (!taskTitle.isEmpty()) {
                    // Criação de uma nova instância da classe Task com os dados informados
                    Task task = new Task();
                    task.setTitle(taskTitle);
                    task.setDescription(taskDescription);
                    task.setPriority(taskPriority);

                    // Adição da nova tarefa à lista de tarefas e notificação do adaptador sobre a mudança
                    tasks.add(task);
                    taskAdapter.notifyDataSetChanged();

                    // Limpeza dos campos de entrada de dados
                    editTextTaskTitle.setText("");
                    editTextTaskDescription.setText("");
                } else {
                    // Exibição de um aviso se o título da tarefa estiver vazio
                    Toast.makeText(getApplicationContext(), "Digite um título para a tarefa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSendTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTasksByEmail();
            }
        });
    }

    private void sendTasksByEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        /*
        Outros tipos possiveis são, por exemplo:
        message/rfc822: Para enviar um email com formato MIME, que pode incluir anexos e formatação avançada.
        text/html: Para enviar um email com conteúdo HTML, permitindo formatação avançada e elementos interativos.
        image/jpeg, image/png, etc.: Para enviar imagens como anexos no email.
        application/pdf, application/msword, etc.: Para enviar arquivos PDF, documentos do Word, e
        outros tipos de arquivos como anexos.
         */
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lista de Tarefas");
        emailIntent.putExtra(Intent.EXTRA_TEXT, generateTasksText());
        startActivity(Intent.createChooser(emailIntent, "Enviar Lista de Tarefas"));
    }

    private String generateTasksText() {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append("Título: ").append(task.getTitle()).append("\n");
            sb.append("Descrição: ").append(task.getDescription()).append("\n");
            sb.append("Prioridade: ").append(task.getPriority()).append("\n");
            sb.append("Concluída: ").append(task.isCompleted() ? "Sim" : "Não").append("\n\n");
        }
        return sb.toString();
    }

    // Classe interna para o adaptador do RecyclerView
    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private List<Task> tasks;

        // Construtor que recebe a lista de tarefas
        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        // Método que cria novas visualizações (layout) para os itens da lista
        @NonNull
        @Override
        /*
         Este método é chamado quando o RecyclerView precisa criar uma nova visualização (item de lista)
         para exibir. Ele recebe dois parâmetros: parent, que é o ViewGroup no qual a nova visualização
         será inserida após a criação, e viewType, que é o tipo de visualização, caso o RecyclerView
         tenha vários tipos de itens de lista.
         */
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /*
            Aqui, estamos inflando o layout task_item.xml para criar a visualização do item da lista.
            O LayoutInflater é usado para inflar layouts XML em objetos de visualização reais. parent.getContext()
            retorna o contexto do ViewGroup pai, que é necessário para o LayoutInflater. O método inflate cria uma
            nova instância de View a partir do arquivo de layout XML task_item.xml. O terceiro argumento false
            indica que a visualização recém-criada não deve ser anexada ao ViewGroup pai automaticamente,
            pois o RecyclerView cuidará disso.
             */
            /*
            O processo de inflar um layout envolve a interpretação do arquivo XML para criar os objetos de visualização
            correspondentes, como TextView, Button, LinearLayout, entre outros, conforme definido no arquivo XML.
            Isso permite que você defina a estrutura e a aparência de suas interfaces de usuário de forma declarativa,
            sem precisar criar manualmente cada elemento de interface no código Java.
             */
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            return new TaskViewHolder(view);
        }

        // Método que atualiza o conteúdo das visualizações com base nos dados da tarefa
        @Override
        /*
        Este método é chamado pelo RecyclerView para exibir os dados de uma tarefa em um item da lista
        específico. Ele recebe dois parâmetros: holder, que é a instância do TaskViewHolder que contém
        os elementos de interface do item da lista, e position, que é a posição da tarefa na lista de tarefas.
         */
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            /*
            Aqui, estamos obtendo a tarefa na posição especificada da lista de tarefas. A lista de
            tarefas (tasks) é a fonte de dados que contém todas as tarefas a serem exibidas na lista.
             */
            Task task = tasks.get(position);
            /*
             Em seguida, estamos chamando o método bind do TaskViewHolder para associar os dados da tarefa
             aos elementos de interface do item da lista. O método bind é responsável por atualizar os elementos
             de interface (como TextView para título, descrição e prioridade, e CheckBox para concluído)
             com os dados da tarefa específica.
             */
            holder.bind(task);
        }

        // Método que retorna o número total de itens na lista de tarefas
        @Override
        public int getItemCount() {
            return tasks.size();
        }

        // Classe interna que representa cada item de tarefa na lista
        public class TaskViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewTitle;
            private TextView textViewDescription;
            private TextView textViewPriority;
            private CheckBox checkBoxCompleted;

            // Construtor que recebe a visualização do item de tarefa
            public TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                // Inicializa os elementos de interface do item de tarefa
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
                textViewPriority = itemView.findViewById(R.id.textViewPriority);
                checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            }

            // Método que associa os dados da tarefa aos elementos de interface do item de tarefa
            public void bind(Task task) {
                textViewTitle.setText("Título: " + task.getTitle());
                textViewDescription.setText("Descrição: " + task.getDescription());
                textViewPriority.setText("Prioridade: " + task.getPriority());
                checkBoxCompleted.setChecked(task.isCompleted());
            }
        }
    }
}
