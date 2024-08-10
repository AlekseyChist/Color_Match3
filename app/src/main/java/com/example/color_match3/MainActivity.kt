package com.example.color_match3

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Список всех цветов с их ресурсами
    val allColors = listOf(
        "Black" to R.color.black,
        "White" to R.color.white,
        "Gray" to R.color.gray,
        "Navy Blue" to R.color.navy_blue,
        "Brown" to R.color.brown,
        "Beige" to R.color.beige,
        "Olive Green" to R.color.olive_green,
        "Forest Green" to R.color.forest_green,
        "Burgundy" to R.color.burgundy,
        "Khaki" to R.color.khaki,
        "Indigo" to R.color.indigo,
        "Dark Brown" to R.color.dark_brown,
        "Charcoal Gray" to R.color.charcoal_gray,
        "Chocolate" to R.color.chocolate,
        "Sand" to R.color.sand,
        "Eggplant" to R.color.eggplant
    )

    // Словарь с возможными сочетаниями цветов
    val colorCombinations = mapOf(
        "Black" to listOf("White", "Gray", "Burgundy", "Olive Green", "Beige", "Khaki", "Sand", "Eggplant"),
        "White" to listOf("Black", "Gray", "Navy Blue", "Brown", "Beige", "Olive Green", "Khaki", "Indigo"),
        "Gray" to listOf("Black", "White", "Navy Blue", "Burgundy", "Brown", "Olive Green", "Indigo", "Eggplant"),
        "Navy Blue" to listOf("White", "Gray", "Brown", "Beige", "Khaki", "Burgundy", "Olive Green"),
        "Brown" to listOf("Beige", "White", "Gray", "Olive Green", "Navy Blue", "Khaki", "Sand"),
        "Beige" to listOf("Brown", "Navy Blue", "Olive Green", "Burgundy", "White", "Khaki", "Sand"),
        "Olive Green" to listOf("Beige", "Brown", "Black", "White", "Khaki", "Navy Blue", "Sand"),
        "Forest Green" to listOf("Beige", "Brown", "Khaki", "Sand"),
        "Burgundy" to listOf("Gray", "Navy Blue", "Black", "Brown", "Beige", "Khaki", "Eggplant"),
        "Khaki" to listOf("Brown", "Olive Green", "Navy Blue", "Burgundy", "Black", "Sand"),
        "Indigo" to listOf("Gray", "White", "Beige", "Eggplant"),
        "Dark Brown" to listOf("Beige", "White", "Gray", "Olive Green", "Khaki"),
        "Charcoal Gray" to listOf("White", "Black", "Burgundy", "Olive Green"),
        "Chocolate" to listOf("Beige", "White", "Khaki", "Sand"),
        "Sand" to listOf("Brown", "Olive Green", "Khaki", "Chocolate"),
        "Eggplant" to listOf("Gray", "Burgundy", "Indigo", "Black")
    )

    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var spinner3: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        spinner3 = findViewById(R.id.spinner3)

        // Изначально делаем все спиннеры, кроме первого, недоступными
        spinner2.isEnabled = false
        spinner3.isEnabled = false

        // Настройка первого спиннера со всеми цветами
        setupSpinner(spinner1, allColors)

        // Обработчик выбора первого спиннера
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedColor = allColors[position].first
                val availableColorsForSecondSpinner = colorCombinations[selectedColor]?.map { colorName ->
                    allColors.find { it.first == colorName }!!
                } ?: emptyList()
                setupSpinner(spinner2, availableColorsForSecondSpinner)

                spinner2.isEnabled = true
                spinner3.isEnabled = false // Отключаем третий спиннер до выбора во втором
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Обработчик выбора второго спиннера
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val firstColor = spinner1.selectedItem as Pair<String, Int>
                val secondColor = spinner2.selectedItem as Pair<String, Int>
                val availableColorsForThirdSpinner = allColors.filter {
                    it != firstColor && it != secondColor
                }
                setupSpinner(spinner3, availableColorsForThirdSpinner)

                spinner3.isEnabled = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<Pair<String, Int>>) {
        val adapter = ColorSpinnerAdapter(this, items)
        spinner.adapter = adapter
    }
}