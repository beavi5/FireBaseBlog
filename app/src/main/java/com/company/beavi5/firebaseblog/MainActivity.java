package com.company.beavi5.firebaseblog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseLike;


    private boolean mProcessLike =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.d("act", "MAIN - ща стартанем авторизацию");
                    startActivity(registerIntent);
                }
            }
        };

        setContentView(R.layout.activity_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
       mDatabase.keepSynced(true);

        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike= FirebaseDatabase.getInstance().getReference().child("Likes");
       mDatabaseUsers.keepSynced(true);

        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
       mBlogList.setHasFixedSize(true);
      LinearLayoutManager mllm = new LinearLayoutManager(this);
        mllm.setStackFromEnd(true);
        mllm.setReverseLayout(true);

        Log.d("act", "MAIN - еще не стартанулась?))");

        mBlogList.setLayoutManager(mllm);
        checkUserExist();
    }




    private void checkUserExist() {


        if (mAuth.getCurrentUser()!=null) {
            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_id)){
                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }



                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this,PostActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, final int position) {

            final String post_key = getRef(position).getKey();

            viewHolder.post_title.setText(model.getTitle());
                viewHolder.setLikeBtn(post_key);

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    mProcessLike = true;

                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessLike){
                                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                                    mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                    mProcessLike=false;
                                }
                                else
                                {
                                    mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                    mProcessLike=false;
                                }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


           if (mAuth.getCurrentUser().getUid().equals(model.getUid()))  viewHolder.mDeleteBtn.setVisibility(View.VISIBLE);
                else viewHolder.mDeleteBtn.setVisibility(View.INVISIBLE); // очистка

           viewHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   final DatabaseReference mDatabasePostRef= getRef(viewHolder.getAdapterPosition());
                    final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                deleteDialog.setTitle("Delete post");
                    deleteDialog.setMessage("You sure want delete post?");
                    deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mDatabasePostRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Post was deleted...", Toast.LENGTH_SHORT).show();
                                }
                    });


                           // notifyDataSetChanged();

                        }
                    });

                    deleteDialog.show();
                 //   notifyDataSetChanged();
                  //  FirebaseDatabase.getInstance().getReference().child("Blog");


                }
            });

                //viewHolder.post_desc.setText("OPA C!!!6un");
             //   else

                viewHolder.post_desc.setText(model.getDesc());
                viewHolder.post_username.setText(model.getUsername());

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

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
