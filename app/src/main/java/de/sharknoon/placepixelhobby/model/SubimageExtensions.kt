package de.sharknoon.placepixelhobby.model

private val FAVORITE_CACHE = mutableSetOf<Subimage>()
private val SELECTED_CACHE = mutableSetOf<Subimage>()

object SubimageExtensions {

    var Subimage.favorite: Boolean
        get() = FAVORITE_CACHE.contains(this)
        set(value) {
            if (value) FAVORITE_CACHE.add(this) else FAVORITE_CACHE.remove(this)
        }

    var Subimage.selected: Boolean
        get() = SELECTED_CACHE.contains(this)
        set(value) {
            if (value) SELECTED_CACHE.add(this) else SELECTED_CACHE.remove(this)
            triggerAllSelectionChangeListeners()
        }

    private val SELECTION_CHANCE_LISTENERS = mutableSetOf<(Int) -> Unit>()
    fun addSelectionChangeListener(change: (newSelection: Int) -> Unit) {
        SELECTION_CHANCE_LISTENERS.add(change)
        change(SELECTED_CACHE.size)
    }

    fun removeSelectionChangeListener(change: (newSelection: Int) -> Unit) {
        SELECTION_CHANCE_LISTENERS.remove(change)
    }

    fun getSelectedImages(): Set<Subimage> {
        return SELECTED_CACHE
    }

    fun clearSelections() {
        SELECTED_CACHE.clear()
        triggerAllSelectionChangeListeners()
    }

    private fun triggerAllSelectionChangeListeners() {
        SELECTION_CHANCE_LISTENERS.forEach {
            it(SELECTED_CACHE.size)
        }
    }
}