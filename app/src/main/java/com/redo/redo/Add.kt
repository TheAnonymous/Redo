package com.redo.redo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

/**
 * Created by jakob on 12.08.15.
 */
public class Add : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add)
        val actionbar = getActionBar()
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setSubtitle("Add Reminder")
        val intent = getIntent()
        val button = findViewById(R.id.save) as Button
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                openList()
            }
        })

    }



    public fun openList() {
        val myIntent = Intent(this, javaClass<MainList>())
        var type = "-"
        val radioButtonId = (findViewById(R.id.type) as RadioGroup).getCheckedRadioButtonId()
        type = (findViewById(radioButtonId) as RadioButton).getText() as String
        Log.i("Add_Numbers", (findViewById(R.id.delay) as TextView).getText().toString())
        Log.i("Add_Name", (findViewById(R.id.name) as TextView).getText().toString())
        Log.i("Add_type", Integer.toString((findViewById(R.id.type) as RadioGroup).getCheckedRadioButtonId()))
        val delay = (findViewById(R.id.delay) as TextView).getText().toString()
        val name = (findViewById(R.id.name) as TextView).getText().toString()


        myIntent.putExtra("delay", delay)
        myIntent.putExtra("name", name)
        myIntent.putExtra("type", type)
        startActivity(myIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
