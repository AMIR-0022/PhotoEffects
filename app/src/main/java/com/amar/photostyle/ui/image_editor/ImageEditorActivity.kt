package com.amar.photostyle.ui.image_editor

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.amar.photostyle.R
import coil.imageLoader
import coil.request.ImageRequest
import com.amar.photostyle.MainActivity
import com.amar.photostyle.constants.AppConstants
import com.amar.photostyle.databinding.ActivityImageEditorBinding
import com.amar.photostyle.custom_views.MaskableFrameLayout
import com.amar.photostyle.porter_duff.PorterDuffEffects
import com.amar.photostyle.touch_listener.MultiTouchListener
import com.amar.photostyle.ui.image_gallery.ImageGalleryActivity
import com.amar.photostyle.utils.AppUtils
import com.amar.photostyle.utils.downloadedFrame
import com.amar.photostyle.utils.imgGallery
import com.amar.photostyle.utils.potterDuffMode
import com.amar.photostyle.utils.isTemplateSelect
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.wysaid.common.SharedContext
import org.wysaid.nativePort.CGEImageHandler

class ImageEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageEditorBinding

    private var filterImg = MutableLiveData<Bitmap>()
    private var isReplace = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_editor)
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        val gpuImage = GPUImage(this)
        gpuImage.setImage(imgGallery)

        setEffect()
        eventListener()

        filterImg.observe(this) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.maskedImageView.setImageBitmap(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setEffect()
    }

    private fun setEffect() {
        CoroutineScope(Dispatchers.Main).launch {
            if (imgGallery != null && downloadedFrame != null) {

                isTemplateSelect = false
                isReplace = false
                setMaskEffect(imgGallery, downloadedFrame, downloadedFrame,
                    PorterDuffEffects.BlendModes[potterDuffMode] //9/10/11
                )
            } else {
                Toast.makeText(this@ImageEditorActivity, "Image Not Found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setMaskEffect(maskedImageBitmap: Bitmap?, dstIn: Drawable?, blendMask: Drawable?, mode: PorterDuff.Mode?, ) {
        try {
            if (maskedImageBitmap != null) {
                binding.maskedImageView.setImageBitmap(maskedImageBitmap)
                setMask(dstIn!!, binding.maskDSTIN) //cloud outer
                setMask(blendMask!!, binding.maskBlendable)//inner color
                binding.maskBlendable.setPorterDuffXferMode(mode)
            }
        } catch (e: Exception) {
            Log.e("TAG", "setMaskEffect: $e")
        }
    }

    private fun setMask(drawable: Drawable, maskableFrameLayout: MaskableFrameLayout) {
        try {
            val request = ImageRequest.Builder(this)
                .data(drawable)
                .target { it ->
                    // Handle the result.
                    maskableFrameLayout.setMask(it)
                }
                .build()
            val disposable = imageLoader.enqueue(request)

        } catch (e: Exception) {
            Log.e("TAG", "setMask---: $e")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun eventListener() {
        binding.maskedImageView.setOnTouchListener(
            MultiTouchListener(
                applicationContext, binding.maskedImageView
            )
        )
        binding.btnChangeImage.setOnClickListener {
            isReplace = true
            val intent = Intent(this, ImageGalleryActivity::class.java)
            intent.putExtra(AppConstants.KEY_IS_REPLACE, true)
            startActivity(intent)
        }
        binding.btnChangeEffect.setOnClickListener {
            isTemplateSelect = true
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(AppConstants.KEY_IS_TEMP, true)
            startActivity(intent)
        }
        binding.btnSave.setOnClickListener {
            AppUtils.saveImage(AppUtils.viewToBitmap(binding.saveImageLayout), this, this@ImageEditorActivity)
        }
    }

    fun getFilteredBitmap(ruleString: String?): Bitmap? {
        val bitmap = filterBitmap(imgGallery)
        var dst: Bitmap? = imgGallery
        try {
            val glContext = SharedContext.create()
            glContext.makeCurrent()

            //You can also use "NativeLibrary.filterImage_MultipleEffects" like the function "testCaseCustomFilter".
            //But when you use a CGEImageHandler, you can do the filter faster, because the handler will not be created for every filter.

            val handler = CGEImageHandler()
            handler.initWithBitmap(bitmap) // bmp = bmp.copy(Bitmap.Config.ARGB_8888, false); anr in library
            handler.setFilterWithConfig(ruleString)
            handler.setFilterIntensity(0.5f)
            handler.processFilters()

            //To accelerate this, you can add a Bitmap arg for "getResultBitmap",
            // and reuse the Bitmap instead of recycle it every time.
            try {
                dst = handler.resultBitmap //crash here
            } catch (e: OutOfMemoryError) {
                dst = imgGallery
            } catch (e: java.lang.Exception) {
                dst = imgGallery
            }
            //                    dst.recycle();  //Maybe reuse it will be better.
            glContext.release()
        } catch (e: IllegalArgumentException) {
        }
        return dst
    }

    private fun filterBitmap(bitmap: Bitmap?): Bitmap? {
        var mBitmap: Bitmap? = null
        mBitmap = try {
            Bitmap.createBitmap(bitmap!!.copy(
                Bitmap.Config.RGB_565, true))
        } catch (e: java.lang.Exception) {
            return mBitmap
        }
        return mBitmap
    }


}