package com.redo.redo

import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Calendar
import java.util.Date

/**
 * Created by jakob on 12.08.15.
 */
public class Reminder : Serializable {
    public var name: String
    public var type: String
    public var delay: Int? = null
    public var lastTimeDone: Long = 0


    public constructor(name: String, type: String, delay: Int?, lastTimeDone: Long) {
        this.name = name
        this.type = type
        this.delay = delay
        this.lastTimeDone = lastTimeDone

    }

    public constructor() {
        this.name = ""
        this.type = ""
        this.delay = 0
        this.lastTimeDone = 0

    }

}
