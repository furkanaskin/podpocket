package com.furkanaskin.app.podpocket.ui.after_register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Observable
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityAfterRegisterBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream


/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterActivity : BaseActivity<AfterRegisterViewModel, ActivityAfterRegisterBinding>(AfterRegisterViewModel::class.java) {

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    var profileImageUrl: String = ""

    override fun getLayoutRes() = R.layout.activity_after_register

    override fun initViewModel(viewModel: AfterRegisterViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (isCurrentUserIDAvailable()) {
            true -> binding.buttonDone.visibility = View.VISIBLE
            false -> binding.buttonDone.visibility = View.GONE
        }



        binding.buttonDone.setOnClickListener {
            if (viewModel.getValidationMessages() && viewModel.userID.get() != null) {
                viewModel.progressBarView.set(true)


                val willBeUpdated = UserEntity(
                        id = user?.id ?: 0,
                        uniqueId = user?.uniqueId ?: "",
                        email = user?.email,
                        userName = viewModel.userName.get(),
                        name = viewModel.name.get(),
                        surname = viewModel.surname.get(),
                        birthday = viewModel.userBirthDay.get(),
                        profilePictureUrl = viewModel.profileImageUrl.get())

                doAsync {

                    viewModel.db.userDao().updateUser(willBeUpdated)
                    viewModel.saveSuccess.set(true)

                }
                viewModel.progressBarView.set(false)

            }
        }

        binding.fabChangeImage.setOnClickListener {
            showAddAvatarDialog()
        }
        binding.imageViewUser.setOnClickListener {
            showAddAvatarDialog()

        }


        viewModel.saveSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Thread.sleep(100)
                val intent = Intent(this@AfterRegisterActivity, DashboardActivity::class.java)
                runOnUiThread {
                    startActivity(intent)
                    finish()
                }
            }
        })
        getUser()
    }

    private fun isCurrentUserIDAvailable(): Boolean {
        return viewModel.userID.get() != null
    }

    private fun showAddAvatarDialog() {
        val builder = AlertDialog.Builder(this)
        // Display a message on alert dialog
        builder.setMessage("Nereden eklemek istersin?")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Galeri") { dialog, which ->
            // Do something when user press the positive button
            val pickPhoto = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 1)//one can be replaced with any action code
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton("Kamera") { dialog, which ->
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)//zero can be replaced with any action code
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Vazgeç") { _, _ ->
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {

                val selectedImage = data?.extras?.get("data")

                binding.imageViewUser.setImageBitmap(selectedImage as Bitmap?)
                binding.fabChangeImage.visibility = View.GONE
                updateProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
            }
            1 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.data
                binding.imageViewUser.setImageURI(selectedImage)
                binding.fabChangeImage.visibility = View.GONE
                updateProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")

            }

        }
    }


    private fun removeProfilePicture(profilePicturePath: String) {
        storageRef.child(profilePicturePath).delete().addOnSuccessListener {
            // File deleted successfully
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
    }

    private fun updateProfilePicture(profilePicturePath: String) {
        showProgress()
        val pathRef = storageRef.child(profilePicturePath)

        binding.imageViewUser.isDrawingCacheEnabled = true
        binding.imageViewUser.buildDrawingCache()
        val bitmap = (binding.imageViewUser.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = pathRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            getProfilePicture()

        }
    }


    private fun getProfilePicture() {
        val profilePicturePath = mAuth.currentUser?.uid + "_" + "profile_picture.jpg"
        storageRef.child(profilePicturePath).downloadUrl.addOnSuccessListener {
            viewModel.profileImageUrl.set(it.toString())
            profileImageUrl = it.toString()
            hideProgress()

        }
    }
}