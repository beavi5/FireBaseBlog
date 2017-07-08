package com.company.beavi5.firebaseblog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 02.07.2017.
 */

public class BlogViewHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView post_title,post_desc;
    ImageView image;
    Button delete_post_btn;
    public BlogViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        delete_post_btn= (Button) mView.findViewById(R.id.deletePostBtn);
        image = (ImageView) mView.findViewById(R.id.post_image);
        post_desc = (TextView) mView.findViewById(R.id.post_text);
        post_title = (TextView) mView.findViewById(R.id.post_title);
    }
}
