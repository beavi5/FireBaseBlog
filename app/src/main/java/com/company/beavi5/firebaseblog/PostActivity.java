package com.company.beavi5.firebaseblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class PostActivity extends AppCompatActivity {
    private ImageButton mSelectImage;
    private EditText mPostTitle, mPostDesc;
    private Uri mImageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;

    private Button mSubmitBtn;
    private final static int GALLERY_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);

        mProgress = new ProgressDialog(this);


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

    }

    private void startPosting() {
        mProgress.setMessage("Posting to blog..");

        final String title_val=mPostTitle.getText().toString().trim();
        final String desc_val=mPostDesc.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val)&&!TextUtils.isEmpty(desc_val)&& mImageUri !=null){
            mProgress.show();
            StorageReference filepath  = mStorage.child("blog_images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")  Uri downloadUrl = taskSnapshot .getDownloadUrl();

                DatabaseReference newPost = mDatabase.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("uid").setValue(mCurrentUser.getUid());

                mProgress.dismiss();
                    ((ImageView)findViewById(R.id.imageSelect)).setImageBitmap(null);
                    startActivity(new Intent(PostActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            });



        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
             mImageUri = data.getData();
            Picasso.with(this)
                    .load(mImageUri)
                    .fit().centerInside()
                    .into(mSelectImage);
            //mSelectImage.setImageURI(mImageUri);



        }
    }
}
