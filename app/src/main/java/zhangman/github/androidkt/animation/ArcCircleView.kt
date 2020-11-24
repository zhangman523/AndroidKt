package zhangman.github.androidkt.animation

import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator

/**
 * Created by admin on 2020/11/23 15:23.
 * Email: zhangman523@126.com
 */
class ArcCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var mArcPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var x: Int = 0
    var y: Int = 0
    var mRadius = 1920
    var degree = 0f

    init {
        mArcPaint.strokeWidth = 0f
        mArcPaint.style = Paint.Style.FILL_AND_STROKE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        var height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        x = width / 2
        y = height / 2
        super.onMeasure(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var rect = RectF(
            (x - mRadius).toFloat(), (y - mRadius).toFloat(), (x + mRadius).toFloat(),
            (y + mRadius).toFloat()
        )
        mArcPaint.isAntiAlias = true
//        mArcPaint.isFilterBitmap = true
        mArcPaint.color=Color.parseColor("#494949")
        canvas?.drawCircle(x.toFloat(), y.toFloat(), mRadius.toFloat(), mArcPaint)
        var gapAngle = 6f
        for (i in 0..59) {
            if (i % 2 == 0) {
                continue
            }
            drawArcByAngle(
                canvas,
                rect,
                i * gapAngle + degree,
                gapAngle,
                if (i % 2 == 0) Color.parseColor("#494949") else Color.parseColor("#3d3d3d")
            )
        }
    }

    fun drawArcByAngle(
        canvas: Canvas?,
        rectF: RectF,
        startAngle: Float,
        sweepAngle: Float,
        color: Int
    ) {
        mArcPaint.color = color
        canvas?.drawArc(rectF, startAngle, sweepAngle, true, mArcPaint)
    }

    fun startAnimation() {
        degree = 0f
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.setDuration(24000).start()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            degree = animation.animatedValue as Float
            invalidate()
        }
    }
}