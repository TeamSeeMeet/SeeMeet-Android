package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityApplyBinding

class ApplyActivity : AppCompatActivity() {
    private val binding: ActivityApplyBinding by lazy {
        ActivityApplyBinding.inflate(layoutInflater)
    }

    private val firstApplyFragment by lazy { FirstApplyFragment() }
    private val secondApplyFragment by lazy { SecondApplyFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_navHost_apply, fragment)
            .commit()
    }
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}