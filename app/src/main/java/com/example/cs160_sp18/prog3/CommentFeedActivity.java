package com.example.cs160_sp18.prog3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

// Displays a list of comments for a particular landmark.
public class CommentFeedActivity extends AppCompatActivity {

    private static final String TAG = CommentFeedActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Comment> mComments = new ArrayList<Comment>();
    private String username;

    // UI elements
    EditText commentInputBox;
    RelativeLayout layout;
    Button sendButton;
    Toolbar mToolbar;

    /* TODO: right now mRecyclerView is using hard coded comments.
     * You'll need to add functionality for pulling and posting comments from Firebase
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_feed);

        Bear bear = getIntent().getExtras().getParcelable("bear");
        username = getIntent().getExtras().getString("username");
        // TODO: replace this with the name of the landmark the user chose
        String landmarkName = bear.bearName;

        // hook up UI elements
        layout = (RelativeLayout) findViewById(R.id.comment_layout);
        commentInputBox = (EditText) layout.findViewById(R.id.comment_input_edit_text);
        sendButton = (Button) layout.findViewById(R.id.send_button);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(landmarkName + ": Posts");
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an onclick for the send button
        setOnClickForSendButton(landmarkName);

        // Generate comments from database
        fetchComments(landmarkName);

        // use the comments in mComments to create an adapter. This will populate mRecyclerView
        // with a custom cell (with comment_cell_layout) for each comment in mComments
        setAdapterAndUpdateData();
    }

    private void fetchComments(final String landmarkName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference commentRef = database.getReference("Comments");  // ~/Comments
        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child != null && child.getKey().equals(landmarkName)) {
                        Map<String, Object> comments = (Map<String, Object>) child.getValue();
                        for (Map.Entry<String, Object> entry : comments.entrySet()) {
                            //Get comment map
                            Map singleComment = (Map) entry.getValue();
                            String tempText = (String) singleComment.get("text");
                            String tempUsername = (String) singleComment.get("username");
                            long tempDate = (long) singleComment.get("date");
                            Comment tempComment = new Comment(tempText, tempUsername, tempDate);
                            mComments.add(tempComment);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setOnClickForSendButton(final String landmarkName) {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInputBox.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    // don't do anything if nothing was added
                    commentInputBox.requestFocus();
                } else {
                    // clear edit text, post comment
                    commentInputBox.setText("");
                    postNewComment(comment, landmarkName);
                }
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new CommentAdapter(this, mComments);
        mRecyclerView.setAdapter(mAdapter);

        // scroll to the last comment
        if (mComments.size() > 0) {
            mRecyclerView.smoothScrollToPosition(mComments.size() - 1);
        }
    }

    private void postNewComment(String commentText, String landmarkName) {
        Comment newComment = new Comment(commentText, this.username, System.currentTimeMillis());
        mComments.add(newComment);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentRef = database.getReference("Comments");  // ~/Comments
        DatabaseReference landmarkRef = commentRef.child(landmarkName);      // ~/Comments/landmark
        landmarkRef.push().setValue(newComment);
//        landmarkRef.child("text").push().setValue(newComment.text);          // ~/Comments/landmark/username
//        landmarkRef.child("username").push().setValue(newComment.username);  // ~/Comments/landmark/text
//        landmarkRef.child("date").push().setValue(newComment.date);          // ~/Comments/landmark/date

        setAdapterAndUpdateData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
