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

import com.company.beavi5.firebaseblog.presenter.PresenterMainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

import interfaces.presenter.IPresenterMainActivity;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mBlogList;

    private IPresenterMainActivity presenterMainActivity;

    private boolean mProcessLike =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBlogList = (RecyclerView) findViewById(R.id.blog_list);


      mBlogList.setHasFixedSize(true);
      LinearLayoutManager mllm = new LinearLayoutManager(this);
        mllm.setStackFromEnd(true);
        mllm.setReverseLayout(true);



        mBlogList.setLayoutManager(mllm);

        //подключаем презентер
        presenterMainActivity = new PresenterMainActivity(this,mBlogList);

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
        presenterMainActivity.logout();

    }


    @Override
    protected void onStart() {
        super.onStart();

        presenterMainActivity.onStartActivity();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
