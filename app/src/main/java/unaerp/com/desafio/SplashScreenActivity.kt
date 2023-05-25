package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(auth.currentUser != null) {
            // navega pra home
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // navega pro login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Usuário NÃO logado!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}




