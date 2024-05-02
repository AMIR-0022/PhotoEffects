package com.amar.photostyle.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardVM: ViewModel() {

    val categoryList: LiveData<List<Category>>
    private var _categoryList = MutableLiveData<List<Category>>()
    private var catPreviousPos = 0

    val thumbList: LiveData<List<Thumb>>
    private var _thumbList = MutableLiveData<List<Thumb>>()


    init {
        categoryList = _categoryList
        getCategoryList()

        thumbList = _thumbList
        getThumbEffect("0")
    }

    private fun getCategoryList() {
        val list = arrayListOf<Category>()
        list.clear()
        list.add(Category(id = 0, title = "Mask", isChecked = true))
        list.add(Category(id = 1, title = "Overlay"))
        list.add(Category(id = 2, title = "Pixel"))
        _categoryList.postValue(list)
    }

    fun changeActiveCatTab(position: Int, value: Boolean) {
        // Get the current list of categories
        val currentList = _categoryList.value?.toMutableList() ?: return

        // change the value of item check or not
        currentList[position].isChecked = value
        currentList[catPreviousPos].isChecked = false
        catPreviousPos = position

        // update the item list
        _categoryList.postValue(currentList)
    }

    fun getThumbEffect(catId: String) {
        val list = arrayListOf<Thumb>()
        when (catId) {
            "0" -> {
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_1.webp",
                    cover = "file:///android_asset/mask_thumbs/1.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_2.webp",
                    cover = "file:///android_asset/mask_thumbs/2.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_3.webp",
                    cover = "file:///android_asset/mask_thumbs/3.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_4.webp",
                    cover = "file:///android_asset/mask_thumbs/4.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_5.webp",
                    cover = "file:///android_asset/mask_thumbs/5.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_6.webp",
                    cover = "file:///android_asset/mask_thumbs/6.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/mask_effects/mask_7.webp",
                    cover = "file:///android_asset/mask_thumbs/7.webp",
                    blend = "9", isDownloaded = true))
            }
            "1" -> {
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_1.webp",
                    cover = "file:///android_asset/overlay_thumbs/1.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_2.webp",
                    cover = "file:///android_asset/overlay_thumbs/2.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_3.webp",
                    cover = "file:///android_asset/overlay_thumbs/3.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_4.webp",
                    cover = "file:///android_asset/overlay_thumbs/4.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_5.webp",
                    cover = "file:///android_asset/overlay_thumbs/5.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_6.webp",
                    cover = "file:///android_asset/overlay_thumbs/6.webp",
                    blend = "10", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/overlay_effect/overlay_7.webp",
                    cover = "file:///android_asset/overlay_thumbs/7.webp",
                    blend = "10", isDownloaded = true))
            }
            "2" -> {
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_1.webp",
                    cover = "file:///android_asset/pixel_thumbs/1.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_2.webp",
                    cover = "file:///android_asset/pixel_thumbs/2.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_3.webp",
                    cover = "file:///android_asset/pixel_thumbs/3.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_4.webp",
                    cover = "file:///android_asset/pixel_thumbs/4.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_5.webp",
                    cover = "file:///android_asset/pixel_thumbs/5.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_6.webp",
                    cover = "file:///android_asset/pixel_thumbs/6.webp",
                    blend = "9", isDownloaded = true))
                list.add(Thumb(
                    mask = "file:///android_asset/pixel_effect/pixel_7.webp",
                    cover = "file:///android_asset/pixel_thumbs/7.webp",
                    blend = "9", isDownloaded = true))
            }
        }
        _thumbList.postValue(list)
    }

}