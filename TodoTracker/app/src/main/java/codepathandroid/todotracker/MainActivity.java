package codepathandroid.todotracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity  extends Activity implements RecyclerViewAdapter.RecycleViewItemCLickListener {

    private RecyclerViewAdapter adapter;

    private Button btn_addItem;
    private EditText et_newItem;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(readFromFile(), this);
        recyclerView.setAdapter(adapter);
        btn_addItem = (Button) findViewById(R.id.btn_addItem);
        et_newItem = (EditText) findViewById(R.id.et_newItem);
        btn_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addItem(et_newItem.getText().toString());
                et_newItem.setText("");
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        writeToFile();
    }

    public void taskDeleted(View view) {
        adapter.taskDeleted(recyclerView.getChildAdapterPosition((View) view.getParent()));
    }

    private void writeToFile() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            for (String item : adapter.getAllItems()) {
                outputStream.write(item.getBytes());
                outputStream.write(getString(R.string.doneSeparator).getBytes());
                outputStream.write('\n');
            }
            outputStream.close();
        } catch (IOException e) {
            //TODO: handle
        }
    }


    private ArrayList<String> readFromFile() {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        try {
            FileReader reader = new FileReader(getFilePath());
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                int separator = line.lastIndexOf(getString(R.string.doneSeparator));
                if(separator > 0)
                lines.add(line.substring(0, separator));
            }
        } catch (IOException e) {
            //TODO: handle
        }
        return lines;
    }


    private String getFilePath() {
        return getBaseContext().getFilesDir() + "/" + getString(R.string.file_name);
    }

    @Override
    public void onItemClicked(View v) {
        TextView et_itemToBeEdited = (TextView) v.findViewById(R.id.item_text);
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        int position = recyclerView.getChildPosition((View) et_itemToBeEdited.getParent());
        intent.putExtra("ItemToBeEdited", et_itemToBeEdited.getText().toString());
        intent.putExtra("position", position);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onItemLongClicked(View v) {
        TextView et_itemToBeRemoved = (TextView) v.findViewById(R.id.item_text);
        taskDeleted(et_itemToBeRemoved);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            String editedItem = data.getExtras().getString("editedItem");
            int position = data.getExtras().getInt("position");
            adapter.updateItem(editedItem, position);
            adapter.notifyDataSetChanged();
        }
    }
}
