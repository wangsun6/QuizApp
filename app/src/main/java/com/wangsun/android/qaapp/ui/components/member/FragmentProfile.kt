package com.wangsun.android.qaapp.ui.components.member


import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.utilities.UtilFile
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import kotlinx.android.synthetic.main.fragment_profile.*

class FragmentProfile : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        Glide.with(context!!)
            .load(UtilFile.getProfileImagePath()+"/"+UtilSharedPreference.getLoggedInEmail(context!!)+".jpg")
            .into(id_profile_image)


        getProfile()
    }

    private fun getProfile() {
        MemberViewModel((activity as ActivityMemberHome).application).getMember(UtilSharedPreference.getLoggedInEmail(context!!)).observe(this,
                Observer<List<Member>> {
                it?.let {p->
                    if(p.isNotEmpty()){
                        id_name.text = p[0].name
                        id_email.text = p[0].email
                        id_mobile.text = p[0].phone
                        id_score.text = p[0].score.toString()
                        id_attempted.text = p[0].totalAttempted.toString()
                    }
                }
            })
    }


}
