package com.example.color_match3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class ColorSpinnerAdapter(
    context: Context,
    private val colors: List<Pair<String, Int?>>
) : ArrayAdapter<Pair<String, Int?>>(context, 0, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val colorCircle = view.findViewById<View>(R.id.color_circle)
        val colorName = view.findViewById<TextView>(R.id.color_name)

        val item = getItem(position)

        item?.let {
            if (it.second != null) {
                val color = ContextCompat.getColor(context, it.second!!)
                colorCircle.setBackgroundColor(color)
            } else {
                colorCircle.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            }
            colorName.text = it.first
        }

        return view
    }
}