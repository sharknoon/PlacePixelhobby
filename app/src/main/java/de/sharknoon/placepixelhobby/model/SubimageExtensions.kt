package de.sharknoon.placepixelhobby.model

private val FAVORITE_CACHE = mutableMapOf<Subimage, Boolean>()
private val SELECTED_CACHE = mutableMapOf<Subimage, Boolean>()

object SubimageExtensions {

    var Subimage.favorite: Boolean
        get() = FAVORITE_CACHE[this] ?: false
        set(value) {
            if (value) FAVORITE_CACHE[this] = true else FAVORITE_CACHE.remove(this)
        }

    var Subimage.selected: Boolean
        get() = SELECTED_CACHE[this] ?: false
        set(value) {
            if (value) SELECTED_CACHE[this] = true else SELECTED_CACHE.remove(this)
            triggerAllSelectionChangeListeners()
        }

    private val SELECTION_CHANCE_LISTENERS = mutableSetOf<(Int) -> Unit>()
    fun onSelectionChanged(change: (newSelection: Int) -> Unit) {
        SELECTION_CHANCE_LISTENERS.add(change)
        change(SELECTED_CACHE.size)
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