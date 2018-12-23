package com.wangsun.android.qaapp.ui.components.admin

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.ui.common.CommonFuction
import com.wangsun.android.qaapp.utilities.UtilFile
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_profile.*

class ActivityEditProfile : AppCompatActivity() {

    var editable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initButton()
        if (intent.getSerializableExtra(Member.ARG_BUNDLE)!=null){
            setData(intent.getSerializableExtra(Member.ARG_BUNDLE) as Member)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initButton() {
        id_save.setOnClickListener {
            if(editable){
                if(!isInputError()){
                    id_name.isEnabled =false
                    id_phone.isEnabled =false
                    id_password.isEnabled =false
                    id_score.isEnabled =false
                    id_attempted.isEnabled =false
                    editable = false
                    id_save.text = "Edit"

                    Observable.create<Int>{emitter ->
                        emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java).updateProfile(id_name.text.toString(),
                            id_phone.text.toString(),id_password.text.toString(),id_score.text.toString().toInt(),
                            id_attempted.text.toString().toInt(),id_email.text.toString()))
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if(it>0){
                                CommonFuction.showToast(this,"Edited!")
                            }
                        },{
                            CommonFuction.showToast(this,"Error: ${it.message}")
                        })
                }

            }
            else{
                id_name.isEnabled =true
                id_email.isEnabled =true
                id_phone.isEnabled =true
                id_password.isEnabled =true
                id_score.isEnabled =true
                id_attempted.isEnabled =true
                editable = true

                id_save.text = "Save"
            }
        }
    }

    private fun setData(member: Member) {
        id_email.text = member.email

        id_name.setText(member.name)
        id_phone.setText(member.phone)
        id_password.setText(member.password)
        id_score.setText(member.score.toString())
        id_attempted.setText(member.totalAttempted.toString())

        Glide.with(id_profile_image)
            .load(UtilFile.getProfileImagePath()+"/"+member.email+".jpg")
            .into(id_profile_image)
    }


    //checking mistake inputs before sign up
    private fun isInputError(): Boolean {
        val tempEmail: String =id_email.text.toString().trim()
        val tempPassword: String =id_password.text.toString().trim()
        val tempPhone: String = id_phone.text.toString().trim()
        val tempName: String = id_name.text.toString().trim()
        val tempScore: String = id_score.text.toString().trim()
        val tempAttempted: String =id_attempted.text.toString().trim()

        if(tempEmail != ""){
            if(tempEmail.contains("@") && tempEmail.contains(".com")){
                if(tempPassword != "" && tempName != "" && tempPhone != "" && tempScore !="" && tempAttempted !=""){
                    if(tempPhone.length==10){
                        return false
                    }
                    else{
                        CommonFuction.showToast(this,"Enter 10 digits mobile no.")
                    }
                }
                else{
                    CommonFuction.showToast(this,"Empty field not allowed.")
                }
            }
            else{
                CommonFuction.showToast(this,"Invalid email format.")
            }
        }
        else{
            CommonFuction.showToast(this,"Email can not be empty.")
        }
        return true
    }

}
