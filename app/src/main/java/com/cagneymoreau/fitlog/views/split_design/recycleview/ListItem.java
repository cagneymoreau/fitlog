package com.cagneymoreau.fitlog.views.split_design.recycleview;

import android.util.Log;

/**
 * easy to manage objects that represent their heirarchy on lists and sublists
 */

public class ListItem {

    public enum type {TITLE, HEADER, FIELD}
    public String title;
    public String content;
    public type myType;
    public int mainList;
    public int subList;
    public boolean uiPlaceHolder;

    public String getEditPrompt()
    {
        switch (myType){

            case TITLE:

                return "Edit split name";


            case HEADER:

                return "Edit day name";

            case FIELD:

                return "Edit movement";


        }

        Log.e("split edit frag", "getEditPrompt: ",null );
        return "error";
    }


}
