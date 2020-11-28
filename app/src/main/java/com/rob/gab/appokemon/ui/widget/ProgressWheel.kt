package com.vit.ant.pokemon.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.rob.gab.appokemon.R


class ProgressWheel(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    //Sizes (with defaults)
    private var layoutHeight = 0
    private var layoutWidth = 0
    private var fullRadius = 100
    private var circleRadius = 80
    private var barLength = 60
    private var barWidth = 20
    private var rimWidth = 20
    private var textSize = 20
    private var contourSize = 0f
    private var autoStart = false

    //Padding (with defaults)
    private var paddingTop = 5
    private var paddingBottom = 5
    private var paddingLeft = 5
    private var paddingRight = 5

    //Colors (with defaults)
    private var barColor = -0x56000000
    private var contourColor = -0x56000000
    private var circleColor = 0x00000000
    private var rimColor = -0x55222223
    private var textColor = -0x1000000

    //Paints
    private val barPaint: Paint? = Paint()
    private val circlePaint: Paint? = Paint()
    private val rimPaint: Paint? = Paint()
    private val textPaint: Paint? = Paint()
    private val contourPaint: Paint? = Paint()

    //Rectangles
    private var innerCircleBounds = RectF()
    private var circleBounds = RectF()
    private var circleOuterContour = RectF()
    private var circleInnerContour = RectF()

    //Animation
    //The amount of pixels to move the bar by on each draw
    private var spinSpeed = 2f

    //The number of milliseconds to wait in between each draw
    private var delayMillis = 10
    private var progress = 0f
    var isWheelSpinning = false

    //Other
    private var text: String? = ""
    private var splitText = arrayOf<String>()


    init {
        parseAttributes(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.ProgressWheel
            )
        )
    }

    /*
     * When this is called, make the view square.
     * From: http://www.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // The first thing that happen is that we call the superclass
        // implementation of onMeasure. The reason for that is that measuring
        // can be quite a complex process and calling the super method is a
        // convenient way to get most of this complexity handled.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // We can’t use getWidth() or getHight() here. During the measuring
        // pass the view has not gotten its final size yet (this happens first
        // at the start of the layout pass) so we have to use getMeasuredWidth()
        // and getMeasuredHeight().
        val width = measuredWidth
        val height = measuredHeight
        val widthWithoutPadding = width - getPaddingLeft() - getPaddingRight()
        val heightWithoutPadding = height - getPaddingTop() - getPaddingBottom()

        // Finally we have some simple logic that calculates the size of the view
        // and calls setMeasuredDimension() to set that size.
        // Before we compare the width and height of the view, we remove the padding,
        // and when we set the dimension we add it back again. Now the actual content
        // of the view will be square, but, depending on the padding, the total dimensions
        // of the view might not be.
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val size = if (heightMode != MeasureSpec.UNSPECIFIED && widthMode != MeasureSpec.UNSPECIFIED) {
            if (widthWithoutPadding > heightWithoutPadding) {
                heightWithoutPadding
            } else {
                widthWithoutPadding
            }
        } else {
            Math.max(heightWithoutPadding, widthWithoutPadding)
        }


        // If you override onMeasure() you have to call setMeasuredDimension().
        // This is how you report back the measured size.  If you don’t call
        // setMeasuredDimension() the parent will throw an exception and your
        // application will crash.
        // We are calling the onMeasure() method of the superclass so we don’t
        // actually need to call setMeasuredDimension() since that takes care
        // of that. However, the purpose with overriding onMeasure() was to
        // change the default behaviour and to do that we need to call
        // setMeasuredDimension() with our own values.
        setMeasuredDimension(
            size + getPaddingLeft() + getPaddingRight(),
            size + getPaddingTop() + getPaddingBottom()
        )
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    override fun onSizeChanged(
        newWidth: Int,
        newHeight: Int,
        oldWidth: Int,
        oldHeight: Int
    ) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        layoutWidth = newWidth
        layoutHeight = newHeight
        setupBounds()
        setupPaints()
        invalidate()
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private fun setupPaints() {
        barPaint!!.color = barColor
        barPaint.isAntiAlias = true
        barPaint.style = Paint.Style.STROKE
        barPaint.strokeWidth = barWidth.toFloat()
        barPaint.isDither = true
        barPaint.strokeJoin = Paint.Join.ROUND
        barPaint.strokeCap = Paint.Cap.ROUND
        barPaint.pathEffect = CornerPathEffect(10f)
        rimPaint!!.color = rimColor
        rimPaint.isAntiAlias = true
        rimPaint.style = Paint.Style.STROKE
        rimPaint.strokeWidth = rimWidth.toFloat()
        circlePaint!!.color = circleColor
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.FILL
        textPaint!!.color = textColor
        textPaint.style = Paint.Style.FILL
        textPaint.isAntiAlias = true
        textPaint.textSize = textSize.toFloat()
        contourPaint!!.color = contourColor
        contourPaint.isAntiAlias = true
        contourPaint.style = Paint.Style.STROKE
        contourPaint.strokeWidth = contourSize
    }

    /**
     * Set the bounds of the component
     */
    private fun setupBounds() {
        // Width should equal to Height, find the min value to setup the circle
        val minValue = Math.min(layoutWidth, layoutHeight)

        // Calc the Offset if needed
        val xOffset = layoutWidth - minValue
        val yOffset = layoutHeight - minValue

        // Add the offset
        paddingTop = getPaddingTop() + yOffset / 2
        paddingBottom = getPaddingBottom() + yOffset / 2
        paddingLeft = getPaddingLeft() + xOffset / 2
        paddingRight = getPaddingRight() + xOffset / 2
        val width = width
        val height = height
        innerCircleBounds = RectF(
            paddingLeft + 1.5f * barWidth,
            paddingTop + 1.5f * barWidth,
            width - paddingRight - 1.5f * barWidth,
            height - paddingBottom - 1.5f * barWidth
        )
        circleBounds = RectF(
            (paddingLeft + barWidth).toFloat(),
            (paddingTop + barWidth).toFloat(),
            (width - paddingRight - barWidth).toFloat(),
            (height - paddingBottom - barWidth).toFloat()
        )
        circleInnerContour = RectF(
            circleBounds.left + rimWidth / 2.0f + contourSize / 2.0f,
            circleBounds.top + rimWidth / 2.0f + contourSize / 2.0f,
            circleBounds.right - rimWidth / 2.0f - contourSize / 2.0f,
            circleBounds.bottom - rimWidth / 2.0f - contourSize / 2.0f
        )
        circleOuterContour = RectF(
            circleBounds.left - rimWidth / 2.0f - contourSize / 2.0f,
            circleBounds.top - rimWidth / 2.0f - contourSize / 2.0f,
            circleBounds.right + rimWidth / 2.0f + contourSize / 2.0f,
            circleBounds.bottom + rimWidth / 2.0f + contourSize / 2.0f
        )
        fullRadius = (width - paddingRight - barWidth) / 2
        circleRadius = fullRadius - barWidth + 1
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private fun parseAttributes(a: TypedArray) {
        barWidth = a.getDimension(R.styleable.ProgressWheel_pwBarWidth, barWidth.toFloat()).toInt()
        rimWidth = a.getDimension(R.styleable.ProgressWheel_pwRimWidth, rimWidth.toFloat()).toInt()
        spinSpeed = a.getDimension(R.styleable.ProgressWheel_pwSpinSpeed, spinSpeed)
        barLength = a.getDimension(R.styleable.ProgressWheel_pwBarLength, barLength.toFloat()).toInt()
        delayMillis = a.getInteger(R.styleable.ProgressWheel_pwDelayMillis, delayMillis)
        if (delayMillis < 0) {
            delayMillis = 10
        }

        // Only set the text if it is explicitly defined
        if (a.hasValue(R.styleable.ProgressWheel_pwText)) {
            setText(a.getString(R.styleable.ProgressWheel_pwText))
        }
        barColor = a.getColor(R.styleable.ProgressWheel_pwBarColor, barColor)
        textColor = a.getColor(R.styleable.ProgressWheel_pwTextColor, textColor)
        rimColor = a.getColor(R.styleable.ProgressWheel_pwRimColor, rimColor)
        circleColor = a.getColor(R.styleable.ProgressWheel_pwCircleColor, circleColor)
        contourColor = a.getColor(R.styleable.ProgressWheel_pwContourColor, contourColor)
        textSize = a.getDimension(R.styleable.ProgressWheel_pwTextSize, textSize.toFloat()).toInt()
        contourSize = a.getDimension(R.styleable.ProgressWheel_pwContourSize, contourSize)
        autoStart = a.getBoolean(R.styleable.ProgressWheel_pwAutoStart, false)
        a.recycle()
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Draw the inner circle
        canvas.drawArc(innerCircleBounds, 360f, 360f, false, circlePaint!!)
        //Draw the rim
        canvas.drawArc(circleBounds, 360f, 360f, false, rimPaint!!)
        canvas.drawArc(circleOuterContour, 360f, 360f, false, contourPaint!!)
        //canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //Draw the bar
        if (isWheelSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength.toFloat(), false, barPaint!!)
        } else {
            canvas.drawArc(circleBounds, -90f, progress, false, barPaint!!)
        }
        //Draw the text (attempts to center it horizontally and vertically)
        val textHeight = textPaint!!.descent() - textPaint.ascent()
        val verticalTextOffset = textHeight / 2 - textPaint.descent()
        for (line in splitText) {
            val horizontalTextOffset = textPaint.measureText(line) / 2
            canvas.drawText(
                line,
                this.width / 2 - horizontalTextOffset,
                this.height / 2 + verticalTextOffset,
                textPaint
            )
        }
        if (isWheelSpinning) {
            scheduleRedraw()
        }
    }

    private fun scheduleRedraw() {
        progress += spinSpeed
        if (progress > 360) {
            progress = 0f
        }
        postInvalidateDelayed(delayMillis.toLong())
    }

    /**
     * Check if the wheel is currently spinning
     */
    fun isSpinning(): Boolean {
        return isWheelSpinning
    }

    /**
     * Reset the count (in increment mode)
     */
    fun resetCount() {
        progress = 0f
        setText("0%")
        invalidate()
    }

    /**
     * Turn off startSpinning mode
     */
    fun stopSpinning() {
        isWheelSpinning = false
        progress = 0f
        postInvalidate()
    }

    /**
     * Puts the view on spin mode
     */
    fun startSpinning() {
        isWheelSpinning = true
        postInvalidate()
    }

    /**
     * Increment the progress by 1 (of 360)
     */
//    @JvmOverloads
    fun incrementProgress(amount: Int = 1) {
        isWheelSpinning = false
        progress += amount.toFloat()
        if (progress > 360) progress %= 360f
        postInvalidate()
    }

    /**
     * Set the progress to a specific value
     */
    fun setProgress(i: Int) {
        isWheelSpinning = false
        progress = i.toFloat()
        postInvalidate()
    }
    //----------------------------------
    //Getters + setters
    //----------------------------------
    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    fun setText(text: String?) {
        this.text = text
        splitText = this.text!!.split("\n").toTypedArray()
    }

    fun getCircleRadius(): Int {
        return circleRadius
    }

    fun setCircleRadius(circleRadius: Int) {
        this.circleRadius = circleRadius
    }

    fun getBarLength(): Int {
        return barLength
    }

    fun setBarLength(barLength: Int) {
        this.barLength = barLength
    }

    fun getBarWidth(): Int {
        return barWidth
    }

    fun setBarWidth(barWidth: Int) {
        this.barWidth = barWidth
        if (barPaint != null) {
            barPaint.strokeWidth = this.barWidth.toFloat()
        }
    }

    fun getTextSize(): Int {
        return textSize
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        if (textPaint != null) {
            textPaint.textSize = this.textSize.toFloat()
        }
    }

    override fun getPaddingTop(): Int {
        return paddingTop
    }

    fun setPaddingTop(paddingTop: Int) {
        this.paddingTop = paddingTop
    }

    override fun getPaddingBottom(): Int {
        return paddingBottom
    }

    fun setPaddingBottom(paddingBottom: Int) {
        this.paddingBottom = paddingBottom
    }

    override fun getPaddingLeft(): Int {
        return paddingLeft
    }

    fun setPaddingLeft(paddingLeft: Int) {
        this.paddingLeft = paddingLeft
    }

    override fun getPaddingRight(): Int {
        return paddingRight
    }

    fun setPaddingRight(paddingRight: Int) {
        this.paddingRight = paddingRight
    }

    fun getBarColor(): Int {
        return barColor
    }

    fun setBarColor(barColor: Int) {
        this.barColor = barColor
        if (barPaint != null) {
            barPaint.color = this.barColor
        }
    }

    fun getCircleColor(): Int {
        return circleColor
    }

    fun setCircleColor(circleColor: Int) {
        this.circleColor = circleColor
        if (circlePaint != null) {
            circlePaint.color = this.circleColor
        }
    }

    fun getRimColor(): Int {
        return rimColor
    }

    fun setRimColor(rimColor: Int) {
        this.rimColor = rimColor
        if (rimPaint != null) {
            rimPaint.color = this.rimColor
        }
    }

    fun getRimShader(): Shader {
        return rimPaint!!.shader
    }

    fun setRimShader(shader: Shader?) {
        rimPaint!!.shader = shader
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        if (textPaint != null) {
            textPaint.color = this.textColor
        }
    }

    fun getSpinSpeed(): Float {
        return spinSpeed
    }

    fun setSpinSpeed(spinSpeed: Float) {
        this.spinSpeed = spinSpeed
    }

    fun getRimWidth(): Int {
        return rimWidth
    }

    fun setRimWidth(rimWidth: Int) {
        this.rimWidth = rimWidth
        if (rimPaint != null) {
            rimPaint.strokeWidth = this.rimWidth.toFloat()
        }
    }

    fun getDelayMillis(): Int {
        return delayMillis
    }

    fun setDelayMillis(delayMillis: Int) {
        this.delayMillis = delayMillis
    }

    fun getContourColor(): Int {
        return contourColor
    }

    fun setContourColor(contourColor: Int) {
        this.contourColor = contourColor
        if (contourPaint != null) {
            contourPaint.color = this.contourColor
        }
    }

    fun getContourSize(): Float {
        return contourSize
    }

    fun setContourSize(contourSize: Float) {
        this.contourSize = contourSize
        if (contourPaint != null) {
            contourPaint.strokeWidth = this.contourSize
        }
    }

    fun getProgress(): Int {
        return progress.toInt()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (autoStart) {
            startSpinning()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isWheelSpinning) {
            stopSpinning()
        }
    }

}
