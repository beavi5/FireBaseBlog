package com.company.beavi5.firebaseblog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by admin on 02.07.2017.
 */

public class BlogViewHolder extends RecyclerView.ViewHolder {
      TextView mLikeCount;
    ImageButton mDeleteBtn;
    View mView;
    public ImageButton mLikeBtn;
    TextView post_title,post_desc, post_username;
    ImageView image;
    Button delete_post_btn;

    DatabaseReference mDatabaseLike;
    FirebaseAuth mAuth;
    public BlogViewHolder(View itemView) {
        super(itemView);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseLike.keepSynced(true);



        mView = itemView;

      //  delete_post_btn= (Button) mView.findViewById(R.id.deletePostBtn);
        mLikeBtn= (ImageButton) mView.findViewById(R.id.like_btn);
        mLikeCount= (TextView) mView.findViewById(R.id.post_like_count);
        mDeleteBtn= (ImageButton) mView.findViewById(R.id.delete_post);
        image = (ImageView) mView.findViewById(R.id.post_image);
        post_desc = (TextView) mView.findViewById(R.id.post_text);
        post_title = (TextView) mView.findViewById(R.id.post_title);
        post_username = (TextView) mView.findViewById(R.id.post_username);

    }



    public void setLikeBtn(final String post_key){

        mDatabaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                mLikeBtn.setImageResource(R.drawable.like_red);

                }
                else
                {
                    mLikeBtn.setImageResource(R.drawable.like_gray);

                }
                mLikeCount.setText(""+dataSnapshot.child(post_key).getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
