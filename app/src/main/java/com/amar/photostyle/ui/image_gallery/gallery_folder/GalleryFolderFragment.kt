package com.amar.photostyle.ui.image_gallery.gallery_folder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amar.photostyle.R
import com.amar.photostyle.databinding.FragmentGalleryFolderBinding
import com.amar.photostyle.ui.dashboard.DashboardVM
import com.amar.photostyle.ui.image_gallery.GalleryVM

class GalleryFolderFragment : Fragment() {

    private lateinit var binding: FragmentGalleryFolderBinding

    private val viewModel: GalleryVM by lazy {
        ViewModelProvider(this)[GalleryVM::class.java]
    }

    private val folderAdapter: GalleryFolderAdapter by lazy {
        GalleryFolderAdapter {position, folder ->
            onGalleryFolderClick(position, folder)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_gallery_folder, container, false)
        binding = FragmentGalleryFolderBinding.bind(myView)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllFolders(requireContext())
    }

    private fun init() {
        populateData()
    }

    private fun populateData(){
        binding.rvGalleryFolder.adapter = folderAdapter
        parentFragment?.viewLifecycleOwner?.let {
            viewModel.getFolderList().observe(it, Observer { list ->
                if (list != null) {
                    if (list.isEmpty()){
                        binding.tvEmptyGallery.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptyGallery.visibility = View.GONE

                        Log.d("UserGalleryData", "this is a data of user gallery")
                        folderAdapter.setData(list as ArrayList<GalleryFolder>)
                    }
                }
            })
            binding.pbGalleryFolder.isVisible = false
        }
    }

    private fun onGalleryFolderClick(position: Int, folder: GalleryFolder) {
        if (folder.folderName != null) {
            val action = GalleryFolderFragmentDirections.actionGalleryFolderFragmentToGalleryImageFragment(folderName = "${folder.folderName}")
            findNavController().navigate(action)

        }
    }

}