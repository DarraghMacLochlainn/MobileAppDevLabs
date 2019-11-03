package org.wit.hillfort.activities
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import org.wit.hillfort.R
import org.wit.hillfort.activities.HillfortListActivity

class SplashActivity : AppCompatActivity() {

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  window.requestFeature(Window.FEATURE_NO_TITLE)
  window.setFlags(
   WindowManager.LayoutParams.FLAG_FULLSCREEN,
   WindowManager.LayoutParams.FLAG_FULLSCREEN
  )
  setContentView(R.layout.activity_splash)

  Handler().postDelayed({
   startActivity(Intent(this@SplashActivity, HillfortListActivity::class.java))
   finish()
  }, 3000)

 }
}