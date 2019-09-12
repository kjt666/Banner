package wowo.kjt.library.transform

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by kjt on 2019-06-18
 */
private const val MIN_SCALE_Y = 0.55f
private const val MIN_SCALE_X = 0.75f
private const val MIN_ALPHA = 0.7f

class AngleGalleryPageTransform : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < -1 -> { // [-∞,-1)
                    // 屏幕左边的view
                    rotationY = -35f
                    alpha = MIN_ALPHA
                    scaleX = MIN_SCALE_X
                    scaleY = MIN_SCALE_Y
                }
                position <= 1 -> { // [-1,1]
                    //X/Y方向上的缩放比例，跟随下标变化，介于分别的最小值和1之间
                    val scaleFactorY = (MIN_SCALE_Y + (1 - MIN_SCALE_Y) * (1 - Math.abs(position)))
                    val scaleFactorX = (MIN_SCALE_X + (1 - MIN_SCALE_X) * (1 - Math.abs(position)))
                    scaleX = scaleFactorX
                    scaleY = scaleFactorY
                    rotationY = 35 * position
                    // 随着下标变化的透明度
                    alpha = (MIN_ALPHA + ((1 - Math.abs(position)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+∞]
                    // 屏幕右边的view
                    rotationY = 35f
                    alpha = MIN_ALPHA
                    scaleX = MIN_SCALE_X
                    scaleY = MIN_SCALE_Y
                }
            }
        }
    }
}