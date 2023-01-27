package com.example.birthdaywishing

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.birthdaywishing.databinding.ActivityHappyBirthDayBinding
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors


class HappyBirthDayActivity : AppCompatActivity() {
    private var index = 0
    private lateinit var imageUrls: Array<String>
    private val prefix = "https://images.unsplash.com/photo-"
    private val infix = "?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w="
    private val suffix = "&q=80"

    private lateinit var binding: ActivityHappyBirthDayBinding

    companion object {
        const val NAME_EXTRA = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyBirthDayBinding.inflate(layoutInflater)
        val view = binding.root

        val name = intent.getStringExtra(NAME_EXTRA)
        binding.nameTV.text = "Happy BirthDay\n$name!"

        binding.nextImageBtn.setOnClickListener {
            loadImageUrl()
        }

        binding.downloadImageBtn.setOnClickListener {
            downloadImage()
        }

        setContentView(view)
    }

    private fun loadImageUrl() {

        imageUrls = arrayOf(
            prefix+"1557164158-11e97f2bb220"+infix+"347"+suffix,
            prefix+"1554080358-b1d86f4cbb24"+infix+"404"+suffix,
            prefix+"1551879403-6adb554966fd"+infix+"1287"+suffix,
            prefix+"1518566585952-954bb14432d1"+infix+"387"+suffix,
            prefix+"1569289522127-c0452f372d46"+infix+"381"+suffix,
            prefix+"1429087969512-1e85aab2683d"+infix+"387"+suffix,
            prefix+"1528459801416-a9e53bbf4e17"+infix+"412"+suffix,
            prefix+"1479750178258-aec5879046ce"+infix+"499"+suffix,
            prefix+"1602153883150-a442f874f839"+infix+"436"+suffix,
            prefix+"1517273666229-35e76c06b18d"+infix+"387"+suffix,
            prefix+"1562440499-64c9a111f713"+infix+"387"+suffix,
            prefix+"1616690710400-a16d146927c5"+infix+"387"+suffix,
            prefix+"1552689486-ce080445fbb6"+infix+"431"+suffix,

            prefix+"1558301211-0d8c8ddee6ec"+infix+"1536"+suffix
        )
//        prefix+"1663839331379-cfff1bb61db8"+infix+"387"+suffix,
//        prefix+"1663839412010-274ddec80751"+infix+"387"+suffix,
        // prefix+"1663839412021-c08ce31f47da"+infix+"387"+suffix,

        val imageUrl = binding.wishHappyBirthDayIV
        Glide.with(this)
            .load(imageUrls[index++])
            .error(R.drawable.baseline_error)
            .into(imageUrl)

        if(index >= imageUrls.size) index = 0
    }

    private fun downloadImage() {

        // Declaring a Bitmap local
        var mImage: Bitmap?

        // Declaring a webpath as a string
        // "https://media.geeksforgeeks.org/wp-content/uploads/20210224040124/JSBinCollaborativeJavaScriptDebugging6-300x160.png"

        val mWebPath = imageUrls[index]

        // Declaring and initializing an Executor and a Handler
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

        myExecutor.execute {
            mImage = mLoad(mWebPath)
            myHandler.post {
                binding.wishHappyBirthDayIV.setImageBitmap(mImage)
                if(mImage!=null){
                    mSaveMediaToStorage(mImage)
                }
            }
        }
    }

    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    // Function to save image on the device.
    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }

}