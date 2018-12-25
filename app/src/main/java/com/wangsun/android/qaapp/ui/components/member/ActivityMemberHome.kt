package com.wangsun.android.qaapp.ui.components.member

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.ui.common.CommonFuction
import com.wangsun.android.qaapp.utilities.UtilFile
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import kotlinx.android.synthetic.main.activity_member_home.*


@SuppressLint("CheckResult")
class ActivityMemberHome : AppCompatActivity() {

    var profileMenu: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_home)

        initButton()
        getProfileData(UtilSharedPreference.getLoggedInEmail(this))
    }


    private fun getProfileData(email: String) {
        ViewModelProviders.of(this).get(MemberViewModel::class.java).getMember(email).observe(this,
            Observer<List<Member>> { t ->
                t?.let {
                    if(it.size>0){
                        id_name.text = it[0].name
                        id_score.text = it[0].score.toString()
                        id_attemted.text = it[0].totalAttempted.toString()

                        Glide.with(id_dp)
                            .load(UtilFile.getProfileImagePath()+"/"+it[0].email+".jpg")
                            .into(id_dp)
                        //CommonFuction.showToast(this@ActivityMemberHome,it.toString())
                    }
                }
            })
    }

    private fun initButton() {
        id_start_btn.setOnClickListener {
            startActivity(Intent(this, ActivityQuiz::class.java))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.member_option_menu, menu)

        profileMenu = menu?.getItem(0)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                profileMenu?.isVisible = false
                startFragmentProfile()
            }
            R.id.action_logout -> {
                UtilSharedPreference.setIsLoggedIn(this,false)
                finish()
            }
        }
        return true
    }

    private fun startFragmentProfile(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.id_content, FragmentProfile()).commit()
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.id_content)

        if (frag != null && frag.isVisible) {
            supportFragmentManager.beginTransaction().remove(frag).commit()
            profileMenu?.isVisible = true
        }
        else {
            val intent = Intent()
            intent.putExtra("isExit",true)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }
}
