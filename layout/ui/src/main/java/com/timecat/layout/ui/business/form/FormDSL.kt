package com.timecat.layout.ui.business.form

import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.business.setting.*
import com.timecat.layout.ui.layout.setShakelessClickListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/14
 * @description null
 * @usage null
 */
private fun Int?.wrapContext(context: Context): Context {
    return if (this != null)
        ContextThemeWrapper(context, this)
    else
        context
}

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
    style: Int? = null,
    autoAdd: Boolean = true,
    onNext: (NextItem) -> Unit
): NextItem = NextItem(style.wrapContext(context)).apply {
    this.title = title
    hint = null
    onNext {
        onNext(this)
    }
}.also { if (autoAdd) addView(it) }

fun ViewGroup.Next(
    title: String,
    text: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onNext: (NextItem) -> Unit
): NextItem = NextItem(style.wrapContext(context)).apply {
    this.title = title
    this.text = text
    hint = null
    onNext {
        onNext(this)
    }
}.also { if (autoAdd) addView(it) }

fun ViewGroup.Next(
    title: String,
    hint: String?,
    initialText: String = "",
    message: String? = null,
    style: Int? = null,
    autoAdd: Boolean = true,
    go: (NextItem) -> Unit
): NextItem = NextItem(style.wrapContext(context)).apply {
    this.title = title
    this.hint = hint
    text = initialText
    onNext {
        message?.let { letMeKnow(it) }
        go(this)
    }
}.also { if (autoAdd) addView(it) }
//endregion

//region 滑块
fun ViewGroup.UnitSlide(
    title: String,
    unit: String,
    from: Float, to: Float, value: Float,
    style: Int? = null,
    autoAdd: Boolean = true,
    SetValue: (Float) -> Unit
): SliderItem = SliderItem(style.wrapContext(context)).apply {
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
}.also { if (autoAdd) addView(it) }

fun ViewGroup.Slide(
    title: String,
    unit: String,
    from: Float, to: Float, value: Float,
    style: Int? = null,
    autoAdd: Boolean = true,
    SetValue: (Float) -> Unit
): SliderItem = SliderItem(style.wrapContext(context)).apply {
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
}.also { if (autoAdd) addView(it) }
//endregion

//region 开关
fun ViewGroup.Switch(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    getInitialCheck: () -> Boolean,
    check: (Boolean) -> Unit
): SwitchItem = Switch(title, null, style, autoAdd, getInitialCheck, check)

fun ViewGroup.Switch(
    title: String,
    hint: String?,
    style: Int? = null,
    autoAdd: Boolean = true,
    getInitialCheck: () -> Boolean,
    check: (Boolean) -> Unit
): SwitchItem = SwitchItem(style.wrapContext(context)).apply {
    this.title = title
    this.hint = hint
    initCheck = getInitialCheck()
    onCheckChange = { check(isChecked) }
}.also { if (autoAdd) addView(it) }
//endregion

//region 下拉选择
fun <T> ViewGroup.Spinner(
    title: String,
    items: List<T>,
    style: Int? = null,
    autoAdd: Boolean = true,
    onSelect: (data: T, index: Int) -> Unit
): SpinnerItem = SpinnerItem(style.wrapContext(context)).apply {
    this.title = title
    onItemSelected(items, onSelect)
}.also { if (autoAdd) addView(it) }

fun <T> ViewGroup.Dropdown(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    items: List<T>
): DropdownInputItem<T> = DropdownInputItem<T>(style.wrapContext(context)).apply {
    this.hint = title
    this.items = items
}.also { if (autoAdd) addView(it) }


//endregion

//region 图片
fun ViewGroup.Image(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: ImageItem.() -> Unit
) = ImageItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.Image(
    title: String,
    icon: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onSelectImage: (item: ImageItem) -> Unit
) = ImageItem(style.wrapContext(context)).apply {
    this.title = title
    this.icon = icon
    onClick(onSelectImage)
}.also { if (autoAdd) addView(it) }

fun ViewGroup.Image(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onSelectImage: (item: ImageItem) -> Unit
) = ImageItem(style.wrapContext(context)).apply {
    this.title = title
    onClick(onSelectImage)
}.also { if (autoAdd) addView(it) }
//endregion

//region text
fun ViewGroup.H1(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 24f, style, autoAdd)

fun ViewGroup.H2(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 20f, style, autoAdd)

fun ViewGroup.H3(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 18f, style, autoAdd)

fun ViewGroup.H4(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 16f, style, autoAdd)

fun ViewGroup.H5(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 14f, style, autoAdd)

fun ViewGroup.H6(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 12f, style, autoAdd)

fun ViewGroup.Text(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = Head(title, 16f, style, autoAdd)

fun ViewGroup.CenterH1(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 24f, style, autoAdd)

fun ViewGroup.CenterH2(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 20f, style, autoAdd)

fun ViewGroup.CenterH3(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 18f, style, autoAdd)

fun ViewGroup.CenterH4(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 16f, style, autoAdd)

fun ViewGroup.CenterH5(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 14f, style, autoAdd)

fun ViewGroup.CenterH6(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 12f, style, autoAdd)

fun ViewGroup.CenterText(
    title: String,
    style: Int? = null,
    autoAdd: Boolean = true
) = CenterHead(title, 16f, style, autoAdd)

fun ViewGroup.CenterHead(
    title: String,
    textSize: Float,
    style: Int? = null,
    autoAdd: Boolean = true
): HeadItem = Head(title, textSize, style, autoAdd).apply {
    gravity = Gravity.CENTER
}

fun ViewGroup.Head(
    title: String,
    textSize: Float,
    style: Int? = null,
    autoAdd: Boolean = true
): HeadItem = HeadItem(style.wrapContext(context)).apply {
    setText(title)
    setTextSize(textSize)
    setTextColor(Attr.getPrimaryTextColor(context))
}.also { if (autoAdd) addView(it) }

fun ViewGroup.CenterBody(
    content: String,
    style: Int? = null,
    autoAdd: Boolean = true
): HeadItem = Body(content, style, autoAdd).apply {
    gravity = Gravity.CENTER
}

fun ViewGroup.Body(
    content: String,
    style: Int? = null,
    autoAdd: Boolean = true
): HeadItem = HeadItem(style.wrapContext(context)).apply {
    setText(content)
    setTextColor(Attr.getSecondaryTextColor(context))
}.also { if (autoAdd) addView(it) }

fun ViewGroup.Input(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: InputItem.() -> Unit
) = OneLineInput(style, autoAdd, build)

fun ViewGroup.OneLineInput(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: InputItem.() -> Unit
) = InputItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.OneLineInput(
    title: String,
    prefill: String,
    style: Int? = null,
    autoAdd: Boolean = true,
) = InputItem(style.wrapContext(context)).apply {
    this.hint = title
    this.text = prefill
}.also { if (autoAdd) addView(it) }

fun ViewGroup.NumberInput(
    title: String,
    prefill: String,
    style: Int? = null,
    autoAdd: Boolean = true,
) = InputItem(style.wrapContext(context)).apply {
    this.hint = title
    this.text = prefill
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
}.also { if (autoAdd) addView(it) }

fun ViewGroup.OneLineInput(
    title: String,
    prefill: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onTextChange: (value: String?) -> Unit
) = InputItem(style.wrapContext(context)).apply {
    this.hint = title
    this.text = prefill
    this.onTextChange = onTextChange
}.also { if (autoAdd) addView(it) }

fun ViewGroup.MultiLineInput(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: InputItem.() -> Unit
) = InputItem(style.wrapContext(context)).apply(build).also {
    it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
}.also { if (autoAdd) addView(it) }

fun ViewGroup.MultiLineInput(
    title: String,
    prefill: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onTextChange: (value: String?) -> Unit
) = InputItem(style.wrapContext(context)).apply {
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
    this.hint = title
    this.text = prefill
    this.onTextChange = onTextChange
}.also { if (autoAdd) addView(it) }
//endregion

//region DSL
fun ViewGroup.Divider(
    style: Int? = null,
    autoAdd: Boolean = true,
): DividerItem = DividerItem(style.wrapContext(context))
    .also { if (autoAdd) addView(it) }

fun ViewGroup.Spinner(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: SpinnerItem.() -> Unit
) = SpinnerItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun <T> ViewGroup.Dropdown(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: DropdownInputItem<T>.() -> Unit
) = DropdownInputItem<T>(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.Switch(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: SwitchItem.() -> Unit
) = SwitchItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.Slider(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: SliderItem.() -> Unit
) = SliderItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.Next(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: NextItem.() -> Unit
) = NextItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.VerticalContainer(
    style: Int? = null,
    autoAdd: Boolean = true,
    build: ContainerItem.() -> Unit
) = ContainerItem(style.wrapContext(context)).apply(build).also { if (autoAdd) addView(it) }

fun ViewGroup.MaterialButton(
    text: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onClick: (View) -> Unit
) = com.google.android.material.button.MaterialButton(style.wrapContext(context)).apply {
    setText(text)
    setShakelessClickListener(onClick = onClick)
}.also { if (autoAdd) addView(it) }

fun ViewGroup.Chip(
    text: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onClick: (View) -> Unit
) = com.google.android.material.chip.Chip(style.wrapContext(context)).apply {
    setText(text)
    setShakelessClickListener(onClick = onClick)
}.also { if (autoAdd) addView(it) }
//endregion
