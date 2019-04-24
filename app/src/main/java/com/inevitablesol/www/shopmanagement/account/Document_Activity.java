package com.inevitablesol.www.shopmanagement.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.inevitablesol.www.shopmanagement.R;

public class Document_Activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_);

        ImageView addDocument=(ImageView)findViewById(R.id.addDcocument);

        ImageView editDocument=(ImageView)findViewById(R.id.editDocument);
        editDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Document_Activity.this,Edit_Document.class));

            }
        });
        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Document_Activity.this,Add_Document.class));
            }
        });

        ImageView View =(ImageView)findViewById(R.id.viewDoc);
        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Document_Activity.this,View_Documment.class));

            }
        });


    }
}
