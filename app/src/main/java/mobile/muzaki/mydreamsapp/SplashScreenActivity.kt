package mobile.muzaki.mydreamsapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    val mContext: Context =this;
    val SPLASH_TIME_OUT = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /// tampilkan loading gif
        Glide.with(mContext).load(R.drawable.loading1).into(ivLoadingScreen);

        /// menampilkan versi android
        try {
            val pInfo: PackageInfo =
                mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0)
            val version = pInfo.versionName
            tvVersionApp.setText(version);
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        /// pindah halaman
        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
}