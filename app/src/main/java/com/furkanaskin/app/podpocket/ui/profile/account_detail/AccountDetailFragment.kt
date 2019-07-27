package com.furkanaskin.app.podpocket.ui.profile.account_detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentAccountDetailBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.ByteArrayOutputStream
import java.util.*

class AccountDetailFragment : BaseFragment<AccountDetailViewModel, FragmentAccountDetailBinding>(AccountDetailViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.fragment_account_detail

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private var isUserHavePicture = false
    private var isUserChangePicture = false
    private var profileImageUrl: ObservableField<String> = ObservableField("")

    override fun init() {
        mBinding.viewModel?.userData = viewModel.user
        if (viewModel.userData?.profilePictureUrl.isNullOrEmpty()) {
            mBinding.fabChangeImage.show()
        } else {
            isUserHavePicture = true
            mBinding.fabChangeImage.hide()
            profileImageUrl.set(viewModel.user?.profilePictureUrl.toString())
            Glide.with((activity as DashboardActivity)).load(viewModel.user?.profilePictureUrl).into(mBinding.imageViewProfilePicture)
        }

        mBinding.imageViewProfilePicture.setOnClickListener {
            showAddAvatarDialog()
        }

        mBinding.buttonSave.setOnClickListener {
            editProfile()
        }

        mBinding.editTextBirthday.showSoftInputOnFocus = false
        mBinding.editTextBirthday.keyListener = null
        mBinding.editTextBirthday.setOnClickListener {
            openDatePickerDialog()
        }
        mBinding.detailTilBirthday.setOnClickListener {
            openDatePickerDialog()
        }
    }

    private fun showAddAvatarDialog() {

        val builder = AlertDialog.Builder((activity as DashboardActivity))
        builder.setMessage("Nereden eklemek istersin?")
        builder.setPositiveButton("Galeri") { dialog, which ->
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1)
        }
        builder.setNegativeButton("Kamera") { dialog, which ->
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
            0 -> if (resultCode === Activity.RESULT_OK) {

                val selectedImage = data?.extras?.get("data")

                mBinding.imageViewProfilePicture.setImageBitmap(selectedImage as Bitmap?)
                changeProfilePicture()
            }
            1 -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage = data?.data
                mBinding.imageViewProfilePicture.setImageURI(selectedImage)
                changeProfilePicture()
            }
        }
    }

    private fun changeProfilePicture() {
        showProgress()

        if (isUserHavePicture) {
            removeProfilePicture(viewModel.mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
        } else {
            updateProfilePicture(viewModel.mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
        }
    }

    private fun removeProfilePicture(profilePicturePath: String) {
        storageRef.child(profilePicturePath).delete()
            .addOnSuccessListener {
                updateProfilePicture(viewModel.mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
            }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }.addOnSuccessListener {
            hideProgress()
        }
    }

    private fun updateProfilePicture(profilePicturePath: String) {
        val pathRef = storageRef.child(profilePicturePath)

        mBinding.imageViewProfilePicture.isDrawingCacheEnabled = true
        mBinding.imageViewProfilePicture.buildDrawingCache()
        val bitmap = (mBinding.imageViewProfilePicture.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = pathRef.putBytes(data)
        uploadTask
            .addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
            getProfilePicture()
            isUserChangePicture = true
        }
    }

    private fun editProfile() {
        val updateUser = UserEntity(
            id = viewModel.user?.id ?: 0,
            podcaster = viewModel.user?.podcaster,
            verifiedUser = viewModel.user?.verifiedUser,
            uniqueId = viewModel.user?.uniqueId ?: "",
            accountCreatedAt = viewModel.user?.accountCreatedAt,
            name = mBinding.editTextName.text?.toString(),
            userName = mBinding.editTextUserName.text?.toString(),
            surname = mBinding.editTextSurname.text?.toString(),
            birthday = mBinding.editTextBirthday.text?.toString(),
            profilePictureUrl = profileImageUrl.get(),
            email = mBinding.editTextEmail.text?.toString(),
            mostLovedCategory = mBinding.editTextMostLovedCategory.text?.toString(),
            lastPlayedPodcast = viewModel.user?.lastPlayedPodcast,
            lastPlayedEpisode = viewModel.user?.lastPlayedEpisode
        )

        viewModel.changeUserData(updateUser)

        runOnUiThread {
            viewModel.equalizeFirebase(updateUser)
            activity?.onBackPressed()
        }
    }

    private fun getProfilePicture() {
        val profilePicturePath = viewModel.mAuth.currentUser?.uid + "_" + "profile_picture.jpg"
        storageRef.child(profilePicturePath).downloadUrl.addOnSuccessListener {
            profileImageUrl.set(it.toString())
        }
    }

    private fun openDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            (activity as DashboardActivity),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                mBinding.editTextBirthday.setText("$day/${month + 1}/$year")
            },
            year, month, day
        )
        datePicker.datePicker.maxDate = c.timeInMillis
        datePicker.show()
    }
}
