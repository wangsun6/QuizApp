package com.wangsun.android.qaapp.ui.components.auth

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Admin
import com.wangsun.android.qaapp.sqlite.tables.AdminViewModel
import com.wangsun.android.qaapp.ui.common.CommonFuction
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_admin_login.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.progress_dialog.*

@SuppressLint("CheckResult")
class FragmentAdminLogin : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_login, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initButton()
    }

    fun initButton(){

        id_select_login.setOnClickListener {
            id_layout_signup.visibility = View.GONE
            id_layout_login.visibility = View.VISIBLE
        }

        id_select_signup.setOnClickListener {
            id_layout_login.visibility = View.GONE
            id_layout_signup.visibility = View.VISIBLE
        }

        id_select_member_section.setOnClickListener {
            (activity as ActivityAuth).startFragmentMember()
        }


        id_signup.setOnClickListener {
            if(!isInputError(id_new_email.text.toString().trim(),id_new_password.text.toString().trim())){
                searchForEmail(id_new_email.text.toString())
            }
        }
        id_login.setOnClickListener {
            if(!isInputError(id_email.text.toString().trim(),id_password.text.toString().trim())) {
                checkPassword(id_email.text.toString(),id_password.text.toString())
            }
        }
    }


    //checking mistake inputs before sign up
    private fun isInputError(temp_email: String, temp_password: String): Boolean {

        if(!temp_email.equals("")){
            if(temp_email.contains("@") && temp_email.contains(".com")){
                if(!temp_password.equals("")){
                    return false
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
    @SuppressLint("CheckResult")
    fun searchForEmail(email: String){
        id_progress.visibility = View.VISIBLE

        Observable.create<List<Admin>>{emitter->
            emitter.onNext(AdminViewModel((activity as ActivityAuth).application).getAdminOnce(email))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res->
                id_progress.visibility = View.GONE
                if(res.isEmpty()){
                    //CommonFuction.showToast(context,": Email doesn't exist")
                    val newAdmin = Admin(id_new_email.text.toString(),id_new_password.text.toString())

                    Observable.create<Long>{emitter ->
                            emitter.onNext(ViewModelProviders.of(this).get(AdminViewModel::class.java).insertCallback(newAdmin))
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            println("Total Admins: ${it}")

                            //********save data*****************
                            CommonFuction.showToast(context!!,"Profile saved.")
                            UtilSharedPreference.setIsLoggedIn(context!!,true)
                            UtilSharedPreference.setLoggedInEmail(context!!,email)
                            UtilSharedPreference.setUserType(context!!, UtilSharedPreference.ADMIN)
                            (activity as ActivityAuth).startActivityAdminHome()

                        },{
                            id_progress.visibility = View.GONE
                            CommonFuction.showToast(context,"Error: ${it.message}")
                        })
                }
                else{
                    CommonFuction.showToast(context,": Email already exist, try different email.")
                }
            },{
                id_progress.visibility = View.GONE
                println("Error: ${it.message}")
            })
    }

    //check if email exist, if so then check password is correct or not
    private fun checkPassword(email: String, password: String) {
        id_progress.visibility = View.VISIBLE

        Observable.create<List<Admin>>{emitter->
            emitter.onNext(ViewModelProviders.of(this).get(AdminViewModel::class.java).getAdminOnce(email))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res->
                id_progress.visibility = View.GONE
                if(res.size==0){
                    //stop here
                    CommonFuction.showToast(context,": Email doesn't exist")
                } else{
                    if(password.equals(res[0].password)){
                        //CommonFuction.showToast(context,": Password is correct")

                        //********save data*****************
                        UtilSharedPreference.setIsLoggedIn(context!!,true)
                        UtilSharedPreference.setLoggedInEmail(context!!,email)
                        UtilSharedPreference.setUserType(context!!, UtilSharedPreference.ADMIN)
                        (activity as ActivityAuth).startActivityAdminHome()
                    } else{
                        CommonFuction.showToast(context,": Password is incorrect")
                    }
                }
            },{
                id_progress.visibility = View.GONE
                println("Error: ${it.message}")
            })
    }
}
