package com.timecat.layout.ui.business.form

import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.Assertion
import com.afollestad.vvalidator.checkAttached
import com.afollestad.vvalidator.field.FieldBuilder
import com.afollestad.vvalidator.field.FieldValue
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.field.TextFieldValue
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.GenericFormField
import com.timecat.layout.ui.business.setting.NextItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/22
 * @description null
 * @usage null
 */
fun Form.next(
    view: NextItem,
    name: String? = null,
    optional: Boolean = false,
    builder: FieldBuilder<PathField>
): GenericFormField {
    val newField = PathField(
        container = this.container.checkAttached(),
        nextItem = view,
        name = name
    )
    if (optional) {
        newField.isEmptyOr(builder)
    } else {
        newField.builder()
    }
    return appendField(newField)
}

class PathField(
    container: ValidationContainer,
    nextItem: NextItem,
    name: String?
) : FormField<PathField, NextItem, CharSequence>(container, nextItem, name) {
    init {
        onErrors { view, errors ->
            view.text = errors.firstOrNull()?.toString()
        }
    }

    override fun obtainValue(id: Int, name: String): FieldValue<CharSequence>? {
        val currentValue = view.hint ?: return null
        return TextFieldValue(
            id = id,
            name = name,
            value = currentValue
        )
    }

    fun isEmptyOr(builder: PathField.() -> Unit) = conditional(
        condition = {
            view.hint.isNullOrEmpty()
        },
        builder = builder
    )

    fun isNotEmpty() = assert(NotEmptyAssertion())

    class NotEmptyAssertion internal constructor() : Assertion<NextItem, NotEmptyAssertion>() {
        override fun isValid(view: NextItem) = !view.hint.isNullOrEmpty()

        override fun defaultDescription() = "cannot be empty"
    }

    override fun startRealTimeValidation(debounce: Int) {
        view.hintTextView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            validate()
        }
    }

}