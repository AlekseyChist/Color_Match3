package com.example.color_match3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private lateinit var imageView: ImageView
    private lateinit var generateButton: Button

    private val allColors: List<Pair<String, Int?>> = listOf(
        "Выберите цвет" to null,
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

    private val colorCombinations: Map<String, List<String>> = mapOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        spinner3 = findViewById(R.id.spinner3)
        imageView = findViewById(R.id.imageView)
        generateButton = findViewById(R.id.generateButton)

        setupSpinners()
        setupGenerateButton()
    }

    private fun setupSpinners() {
        setupSpinner(spinner1, allColors)

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedColor = allColors[position].first
                if (selectedColor != "Выберите цвет") {
                    val availableColorsForSecondSpinner = colorCombinations[selectedColor]?.map { colorName ->
                        allColors.find { it.first == colorName }!!
                    } ?: emptyList()
                    setupSpinner(spinner2, listOf("Выберите цвет" to null) + availableColorsForSecondSpinner)
                    spinner2.isEnabled = true
                    spinner3.isEnabled = false
                } else {
                    spinner2.isEnabled = false
                    spinner3.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val firstColor = (spinner1.selectedItem as? Pair<String, Int?>)?.first ?: return
                val secondColor = (spinner2.selectedItem as? Pair<String, Int?>)?.first ?: return
                if (secondColor != "Выберите цвет") {
                    val availableColorsForThirdSpinner = getAvailableColorsForThirdSpinner(firstColor, secondColor)
                    setupSpinner(spinner3, listOf("Выберите цвет" to null) + availableColorsForThirdSpinner)
                    spinner3.isEnabled = true
                } else {
                    spinner3.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<Pair<String, Int?>>) {
        val adapter = ColorSpinnerAdapter(this, items)
        spinner.adapter = adapter
    }

    private fun getAvailableColorsForThirdSpinner(firstColor: String, secondColor: String): List<Pair<String, Int?>> {
        val firstColorOptions = colorCombinations[firstColor] ?: emptyList()
        val secondColorOptions = colorCombinations[secondColor] ?: emptyList()
        val intersection = firstColorOptions.intersect(secondColorOptions).toList()
        return intersection.mapNotNull { colorName ->
            allColors.find { it.first == colorName }
        }
    }

    private fun setupGenerateButton() {
        generateButton.setOnClickListener {
            val color1 = (spinner1.selectedItem as? Pair<String, Int?>)?.first
            val color2 = (spinner2.selectedItem as? Pair<String, Int?>)?.first
            val color3 = (spinner3.selectedItem as? Pair<String, Int?>)?.first

            if (color1 != null && color2 != null && color3 != null &&
                color1 != "Выберите цвет" && color2 != "Выберите цвет" && color3 != "Выберите цвет") {
                generateImageFromColors(color1, color2, color3)
            } else {
                Toast.makeText(this, "Пожалуйста, выберите все три цвета", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateImageFromColors(color1: String, color2: String, color3: String) {
        val prompt = "Generate a realistic image of a men's outfit with a $color1 jacket, $color2 shirt, and $color3 trousers."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = ImageRequest(prompt)
                Log.d("API_REQUEST", "Request: $request")
                val response = ApiClient.api.generateImage(request)
                Log.d("API_RESPONSE", "Response: $response")

                withContext(Dispatchers.Main) {
                    if (response.data.isNotEmpty()) {
                        Log.d("IMAGE_URL", "Loading image from URL: ${response.data[0].url}")
                        Glide.with(this@MainActivity)
                            .load(response.data[0].url)
                            .into(imageView)
                    } else {
                        Log.e("API_ERROR", "Response data is empty")
                        Toast.makeText(this@MainActivity, "Не удалось получить URL изображения", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                val errorBody = (e as? retrofit2.HttpException)?.response()?.errorBody()?.string()
                Log.e("API_ERROR", "Error body: $errorBody")

                withContext(Dispatchers.Main) {
                    when {
                        errorBody?.contains("billing_hard_limit_reached") == true -> {
                            Toast.makeText(this@MainActivity, "Достигнут лимит использования API. Пожалуйста, попробуйте позже.", Toast.LENGTH_LONG).show()
                        }
                        errorBody != null -> {
                            try {
                                val errorJson = JSONObject(errorBody)
                                val errorMessage = errorJson.optJSONObject("error")?.optString("message")
                                Toast.makeText(this@MainActivity, "Ошибка API: $errorMessage", Toast.LENGTH_LONG).show()
                            } catch (jsonException: Exception) {
                                Toast.makeText(this@MainActivity, "Ошибка при генерации изображения: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Ошибка при генерации изображения: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
        }
    }
}