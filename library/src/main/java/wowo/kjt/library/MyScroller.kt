package wowo.kjt.banner

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * Created by kjt on 2019-06-21
 */
class MyScroller:Scroller {

    var scrollDuration : Long = 0

    constructor(context: Context):super(context)

    constructor(context: Context,interpolator: Interpolator):super(context,interpolator)

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration.toInt())
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration.toInt())
    }

}