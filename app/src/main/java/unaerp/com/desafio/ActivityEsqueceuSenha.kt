package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ActivityEsqueceuSenha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceusenha)
        supportActionBar?.hide()

        val emailRecupera = findViewById<TextView>(R.id.email_recupera)
        val btnRecupera = findViewById<Button>(R.id.btn_recupera)
        val back = findViewById<ImageView>(R.id.back)

        btnRecupera.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            val email = emailRecupera.text.toString()

            if (email.isNotEmpty()) {
                // Verifica se o usuário já foi verificado
                val user = mAuth.currentUser
                if (user != null && user.isEmailVerified) {
                    // Envia o e-mail de redefinição de senha
                    // Envia o e-mail de redefinição de senha
                    mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // E-mail de redefinição de senha enviado com sucesso
                                Toast.makeText(
                                    this,
                                    "Um e-mail de redefinição de senha foi enviado para $email",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Redireciona o usuário para a tela de redefinição de senha
                                val intent = Intent(this, ActivityRedefinirSenha::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Ocorreu um erro ao enviar o e-mail de redefinição de senha
                                Toast.makeText(
                                    this,
                                    "Não foi possível enviar o e-mail de redefinição de senha.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                } else {
                    // O campo de e-mail está vazio
                    Toast.makeText(
                        this,
                        "Por favor, insira um endereço de e-mail válido.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }



        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}