package com.byted.camp.todolist.beans;


import android.graphics.Color;

public enum Priority {
    LOW(-1), MEDIUM(0), HIGH(1);

    public final int intValue;

    Priority(int intValue) {
        this.intValue = intValue;
    }

    public static Priority from(int intValue) {
        for (Priority priority : Priority.values()) {
            if (priority.intValue == intValue) {
                return priority;
            }
        }
        return LOW; // default
    }

    public static int toColor(Priority priority) {
        if(priority == HIGH) {
            return Color.parseColor("#ef9a9a");
        }
        if(priority == MEDIUM) {
            return Color.parseColor("#ffcc80");
        }
        return Color.parseColor("#a5d6a7");
    }
}
