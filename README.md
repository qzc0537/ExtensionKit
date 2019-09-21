# ExtensionKit
Kotlin常用扩展库

[![](https://jitpack.io/v/qzc0537/ExtensionKit.svg)](https://jitpack.io/#qzc0537/ExtensionKit)


使用
--
1.project build.gradle下添加：
maven { url 'https://jitpack.io' }

如下：

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

2.app build.gradle下添加依赖 ：

```
implementation 'com.github.qzc0537:ExtensionKit:1.0.3'
```

3.愉快的使用：
```
EkConfigs.initToast(application, false)
EkConfigs.snackBarBgColor = color(R.color.colorPrimary)
EkConfigs.snackBarTextColor = Color.WHITE

dialogExtBtn1.onClick {
    alert {
        setTheme(R.style.AppTheme)
        setTitle("对话框")
        setMessage("这是DSL风格的对话框")
        setPositiveButton("确定") { dialog, which -> toast("ok") }
        setNegativeButton("取消") { dialog, which -> toast("no") }
        setNeutralButton("再想想") { dialog, which -> toast("let me think") }
    }
}
dialogExtBtn2.onClick {
    val items = arrayOf("男", "女")
    alert {
        setTitle("对话框")
        setSingleChoiceItems(items, 0) { dialog, which ->
            toast(items[which])
        }
        setPositiveButton("确定") { dialog, which -> toast("ok") }
        setNegativeButton("取消") { dialog, which -> toast("no") }
        setCancelable(false)
        setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) dialog.dismiss()
            true
        }
    }
}
dialogExtBtn3.onClick {
    val items = arrayOf("苹果", "香蕉", "西瓜", "哈密瓜", "葡萄")
    val checkedItems = booleanArrayOf(true, true, false, false, false)
    alert {
        setTitle("对话框")
        setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            toast(items[which])
        }
        setPositiveButton("确定") { dialog, which -> toast("ok") }
        setNegativeButton("取消") { dialog, which -> toast("no") }
    }
}
snackBarExtBtn.onClick {
    snackBarExtBtn.snackBarIndefinite("This is snackBar")
        .setAction("Your Action") {
            toast("Amazing")
        }
}
doAsyncBtn.onClick {
    doAsync {
        Thread.sleep(1000)
        activityUiThread {
            toast("update UI")
        }
    }
}

private fun otherApis() {
    logi("Hello")

    toast("Hello")
    val view = LayoutInflater.from(this).inflate(R.layout.layout_toast, null)
    toast(view)

    startActivity<MainActivity>("id" to 5)
    startActivity(intentFor<MainActivity>("id" to 5).singleTop())
    startActivityForResult<MainActivity>(
        1024, "id" to 5,
        "id" to 5,
        "id" to 5
    )

    if (afterL) {
        logi("versionName->" + getVersionName())
        for (abi in phoneSupoortAbis) {
            logi(abi)
        }
    }
}
