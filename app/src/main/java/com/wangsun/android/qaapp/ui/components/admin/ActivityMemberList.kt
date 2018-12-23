package com.wangsun.android.qaapp.ui.components.admin

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.ui.adapter.AdapterMemberList
import com.wangsun.android.qaapp.ui.components.ConstantUi
import kotlinx.android.synthetic.main.activity_member_list.*

class ActivityMemberList : AppCompatActivity(), AdapterMemberList.OnItemClickListener {


    var mAdapter = AdapterMemberList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)

        initAdapter()
    }

    override fun onItemClick(member: Member) {
        val tempIntent = Intent(this, ActivityEditProfile::class.java)
        tempIntent.putExtra(Member.ARG_BUNDLE,member)
        startActivityForResult(tempIntent,ConstantUi.Intent.ACTIVITY_EDIT_PROFILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == ConstantUi.Intent.ACTIVITY_EDIT_PROFILE && resultCode==Activity.RESULT_OK){
            //refresh the list// lets check first whether livedata works
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initAdapter() {
        id_recycler.layoutManager = LinearLayoutManager(this)
        id_recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener(this)
        setAdapterData()
    }

    private fun setAdapterData() {
        ViewModelProviders.of(this).get(MemberViewModel::class.java).getAllMembers()
            .observe(this, Observer<List<Member>>{
                it?.let {t1->
                    mAdapter.setData(t1.toMutableList())
                }
            })
    }
}
