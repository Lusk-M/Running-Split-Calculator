package com.bluegoober.runningsplitcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SplitRecyclerAdapter (val splitList : ArrayList<SplitObject>, val context: Context) : RecyclerView.Adapter<SplitRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val splitLayout: LinearLayout = view.findViewById(R.id.split_card_holder)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.split_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardLayout: LinearLayout = holder.splitLayout
        val splitTextView: TextView = cardLayout.findViewById(R.id.split_container)
        val splitTitle: TextView = cardLayout.findViewById(R.id.split_title)
        val currentSplit: SplitObject = splitList[position]
        val expandCard: ImageView = cardLayout.findViewById(R.id.expand_split_card)
        val expandCardBottom: ImageView = cardLayout.findViewById(R.id.expand_split_card_bottom)
        splitTextView.text = currentSplit.splitText
        splitTitle.text = currentSplit.splitName

        //Set the click listener to expand and collapse the split view on user input
        expandCard.setOnClickListener {
            val expandLayout: LinearLayout = cardLayout.findViewById(R.id.split_expand_list)
            if (expandLayout.visibility == View.GONE) {
                expandLayout.visibility = View.VISIBLE
                expandCard.setImageResource(R.drawable.baseline_expand_less_24)
                expandCardBottom.visibility = View.VISIBLE
            } else {
                expandLayout.visibility = View.GONE
                expandCardBottom.visibility = View.GONE
                expandCard.setImageResource(R.drawable.baseline_expand_more_24)
            }

        }
        //Set the click listener for the bottom button to expand and collapse the split view on user input
        expandCardBottom.setOnClickListener {
            val expandLayout: LinearLayout = cardLayout.findViewById(R.id.split_expand_list)
            if (expandLayout.visibility == View.GONE) {
                expandLayout.visibility = View.VISIBLE
                expandCard.setImageResource(R.drawable.baseline_expand_less_24)
                expandCardBottom.visibility = View.VISIBLE
            } else {
                expandLayout.visibility = View.GONE
                expandCardBottom.visibility = View.GONE
                expandCard.setImageResource(R.drawable.baseline_expand_more_24)
            }

        }
    }

    override fun getItemCount() = splitList.size

}


