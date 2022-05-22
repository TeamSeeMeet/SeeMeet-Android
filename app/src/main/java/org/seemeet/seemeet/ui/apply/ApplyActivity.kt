package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityApplyBinding

class ApplyActivity : AppCompatActivity() {
    private val binding: ActivityApplyBinding by lazy {
        ActivityApplyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()

        val fragment = FirstApplyFragment()
        supportFragmentManager.beginTransaction().add(R.id.container_apply, fragment).commit()

        val username = intent.getStringExtra("username")
        val userposition = intent.getIntExtra("userposition",-1)
        val userid = intent.getIntExtra("userid",-1)
        val useremail = intent.getStringExtra("useremail")

        if (username != null) {
            Log.d("********************username", username+ userposition.toString()+userid + useremail.toString())
            val bundle = Bundle()
            bundle.putString("name", username)
            bundle.putInt("id", userid)
            bundle.putInt("pos",userposition)
            bundle.putString("email",useremail)

            fragment.arguments = bundle
        }

        supportFragmentManager.beginTransaction().replace(R.id.container_apply, fragment)
        supportFragmentManager.beginTransaction().commit()
    }

    private fun initClickListener() {
        binding.ivX.setOnClickListener {
            val dialogView = ApplyBackDialogFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener(object :
                ApplyBackDialogFragment.OnButtonClickListener {
                override fun onCancelOutClicked() {
                    finish()
                    backPressed()
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
    }

    fun backPressed(){
        super.onBackPressed()
    }

    override fun onBackPressed() {
            val dialogView = ApplyBackDialogFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener(object :
                ApplyBackDialogFragment.OnButtonClickListener {
                override fun onCancelOutClicked() {
                    finish()
                    backPressed()
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}