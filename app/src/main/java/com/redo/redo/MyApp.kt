package com.redo.redo

import android.app.Application

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by jakob on 12.08.15.
 */
public class MyApp : Application() {
    public var reminders: ArrayList<Reminder>

    init {
        reminders = ArrayList<Reminder>()
    }
}
