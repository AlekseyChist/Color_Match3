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

    suspend fun replaceColorsInDrawable(
        context: Context,
        drawableResource: Int,
        firstColor: Int,
        secondColor: Int,
        thirdColor: Int
    ): Bitmap = withContext(Dispatchers.Default) {
        val drawable = ContextCompat.getDrawable(context, drawableResource)!!
        val bitmap = getBitmapFromDrawable(drawable)

        val pixelColors = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixelColors, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val whiteColor2 = ContextCompat.getColor(context, R.color.jacket)
        val whiteColor3 = ContextCompat.getColor(context, R.color.pullover)
        val whiteColor = ContextCompat.getColor(context, R.color.pants)

        for (i in pixelColors.indices) {
            pixelColors[i] = when (pixelColors[i]) {
                whiteColor2 -> firstColor
                whiteColor3 -> secondColor
                whiteColor -> thirdColor
                else -> pixelColors[i]
            }
        }

        val modifiedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        modifiedBitmap.setPixels(pixelColors, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        modifiedBitmap
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
}