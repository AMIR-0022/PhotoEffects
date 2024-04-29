package com.amar.photostyle.utils

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.amar.photostyle.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.amar.photostyle.constants.AppConstants
import com.amar.photostyle.constants.AppConstants.KEY_FOR_ADD_PHOTO
import com.amar.photostyle.constants.AppConstants.KEY_FRAME_LINK
import com.amar.photostyle.ui.dashboard.Thumb
import com.amar.photostyle.ui.image_gallery.ImageGalleryActivity
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


object AppUtils {
    const val KEY_URL_OBJ = "url_obj"
    const val APP_TAG = "com.peopleperfectae"
    var currentVersion: String? = null
    var currentApiVersion: String = android.os.Build.VERSION.RELEASE
    var selectedImagePlus = MutableLiveData<Uri>()
    var isAdd = false

    @JvmStatic
    var shareBitmap: Bitmap? = null

    fun resizeBitmap(input: Bitmap, destWidth: Int, destHeight: Int, rotation: Int = 0, view: ConstraintLayout): Bitmap? {
        var bmp: Bitmap? = null
        Log.e("TAG", "ResizeBitmap: false", )
        val job = CoroutineScope(Dispatchers.IO).async {
            var dstWidth = destWidth
            var dstHeight = destHeight
            val srcWidth = input.width
            val srcHeight = input.height
            if (rotation == 90 || rotation == 270) {
                dstWidth = destHeight
                dstHeight = destWidth
            }
            var needsResize = false
            val p: Float
            if (srcWidth > dstWidth || srcHeight > dstHeight) {
                needsResize = true
                if (srcWidth > srcHeight && srcWidth > dstWidth) {
                    p = dstWidth.toFloat() / srcWidth.toFloat()
                    dstHeight = (srcHeight * p).toInt()
                } else {
                    p = dstHeight.toFloat() / srcHeight.toFloat()
                    dstWidth = (srcWidth * p).toInt()
                }
            } else {
                dstWidth = srcWidth
                dstHeight = srcHeight
            }

            if (needsResize || rotation != 0) {
                bmp = if (rotation == 0) {
                    Bitmap.createScaledBitmap(input, dstWidth, dstHeight, true)
                } else {
                    val matrix = Matrix()
                    matrix.postScale(dstWidth.toFloat() / srcWidth, dstHeight.toFloat() / srcHeight)
                    matrix.postRotate(rotation.toFloat())
                    Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight, matrix, true)
                }

            } else {
                bmp = input
            }
            Log.e("TAG", "Final bitmap ")
        }
        runBlocking {

            job.join()
        }
        Log.e("TAG", "ResizeBitmap: true", )
        return bmp
    }


    fun preDownloadImg(activity: Activity, progressBar: ConstraintLayout, thumb: Thumb,) {
        progressBar.visibility = View.VISIBLE
        Glide.with(activity)
            .load(Uri.parse(thumb.mask))
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Drawable,transition: Transition<in Drawable?>?) {
                    progressBar.visibility = View.GONE
                    downloadedFrame = resource

                    potterDuffMode = thumb.blend.toInt()
                    if (selectTemp) {
                        activity.finish()
                    } else {
                        thumb.mask.let {
                            thumb.isDownloaded = true
                            downloadedFrameLink = it

                            // jump to next gallery activity
                            val intent: Intent = Intent(activity, ImageGalleryActivity::class.java)
                            activity.startActivity(intent)
                        }
                    }

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Log.e("TAG", "failed to preload")
                    progressBar.visibility = View.GONE
                }
            })
    }


}