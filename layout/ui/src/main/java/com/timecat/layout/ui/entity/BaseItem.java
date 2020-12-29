package com.timecat.layout.ui.entity;

import org.jetbrains.annotations.NotNull;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * This class will benefit of the already implemented methods (getter and setters) in
 * {@link AbstractFlexibleItem}.
 * <p>
 * It is used as base item for all example models.
 */
public abstract class BaseItem<VH extends FlexibleViewHolder>
        extends AbstractFlexibleItem<VH> {

    @NotNull
    public String id;
    public String title;
    public String subtitle = "";
    public boolean isArchived = false;
    public long createdTime = 0L;
    /* number of times this item has been refreshed */

    public BaseItem(@NotNull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof BaseItem) {
            BaseItem inItem = (BaseItem) inObject;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    /**
     * Override this method too, when using functionalities like StableIds, Filter or CollapseAll.
     * FlexibleAdapter is making use of HashSet to improve performance, especially in big list.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @NotNull
    @Override
    public String toString() {
        return "id=" + id +
                ", title=" + title;
    }

}