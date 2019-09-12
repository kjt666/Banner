# Banner
自动轮播控件、无限循环，支持画廊等多种样式，动画型指示器。

使用
------
要将Git项目放入您的构建中：

步骤1.将JitPack存储库添加到构建文件中

将其添加到存储库末尾的根build.gradle中：
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
步骤2.添加依赖项
```
dependencies {
	        implementation 'com.github.kjt666:Banner:v1.0'
	}
  ```
  
演示
------
![](https://github.com/kjt666/Images/blob/master/banner.gif)
```
<wowo.kjt.library.GalleryBanner
            android:id="@+id/gallery_banner"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:layout_marginTop="50dp"
            app:dotLightColor="#03A9F4"
            app:showIndicator="true"/>

    <wowo.kjt.library.Banner
            android:id="@+id/banner"
            android:layout_marginTop="50dp"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            app:dotLightColor="#FF0000"
            app:indicator_gravity="right"/>
```

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        banner
            .setBannerStyle(Banner.BannerStyleDepth)
            .setOnPageClickListener(object : onPageClickListener {
                override fun onPageClick(index: Int) {
                    Toast.makeText(this@MainActivity, index.toString(), Toast.LENGTH_SHORT).show()
                }
            })
            .setDataFromRes(mImgIdRes)
            .startLoop()
        gallery_banner
            .setDuration(2000)
            .setOnPageClickListener(object :onPageClickListener{
                override fun onPageClick(index: Int) {
                    Toast.makeText(this@MainActivity, index.toString(), Toast.LENGTH_SHORT).show()
                }
            })
            .useAngleGalleryStyle()
            .setDataFromUrl(mImgIdUrls)
            .startLoop()

    }

    override fun onRestart() {
        super.onRestart()
        banner.startLoop()
        gallery_banner.startLoop()
    }

    override fun onStop() {
        super.onStop()
        banner.stopLoop()
        gallery_banner.stopLoop()
    }
```
属性说明
-------
通用属性|说明
-------- | :-----------:
showIndicator|是否展示指示器 
dotLightColor|指示器亮点颜色
dotDarkColor|指示器暗点颜色
indicatorBackgroundColor|指示器背景颜色

Banner属性|说明
-------- | :-----------:
indicator_gravity|指示器位置

方法说明
-------

通用方法|说明
-------- | :-----------
setDuration(duration: Long) | 设置轮播切换等待时间
setLoadingImg(resId: Int)|设置轮播图加载时显示的图片资源
setLoadingImg(drawable: Drawable)|设置轮播图加载时显示的Drawable
setErrorImg(resId: Int)|设置轮播图加载出错时显示的图片资源
setErrorImg(drawable: Drawable)|设置轮播图加载出错时显示的Drawable
setOnPageClickListener(listener: onPageClickListener)|设置轮播图点击事件
setDataFromRes(resIds: ArrayList<Int>)|设置轮播图片加载本地资源
setDataFromFile(files: ArrayList<File>)|设置轮播图片加载本地资源
setDataFromUrl(urls: ArrayList<String>)|设置轮播图片加载网络资源
startLoop()|开启图片轮播
stopLoop()|关闭图片轮播

Banner方法|说明
-------- | :-----------
setBannerStyle(bannerStyle: Int)|设置轮播器样式，样式由Banner提供
setBannerStyle(transformer: ViewPager.PageTransformer)|设置自定义轮播器样式

GalleryBanner方法|说明
-------- | :-----------
useAngleGalleryStyle()|使用带角度的轮播器画廊样式

