package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class ActivityRedefinirSenha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefinirsenha)
        supportActionBar?.hide()

        val iconeEsconde = findViewById<ImageView>(R.id.esconde_svg)
        val iconeEsconde2 = findViewById<ImageView>(R.id.esconde_svg2)
        val back = findViewById<ImageView>(R.id.back)
        val btn_redefini = findViewById<Button>(R.id.btn_redifini)
        val senha = findViewById<EditText>(R.id.senha_atual)
        val senha_nova = findViewById<EditText>(R.id.senha_nova)
        val senha_nova2 = findViewById<EditText>(R.id.senha_nova2)


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

        btn_redefini.setOnClickListener {
            if (senha.text.toString() == senha_nova.text.toString()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show()
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}