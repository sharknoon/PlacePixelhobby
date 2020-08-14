package de.sharknoon.placepixelhobby.activities


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.print.PrintHelper
import com.github.chrisbanes.photoview.PhotoView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.utils.AliasingDrawableWrapper
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        initImage()
        title = createTitle()
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

    private fun createTitle(): String {
        val imageNo = intent.getIntExtra("imageNo", -1)
        val imageX = intent.getIntExtra("imageX", -1)
        val imageY = intent.getIntExtra("imageY", -1)

        return getString(R.string.image_no_d_x_y, imageNo, imageX, imageY)
    }

    private fun createShortTitle(): String {
        val imageNo = intent.getIntExtra("imageNo", -1)

        return getString(R.string.image_d, imageNo)
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
            R.id.button_image_activity_action_bar_menu_share -> {
                val imageName = intent.getStringExtra("imageName") ?: ""
                val id = applicationContext.resources
                    .getIdentifier(imageName, "drawable", applicationContext.packageName)
                openShareMenu(imageName, id)
                true
            }
            R.id.button_image_activity_action_bar_menu_print -> {
                val imageName = intent.getStringExtra("imageName") ?: ""
                val id = applicationContext.resources
                    .getIdentifier(imageName, "drawable", applicationContext.packageName)
                printImage(id, imageName)
                true
            }
            R.id.button_image_activity_action_bar_menu_shortcut -> {
                createShortcutOfPlate()
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

    private fun openShareMenu(drawableName: String, drawableId: Int) {
        // create file from drawable image
        val bm = BitmapFactory.decodeResource(this.resources, drawableId)

        val filesDir: File = applicationContext.filesDir
        val imageFile = File(filesDir, "$drawableName.png")

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bm.compress(Bitmap.CompressFormat.PNG, 100, os) // 100% quality
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }


        // create new Intent
        val intent = Intent()
        intent.action = Intent.ACTION_SEND

        // set flag to give temporary permission to external app to use your FileProvider
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        // generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
        val uri =
            FileProvider.getUriForFile(this, "de.sharknoon.placepixelhobby.fileprovider", imageFile)
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // Set type to only show apps that can open your PNG file
        intent.type = "image/png"

        // start activity!
        startActivity(Intent.createChooser(intent, "send"))
    }

    private fun printImage(drawableId: Int, drawableName: String) {
        val bitmapPrinter = PrintHelper(this)
        bitmapPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
        val bitmap = BitmapFactory.decodeResource(this.resources, drawableId)
        bitmapPrinter.printBitmap("printing $drawableName", bitmap)
    }

    private fun createShortcutOfPlate() {
        val shortcutId = intent.getIntExtra("imageNo", -1).toString()
        val imageName = intent.getStringExtra("imageName")
        var imageId = applicationContext.resources
            .getIdentifier(imageName, "drawable", applicationContext.packageName)
        if (imageId == 0) imageId = R.drawable.ic_image
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            val shortcutInfo = ShortcutInfoCompat.Builder(this, shortcutId)
                .setIntent(
                    Intent(
                        this,
                        ImageActivity::class.java
                    ).setAction(Intent.ACTION_MAIN)
                        .putExtras(intent)
                ) // !!! intent's action must be set on oreo
                .setShortLabel(createShortTitle())
                .setIcon(IconCompat.createWithResource(this, imageId))
                .build()
            ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.image_activity_action_bar_menu, menu)
//        if (menu is MenuBuilder){
//            menu.setOptionalIconsVisible(true)
//        }
        return super.onCreateOptionsMenu(menu)
    }
}
