package com.wangsun.android.qaapp.ui.components.member

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.sqlite.tables.Question
import com.wangsun.android.qaapp.sqlite.tables.QuestionViewModel
import com.wangsun.android.qaapp.utilities.UtilSharedPreference
import kotlinx.android.synthetic.main.activity_quiz.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import com.wangsun.android.qaapp.ui.common.CommonFuction
import android.widget.RadioButton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.os.CountDownTimer
import android.view.MenuItem
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.*


@SuppressLint("CheckResult")
class ActivityQuiz : AppCompatActivity() {

    private lateinit var mProfile: Member
    private lateinit var mQuestionList: MutableList<Question>
    var mAnswered: Boolean  = false

    private var mCountDownTimer: CountDownTimer? = null
    private var mTimeLeftInMillis: Long = 0

    private var mDefaultColorRadio: ColorStateList? = null
    private var mDefaultColorCd: ColorStateList? = null


    companion object {
        private val COUNTDOWN_IN_MILLIS: Long = 30000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        //mEmail = UtilSharedPreference.getLoggedInEmail(this)
        mDefaultColorRadio = id_radio1.textColors
        mDefaultColorCd = id_countdown.textColors

        initButton()
        observeQuestion()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initButton() {
        id__confirm_next.setOnClickListener {
            nextOrConfirm()
        }
    }


    private fun nextOrConfirm(){
        if(!mAnswered){
            if (id_radio1.isChecked() || id_radio2.isChecked() || id_radio3.isChecked()) {
                checkAnswer()
            } else {
                CommonFuction.showToast(this, "Please select an answer")
            }
        }
        else{

            id_radio1.isEnabled = true
            id_radio2.isEnabled = true
            id_radio3.isEnabled = true

            id_radio1.setTextColor(mDefaultColorRadio);
            id_radio2.setTextColor(mDefaultColorRadio);
            id_radio3.setTextColor(mDefaultColorRadio);
            id_radio_group.clearCheck()
            id_answer.visibility = View.INVISIBLE
            getProfile(mProfile.email)
        }
    }

    private fun observeQuestion() {
        ViewModelProviders.of(this).get(QuestionViewModel::class.java).getAllQuestions().observe(this,
            Observer<List<Question>> { t ->
                t?.let {
                    if(it.isNotEmpty()){
                        mQuestionList = it.toMutableList()
                        getProfile(UtilSharedPreference.getLoggedInEmail(this@ActivityQuiz))
                    }
                }
            })
    }

    private fun getProfile(email: String) {
        Observable.create<List<Member>> { emitter ->
            emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java).getMemberOnce(email))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.isNotEmpty()){
                    mProfile = it[0]
                    id_score.text = "Score: "+mProfile.score.toString()
                    id_question.text = mQuestionList[mProfile.questPosition].quest
                    mAnswered = false
                    id__confirm_next.text = "Confirm"
                    mTimeLeftInMillis = COUNTDOWN_IN_MILLIS;
                    startCountDown()
                }
            },{
                CommonFuction.showToast(this@ActivityQuiz,"error: "+it.message.toString())
            })
    }

    private fun checkAnswer() {
        mCountDownTimer?.cancel()
        val rbSelected = findViewById<RadioButton>(id_radio_group.getCheckedRadioButtonId())
        val answerNr = id_radio_group.indexOfChild(rbSelected) + 1

        //CommonFuction.showToast(this, "Result: "+answerNr)
        var count = 0
        if(answerNr == mQuestionList[mProfile.questPosition].answer){
            count++
        }
        showSolution(mQuestionList[mProfile.questPosition].answer,count)
    }

    private fun showSolution(restult: Int,count: Int) {
        id_radio1.setTextColor(Color.RED)
        id_radio2.setTextColor(Color.RED)
        id_radio3.setTextColor(Color.RED)

        id_radio1.isEnabled = false
        id_radio2.isEnabled = false
        id_radio3.isEnabled = false

        id_answer.visibility = View.VISIBLE

        when (restult) {
            1 -> {
                id_radio1.setTextColor(Color.GREEN)
                id_answer.text = "Answer is 1"
            }
            2 -> {
                id_radio2.setTextColor(Color.GREEN)
                id_answer.text = "Answer is 2"
            }
            3 -> {
                id_radio3.setTextColor(Color.GREEN)
                id_answer.text = "Answer is 3"
            }
        }

        if(mProfile.questPosition+1<mQuestionList.size){
            Observable.create<Int>{emitter->
                emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java)
                    .updateScorePosition(mProfile.email,mProfile.score+count,mProfile.questPosition+1,mProfile.totalAttempted+1))
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({t1->
                    mAnswered = true
                    id__confirm_next.text = "Next"
                    //CommonFuction.showToast(this,"Result: ${t1}")
                },{t2->
                    CommonFuction.showToast(this,"Error: ${t2.message}")
                })
        }
        else{
            //Question will go in loop
            Observable.create<Int>{emitter->
                emitter.onNext(ViewModelProviders.of(this).get(MemberViewModel::class.java)
                    .updateScorePosition(mProfile.email,mProfile.score+count,0,mProfile.totalAttempted+1))
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({t1->
                    //CommonFuction.showToast(this,"Result: ${t1}")
                    mAnswered = true
                    id__confirm_next.text = "Next"
                },{t2->
                    CommonFuction.showToast(this,"Error: ${t2.message}")
                })
        }
    }


    private fun startCountDown() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimeLeftInMillis = 0
                updateCountDownText()
                checkAnswer()
            }
        }.start()
    }

    private fun updateCountDownText() {
        val minutes = ((mTimeLeftInMillis / 1000)/ 60).toInt()
        val seconds = ((mTimeLeftInMillis / 1000) % 60).toInt()

        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        id_countdown.setText(timeFormatted)
        if (mTimeLeftInMillis < 10000) {
            id_countdown.setTextColor(Color.RED)
        } else {
            id_countdown.setTextColor(mDefaultColorCd)
        }
    }
}
