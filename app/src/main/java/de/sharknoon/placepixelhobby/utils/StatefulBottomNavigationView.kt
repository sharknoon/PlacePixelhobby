package de.sharknoon.placepixelhobby.utils

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView


class StatefulBottomNavigationView :
    BottomNavigationView {

    private var mSelectedItemIdSavedState: Int? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SAVED_SUPER_STATE, super.onSaveInstanceState())
        bundle.putInt(SAVED_SELECTED_ITEM_ID_STATE, selectedItemId)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var s = state
        if (state is Bundle) {
            mSelectedItemIdSavedState = state.getInt(SAVED_SELECTED_ITEM_ID_STATE)
            s = state.getParcelable(SAVED_SUPER_STATE)
        }
        restoreSelection()
        super.onRestoreInstanceState(s)
    }

    private fun restoreSelection() {
        mSelectedItemIdSavedState?.also {
            selectedItemId = it
        }
    }

    companion object {
        private const val SAVED_SUPER_STATE = "super-state"
        private const val SAVED_SELECTED_ITEM_ID_STATE = "selected-item-id-state"
    }

}