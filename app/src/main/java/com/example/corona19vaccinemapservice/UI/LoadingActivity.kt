package com.example.corona19vaccinemapservice.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import com.example.corona19vaccinemapservice.R



class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        var progressBar = findViewById<ProgressBar>(R.id.progress_circular)
        var count = 0


        increaseProgressBar(count,progressBar)





    }

    fun goToMainActivity(){
        var intent = Intent(this,MainActivity::class.java)


        // 데이터 첨부 해서 보낼걸/!!
        startActivity(intent)
    }

    fun increaseProgressBar(count : Int,progressBar : ProgressBar){
        var count = 0
        Thread(Runnable {
            while (true){


                runOnUiThread {
                    progressBar.progress = count++
                }
                if(count>100) {
                    goToMainActivity()
                    break
                }
                Thread.sleep(400)
            }
        }).start()



    }
}