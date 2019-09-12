package wowo.kjt.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import wowo.kjt.banner.MyScroller
import wowo.kjt.library.transform.AngleGalleryPageTransform
import wowo.kjt.library.transform.GalleryPageTransform
import java.io.File
import java.lang.reflect.Field
import java.util.*


/**
 * Created by kjt on 2019-06-21
 */
class GalleryBanner : FrameLayout {

    companion object {
        const val BannerStyleAngle = 1
    }

    private val DOT_LIGHT_DEFAULT_COLOR = 0xFFD81B60.toInt()
    private val DOT_DARK_DEFAULT_COLOR = 0xFF000000.toInt()
    private val INDICATOR_BACKGROUND_DEFAULT_COLOR = 0x77ffffff

    //是否显示指示器
    private var mShowIndicator = true
    //指示器亮点颜色
    private var mDotLightColor: Int = DOT_LIGHT_DEFAULT_COLOR
    //指示器暗点颜色
    private var mDotDarkColor: Int = DOT_DARK_DEFAULT_COLOR
    //指示器背景颜色
    private var mIndicatorBackgroundColor = INDICATOR_BACKGROUND_DEFAULT_COLOR
    //轮播间隔时间
    private var mDuration = 3000L
    private var mLoadingImgId = 0
    private var mErrorImgId = 0
    private var mLoadingDrawable: Drawable
    private var mErrorDrawable: Drawable
    private var mUseLoadingDrawable = true
    private var mUseErrorDrawable = true
    private var mUserAngleGalleryStyle = false

    private val mDotLightDrawable: GradientDrawable by lazy {
        val drawable = GradientDrawable()
        drawable.apply {
            shape = GradientDrawable.OVAL
            setSize(dp2px(3), dp2px(3))
            setColor(mDotLightColor)
        }
        drawable
    }
    private val mDotDarkDrawable: GradientDrawable by lazy {
        val drawable = GradientDrawable()
        drawable.apply {
            shape = GradientDrawable.OVAL
            setSize(dp2px(3), dp2px(3))
            setColor(mDotDarkColor)
        }
        drawable
    }

    private lateinit var mAdapter: VpImagesAdapter
    private var mImgViews: ArrayList<ImageView> = arrayListOf()
    private val mViewPager = ViewPager(context)
    private val mFrameIndicators = FrameLayout(context)
    private val mLlIndicators = LinearLayout(context)
    private val mRedIndicator = ImageView(context)
    private var mInitialImgSize = 0
    private var mListener: onPageClickListener? = null
    private var mImgs: ArrayList<Any> = arrayListOf()
    var handlerr = Handler(Handler.Callback { msg ->
        mViewPager.currentItem = msg.what
        true
    })

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.GalleryBanner)
        mShowIndicator = ta.getBoolean(R.styleable.GalleryBanner_showIndicator, true)
        mDotLightColor = ta.getColor(R.styleable.GalleryBanner_dotLightColor, DOT_LIGHT_DEFAULT_COLOR)
        mDotDarkColor = ta.getColor(R.styleable.GalleryBanner_dotDarkColor, DOT_DARK_DEFAULT_COLOR)
        mIndicatorBackgroundColor =
            ta.getColor(R.styleable.GalleryBanner_indicatorBackgroundColor, INDICATOR_BACKGROUND_DEFAULT_COLOR)
        clipChildren = false
        mViewPager.clipChildren = false
        mLoadingDrawable = ColorDrawable(Color.LTGRAY)
        mErrorDrawable = mLoadingDrawable
        //add vp
        addView(mViewPager)
        ta.recycle()
    }

    fun startLoop() {
        handlerr.sendEmptyMessageDelayed(mViewPager.currentItem + 1, mDuration)
    }

    fun stopLoop() {
        handlerr.removeCallbacksAndMessages(null)
    }

    fun useAngleGalleryStyle(): GalleryBanner {
        mUserAngleGalleryStyle = true
        return this
    }

    fun setDuration(duration: Long): GalleryBanner {
        mDuration = duration
        return this
    }

    fun setLoadingImg(resId: Int): GalleryBanner {
        mUseLoadingDrawable = false
        mLoadingImgId = resId
        return this
    }

    fun setLoadingImg(drawable: Drawable): GalleryBanner {
        mUseLoadingDrawable = true
        mLoadingDrawable = drawable
        return this
    }

    fun setErrorImg(resId: Int): GalleryBanner {
        mUseErrorDrawable = false
        mErrorImgId = resId
        return this
    }

    fun setErrorImg(drawable: Drawable): GalleryBanner {
        mUseErrorDrawable = true
        mErrorDrawable = drawable
        return this
    }

    fun setOnPageClickListener(listener: onPageClickListener): GalleryBanner {
        mListener = listener
        return this
    }

    fun setDataFromRes(resIds: ArrayList<Int>): GalleryBanner {
        mImgs.addAll(resIds)
        return setData()
    }

    fun setDataFromUrl(urls: ArrayList<String>): GalleryBanner {
        mImgs.addAll(urls)
        return setData()
    }

    fun setDataFromFile(files: ArrayList<File>): GalleryBanner {
        mImgs.addAll(files)
        return setData()
    }

    private fun setData(): GalleryBanner {
        mImgs.apply {
            //图片列表初识大小
            mInitialImgSize = size
            add(0, last())
            add(0, get(lastIndex - 1))
            add(mImgs[2])
            add(mImgs[3])

            var img: ImageView
            val load = Glide.with(context).load("")
            if (mUseLoadingDrawable) load.placeholder(mLoadingDrawable) else load.placeholder(mLoadingImgId)
            if (mUseErrorDrawable) load.error(mErrorDrawable) else load.error(mErrorImgId)
            forEachIndexed { index, it ->
                img = ImageView(context)
                img.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                img.scaleType = ImageView.ScaleType.FIT_XY
                load.load(it).into(img)
                if (index in 2 until lastIndex - 1)
                    img.setOnClickListener { mListener?.onPageClick(index - 2) }
                mImgViews.add(img)
            }
        }
        setData2Vp()
        if (mShowIndicator)
            addIndicators()
        return this
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var heightVal = MeasureSpec.getSize(heightMeasureSpec)
        val widthVal = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.UNSPECIFIED or MeasureSpec.AT_MOST)
            heightVal = dp2px(200)
        setMeasuredDimension(widthVal, heightVal)

        mViewPager.layoutParams =
            LayoutParams(measuredWidth * 4 / 5, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL)

        /* Log.d("kkk", "fram indicators width is " + mFrameIndicators.measuredWidth)
         Log.d("kkk", "ll indicators width is " + mLlIndicators.measuredWidth)
         Log.d("kkk", "indicator width is " + mLlIndicators.getChildAt(0).measuredWidth)*/
    }

    private fun setData2Vp() {
        mAdapter = VpImagesAdapter(mImgViews)
        mViewPager.apply {
            adapter = mAdapter

            setPageTransformer(
                true,
                if (mUserAngleGalleryStyle) AngleGalleryPageTransform() else GalleryPageTransform()
            )
            overScrollMode = ViewPager.OVER_SCROLL_NEVER
            setVPScrollSpeed()
            offscreenPageLimit = mImgViews.size
            currentItem = 2

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                val lastIndex = mImgViews.lastIndex
                override fun onPageScrollStateChanged(p0: Int) {
                    if (p0 == ViewPager.SCROLL_STATE_DRAGGING) {
                        handlerr.removeCallbacksAndMessages(null)
                    } else if (p0 == ViewPager.SCROLL_STATE_IDLE) {
                        //当手滑动到我们后期添加的页面时，切换到原顺序对应图片
                        when (mViewPager.currentItem) {
                            0 -> mViewPager.setCurrentItem(lastIndex - 3, false)
                            1 -> mViewPager.setCurrentItem(lastIndex - 2, false)
                            lastIndex - 1 -> mViewPager.setCurrentItem(2, false)
                            lastIndex -> mViewPager.setCurrentItem(3, false)
                        }
                        handlerr.sendEmptyMessageDelayed(mViewPager.currentItem + 1, mDuration - 1000L)
                    }
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                    if (mShowIndicator && p1 != 0.toFloat()) {
//                        Log.d("kkk", "position is " + p0)
                        if (p0 > 1 && p0 < lastIndex - 2)
                            mRedIndicator.translationX =
                                (p1 * mRedIndicator.measuredWidth) + ((p0 - 2) * mRedIndicator.measuredWidth)
                        else if (p0 == lastIndex - 2 || p0 == 1)
                            mRedIndicator.translationX =
                                ((1 - p1) * (mFrameIndicators.measuredWidth - mLlIndicators.getChildAt(0).measuredWidth))
                    }
                }

                override fun onPageSelected(p0: Int) {
                }
            })
        }
    }

    private fun setVPScrollSpeed(duration: Long = 1000) {
        val field: Field = ViewPager::class.java.getDeclaredField("mScroller")
        field.isAccessible = true
        val scroller = MyScroller(context)
        scroller.scrollDuration = duration
        field.set(mViewPager, scroller)
    }

    private fun addIndicators() {
        //add indicators frame
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        )
        params.bottomMargin = dp2px(10)
        mFrameIndicators.layoutParams = params
        val bgDrawable = GradientDrawable()
        bgDrawable.apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 30F
            setColor(mIndicatorBackgroundColor)
        }
        mFrameIndicators.background = bgDrawable
        addView(mFrameIndicators)
        //add indicators ll in frame
        val padding = dp2px(8)
        mLlIndicators.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mLlIndicators.orientation = LinearLayout.HORIZONTAL
        var indicator: ImageView
        for (i in 0 until mInitialImgSize) {
            indicator = ImageView(context)
            indicator.setImageDrawable(mDotDarkDrawable)
            indicator.setPadding(padding, padding, padding, padding)
            indicator.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            mLlIndicators.addView(indicator)
        }
        mFrameIndicators.addView(mLlIndicators)
        //add red indicator in frame
        mRedIndicator.setImageDrawable(mDotLightDrawable)
        mRedIndicator.setPadding(padding, padding, padding, padding)
        mRedIndicator.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mFrameIndicators.addView(mRedIndicator)
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
    }
}