package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
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
                mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (signInMethods.isNullOrEmpty()) {
                                // Nenhum método de autenticação encontrado para o e-mail
                                Toast.makeText(
                                    this,
                                    "Este e-mail não está associado a uma conta.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val isEmailVerified = signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)
                                if (isEmailVerified) {
                                    mAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener { resetTask ->
                                            if (resetTask.isSuccessful) {
                                                // E-mail de redefinição de senha enviado com sucesso
                                                Toast.makeText(
                                                    this,
                                                    "Um e-mail de redefinição de senha foi enviado para $email",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                // Redireciona o usuário para a tela de redefinição de senha
                                                val intent = Intent(this, LoginActivity::class.java)
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
                                    // O e-mail do usuário ainda não foi verificado
                                    Toast.makeText(
                                        this,
                                        "Por favor, verifique seu e-mail antes de redefinir sua senha.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            // Ocorreu um erro ao verificar o e-mail
                            Toast.makeText(
                                this,
                                "Não foi possível verificar o e-mail. Tente novamente mais tarde.",
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




        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}