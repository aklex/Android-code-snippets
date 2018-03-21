package com.flexdecision.ak_lex.noeditableedittext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by a_Lex on 3/21/2018.
 */

public class NoEditableEditText extends android.support.v7.widget.AppCompatEditText {

    boolean canPaste()
    {
        return false;
    }

    boolean canCut(){
        return false;
    }

    @Override
    public boolean isSuggestionsEnabled()
    {
        return false;
    }

    public NoEditableEditText(Context context)
    {
        super(context);
    }

    public NoEditableEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NoEditableEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
     * by intercepting the callback that would cause it to be created, and returning false.
     */
    /*private class ActionModeCallbackInterceptor implements android.view.ActionMode.Callback {
        private final String TAG = NoMenuEditText.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {

        }
    }*/
    @Override
    public int getSelectionStart() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getMethodName().equals("canPaste")) {
                return -1;
            }
            if (element.getMethodName().equals("canCut")){
                return -1;
            }
        }
        return super.getSelectionStart();
    }
}
