package com.redo.redo

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

public class MainList : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        val intent = getIntent()
        val app = getApplicationContext() as MyApp
        if (app.reminders != null)
            for (i in app.reminders.indices) {
                val tmp = app.reminders.get(i)
                addItem(tmp.name, tmp.type, tmp.delay, tmp.lastTimeDone)
            }
        else {
            app.reminders = ArrayList<Reminder>()
        }

        if (intent.getExtras() != null) {
            val bundle = intent.getExtras()
            if (bundle.getString("delay") != null) {

                Log.i("BUNDLE", bundle.toString())
                val delay = Integer.parseInt(bundle.getString("delay"))
                Log.i("Delay", bundle.getString("delay"))
                val name = bundle.getString("name")
                Log.i("Name", name)
                val type = bundle.getString("type")
                Log.i("Type", type)

                val r = Reminder()
                r.delay = delay
                r.lastTimeDone = Calendar.getInstance().getTime().getTime()
                r.name = name
                r.type = type
                app.reminders.add(r)
                addItem(name, type, delay, Calendar.getInstance().getTime().getTime())
                saveState(app.reminders)
            } else {
                Log.i("GOING_TO_LOADING", "-------->bundle.getString(\"delay\") != null")
                val tablelayout = findViewById(R.id.tableLayout) as TableLayout
                tablelayout.removeAllViews()
                app.reminders = loadReminders()
                for (i in app.reminders.indices) {
                    val tmp = app.reminders.get(i)
                    addItem(tmp.name, tmp.type, tmp.delay, tmp.lastTimeDone)
                }
            }
        } else {
            Log.i("GOING_TO_LOADING", "-------->intent.getExtras() != null")
            val tablelayout = findViewById(R.id.tableLayout) as TableLayout
            tablelayout.removeAllViews()
            app.reminders = loadReminders()
            if (app.reminders == null) {
                app.reminders = ArrayList<Reminder>()
            }
            for (i in app.reminders.indices) {
                val tmp = app.reminders.get(i)
                addItem(tmp.name, tmp.type, tmp.delay, tmp.lastTimeDone)
            }
        }


    }

    public fun loadReminders(): ArrayList<Reminder> {
        Log.i("INSIDE_LOADING", "<===>")
        val sdcard = Environment.getExternalStorageDirectory()
        val f = File(sdcard, "reminders.ser")
        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(FileInputStream(f))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            if (ois != null) {
                val o = ois.readObject() as ArrayList<Reminder>
                if (o != null) {
                    Log.i("ARRAYSIZE", Integer.toString(o.size()))
                } else {
                    Log.i("ARRAYSIZE", "--------->NULL")
                }
                return o
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ArrayList<Reminder>()
    }

    public fun saveState(reminders: ArrayList<Reminder>) {

        try {
            val sdcard = Environment.getExternalStorageDirectory()
            val file = File(sdcard, "reminders.ser")
            val fileOut = FileOutputStream(file)
            val out = ObjectOutputStream(fileOut)
            out.writeObject(reminders)
            out.close()
            fileOut.close()
            Log.i("saveState", "Serialized data is saved in /tmp/employee.ser")
        } catch (i: IOException) {
            i.printStackTrace()
        }

    }

    public fun removeItemDialog(pb: ProgressBar, tv1: TextView, tv2: TextView, tl: TableLayout, id: Int) {
        val dialogClickListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                val app = getApplicationContext() as MyApp
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        //Done button clicked
                        app.reminders = loadReminders()
                        val rem = app.reminders.get(id)
                        app.reminders.set(id, Reminder(rem.name, rem.type, rem.delay, Date().getTime()))
                        saveState(app.reminders)
                        tl.removeAllViews()
                        app.reminders = loadReminders()
                        for (i in app.reminders.indices) {
                            val tmp = app.reminders.get(i)
                            addItem(tmp.name, tmp.type, tmp.delay, tmp.lastTimeDone)
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        //Remove button clicked
                        tl.removeView(pb)
                        tl.removeView(tv1)
                        tl.removeView(tv2)
                        app.reminders.remove(id)
                        saveState(app.reminders)


                        tl.removeAllViews()
                        app.reminders = loadReminders()
                        for (i in app.reminders.indices) {
                            val tmp = app.reminders.get(i)
                            addItem(tmp.name, tmp.type, tmp.delay, tmp.lastTimeDone)
                        }
                    }
                }
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Done task or remove reminder?").setPositiveButton("Done", dialogClickListener).setNegativeButton("Remove", dialogClickListener).show()
    }

    public fun addItem(name: String, type: String, delay: Int?, date: Long) {
        //Debug
        Log.d("TimePassedInPercentage", Integer.toString(TimeCalculator.TimePassedInPercentage(date, delay!!, type)))
        Log.d("TimeLeftSentence", TimeCalculator.TimeLeftSentence(date, delay, type))

        //Initaliaize
        val tablelayout = findViewById(R.id.tableLayout) as TableLayout
        val tvlabel = TextView(this)
        val tvtime = TextView(this)
        val pbprogress = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        val app = getApplicationContext() as MyApp
        val id = app.reminders.size() - 1
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val trparams = TableRow.LayoutParams(-1, -2)

        //Set First Labe with name etc.

        tvlabel.setText(name + " - every " + delay.toString() + " " + type)
        tvlabel.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium)
        tvlabel.setLayoutParams( trparams)
        tvlabel.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View): Boolean {
                removeItemDialog(pbprogress, tvlabel, tvtime, tablelayout, id)
                return false
            }
        })

        //SetProgressbar
        pbprogress.setProgress(TimeCalculator.TimePassedInPercentage(date, delay, type))
        pbprogress.setLayoutParams(trparams)
        pbprogress.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View): Boolean {
                removeItemDialog(pbprogress, tvlabel, tvtime, tablelayout, id)
                return true
            }
        })

        //Set Time left

        tvtime.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small)
        tvtime.setLayoutParams(trparams)
        tvtime.setText(TimeCalculator.TimeLeftSentence(date, delay, type))
        tvtime.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View): Boolean {
                removeItemDialog(pbprogress, tvlabel, tvtime, tablelayout, id)
                return false
            }
        })

        //Add to List
        tablelayout.addView(tvlabel)
        tablelayout.addView(pbprogress)
        tablelayout.addView(tvtime)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            AddReminder()
            return true
        }

        // Handle presses on the action bar items
        when (item.getItemId()) {
            R.id.action_add -> {
                AddReminder()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    public fun AddReminder() {

        val myIntent = Intent(this, javaClass<Add>())
        startActivity(myIntent)
    }
}
