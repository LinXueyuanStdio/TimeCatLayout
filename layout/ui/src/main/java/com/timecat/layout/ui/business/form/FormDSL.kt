package com.timecat.layout.ui.business.form

import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.business.setting.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/14
 * @description null
 * @usage null
 */

fun letMeKnow(message: String) = LetMeKnow.report(message)

fun ViewGroup.add(view: () -> View) = addView(view())
fun ViewGroup.add(vararg view: () -> View) {
    for (v in view) {
        addView(v())
    }
}

fun ViewGroup.add(vararg view: View) {
    for (v in view) {
        addView(v)
    }
}

fun ViewGroup.add(vararg view: Pair<View, Int>) {
    for (v in view) {
        addView(v.first, v.second)
    }
}
//region 下一个

fun ViewGroup.Next(
    title: String,
    onNext: (NextItem) -> Unit
): NextItem = NextItem(context).apply {
    this.title = title
    hint = null
    onNext {
        onNext(this)
    }
}

fun ViewGroup.Next(
    title: String,
    text: String,
    onNext: (NextItem) -> Unit
): NextItem = NextItem(context).apply {
    this.title = title
    this.text = text
    hint = null
    onNext {
        onNext(this)
    }
}

fun ViewGroup.Next(
    title: String,
    hint: String?,
    initialText: String = "",
    message: String? = null,
    go: (NextItem) -> Unit
): NextItem = NextItem(context).apply {
    this.title = title
    this.hint = hint
    text = initialText
    onNext {
        message?.let { letMeKnow(it) }
        go(this)
    }
}
//endregion

//region 滑块
fun ViewGroup.UnitSlide(
    title: String,
    unit: String,
    from: Float, to: Float, value: Float,
    SetValue: (Float) -> Unit
): SliderItem = SliderItem(context).apply {
    valueFrom = from
    valueTo = to
    stepSize = 1f
    hint = null
    this.title = title
    this.value = value
    this.text = "${this.value} $unit"
    onSlide { value ->
        text = "$value $unit"
        SetValue(value)
    }
}

fun ViewGroup.Slide(
    title: String,
    unit: String,
    from: Float, to: Float, value: Float,
    SetValue: (Float) -> Unit
): SliderItem = SliderItem(context).apply {
    valueFrom = from
    valueTo = to
    stepSize = 1f
    hint = null
    this.title = title
    this.value = value
    this.text = unit
    onSlide { value ->
        SetValue(value)
    }
}
//endregion

//region 开关
fun ViewGroup.Switch(
    title: String,
    getInitialCheck: () -> Boolean,
    check: (Boolean) -> Unit
): SwitchItem = Switch(title, null, getInitialCheck, check)

fun ViewGroup.Switch(
    title: String,
    hint: String?,
    getInitialCheck: () -> Boolean,
    check: (Boolean) -> Unit
): SwitchItem = SwitchItem(context).apply {
    this.title = title
    this.hint = hint
    initCheck = getInitialCheck()
    onCheckChange = { check(isChecked) }
}
//endregion

//region 下拉选择
fun <T> ViewGroup.Spinner(
    title: String,
    items: List<T>,
    onSelect: (data: T, index: Int) -> Unit
): SpinnerItem = SpinnerItem(context).apply {
    this.title = title
    onItemSelected(items, onSelect)
}

fun <T> ViewGroup.Dropdown(
    title: String,
    items: List<T>
): DropdownInputItem<T> = DropdownInputItem<T>(context).apply {
    this.hint = title
    this.items = items
}


//endregion

//region 图片
fun ViewGroup.Image(build: ImageItem.() -> Unit) = ImageItem(context).apply(build)

fun ViewGroup.Image(title: String, icon: String, onSelectImage: (item: ImageItem) -> Unit) = ImageItem(context).apply {
    this.title = title
    this.icon = icon
    onClick(onSelectImage)
}

fun ViewGroup.Image(title: String, onSelectImage: (item: ImageItem) -> Unit) = ImageItem(context).apply {
    this.title = title
    onClick(onSelectImage)
}
//endregion

//region text
fun ViewGroup.H1(title: String) = Head(title, 24f)
fun ViewGroup.H2(title: String) = Head(title, 20f)
fun ViewGroup.H3(title: String) = Head(title, 18f)
fun ViewGroup.H4(title: String) = Head(title, 16f)
fun ViewGroup.H5(title: String) = Head(title, 14f)
fun ViewGroup.H6(title: String) = Head(title, 12f)
fun ViewGroup.Text(title: String) = Head(title, 16f)

fun ViewGroup.CenterH1(title: String) = CenterHead(title, 24f)
fun ViewGroup.CenterH2(title: String) = CenterHead(title, 20f)
fun ViewGroup.CenterH3(title: String) = CenterHead(title, 18f)
fun ViewGroup.CenterH4(title: String) = CenterHead(title, 16f)
fun ViewGroup.CenterH5(title: String) = CenterHead(title, 14f)
fun ViewGroup.CenterH6(title: String) = CenterHead(title, 12f)
fun ViewGroup.CenterText(title: String) = CenterHead(title, 16f)
fun ViewGroup.CenterHead(title: String, textSize: Float): HeadItem = Head(title, textSize).apply {
    gravity = Gravity.CENTER
}

fun ViewGroup.Head(title: String, textSize: Float): HeadItem = HeadItem(context).apply {
    setText(title)
    setTextSize(textSize)
    setTextColor(Attr.getPrimaryTextColor(context))
}

fun ViewGroup.CenterBody(content: String): HeadItem = Body(content).apply {
    gravity = Gravity.CENTER
}

fun ViewGroup.Body(content: String): HeadItem = HeadItem(context).apply {
    setText(content)
    setTextColor(Attr.getSecondaryTextColor(context))
}

fun ViewGroup.Input(build: InputItem.() -> Unit) = OneLineInput(build)
fun ViewGroup.OneLineInput(build: InputItem.() -> Unit) = InputItem(context).apply(build)

fun ViewGroup.OneLineInput(title: String, prefill: String) = InputItem(context).apply {
    this.hint = title
    this.text = prefill
}
fun ViewGroup.NumberInput(title: String, prefill: String) = InputItem(context).apply {
    this.hint = title
    this.text = prefill
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
}

fun ViewGroup.OneLineInput(
    title: String,
    prefill: String,
    onTextChange: (value: String?) -> Unit
) = InputItem(context).apply {
    this.hint = title
    this.text = prefill
    this.onTextChange = onTextChange
}

fun ViewGroup.MultiLineInput(build: InputItem.() -> Unit) = InputItem(context).apply(build).also {
    it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
}

fun ViewGroup.MultiLineInput(
    title: String,
    prefill: String,
    onTextChange: (value: String?) -> Unit
) = InputItem(context).apply {
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
    this.hint = title
    this.text = prefill
    this.onTextChange = onTextChange
}
//endregion

//region DSL
fun ViewGroup.addDivider(): DividerItem = DividerItem(context).also {
    addView(it)
    it.setup(context)
}

fun ViewGroup.Spinner(build: SpinnerItem.() -> Unit) = SpinnerItem(context).apply(build)

fun <T> ViewGroup.Dropdown(build: DropdownInputItem<T>.() -> Unit) = DropdownInputItem<T>(context).apply(build)

fun ViewGroup.Switch(build: SwitchItem.() -> Unit) = SwitchItem(context).apply(build)

fun ViewGroup.Slider(build: SliderItem.() -> Unit) = SliderItem(context).apply(build)

fun ViewGroup.Next(build: NextItem.() -> Unit) = NextItem(context).apply(build)

fun ViewGroup.VerticalContainer(build: ContainerItem.() -> Unit) = ContainerItem(context).apply(build)
//endregion
