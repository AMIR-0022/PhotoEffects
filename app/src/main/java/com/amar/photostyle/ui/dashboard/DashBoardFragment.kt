package com.amar.photostyle.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.amar.photostyle.R
import com.amar.photostyle.databinding.FragmentDashBoardBinding

class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashBoardBinding

    private val viewModel: CategoryVM by lazy {
        ViewModelProvider(this)[CategoryVM::class.java]
    }

    private val adapter: CategoryAdapter by lazy {
        CategoryAdapter{
            viewModel.changeActiveCatTab(it.id, true)
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
        setOnClickListener()
    }

    private fun populateData() {
        binding.rvEffectCategory.adapter = adapter
        viewModel.categoryList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setData(it)
            }
        }
    }

    private fun setOnClickListener() {

    }

}