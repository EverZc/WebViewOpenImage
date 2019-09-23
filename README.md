# WebViewOpenImage

[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

## 新框架发布，欢迎大家Star

通常在社交类型的APP上都会有这么一个需求，加载图文混排的文章，点击图片可以查看详情，左滑或者右滑动可以查看上一个图片或者下一个图片。
针对这一需求设计了一款相关的控件WebViewOpenImage。

本框架无需处理复杂的逻辑，只需要简单调用即可，本框架图片选择使用[WebViewOpenImage](https://github.com/EverZc/WebViewOpenImage)。


## 效果图


![效果示例](https://upload-images.jianshu.io/upload_images/4677908-78fefb287880dfe1.gif?imageMogr2/auto-orient/strip)


## 方法
|方法名|描述|
|---|---|
|delegation(Context context)|创建方法
|show(String hint)|弹出评论框并填写评论的hint
|dismiss()|隐藏评论弹出框，并隐藏软键盘
|setImages(ArrayList<ImageFile> images)|添加图片
|getAdapterData()|获取当前评论框内的图片
|getAdapter()|获取弹出框图片的adapter
|getCommentText()|获取评论的内容
|clear()|清理评论文本内容以及评论的图片内容
|appendText(String text)|拼接评论文字

## 使用步骤

#### Step 1.添加依赖WebViewOpenImage
首先要在项目的根`build.gradle`下添加：
```
allprojects {
	repositories {
        maven { url "https://jitpack.io" }
    }
}
```
然后要在要依赖的module中添加
```
dependencies {
    implementation 'com.github.EverZc:WebViewOpenImage:latest.release.here'
}
```

#### Step 2.使用流程
WebViewOpenImage使用起来非常简单
```

```

框架内部实现功能见源码或[简书](https://www.jianshu.com/p/83794a4f8752)以及[Wiki](https://github.com/EverZc/CommentBottomBar/wiki)

## 混淆代码
```java


```

## 常见问题


### 联系方式
* 我的简书：https://www.jianshu.com/u/197319888337 有兴趣的也可以关注，大家一起交流
