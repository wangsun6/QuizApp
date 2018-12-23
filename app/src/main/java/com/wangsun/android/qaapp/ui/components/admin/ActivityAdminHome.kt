package com.wangsun.android.qaapp.ui.components.admin

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.*
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import kotlinx.android.synthetic.main.activity_admin_home.*

class ActivityAdminHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)


        println("Check105: created")


        initButton()
        getAllData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.member_option_menu, menu)

        //profileMenu = menu?.getItem(0)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                //profileMenu?.isVisible = false
                //startFragmentProfile()
            }
            R.id.action_logout -> {
                UtilSharedPreference.setIsLoggedIn(this,false)
                finish()
            }
        }
        return true
    }
    override fun onBackPressed() {
        //val frag = supportFragmentManager.findFragmentById(R.id.id_content)

//        if (frag != null && frag.isVisible()) {
//            supportFragmentManager.beginTransaction().remove(FragmentProfile()).commit()
//            profileMenu?.isVisible = true
//        }
//        else {
//            val intent = Intent()
//            intent.putExtra("isExit",true)
//            setResult(Activity.RESULT_OK,intent)
//            finish()
//        }

        val intent = Intent()
        intent.putExtra("isExit",true)
        setResult(Activity.RESULT_OK,intent)
        finish()
        //super.onBackPressed()
    }

    private fun getAllData() {
        ViewModelProviders.of(this).get(AdminViewModel::class.java).getAdmin(UtilSharedPreference.getLoggedInEmail(this))?.
            observe(this,Observer<List<Admin>>{
                it?.let { t1->
                    if(t1.isNotEmpty()){
                        id_name.text = t1[0].email
                    }
                }
            })


        ViewModelProviders.of(this).get(MemberViewModel::class.java).getAllMembers()?.
            observe(this, Observer<List<Member>>{
            it?.let {t1->
                if(t1.isNotEmpty()){
                    id_total_member.text = "Total Members: ${t1.size}"
                }
                else
                    id_total_member.text = "Total Members: 0"
            }
        })

        ViewModelProviders.of(this).get(QuestionViewModel::class.java).getAllQuestions()?.
            observe(this,Observer<List<Question>>{
            it?.let { t1->
                if(t1.isNotEmpty()){
                    id_total_question.text = "Total Questions: ${t1.size}"
                }
                else
                    id_total_question.text = "Total Questions: 0"
            }
        })
    }

    private fun initButton() {
        id_leader_board.setOnClickListener { startActivityMemberList() }
    }

    private fun startActivityMemberList() {
        startActivity(Intent(this,ActivityMemberList::class.java))
    }
}
