package com.company.beavi5.firebaseblog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SinglePost extends AppCompatActivity {
EditText newComment;
    TextView mSinglePostTitle,mSinglePostDesc, mSinglePostAuthor;
    ImageView image;
   // private DatabaseReference mDatabaseComment;
    private DatabaseReference mDatabaseCommentsInPost;
    private DatabaseReference mDatabaseSinglePost;
    private RecyclerView mCommendList;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;

    private boolean mProcessLike =false;
    private ImageButton mLikeBtn;
    private DatabaseReference mDatabaseLike;
    private String post_key;
    private TextView mLikeCount;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add_comment){

            //Получаем вид с файла prompt.xml, который применим для диалогового окна:
            LayoutInflater li = LayoutInflater.from(SinglePost.this);
            View addNewCommentView = li.inflate(R.layout.dialog_new_comment, null);

            //Создаем AlertDialog
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SinglePost.this);

            //Настраиваем prompt.xml для нашего AlertDialog:
            mDialogBuilder.setView(addNewCommentView);

            //Настраиваем отображение поля для ввода текста в открытом диалоге:
            final EditText etNewComment = (EditText) addNewCommentView.findViewById(R.id.etNewComment);
            //Настраиваем сообщение в диалоговом окне:
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Send",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //Вводим текст и отображаем в строке ввода на основном экране:
                                    addCommentToPost(etNewComment.getText().toString().trim()); }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            //Создаем AlertDialog:
            AlertDialog alertDialog =  mDialogBuilder.create();

            //и отображаем его:
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
       // newComment= (EditText) findViewById(R.id.etNewComment);
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseLike= FirebaseDatabase.getInstance().getReference().child("Likes");


        mLikeBtn= (ImageButton) findViewById(R.id.like_btn_on_single_post);

        image = (ImageView) findViewById(R.id.imagePost);
        mSinglePostDesc= (TextView) findViewById(R.id.singlePostDesc);
        mSinglePostTitle= (TextView) findViewById(R.id.singlePostTitle);
        mSinglePostAuthor= (TextView) findViewById(R.id.singlePostUsername);
        mLikeCount = (TextView) findViewById(R.id.post_like_count_on_single_post);
post_key=getIntent().getStringExtra("postId");
        mDatabaseSinglePost=FirebaseDatabase.getInstance().getReference().child("Blog").child(post_key);
        mDatabaseSinglePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_tile = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String  image_uri = (String) dataSnapshot.child("image").getValue();
                String  user_name = (String) dataSnapshot.child("username").getValue();
                mSinglePostDesc.setText(post_desc);
                mSinglePostAuthor.setText(user_name);
                mSinglePostTitle.setText(post_tile);
                Picasso.with(SinglePost.this).load(image_uri).into(image);


            }
            @Override
                    public void onCancelled(DatabaseError databaseError) {



            }
        });



        mCommendList= (RecyclerView) findViewById(R.id.postRecyclerView);
        mCommendList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
String s = getIntent().getStringExtra("postId");
        mCommendList.setItemViewCacheSize(60);
    mDatabaseCommentsInPost= FirebaseDatabase.getInstance().getReference().child("Comments").child(getIntent().getStringExtra("postId"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_post_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();






        FirebaseRecyclerAdapter<Commend,CommendViewHolder> firebaseCommendRecyclerAdapter =
                new FirebaseRecyclerAdapter<Commend, CommendViewHolder>
                        (Commend.class,R.layout.commend_row,CommendViewHolder.class, mDatabaseCommentsInPost) {
                    @Override
                    protected void populateViewHolder(final CommendViewHolder viewHolder, final Commend model, final int position) {


                        viewHolder.comment.setText(model.getComment());
                        if (model.getDate()!=null) {

                            Format formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                            Date date=  new Date(model.getDate());

                            String s = formatter.format(date);
                            viewHolder.commentInfo.setText(model.getUsername() + " write at "+s);
                        }



                }
                    };
        mCommendList.setAdapter(firebaseCommendRecyclerAdapter);




        //лайки

        //public void setLikeBtn(final String post_key){

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

//        }


        mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;

                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mProcessLike) {
                            if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();


                                mProcessLike = false;
                            } else {
                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                mProcessLike = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }        });}




    public void addCommentToPost(final String s) {


        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    DatabaseReference mDatabaseComment = mDatabaseCommentsInPost.push();
                                                    mDatabaseComment.child("comment").setValue(s);
                                                    mDatabaseComment.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                                    mDatabaseComment.child("username").setValue(dataSnapshot.child("name").getValue());
                                                    mDatabaseComment.child("date").setValue(ServerValue.TIMESTAMP);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            }
            );
}
}
