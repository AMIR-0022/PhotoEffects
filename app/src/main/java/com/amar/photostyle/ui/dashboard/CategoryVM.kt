package com.amar.photostyle.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.photostyle.utils.catCurrentPos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryVM: ViewModel() {

    val effectCategory: LiveData<List<Category>>
    private var _effectCategory = MutableLiveData<List<Category>>()

    private var catPreviousPos = 0

    init {
        effectCategory = _effectCategory
        getCategoryList()
    }

    private fun getCategoryList() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = arrayListOf<Category>()
            list.clear()
            list.add(Category(id = 0, title = "Mask", isChecked = true))
            list.add(Category(id = 1, title = "Pixel"))
            list.add(Category(id = 2, title = "Overlay"))
            _effectCategory.postValue(list)
        }
    }

    fun changeActiveCatTab(position: Int, value: Boolean) {
        // Get the current list of categories
        val currentList = _effectCategory.value?.toMutableList() ?: return

        // change the value of item check or not
        currentList[position].isChecked = value
        currentList[catPreviousPos].isChecked = false
        catPreviousPos = position

        // update the item list
        _effectCategory.postValue(currentList)
    }

}