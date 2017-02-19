package codepathandroid.todotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by s0195620 on 2/18/17.
 */

public class EditItemActivity extends Activity {

    private EditText updateItem;
    private Button saveUpdate;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        String currentItem = getIntent().getStringExtra("ItemToBeEdited");
        final int position = getIntent().getIntExtra("position", 0);
        updateItem = (EditText) findViewById(R.id.edit_item);
        updateItem.setText(currentItem);
        saveUpdate = (Button) findViewById(R.id.updateItem);

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("editedItem", updateItem.getText().toString());
                data.putExtra("position", position);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
}