package com.amar.photostyle.utils

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.amar.photostyle.constants.AppConstants
import com.amar.photostyle.ui.dashboard.Thumb
import com.amar.photostyle.ui.image_gallery.ImageGalleryActivity
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object AppUtils {
    var selectedImagePlus = MutableLiveData<Uri>()
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
                    if (isTemplateSelect) {
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

    fun saveImage(bitmap: Bitmap?, context: Context, activity: Activity) {
        shareBitmap = bitmap
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        var fos: OutputStream? = null
        var directory: File? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                directory = File(
                    Environment.DIRECTORY_PICTURES + File.separator + AppConstants.FOLDER_NAME
                )

                // getExternalStorageDirectory is deprecated in API 29
                if (!directory!!.exists()) {
                    directory?.mkdirs()
                }

                // Content resolver will process the content-values
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + File.separator + AppConstants.FOLDER_NAME
                    )
                }

                // Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let {
                    resolver.openOutputStream(it)
                }

                fos?.use {
                    //Finally writing the bitmap to the output stream that we opened
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
                Toast.makeText(context, "Image Saved Successfully", Toast.LENGTH_SHORT).show()
            }
        } else {
            directory = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + AppConstants.FOLDER_NAME
            )
            // getExternalStorageDirectory is deprecated in API 29
            if (!directory!!.exists()) {
                directory?.mkdirs()
            }
            val file = File(directory, fileName)
            try {
                bitmap?.let { saveImageToStream(it, FileOutputStream(file), context) }
            } catch (e: Exception) {
                Log.e("TAG", "saveImage: ")
            }

            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            scanFile(file.absolutePath, activity)
            // .DATA is deprecated in API 29
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        withContext(Dispatchers.Default) {
                            context.contentResolver.insert(
                                MediaStore.Images.Media.getContentUri(
                                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                                ),
                                values
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        withContext(Dispatchers.Default) {
                            context.contentResolver.insert(
                                MediaStore.Images.Media.getContentUri(
                                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                                ),
                                values
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
        Log.e("TAG", "saveImage--: ${directory?.absolutePath + fileName}")
    }


    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun scanFile(path: String, activity: Activity) {
        MediaScannerConnection.scanFile(
            activity, arrayOf(path), null
        ) { path, uri -> Log.i("TAG", "Finished scanning $path") }
    }

    private fun contentValues(): ContentValues {
        val values = ContentValues()
        values.put(
            MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis()
        )
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?, context: Context) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
                Toast.makeText(context, "Image Saved Successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}