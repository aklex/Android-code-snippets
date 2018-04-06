package com.flexdecision.ak_lex.recyclerview.RecyclerView;

import android.view.View;

/**
 * Created by a_Lex on 4/5/2018.
 */

public interface RecyclerClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
