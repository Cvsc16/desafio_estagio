package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ActivityEsqueceuSenha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceusenha)

        val emailRecupera = findViewById<TextView>(R.id.email_recupera)
        val btnRecupera = findViewById<Button>(R.id.btn_recupera)
        val back = findViewById<ImageView>(R.id.back)

        btnRecupera.setOnClickListener {
            if (emailRecupera.text.toString() == "teste@gmail.com") {
                val intent = Intent(this, ActivityRedefinirSenha::class.java)
                startActivity(intent)
            }
        }


        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}