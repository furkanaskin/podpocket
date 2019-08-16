package com.furkanaskin.app.podpocket.ui.after_register

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Observable
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseActivity
import com.furkanaskin.app.podpocket.databinding.ActivityAfterRegisterBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.Calendar
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 21.04.2019
 */

class AfterRegisterActivity : BaseActivity<AfterRegisterViewModel, ActivityAfterRegisterBinding>(AfterRegisterViewModel::class.java) {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private var profileImageUrl: String = ""

    override fun getLayoutRes() = R.layout.activity_after_register

    override fun initViewModel(viewModel: AfterRegisterViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (isCurrentUserIDAvailable()) {
            true -> binding.buttonDone.show()
            false -> binding.buttonDone.hide()
        }

        binding.editTextBirthday.showSoftInputOnFocus = false
        binding.editTextBirthday.keyListener = null
        binding.editTextBirthday.setOnClickListener {
            openDatePickerDialog()
        }
        binding.tilBirthday.setOnClickListener {
            openDatePickerDialog()
        }

        binding.buttonDone.setOnClickListener {
            showProgress()

            if (viewModel.getValidationMessages() && viewModel.userID.get() != null) {

                val willBeUpdated = UserEntity(
                    id = user?.id ?: 0,
                    podcaster = user?.podcaster,
                    verifiedUser = user?.verifiedUser,
                    accountCreatedAt = user?.accountCreatedAt,
                    uniqueId = user?.uniqueId ?: "",
                    email = user?.email,
                    userName = viewModel.userName.get(),
                    name = viewModel.name.get(),
                    surname = viewModel.surname.get(),
                    birthday = viewModel.userBirthDay.get(),
                    profilePictureUrl = viewModel.profileImageUrl.get()
                )

                doAsync {

                    viewModel.db?.userDao()?.updateUser(willBeUpdated)

                    runOnUiThread {
                        viewModel.saveSuccess.set(true)
                        hideProgress()
                    }
                }

                viewModel.insertUserToFirebase(willBeUpdated)
            } else hideProgress()
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
        builder.setMessage("Nereden eklemek istersin?")
        builder.setPositiveButton("Galeri") { _, _ ->
            // Do something when user press the positive button
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1)
        }
        builder.setNegativeButton("Kamera") { _, _ ->
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        }
        builder.setNeutralButton("Vazgeç") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {

                val selectedImage = data?.extras?.get("data")

                binding.imageViewUser.setImageBitmap(selectedImage as Bitmap?)
                binding.fabChangeImage.hide()
                updateProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
            }
            1 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.data
                binding.imageViewUser.setImageURI(selectedImage)
                binding.fabChangeImage.hide()
                updateProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
            }
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
        val uploadTask = pathRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
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

    private fun openDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                viewModel.userBirthDay.set("$day/${month + 1}/$year")
            },
            year, month, day
        )
        datePicker.datePicker.maxDate = c.timeInMillis
        datePicker.show()
    }
}