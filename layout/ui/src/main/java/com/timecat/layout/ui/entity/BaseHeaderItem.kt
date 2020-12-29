package com.timecat.layout.ui.entity

import eu.davidea.flexibleadapter.items.IHeader
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
abstract class BaseHeaderItem<T : FlexibleViewHolder>(
    id: String = ""
) : BaseItem<T>(id), IHeader<T> {
    init {
        isHidden = true
        isSelectable = false
    }
}