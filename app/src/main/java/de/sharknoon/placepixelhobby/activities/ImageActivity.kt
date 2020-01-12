package de.sharknoon.placepixelhobby.activities


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v4.print.PrintHelper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
            R.id.button_image_activity_action_bar_menu_share -> {
                val imageName = intent.getStringExtra("imageName")
                val id = applicationContext.resources
                    .getIdentifier(imageName, "drawable", applicationContext.packageName)
                openShareMenu(imageName, id)
                true
            }
            R.id.button_image_activity_action_bar_menu_print -> {
                val imageName = intent.getStringExtra("imageName")
                val id = applicationContext.resources
                    .getIdentifier(imageName, "drawable", applicationContext.packageName)
                printImage(id, imageName)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.image_activity_action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
