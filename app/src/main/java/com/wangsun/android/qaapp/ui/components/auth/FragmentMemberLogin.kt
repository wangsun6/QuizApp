package com.wangsun.android.qaapp.ui.components.auth


import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tbruyelle.rxpermissions2.RxPermissions

import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.ui.common.CommonFuction
import com.wangsun.android.qaapp.ui.common.compressor.ImgCompressor
import com.wangsun.android.qaapp.utilities.UtilFile
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_member_login.*
import kotlinx.android.synthetic.main.progress_dialog.*
import java.io.File





@SuppressLint("CheckResult")
class FragmentMemberLogin : Fragment() {

    var mImageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initButton()
    }

    private fun initButton(){

        id_select_login.setOnClickListener {
            id_layout_signup.visibility = View.GONE
            id_layout_login.visibility = View.VISIBLE
        }

        id_select_signup.setOnClickListener {
            id_layout_login.visibility = View.GONE
            id_layout_signup.visibility = View.VISIBLE
        }

        id_select_admin_section.setOnClickListener {
            (activity as ActivityAuth).startFragmentAdmin()
        }

        id_signup.setOnClickListener {
            if(!isInputError(id_new_email.text.toString().trim(),id_new_password.text.toString().trim(),0)){
                searchForEmail(id_new_email.text.toString())
            }
        }
        id_login.setOnClickListener {
            if(!isInputError(id_email.text.toString().trim(),id_password.text.toString().trim(),1)) {
                checkPassword(id_email.text.toString(),id_password.text.toString())
            }
        }

        id_image_picker.setOnClickListener {
            getPermission()
        }
    }

    private fun getPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (granted) {
                    // All requested permissions are granted
                    (activity as ActivityAuth).imagePicker()
                } else {
                    // At least one permission is denied
                    CommonFuction.showToast(context,"You have to give both permission.")
                    getPermission()
                }
            }

//        rxPermissions
//            .requestEach(
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_PHONE_STATE
//            )
//            .subscribe { // will emit 2 Permission objects
//                    permission ->
//                if (permission.granted) {
//                    // `permission.name` is granted !
//                    if(permission.name == Manifest.permission.CAMERA){
//
//                    }
//
//                } else if (permission.shouldShowRequestPermissionRationale) {
//                    // Denied permission without ask never again
//                } else {
//                    // Denied permission with ask never again
//                    // Need to go to the settings
//                }
//            }
    }

    fun onResultFromActivity(file: File){
        Glide.with(this).load(file).into(id_image_picker)
        mImageFile = file
    }

    private fun compressAndSaveInSd(email: String){
        mImageFile?.let {
            ImgCompressor(context!!)
                .setName(id_new_email.text.toString().trim()+".jpg")
                .setDestinationDirectoryPath(UtilFile.getProfileImagePath())
                .compressToFileAsFlowable(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    //********save data*****************
                    CommonFuction.showToast(context!!,"Profile saved.")
                    UtilSharedPreference.setIsLoggedIn(context!!,true)
                    UtilSharedPreference.setLoggedInEmail(context!!,email)
                    UtilSharedPreference.setUserType(context!!,UtilSharedPreference.MEMBER)
                    id_progress.visibility = View.GONE
                    (activity as ActivityAuth).startActivityHome()
                }
        }

    }

    //checking mistake inputs before sign up
    private fun isInputError(temp_email: String, temp_password: String, option: Int): Boolean {
        val phone: String = id_new_phone.text.toString().trim()
        val name: String = id_new_name.text.toString().trim()

        if(temp_email != ""){
            if(temp_email.contains("@") && temp_email.contains(".com")){
                if(temp_password != ""){
                    if(option==0){
                        if(name != "" && phone != ""){
                            if(phone.length==10){
                                if(mImageFile!=null){
                                    return false
                                }
                                else{
                                    CommonFuction.showToast(context,"Set display image.")
                                }
                            }
                            else{
                                CommonFuction.showToast(context,"Enter 10 digits mobile no.")
                            }
                        }
                        else{
                            CommonFuction.showToast(context,"Empty field not allowed.")
                        }
                    }
                    else{//option1 for login option2 for signup
                        return false
                    }
                }
                else{
                    CommonFuction.showToast(context,"Password can not be empty.")
                }
            }
            else{
                CommonFuction.showToast(context,"Invalid email format.")
            }
        }
        else{
            CommonFuction.showToast(context,"Email can not be empty.")
        }
        return true
    }

    //checking if email already exist to avoid duplicate signup
    //For sign up
    private fun searchForEmail(email: String){
        id_progress.visibility = View.VISIBLE

        Observable.create<List<Member>>{ emitter ->
            emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java).getMemberOnce(email))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res->
                if(res.isEmpty()){
                    val newMember = Member(id_new_email.text.toString(),id_new_name.text.toString()
                        ,id_new_phone.text.toString(),id_new_password.text.toString())

                    Observable.create<Long>{emitter ->
                        emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java).insertCallback(newMember))
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            println("Total users: ${it}")
                            compressAndSaveInSd(email)
                        },{
                            id_progress.visibility = View.GONE
                            CommonFuction.showToast(context,"Error: ${it.message}")
                        })
                }
                else{
                    id_progress.visibility = View.GONE
                    CommonFuction.showToast(context,"Email already exist, try different email.")
                }
            },{
                id_progress.visibility = View.GONE
                println("Eror: ${it.message}")
            })

    }

    //check if email exist, if so then check password is correct or not
    //for login
    private fun checkPassword(email: String, password: String) {

        println("test1: under chacek password")
        id_progress.visibility = View.VISIBLE

        Observable.create<List<Member>>{ emitter ->
            emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java).getMemberOnce(email))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({res->
                println("test1: under chacek password")
                id_progress.visibility = View.GONE
                if(res.isEmpty()){
                    //stop here
                    CommonFuction.showToast(context,": Email doesn't exist")
                }
                else{
                    if(password == res[0].password){
                        //CommonFuction.showToast(context,"Password is correct")

                        //********save info*****************
                        UtilSharedPreference.setIsLoggedIn(context!!,true)
                        UtilSharedPreference.setLoggedInEmail(context!!,email)
                        UtilSharedPreference.setUserType(context!!,UtilSharedPreference.MEMBER)
                        (activity as ActivityAuth).startActivityHome()
                    }
                    else{
                        CommonFuction.showToast(context,": Password is incorrect")
                    }
                }
            },{
                id_progress.visibility = View.GONE
                println("Eror: ${it.message}")
            })
    }


}
