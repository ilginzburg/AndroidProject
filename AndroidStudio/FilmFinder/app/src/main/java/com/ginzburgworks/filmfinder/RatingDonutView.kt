package com.ginzburgworks.filmfinder

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

private const val RADIUS_FACTOR = 0.8f
private const val START_ANGLE = -90f
private const val DEFAULT_SIZE = 300
private const val TO_DEGREES_FACTOR = 3.6f

private const val LOW_FROM = 0
private const val LOW_TO = 25
private const val MID_LOW_FROM = 26
private const val MID_LOW_TO = 50
private const val MID_HIGH_FROM = 51
private const val MID_HIGH_TO = 75

private const val ANIM_ROTATION_DURATION : Long = 400
private const val ANIM_REPEAT_COUNT = 3

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

    private  var lowColor = "#e84258"
    private  var midLowColor = "#fd8060"
    private  var midHighColor = "#fee191"
    private  var highColor = "#b0d8a4"

    private val animRotation = ObjectAnimator.ofFloat(this, View.ROTATION, 360F)

    init {

        val a =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingDonutView, 0, 0)
        try {
            stroke = a.getFloat(R.styleable.RatingDonutView_stroke, stroke )
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
            lowColor = (a.getString(R.styleable.RatingDonutView_low_color)?:lowColor).toString()
            midLowColor = (a.getString(R.styleable.RatingDonutView_mid_low_color)?:midLowColor).toString()
            midHighColor = (a.getString(R.styleable.RatingDonutView_mid_high_color)?:midHighColor).toString()
            highColor = (a.getString(R.styleable.RatingDonutView_high_color)?:highColor).toString()

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
        in LOW_FROM..LOW_TO -> Color.parseColor(lowColor)
        in MID_LOW_FROM..MID_LOW_TO -> Color.parseColor(midLowColor)
        in MID_HIGH_FROM..MID_HIGH_TO -> Color.parseColor(midHighColor)
        else -> Color.parseColor(highColor)
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
            else -> DEFAULT_SIZE
        }



    private fun drawRating(canvas: Canvas) {
        val scale = radius * RADIUS_FACTOR
        canvas.save()
        canvas.translate(centerX, centerY)
        oval.set(0f - scale, 0f - scale, scale, scale)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        canvas.drawArc(oval, START_ANGLE, convertProgressToDegrees(progress), false, strokePaint)
        canvas.restore()
        animateRating()
    }

    private fun animateRating(){
        animRotation.duration = ANIM_ROTATION_DURATION
        animRotation.repeatMode = ObjectAnimator.RESTART
        animRotation.repeatCount = ANIM_REPEAT_COUNT
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

    private fun convertProgressToDegrees(progress: Int): Float = progress * TO_DEGREES_FACTOR


    override fun onDraw(canvas: Canvas) {
        drawRating(canvas)
        drawText(canvas)
    }
}