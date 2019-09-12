package wowo.kjt.library.transform

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by kjt on 2019-06-18
 */
private const val MIN_SCALE = 0.75f

class DashTransform : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-∞,-1)
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0] 移动到左侧的view
                    alpha = 1f - Math.abs(position)
                    scaleX = 1f
                    scaleY = 1f
//                    rotation = 30 * position
                }
                position <= 1 -> { // (0,1] 从右侧移动过来的view
                    alpha = 1 - position
                    // 抵消掉原来的位移，让view固定住
                    translationX = pageWidth * -position
                    // 缩放渐变大小
                    val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+∞]
                    alpha = 0f
                }
            }
        }
    }
}