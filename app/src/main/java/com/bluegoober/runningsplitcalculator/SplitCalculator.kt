package com.bluegoober.runningsplitcalculator

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt

class SplitCalculator(var time: Double, distance: Double, measurementSystem: String) {
    private var distanceMeters = 0.0
    private var distanceFeet = 0.0

    //Function to get a metric split
    fun getMetricSplit(splitDistance: Int): String {
        val split: Double
        //Get the value for the converted distance
        val conversionSplits = distanceMeters / splitDistance

        //Determine the number of splits needed
        val totalSplits: Int = floor(distanceMeters / splitDistance).toInt()

        //Get the split from the time and converted distance
        split = time / conversionSplits

        //Create the split list and split StringBuilder for use in storing the split data
        val splitList = ArrayList<Double>()
        val splitString = StringBuilder()

        //Get the splits for all values up to the total number of splits
        for (i in 1 until totalSplits + 1) {
            //Add the split to the splits list
            splitList.add(split * i)
            //Format the split time from the split long
            val splitTime = LocalTime.MIN.plusSeconds((split * i).roundToInt().toLong()).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            //Append the split distance and split time to the splitString
            splitString.append("${splitDistance * i}m - $splitTime\n")
        }

        //Add the total time to the split list
        splitList.add(time)

        //If the final split distance does not equal the input distance add the final distance to the split string
        if ((totalSplits * splitDistance).toDouble() != distanceMeters) {
            splitString.append(distanceMeters.roundToInt().toString() + "m - " + LocalTime.MIN.plusSeconds(time.roundToInt().toLong()).format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        }

        //Return the split string
        return splitString.toString()
    }

    //Function to get an imperial split
    fun getImperialSplit(splitDistance: Double): String {
        val split: Double
        //Get the values for the converted distance
        val conversionSplits = distanceFeet / (splitDistance * feetInMile)

        //Calculate the total number of splits there are
        val totalSplits: Int = floor(distanceFeet / (splitDistance * feetInMile)).toInt()

        //Get the split from the time and converted distance
        split = time / conversionSplits

        //Create the split list and split StringBuilder for use in storing the split data
        val splitList = ArrayList<Double>()
        val splitString = StringBuilder()

        //Get the splits for all values up to the total number of splits
        for (i in 1 until totalSplits + 1) {
            //Add the split to the split list
            splitList.add(split * i)
            //Format the split time from the split long
            val splitTime = LocalTime.MIN.plusSeconds((split * i).roundToInt().toLong()).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val splitNum = (i.toFloat() * splitDistance)
            //Append the split distance and split time to the splitString
            splitString.append("Mile $splitNum - $splitTime\n")
        }

        //Add the total time to the split list
        splitList.add(time)

        //If the final split distance does not equal the input distance add the final distance to the split string
        if (totalSplits.toDouble() != distanceFeet / (splitDistance * feetInMile)) {
            splitString.append("Mile " + (distanceFeet / feetInMile * 10).roundToInt() / 10.0 + " - " + LocalTime.MIN.plusSeconds(time.roundToInt().toLong()).format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        }

        //Return the split string
        return splitString.toString()
    }

    companion object {
        const val IMPERIAL_UNITS = "imperial"
        const val METRIC_UNITS = "metric"
        const val metersToFeetConversion = 3.2808
        const val feetInMile = 5280
    }

    init {
        if (measurementSystem == IMPERIAL_UNITS) {
            distanceMeters = distance * feetInMile / metersToFeetConversion
            distanceFeet = distance * feetInMile
        } else {
            distanceMeters = distance
            distanceFeet = distance * metersToFeetConversion
        }
    }
}