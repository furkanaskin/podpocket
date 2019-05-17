package com.furkanaskin.app.podpocket.ui.profile.account_detail


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.furkanaskin.app.podpocket.R
import com.furkanaskin.app.podpocket.core.BaseFragment
import com.furkanaskin.app.podpocket.databinding.FragmentAccountDetailBinding
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.ByteArrayOutputStream

class AccountDetailFragment : BaseFragment<AccountDetailViewModel, FragmentAccountDetailBinding>(AccountDetailViewModel::class.java) {

    override fun getLayoutRes(): Int = R.layout.fragment_account_detail

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    var isUserHavePicture = false
    var isUserChangePicture = false
    var profileImageUrl: ObservableField<String> = ObservableField("")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.viewModel?.userData = user
        if (user!!.profilePictureUrl.isNullOrEmpty()) {

        } else {
            isUserHavePicture = true
            profileImageUrl.set(user!!.profilePictureUrl.toString())
            Glide.with(this.activity!!).load(user!!.profilePictureUrl).into(mBinding.imageViewProfilePicture)
        }

        mBinding.imageViewProfilePicture.setOnClickListener {
            showAddAvatarDialog()

        }

        mBinding.buttonSave.setOnClickListener {
            editProfile()
        }
    }

    private fun showAddAvatarDialog() {

        val builder = AlertDialog.Builder(this.activity!!)
        // Display a message on alert dialog
        builder.setMessage("Nereden eklemek istersin?")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Galeri") { dialog, which ->
            // Do something when user press the positive button
            val pickPhoto = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
            removeProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
        } else {
            updateProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
        }
    }

    private fun removeProfilePicture(profilePicturePath: String) {
        storageRef.child(profilePicturePath).delete().addOnSuccessListener {
            // File deleted successfully
            updateProfilePicture(mAuth.currentUser?.uid + "_" + "profile_picture.jpg")
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
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

        var uploadTask = pathRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            isUserChangePicture = true
            getProfilePicture()

        }
    }

    private fun getProfilePicture() {
        val profilePicturePath = mAuth.currentUser?.uid + "_" + "profile_picture.jpg"
        storageRef.child(profilePicturePath).downloadUrl.addOnSuccessListener {
            profileImageUrl.set(it.toString())
            hideProgress()
        }
    }

    fun editProfile() {
        val user = UserEntity(
                id = user?.id ?: 0,
                uniqueId = user?.uniqueId ?: "",
                name = mBinding.editTextName.text?.toString(),
                surname = mBinding.editTextSurname.text?.toString(),
                birthday = mBinding.etBirthday.text?.toString(),
                profilePictureUrl = profileImageUrl.get(),
                email = mBinding.editTextEmail.text?.toString(),
                mostLovedCategory = mBinding.editTextMostLovedCategory.text?.toString(),
                lastPlayedPodcast = user?.lastPlayedPodcast,
                lastPlayedEpisode = user?.lastPlayedEpisode)

        viewModel.changeUserData(user)

        runOnUiThread {
            activity?.onBackPressed()
        }

    }


}
