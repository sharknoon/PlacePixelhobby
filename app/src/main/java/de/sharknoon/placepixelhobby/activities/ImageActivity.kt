package de.sharknoon.placepixelhobby.activities


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.chrisbanes.photoview.PhotoView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.utils.AliasingDrawableWrapper


class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageName = intent.getStringExtra("imageName")
        val id = applicationContext.resources
            .getIdentifier(imageName, "drawable", applicationContext.packageName)
        val drawable = getDrawable(id) ?: return

        val photoView = findViewById<PhotoView>(R.id.imageView)
        val aliasingDrawableWrapper = AliasingDrawableWrapper(drawable)
        photoView.setImageDrawable(aliasingDrawableWrapper)
        photoView.maximumScale = 5F
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
}
