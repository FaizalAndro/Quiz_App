package com.example.quizapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quizapp.model.Questions
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    var mCurrentPosition: Int = 1;
    var mSelectedPosition: Int = 0;
    var mQuestionList: ArrayList<Questions>? = null
    var correctAnswers:Int = 0
    var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mQuestionList = Constants.getQuestion()
        Log.i("Question Size", "${mQuestionList!!.size}")

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        setQuestion()
        tv_option1.setOnClickListener(this)
        tv_option2.setOnClickListener(this)
        tv_option3.setOnClickListener(this)
        tv_option4.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    fun setQuestion() {

        defaultOptionView()

        if(mCurrentPosition == mQuestionList!!.size){
            btn_submit.text = "FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }

        val question: Questions = mQuestionList!![mCurrentPosition - 1]
        tv_question.text = question.question
        iv_flag.setImageResource(question.image)
        progress_bar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition/${mQuestionList!!.size}"
        tv_option1.text = question.opt1
        tv_option2.text = question.opt2
        tv_option3.text = question.opt3
        tv_option4.text = question.opt4
    }

    fun defaultOptionView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_option1)
        options.add(1, tv_option2)
        options.add(2, tv_option3)
        options.add(3, tv_option4)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option1 -> {
                selectedOptionView(tv_option1, 1)
            }
            R.id.tv_option2 -> {
                selectedOptionView(tv_option2, 2)
            }
            R.id.tv_option3 -> {
                selectedOptionView(tv_option3, 3)
            }
            R.id.tv_option4 -> {
                selectedOptionView(tv_option4, 4)
            }
            R.id.btn_submit -> {
                if (mSelectedPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            var intent = Intent( this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
                            startActivity(intent)
                            finish()
                        }

                    }
                }else {
                    val questions: Questions = mQuestionList!![mCurrentPosition-1]
                    if(questions.correctAnswer != mSelectedPosition){
                        answerView(mSelectedPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        correctAnswers++
                    }
                    answerView(questions.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionList!!.size){
                        btn_submit.text = "FINISH"
                    }else{
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedPosition = 0
                }

            }

        }
    }

    private fun answerView(answer: Int, drawable: Int) {
        when (answer) {
            1 -> {
                tv_option1.background = ContextCompat.getDrawable(this, drawable)
            }
            2 -> {
                tv_option2.background = ContextCompat.getDrawable(this, drawable)
            }
            3 -> {
                tv_option3.background = ContextCompat.getDrawable(this, drawable)
            }
            4 -> {
                tv_option4.background = ContextCompat.getDrawable(this, drawable)
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        defaultOptionView()
        mSelectedPosition = selectedOptionNumber
        tv.setTextColor(Color.parseColor("#000000"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)

    }
}