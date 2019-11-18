package de.sharknoon.placepixelhobby.activities


import android.content.Intent
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
    }

    override fun onResume() {
        super.onResume()

        initImage()
        initTitle()
    }

    private fun initImage() {
        val imageName = intent.getStringExtra("imageName")
        val id = applicationContext.resources
            .getIdentifier(imageName, "drawable", applicationContext.packageName)
        val drawable = getDrawable(id) ?: return

        val photoView = findViewById<PhotoView>(R.id.photo_view_activity_image)
        val aliasingDrawableWrapper = AliasingDrawableWrapper(drawable)
        photoView.setImageDrawable(aliasingDrawableWrapper)
        photoView.maximumScale = 10F
    }

    private fun initTitle() {
        val imageNo = intent.getIntExtra("imageNo", -1)
        val imageX = intent.getIntExtra("imageX", -1)
        val imageY = intent.getIntExtra("imageY", -1)

        title = getString(R.string.image_no_d_x_y, imageNo, imageX, imageY)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.button_image_activity_action_bar_menu_info -> {
                val imageName = intent.getStringExtra("originalImageName")
                val id = applicationContext.resources
                    .getIdentifier(imageName, "drawable", applicationContext.packageName)
                openImageColorInfoActivity(id)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openImageColorInfoActivity(drawableId: Int) {
        val intent = Intent(this, ImageColorInfoActivity::class.java)
        intent.putExtra("imageId", drawableId)
        intent.putExtra("imageTitle", title)
        this.startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.image_activity_action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
