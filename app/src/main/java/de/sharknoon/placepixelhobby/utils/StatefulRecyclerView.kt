package de.sharknoon.placepixelhobby.utils


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * Class [StatefulRecyclerView] extends [RecyclerView] and adds position management on configuration changes.
 *
 * @see "https://gist.github.com/FrantisekGazo/a9cc4e18cee42199a287"
 * @author FrantisekGazo
 * @version 2016-03-15
 */
class StatefulRecyclerView : RecyclerView {

    private var mLayoutManagerSavedState: Parcelable? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SAVED_SUPER_STATE, super.onSaveInstanceState())
        bundle.putParcelable(SAVED_LAYOUT_MANAGER, layoutManager?.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var s = state
        if (s is Bundle) {
            mLayoutManagerSavedState = s.getParcelable(SAVED_LAYOUT_MANAGER)
            s = s.getParcelable(SAVED_SUPER_STATE)
        }
        super.onRestoreInstanceState(s)
    }

    /**
     * Restores scroll position after configuration change.
     *
     *
     * **NOTE:** Must be called after adapter has been set.
     */
    private fun restorePosition() {
        if (mLayoutManagerSavedState != null) {
            layoutManager?.onRestoreInstanceState(mLayoutManagerSavedState)
            mLayoutManagerSavedState = null
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        restorePosition()
    }

    companion object {
        private const val SAVED_SUPER_STATE = "super-state"
        private const val SAVED_LAYOUT_MANAGER = "layout-manager-state"
    }
}