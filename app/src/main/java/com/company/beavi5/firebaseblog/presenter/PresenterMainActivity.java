package com.company.beavi5.firebaseblog.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.beavi5.firebaseblog.Blog;
import com.company.beavi5.firebaseblog.BlogViewHolder;
import com.company.beavi5.firebaseblog.LoginActivity;
import com.company.beavi5.firebaseblog.MainActivity;
import com.company.beavi5.firebaseblog.R;
import com.company.beavi5.firebaseblog.SetupActivity;
import com.company.beavi5.firebaseblog.SinglePost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import interfaces.presenter.IPresenterMainActivity;

/**
 * Created by beavi5 on 19.07.2017.
 */

public class PresenterMainActivity implements IPresenterMainActivity {
    private RecyclerView mBlogList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike;

    public PresenterMainActivity(final Context context, RecyclerView mBlogList) {
        this.mBlogList = mBlogList;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    Intent registerIntent = new Intent(context, LoginActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    context.startActivity(registerIntent);
                }
            }
        };

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(false);

        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike= FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseUsers.keepSynced(false);
        checkUserExist();
    }




    private void checkUserExist() {


        if (mAuth.getCurrentUser()!=null) {
            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_id)){
                        Intent setupIntent = new Intent(context, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(setupIntent);

                    }



                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    @Override
    public void onStartActivity() {
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
                Intent singlePostIntent = new Intent(context, SinglePost.class);
                    context.startActivity(singlePostIntent.putExtra("postId",post_key));
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
                    final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
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
                                    Toast.makeText(context, "Post was deleted...", Toast.LENGTH_SHORT).show();
                                }
                    });


                        }
                    });

                    deleteDialog.show();

                }
            });


                viewHolder.post_desc.setText(model.getDesc());
                viewHolder.post_username.setText(model.getUsername());


            viewHolder.post_desc.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((TextView)v).getMaxLines()==1) ((TextView)v).setMaxLines(100);
                            else ((TextView)v).setMaxLines(1);
                        }
                    }
            );


                DrawPic(viewHolder.image, model.image);


            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    private void DrawPic(ImageView imgView, String imageAddr) {
        Picasso.with(context).load(imageAddr)
                .fit()
                .centerInside().into(imgView);
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }
}
