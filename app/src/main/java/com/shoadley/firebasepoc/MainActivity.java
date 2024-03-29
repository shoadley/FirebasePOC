package com.shoadley.firebasepoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String QUOTE_KEY = "quote";
    public static final String AUTHOR_KEY = "author";
    public static final String INSPIRING_QUOTE = "InspiringQuote";

    TextView mQuoteTextView;

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuoteTextView = (TextView) findViewById(R.id.textViewQuote);
    }

    public void fetchQuote (View view) {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String quoteText = documentSnapshot.getString(QUOTE_KEY);
                    String authorText = documentSnapshot.getString(AUTHOR_KEY);
                    mQuoteTextView.setText("\"" + quoteText + "\" -- " + authorText);
                }
            }
        });
    }

    public void saveQuote(View view) {
        EditText quoteView = (EditText) findViewById(R.id.editTextQuote);
        EditText authorView = (EditText) findViewById(R.id.editTextAuthor);

        String quoteText = quoteView.getText().toString();
        String authorText = authorView.getText().toString();

        if (quoteText.isEmpty() || authorText.isEmpty()) {return;}
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(QUOTE_KEY,quoteText);
        dataToSave.put(AUTHOR_KEY,authorText);
        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(INSPIRING_QUOTE,"Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(INSPIRING_QUOTE, "Document was not saved!");
            }
        });
    }
}
