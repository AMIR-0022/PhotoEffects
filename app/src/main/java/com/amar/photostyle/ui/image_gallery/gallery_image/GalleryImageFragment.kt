package com.amar.photostyle.ui.image_gallery.gallery_image

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.amar.photostyle.R
import com.amar.photostyle.constants.AppConstants
import com.amar.photostyle.databinding.FragmentGalleryImageBinding
import com.amar.photostyle.ui.crop_image.CropImageActivity
import com.amar.photostyle.ui.image_gallery.GalleryVM
import com.amar.photostyle.utils.AppUtils
import com.theartofdev.edmodo.cropper.CropImage

class GalleryImageFragment : Fragment() {

    private lateinit var binding: FragmentGalleryImageBinding
    private var isReplace: Boolean = false

    private val viewModel: GalleryVM by lazy {
        ViewModelProvider(this)[GalleryVM::class.java]
    }

    private val imageAdapter: GalleryImageAdapter by lazy {
        GalleryImageAdapter{ position, image ->
            onGalleryImageClick(position, image)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_gallery_image, container, false)
        binding = FragmentGalleryImageBinding.bind(myView)

        isReplace = activity?.intent?.getBooleanExtra(AppConstants.KEY_IS_REPLACE, false) == true

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: GalleryImageFragmentArgs by navArgs()
        val name: String = args.folderName
        viewModel.getAllImages(name, requireContext())

    }

    private fun init() {
        populateData()
    }

    private fun populateData(){
        binding.rvGalleryImages.adapter = imageAdapter
        viewModel.getImageList().observe(viewLifecycleOwner) {
            it?.let {
                imageAdapter.setData(it)
            }
        }
    }

    private fun onGalleryImageClick(position: Int, image: GalleryImage) {
        image.imagePath?.let {
            val intent = Intent(requireContext(), CropImageActivity::class.java)
            intent.putExtra(AppConstants.KEY_PATH, it)
            intent.putExtra(AppConstants.KEY_IS_REPLACE, isReplace)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                if (result.uri.toString() != null){
                    AppUtils.selectedImagePlus.value = result.uri
                }
                if (isReplace) {
                    activity?.finish()
                } else {
                    context?.let { Toast.makeText(requireContext(), "Please Try Again", Toast.LENGTH_SHORT).show() }
                }
            } else {
                context?.let { Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show() }
            }
        }
    }

}