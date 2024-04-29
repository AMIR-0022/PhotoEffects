package com.amar.photostyle.ui.image_gallery

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.photostyle.ui.image_gallery.gallery_folder.GalleryFolder
import com.amar.photostyle.ui.image_gallery.gallery_image.GalleryImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GalleryVM: ViewModel(), CoroutineScope {

    var folderList = ArrayList<GalleryFolder>()
    var imagePathList = ArrayList<String>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private var imagesLiveData: MutableLiveData<List<GalleryImage>?> = MutableLiveData()
    private var foldersLiveData: MutableLiveData<List<GalleryFolder>?> = MutableLiveData()

    fun getImageList(): MutableLiveData<List<GalleryImage>?> {
        return imagesLiveData
    }

    fun getFolderList(): MutableLiveData<List<GalleryFolder>?> {
        return foldersLiveData
    }

    fun getAllImages(path: String?, context: Context) {
        launch(Dispatchers.Main) {
            imagesLiveData.value = withContext(Dispatchers.IO) {
                loadImagesFromCardSpace(path, context)
            }
        }
    }
    fun getAllFolders(context: Context) {
        launch(Dispatchers.Main) {
            foldersLiveData.value = withContext(Dispatchers.IO) {
                loadFoldersFromCardSpace(context)
            }
        }
    }


    private fun loadImagesFromCardSpace(path: String?, context: Context): ArrayList<GalleryImage> {
        var images = ArrayList<GalleryImage>()

        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED + " ASC"
        val cursor = context.contentResolver.query(allImagesUri, projection,
            MediaStore.Images.Media.DATA + " like ? ", arrayOf("%$path%"), orderBy)

        try {
            cursor?.moveToFirst()
            do {
                val img = GalleryImage()
                img.imageName =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                img.imagePath =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                img.imageSize =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                images.add(img)
            } while (cursor!!.moveToNext())
            cursor.close()
            val reSelection = ArrayList<GalleryImage>()
            for (i in images.size - 1 downTo -1 + 1) {
                reSelection.add(images[i])
            }
            images = reSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }
    private fun loadFoldersFromCardSpace(context: Context): ArrayList<GalleryFolder> {
        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED + " DESC"
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        try {
            val cursor =
                context.contentResolver.query(allImagesUri, projection, null, null, orderBy)
            if (cursor != null) {
                cursor.moveToFirst()
                do {
                    val galleryFolder = GalleryFolder()
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    val folder =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val dataPath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    var folderPaths =
                        dataPath?.substring(0, dataPath.lastIndexOf("$folder/")).toString()
                    folderPaths = "$folderPaths$folder/"
                    if (!imagePathList.contains(folderPaths)) {
                        imagePathList.add(folderPaths)
                        galleryFolder.path = folderPaths
                        galleryFolder.folderName = folder
                        galleryFolder.firstImage = dataPath

                        if (folder == "Camera")
                            folderList.add(0, galleryFolder)
                        else
                            folderList.add(galleryFolder)
                    } else {
                        for (i in folderList.indices) {
                            if (folderList[i].path == folderPaths) {
                                folderList[i].firstImage = dataPath
                            }
                        }
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return folderList
    }

}