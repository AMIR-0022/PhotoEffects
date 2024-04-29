package com.amar.photostyle.ui.crop_image

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amar.photostyle.R
import com.amar.photostyle.constants.AppConstants
import com.amar.photostyle.databinding.ActivityCropImageBinding
import com.amar.photostyle.ui.image_editor.ImageEditorActivity
import com.amar.photostyle.utils.AppUtils
import com.amar.photostyle.utils.imgGallery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CropImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropImageBinding
    private var isReplace = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop_image)

        val path = intent.getStringExtra(AppConstants.KEY_PATH)

        binding.ivCrop.setAspectRatio(5, 6)
        binding.ivCrop.setImageUriAsync(
            Uri.fromFile(path?.let { File(it) })
        )

        binding.btnCrop.setOnClickListener {
            if (binding.ivCrop.croppedImage != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    imgGallery = AppUtils.resizeBitmap(binding.ivCrop.croppedImage, 1000, 1000, view = binding.loadingLayout)
                    if (!isReplace){
                        startActivity(Intent(this@CropImageActivity, ImageEditorActivity::class.java))
                        finish()
                    }
                }

            }
        }

    }
}