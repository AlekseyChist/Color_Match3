package com.example.color_match3

class ColorData {

    val allColors: List<Pair<String, Int?>> = listOf(
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

    val colorCombinations: Map<String, List<String>> = mapOf(
        "Black" to listOf(
            "White",
            "Gray",
            "Burgundy",
            "Olive Green",
            "Beige",
            "Khaki",
            "Sand",
            "Eggplant"
        ),
        "White" to listOf(
            "Black",
            "Gray",
            "Navy Blue",
            "Brown",
            "Beige",
            "Olive Green",
            "Khaki",
            "Indigo"
        ),
        "Gray" to listOf(
            "Black",
            "White",
            "Navy Blue",
            "Burgundy",
            "Brown",
            "Olive Green",
            "Indigo",
            "Eggplant"
        ),
        "Navy Blue" to listOf(
            "White",
            "Gray",
            "Brown",
            "Beige",
            "Khaki",
            "Burgundy",
            "Olive Green"
        ),
        "Brown" to listOf("Beige", "White", "Gray", "Olive Green", "Navy Blue", "Khaki", "Sand"),
        "Beige" to listOf(
            "Brown",
            "Navy Blue",
            "Olive Green",
            "Burgundy",
            "White",
            "Khaki",
            "Sand"
        ),
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

    fun getAvailableColorsForSecondSpinner(selectedColor: String): List<Pair<String, Int?>> {
        return if (selectedColor != "Выберите цвет") {
            colorCombinations[selectedColor]?.mapNotNull { colorName ->
                allColors.find { it.first == colorName }
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun getAvailableColorsForThirdSpinner(
        firstColor: String,
        secondColor: String
    ): List<Pair<String, Int?>> {
        val firstColorOptions = colorCombinations[firstColor] ?: emptyList()
        val secondColorOptions = colorCombinations[secondColor] ?: emptyList()
        val intersection = firstColorOptions.intersect(secondColorOptions).toList()
        return intersection.mapNotNull { colorName ->
            allColors.find { it.first == colorName }
        }
    }
}