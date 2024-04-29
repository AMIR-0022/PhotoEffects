package com.amar.photostyle.ui.dashboard

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.amar.photostyle.R
import com.amar.photostyle.constants.AppConstants
import com.amar.photostyle.databinding.FragmentDashBoardBinding
import com.amar.photostyle.utils.AppUtils
import com.amar.photostyle.utils.PermissionsUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import pub.devrel.easypermissions.EasyPermissions

class DashBoardFragment : Fragment(), EasyPermissions.PermissionCallbacks  {

    private lateinit var binding: FragmentDashBoardBinding

    private val viewModel: DashboardVM by lazy {
        ViewModelProvider(this)[DashboardVM::class.java]
    }

    private val adapterCategory: CategoryAdapter by lazy {
        CategoryAdapter {
            viewModel.changeActiveCatTab(it.id, true)
            viewModel.getThumbEffect(it.id.toString())
        }
    }

    private val adapterThumb: ThumbAdapter by lazy {
        ThumbAdapter {position, thumb ->
            onThumbClick(position, thumb)
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_dash_board, container, false)
        binding = FragmentDashBoardBinding.bind(myView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun init(){
        populateData()
    }

    private fun populateData() {
        binding.rvEffectCategory.adapter = adapterCategory
        viewModel.categoryList.observe(viewLifecycleOwner) {
            it?.let {
                adapterCategory.setData(it)
            }
        }

        binding.rvEffectThumb.adapter = adapterThumb
        viewModel.thumbList.observe(viewLifecycleOwner) {
            it?.let {
                adapterThumb.setData(it)
            }
        }
    }

    private fun onThumbClick(position: Int, thumb: Thumb) {
        if (PermissionsUtils.hasPermissions(requireContext())){
            if (thumb.isDownloaded) {
                if (position<=6){
                    AppUtils.preDownloadImg(requireActivity(), binding.loadingLayout, thumb)
                } else {
                    Toast.makeText(requireContext(), "Network connection failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "First download frame to use", Toast.LENGTH_SHORT).show()
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                requireContext().resources.getString(R.string.string_permission_request),
                AppConstants.SELECT_IMAGE_FROM_GALLERY_CODE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(p0: Int, p1: MutableList<String>) {
        TODO("Not yet implemented")
    }

}