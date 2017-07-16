package com.company.beavi5.firebaseblog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by beavi5 on 13.07.2017.
 */

public class CommendViewHolder extends RecyclerView.ViewHolder {
    TextView comment, commentInfo;
    public CommendViewHolder(View itemView) {
        super(itemView);

    comment = (TextView) itemView.findViewById(R.id.comment);
    commentInfo = (TextView) itemView.findViewById(R.id.tvCommentInfo);
    }





}
