package com.example.color_match3

import ColorReplacementManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var spinner3: Spinner
    lateinit var imageView: ImageView
    lateinit var updateButton: Button

    private val colorData = ColorData()
    private val colorReplacementManager = ColorReplacementManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        spinner3 = findViewById(R.id.spinner3)
        imageView = findViewById(R.id.imageView)
        updateButton = findViewById(R.id.updateButton)

        // Изначально скрываем второй и третий спиннеры
        spinner2.visibility = View.GONE
        spinner3.visibility = View.GONE

        setupSpinner(spinner1, colorData.allColors)

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedColor = colorData.allColors[position].first
                if (selectedColor != "Выберите цвет") {
                    val availableColorsForSecondSpinner = colorData.getAvailableColorsForSecondSpinner(selectedColor)
                    setupSpinner(spinner2, listOf("Выберите цвет" to null) + availableColorsForSecondSpinner)

                    spinner2.visibility = View.VISIBLE
                    spinner3.visibility = View.GONE
                } else {
                    spinner2.visibility = View.GONE
                    spinner3.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val firstColor = spinner1.selectedItem as Pair<String, Int?>
                val secondColor = spinner2.selectedItem as Pair<String, Int?>
                if (secondColor.first != "Выберите цвет") {
                    val availableColorsForThirdSpinner = colorData.getAvailableColorsForThirdSpinner(firstColor.first, secondColor.first)
                    setupSpinner(spinner3, listOf("Выберите цвет" to null) + availableColorsForThirdSpinner)

                    spinner3.visibility = View.VISIBLE
                } else {
                    spinner3.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        updateButton.setOnClickListener {
            updateImageColors()
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<Pair<String, Int?>>) {
        val adapter = ColorSpinnerAdapter(this, items)
        spinner.adapter = adapter
    }

    private fun updateImageColors() {
        val firstColorResource = (spinner1.selectedItem as? Pair<String, Int?>)?.second
        val secondColorResource = (spinner2.selectedItem as? Pair<String, Int?>)?.second
        val thirdColorResource = (spinner3.selectedItem as? Pair<String, Int?>)?.second

        if (firstColorResource != null && secondColorResource != null && thirdColorResource != null) {
            val firstColor = ContextCompat.getColor(this, firstColorResource)
            val secondColor = ContextCompat.getColor(this, secondColorResource)
            val thirdColor = ContextCompat.getColor(this, thirdColorResource)

            // Асинхронный вызов
            colorReplacementManager.replaceColorsInDrawableAsync(
                this,
                R.drawable.mansvg,
                firstColor,
                secondColor,
                thirdColor
            ) { modifiedBitmap ->
                imageView.setImageBitmap(modifiedBitmap)
            }
        }
    }
}