package com.bluegoober.runningsplitcalculator

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    //Create variables for the values and views used
    private var hoursInput: EditText? = null
    private var minutesInput: EditText? = null
    private var secondsInput: EditText? = null
    private var distanceInput: EditText? = null
    private var unitSpinner: Spinner? = null
    private var calcSplit: Button? = null
    private var splitTime: Double = 0.0
    private var splitDistance: Double = 0.0
    private var splitUnit: String = SplitCalculator.METRIC_UNITS
    private val splitList = ArrayList<SplitObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Find and set the used views
        hoursInput = findViewById(R.id.hoursInput)
        minutesInput = findViewById(R.id.minutesInput)
        secondsInput = findViewById(R.id.secondsInput)
        distanceInput = findViewById(R.id.distanceInput)
        calcSplit = findViewById(R.id.calcSplitButton)
        unitSpinner = findViewById<View>(R.id.distanceSystemSpinner) as Spinner

        //Setup spinner and onItemSelected handler
        val adapter = ArrayAdapter.createFromResource(this, R.array.measurement_systems, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner!!.adapter = adapter
        unitSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //If the user has imperial units selected, limit the distance input to 3
                //Otherwise keep the input limit to 5
                val filterArray = arrayOfNulls<InputFilter>(1)
                filterArray[0] = InputFilter.LengthFilter(3)
                if(position == 1)
                    distanceInput?.filters = filterArray
                else {
                    filterArray[0] = InputFilter.LengthFilter(5)
                    distanceInput?.filters = filterArray
                }

            }

        }
        calcSplit?.setOnClickListener { onSubmitCalc() }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(TIME_KEY_NAME, java.lang.Double.doubleToRawLongBits(splitTime))
        outState.putLong(DISTANCE_KEY_NAME, java.lang.Double.doubleToRawLongBits(splitDistance))
        outState.putString(UNIT_KEY_NAME, splitUnit)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        splitTime = java.lang.Double.longBitsToDouble(savedInstanceState.getLong(TIME_KEY_NAME, 0))
        splitDistance = java.lang.Double.longBitsToDouble(savedInstanceState.getLong(DISTANCE_KEY_NAME, 0))
        splitUnit = savedInstanceState.getString(UNIT_KEY_NAME, SplitCalculator.METRIC_UNITS).toString()
        createSplitLayout(splitTime, splitDistance, splitUnit)
    }

    private fun onSubmitCalc() {
        var hours = 0.0
        var minutes = 0.0
        var seconds = 0.0
        var distance = 0.0
        //Check that the hours input is not empty and set the hours variable
        if (hoursInput!!.text.toString().isNotEmpty()) {
            hours = hoursInput!!.text.toString().toDouble()
        }
        //Check that the minutes input is not empty and set the minutes variable
        if (minutesInput!!.text.toString().isNotEmpty()) {
            minutes = minutesInput!!.text.toString().toDouble()
        }
        //Check that the seconds input is not empty and set the seconds variable
        if (secondsInput!!.text.toString().isNotEmpty()) {
            seconds = secondsInput!!.text.toString().toDouble()
        }
        val totalSecond = hours * 3600 + minutes * 60 + seconds
        //Check that the distance input is not empty and set the distance variable
        if (distanceInput!!.text.toString().isNotEmpty()) {
            distance = distanceInput!!.text.toString().toDouble()
        }
        //Get the units selected
        val unitType = unitSpinner!!.selectedItem.toString()
        //Set the units to metric unless the user has selected miles
        var units = SplitCalculator.METRIC_UNITS
        if (unitType == "Miles") {
            units = SplitCalculator.IMPERIAL_UNITS
        }

        //Set global variables
        splitTime = totalSecond
        splitDistance = distance
        splitUnit = units

        //Call the method to create the split card layout
        createSplitLayout(totalSecond, distance, units)
    }

    private fun createSplitLayout( totalSecond: Double, distance: Double, units: String) {
        //Clear the split list of any current entries
        splitList.clear()

        //Get new instance of SplitCalculator
        val splitCalculator = SplitCalculator(totalSecond, distance, units)

        //Get the split strings for each distance and put them in SplitObjects
        val split1000: String = splitCalculator.getMetricSplit(1000)
        val split1000Object = SplitObject("1000m", split1000)
        val split1600: String = splitCalculator.getMetricSplit(1600)
        val split1600Object = SplitObject( "1600m", split1600)
        val split800: String = splitCalculator.getMetricSplit(800)
        val split800Object = SplitObject("800m", split800)
        val split400: String = splitCalculator.getMetricSplit(400)
        val split400Object = SplitObject("400m", split400)
        val split200: String = splitCalculator.getMetricSplit(200)
        val split200Object = SplitObject("200m", split200)
        val split100: String = splitCalculator.getMetricSplit(100)
        val split100Object = SplitObject("100m", split100)
        val splitHalfMile: String = splitCalculator.getImperialSplit(0.5)
        val splitHalfMileObject = SplitObject("Half Mile", splitHalfMile)
        val splitMile: String = splitCalculator.getImperialSplit(1.0)
        val splitMileObject = SplitObject("One Mile", splitMile)
        val splitTwoMile: String = splitCalculator.getImperialSplit(2.0)
        val splitTwoMileObject = SplitObject("Two Mile", splitTwoMile)

        //If the user selected imperial units display the mile splits first
        if(units.toLowerCase() == SplitCalculator.IMPERIAL_UNITS) {
            splitList.add(splitHalfMileObject)
            splitList.add(splitMileObject)
            splitList.add(splitTwoMileObject)
        }
        //Add the splits to the split list
        splitList.add(split100Object)
        splitList.add(split200Object)
        splitList.add(split400Object)
        splitList.add(split800Object)
        splitList.add(split1000Object)
        splitList.add(split1600Object)
        //If the user selected metric splits add the mile splits last
        if(units == SplitCalculator.METRIC_UNITS) {
            splitList.add(splitHalfMileObject)
            splitList.add(splitMileObject)
            splitList.add(splitTwoMileObject)
        }

        //Create split adapter, recycler view, and layout manager
        val splitAdapter = SplitRecyclerAdapter(splitList, this)
        val splitRecycler: RecyclerView = findViewById(R.id.split_recycler)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        splitRecycler.layoutManager = layoutManager
        splitRecycler.adapter = splitAdapter

        //Call the method to hide the keyboard
        hideKeyboard(this)
    }

    //Method to hide the keyboard
    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val DISTANCE_KEY_NAME: String = "SAVED_DISTANCE"
        private const val TIME_KEY_NAME: String = "SAVED_TIME"
        private const val UNIT_KEY_NAME: String = "SAVED_UNIT"
    }
}