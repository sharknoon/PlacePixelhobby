package de.sharknoon.placepixelhobby.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.adapters.ColorAdapter
import de.sharknoon.placepixelhobby.alerts.showColorInformationAlert
import de.sharknoon.placepixelhobby.alerts.showPlateCuttingAlert
import de.sharknoon.placepixelhobby.model.PixelColor


class ListColorsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_colors, container, false)
    }


    override fun onResume() {
        super.onResume()
        initColorsRecyclerView()
    }

    private var colorsRVAdapter: ColorAdapter? = null


    private fun initColorsRecyclerView() {
        val view = view ?: return

        val colors = bundleToMap(arguments ?: Bundle.EMPTY)

        // set up the RecyclerView
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.recycler_view_fragment_list_colors)
        recyclerView?.layoutManager = LinearLayoutManager(view.context)
        val adapter = ColorAdapter(
            view.context,
            colors.map { it.key to it.value }
                .sortedByDescending { it.second })

        adapter.setColorInfoClickListener { color ->
            showColorInformationAlert(requireActivity(), color)
        }

        adapter.setButtonCutClickListener { color, amount ->
            showPlateCuttingAlert(requireActivity(), amount, color)
        }

        recyclerView?.adapter = adapter

        colorsRVAdapter = adapter
    }

    companion object {

        private val instances = mutableMapOf<Int, ListColorsFragment>()
        fun getInstance(colors: Map<PixelColor, Int>): ListColorsFragment {
            instances[colors.hashCode()]?.also { return it }

            val listColorsFragment = ListColorsFragment()

            val bundle = Bundle()
            for ((c, a) in colors) {
                bundle.putInt(c.name, a)
            }
            listColorsFragment.arguments = bundle

            instances[colors.hashCode()] = listColorsFragment

            return listColorsFragment
        }

    }

    private fun bundleToMap(bundle: Bundle): MutableMap<PixelColor, Int> {
        val colors = mutableMapOf<PixelColor, Int>()

        for (key in bundle.keySet()) {
            val amount = bundle.getInt(key)
            val pixelColor = PixelColor.valueOf(key)
            colors[pixelColor] = amount
        }

        return colors
    }

}
