import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.color_match3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ColorReplacementManager {

    fun replaceColorsInDrawableAsync(
        appContext: Context, // Переименовал переменную, чтобы избежать конфликта
        drawableResource: Int,
        firstColor: Int,
        secondColor: Int,
        thirdColor: Int,
        onComplete: (Bitmap) -> Unit
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val drawable = ContextCompat.getDrawable(appContext, drawableResource)!!
            val bitmap = getBitmapFromDrawable(drawable)

            // Выполняем замену цветов в фоновом потоке
            val modifiedBitmap = replaceColorsInBitmap(appContext, bitmap, firstColor, secondColor, thirdColor)

            // Возвращаем результат на главный поток
            withContext(Dispatchers.Main) {
                onComplete(modifiedBitmap)
            }
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun replaceColorsInBitmap(
        context: Context, // Используем правильный тип контекста
        bitmap: Bitmap,
        firstColor: Int,
        secondColor: Int,
        thirdColor: Int
    ): Bitmap {
        val modifiedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(modifiedBitmap)
        val paint = Paint()

        // Проходим по каждому пикселю и заменяем нужные цвета
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                paint.color = when (pixel) {
                    ContextCompat.getColor(context, R.color.white2) -> firstColor
                    ContextCompat.getColor(context, R.color.white3) -> secondColor
                    ContextCompat.getColor(context, R.color.white) -> thirdColor
                    else -> pixel
                }
                canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
            }
        }
        return modifiedBitmap
    }
}