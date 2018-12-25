package com.wangsun.android.qaapp.ui.components.auth

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Admin
import com.wangsun.android.qaapp.sqlite.tables.AdminViewModel
import com.wangsun.android.qaapp.ui.components.ConstantUi
import com.wangsun.android.qaapp.ui.components.admin.ActivityAdminHome
import com.wangsun.android.qaapp.ui.components.member.ActivityMemberHome
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import java.io.File

class ActivityAuth : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        //nothing..this will initialize database at startUp
        Observable.create<List<Admin>>{emitter ->
            val res =  ViewModelProviders.of(this).get(AdminViewModel::class.java).getAdminOnce("test")
            emitter.onNext(res)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()


        startFragmentMember()
    }

    override fun onStart() {
        super.onStart()
        if(UtilSharedPreference.getIsLoggedIn(this)){
            if(UtilSharedPreference.getUserType(this).equals(UtilSharedPreference.ADMIN))
                startActivityAdminHome()//start admin
            else
                startActivityHome()//start member
        }
    }



    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val imageUri = CropImage.getPickImageResultUri(this, data)

            CropImage.activity(imageUri)
                .setBorderLineColor(ContextCompat.getColor(this,R.color.yellow))
                .setBorderCornerColor(ContextCompat.getColor(this,R.color.colorAccent))
                .setActivityTitle("Crop")
                .setCropMenuCropButtonTitle("Done")
                .setAspectRatio(1, 1)
                //.setCropMenuCropButtonIcon(R.drawable.ic_done_white_36dp)
                .setAllowFlipping(false)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0f)
                .start(this)
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)

            val resultUri = result.uri

            sendProfileImage(File(resultUri.path))
        }
        else if((requestCode == ConstantUi.Intent.ACTIVITY_MEMBER_HOME || requestCode == ConstantUi.Intent.ACTIVITY_ADMIN_HOME) && resultCode == Activity.RESULT_OK){
            data?.let {
                if(it.getBooleanExtra("isExit",false)){
                    finish()
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }


    }


    fun startFragmentMember(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.id_content, FragmentMemberLogin()).commit()
    }

    fun startFragmentAdmin(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.id_content, FragmentAdminLogin()).commit()
    }

    //start admin home page
    fun startActivityAdminHome(){
        startActivityForResult(Intent(this,ActivityAdminHome::class.java),ConstantUi.Intent.ACTIVITY_ADMIN_HOME)
    }

    //start member home page
    fun startActivityHome(){
        startActivityForResult(Intent(this,ActivityMemberHome::class.java),ConstantUi.Intent.ACTIVITY_MEMBER_HOME)
    }

    //start Image Picker
    fun imagePicker(){
        CropImage.startPickImageActivity(this)
    }


    private fun sendProfileImage(file: File) {
        val fragment = supportFragmentManager.findFragmentById(R.id.id_content)
        if(fragment is FragmentMemberLogin)
            fragment.onResultFromActivity(file)
    }
}
