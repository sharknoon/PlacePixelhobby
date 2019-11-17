package de.sharknoon.placepixelhobby.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.fragments.ListColorsFragment
import de.sharknoon.placepixelhobby.model.Subimage
import de.sharknoon.placepixelhobby.utils.countColors
import de.sharknoon.placepixelhobby.utils.replaceChildFragment

class ImageColorInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_color_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initColorCountFragment()
        initTitle()
    }

    private fun initColorCountFragment() {
        val imageId = intent.getIntExtra("imageId", -1)
        val subimage = Subimage.fromID(this, imageId)

        //count the colors
        val amountColors = countColors(subimage)

        //Changing the fragment
        val fragment = ListColorsFragment.getInstance(amountColors)
        replaceChildFragment(R.id.frame_layout_activity_image_color_info, fragment)
    }

    private fun initTitle() {
        val imageTitle = intent.getStringExtra("imageTitle")
        title = imageTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
