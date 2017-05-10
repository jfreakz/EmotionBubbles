package sg.com.comnet.emotionbubbles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTest extends AppCompatActivity {
    final int GRID = 0;
    final int LIST = 1;
    int type;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager gridLayoutManager, linearLayoutManager;
    MyAdapter adapter;

    Button btnChange;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_test);

        // Display contents in views
        final List<Person> list = new ArrayList<>();
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));
        list.add(new Person("Ariq Row 1"));
        list.add(new Person("Ariq Row 2"));
        list.add(new Person("Ariq Row 3"));

        // Finding views by id
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnChange = (Button) findViewById(R.id.btnChange);

        // Defining Linear Layout Manager
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        // Defining Linear Layout Manager (here, 3 column span count)
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);

        //Setting gird view as default view
        type = GRID;
        adapter = new MyAdapter(list, GRID);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        //Setting click listener
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == LIST) {
                    // Change to grid view
                    adapter = new MyAdapter(list, GRID);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);
                    type = GRID;
                } else {
                    // Change to list view
                    adapter = new MyAdapter(list, LIST);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    type = LIST;
                }
            }
        });

    }
}

//Defining Adapter
class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    final int GRID = 0;
    final int LIST = 1;
    List<Person> list;
    int type;

    MyAdapter(List<Person> list, int type) {
        this.list = list;
        this.type = type;
    }

    // Inflating views if the existing layout items are not being recycled
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == GRID) {
            // Inflate the grid cell as a view item
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cell, parent, false);
        } else {
            // Inflate the list row as a view item
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        }

        return new ViewHolder(itemView, viewType);
    }

    // Add data to your layout items
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = list.get(position);
        holder.textView.setText(person.name);
    }

    // Number of items
    @Override
    public int getItemCount() {
        return list.size();
    }

    // Using the variable "type" to check which layout is to be displayed
    @Override
    public int getItemViewType(int position) {
        if (type == GRID) {
            return GRID;
        } else {
            return LIST;
        }
    }

    // Defining ViewHolder inner class
    public class ViewHolder extends RecyclerView.ViewHolder {
        final int GRID = 0;
        final int LIST = 1;
        TextView textView;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type == GRID) {
                textView = (TextView) itemView.findViewById(R.id.textView2);
            } else {
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }
}

// Data Source Class
class Person {
    String name;

    Person(String name) {
        this.name = name;
    }
}