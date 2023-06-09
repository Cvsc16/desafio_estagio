package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import unaerp.com.desafio.databinding.ActivityLoginBinding
import unaerp.com.desafio.databinding.ActivityRedefinirsenhaBinding

class RedefinirSenhaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRedefinirsenhaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedefinirsenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val iconeEsconde = binding.escondeSvg
        val iconeEsconde2 = binding.escondeSvg2
        val iconeEsconde3 = binding.escondeSvgNovaSenha2
        val back = binding.back
        val btn_redefini = binding.btnRedifini
        val senha = binding.senhaAtual
        val senha_nova = binding.senhaNova
        val senha_nova2 = binding.senhaNova2

        iconeEsconde.setOnClickListener {
            val cursorPosition = senha.selectionEnd

            if (senha.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            senha.setSelection(cursorPosition)
        }

        iconeEsconde2.setOnClickListener {
            val cursorPosition = senha_nova.selectionEnd

            if (senha_nova.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                senha_nova.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                senha_nova.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            senha_nova.setSelection(cursorPosition)
        }

        iconeEsconde3.setOnClickListener {
            val cursorPosition = senha_nova.selectionEnd

            if (senha_nova2.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                senha_nova2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                senha_nova2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            senha_nova2.setSelection(cursorPosition)
        }

        btn_redefini.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                val senhaAtual = senha.text.toString()
                val novaSenha = senha_nova.text.toString()
                val novaSenha2 = senha_nova2.text.toString()

                val credential = EmailAuthProvider.getCredential(user.email!!, senhaAtual)
                user.reauthenticate(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            if (novaSenha == novaSenha2 && novaSenha.length >= 6) {
                                user.updatePassword(novaSenha)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            Toast.makeText(this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show()
                                            onBackPressed()
                                        } else {
                                            Toast.makeText(this, "Erro ao alterar a senha: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Verifique se as senhas estão iguais e têm pelo menos 6 caracteres", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Senha atual incorreta", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        back.setOnClickListener {
            onBackPressed()
        }
    }
}