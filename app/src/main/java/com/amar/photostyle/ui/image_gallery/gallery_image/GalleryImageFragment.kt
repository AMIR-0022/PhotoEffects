package com.amar.photostyle.ui.image_gallery.gallery_image

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.amar.photostyle.R
import com.amar.photostyle.databinding.FragmentGalleryImageBinding
import com.amar.photostyle.ui.image_gallery.GalleryVM

class GalleryImageFragment : Fragment() {

    private lateinit var binding: FragmentGalleryImageBinding

    private val viewModel: GalleryVM by lazy {
        ViewModelProvider(this)[GalleryVM::class.java]
    }

    private val imageAdapter: GalleryImageAdapter by lazy {
        GalleryImageAdapter{ position, image ->
            Toast.makeText(requireContext(), "$position\n${image.imageName}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_gallery_image, container, false)
        binding = FragmentGalleryImageBinding.bind(myView)

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

}