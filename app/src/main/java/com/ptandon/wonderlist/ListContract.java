package com.ptandon.wonderlist;

import android.provider.BaseColumns;



public class ListContract
{
    public static final class ListEntry implements BaseColumns{


        public static final String TABLE_NAME ="lists";
        public static final String COLUMN_TODOTEXT="todoText";
        public static final String COLUMN_DUE_DATE="dueDate";
        public static final String COLUMN_PRIORITY="priority";
        public static final String COLUMN_STATUS="status";
        public static final String COLUMN_TASKNOTE="taskNote";
    }
}
