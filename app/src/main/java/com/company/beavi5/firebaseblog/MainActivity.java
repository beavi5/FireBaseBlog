package com.company.beavi5.firebaseblog;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(registerIntent);
                }
            }
        };

        setContentView(R.layout.activity_main);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
       mBlogList.setHasFixedSize(true);
      LinearLayoutManager mllm = new LinearLayoutManager(this);
        mllm.setStackFromEnd(true);
        mllm.setReverseLayout(true);

        mBlogList.setLayoutManager(mllm);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }
        if (item.getItemId() == R.id.action_logout) {
        logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(Blog.class,R.layout.post_row,BlogViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
            viewHolder.post_title.setText(model.getTitle());
            viewHolder.post_desc.setText(model.getDesc());
//            viewHolder.post_desc.setMaxEms(3);
            viewHolder.post_desc.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((TextView)v).getMaxLines()==1) ((TextView)v).setMaxLines(100);
                            else ((TextView)v).setMaxLines(1);
                        }
                    }
            );

                Log.d("log",model.image);
                Picasso.with(getBaseContext()).load(model.image)
                        .fit()
                        .centerInside().into(viewHolder.image);

//
//    if ( !model.image.isEmpty())
//            viewHolder.imageView.setImageURI(Uri.parse(
//                    model.image));

            }
        };
        Toast.makeText(getApplicationContext(), "aaaaaa", Toast.LENGTH_SHORT).show();

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
