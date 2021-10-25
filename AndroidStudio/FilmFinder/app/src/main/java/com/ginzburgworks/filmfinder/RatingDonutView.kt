package com.ginzburgworks.filmfinder

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

const val radiusFactor = 0.8f
const val startAngle = -90f
const val defSize = 300
const val toDegreesFactor = 3.6f

const val redMin = 0
const val redMax = 25
const val orangeMin = 26
const val orangeMax = 50
const val yellowMin = 51
const val yellowMax = 75

class RatingDonutView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val oval = RectF()
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var stroke = 10f
    private var progress = 50
    private var scaleSize = 60f
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint
    private val animRotation = ObjectAnimator.ofFloat(this, View.ROTATION, 360F)

    init {
        val a =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingDonutView, 0, 0)
        try {
            stroke = a.getFloat(
                R.styleable.RatingDonutView_stroke, stroke
            )
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
        } finally {
            a.recycle()
        }
        initPaint()
    }

    fun setProgress(pr: Int) {
        progress = pr
        initPaint()
        invalidate()
    }

    private fun initPaint() {
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }


    private fun getPaintColor(progress: Int): Int = when (progress) {
        in redMin..redMax -> Color.parseColor("#e84258")
        in orangeMin..orangeMax -> Color.parseColor("#fd8060")
        in yellowMin..yellowMax -> Color.parseColor("#fee191")
        else -> Color.parseColor("#b0d8a4")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> defSize
        }



    private fun drawRating(canvas: Canvas) {
        val scale = radius * radiusFactor
        canvas.save()
        canvas.translate(centerX, centerY)
        oval.set(0f - scale, 0f - scale, scale, scale)
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        canvas.drawArc(oval, startAngle, convertProgressToDegrees(progress), false, strokePaint)
        canvas.restore()
        animateRating()
    }

    private fun animateRating(){
        animRotation.duration = 400
        animRotation.repeatMode = ObjectAnimator.RESTART
        animRotation.repeatCount = 3
        animRotation.start()
    }

    private fun drawText(canvas: Canvas) {
        val message = String.format("%.1f", progress / 10f)
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        canvas.drawText(message, centerX - advance / 2, centerY + advance / 4, digitPaint)
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * toDegreesFactor



    override fun onDraw(canvas: Canvas) {
        drawRating(canvas)
        drawText(canvas)
    }
}