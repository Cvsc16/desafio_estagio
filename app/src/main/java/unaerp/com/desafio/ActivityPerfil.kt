package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityPerfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()


        val senha = findViewById<EditText>(R.id.cadastro_senha)
        val nome = findViewById<EditText>(R.id.cadastro_nome)
        val email = findViewById<EditText>(R.id.cadastro_email)
        val btn_salvar = findViewById<Button>(R.id.btn_salvar)
        val editar_nome = findViewById<ImageView>(R.id.editar_nome)
        val editar_senha = findViewById<ImageView>(R.id.editar_senha)
        val editar_email = findViewById<ImageView>(R.id.editar_email)
        val esconde_svg = findViewById<ImageView>(R.id.esconde_svg)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuFlutuante = resources.getDrawable(R.drawable.ic_novavaga_svg, null)
        bottomNavigationView.menu.findItem(R.id.novaVaga).icon = menuFlutuante


        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.vagas -> {
                    val intent = Intent(this, ActivityVagas::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.perfil -> {
                    // tratamento para a seleção do item Perfil
                    true
                }

                R.id.novaVaga -> {
                    val intent = Intent(this, ActivityNovaVaga::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }

        }

        bottomNavigationView.setSelectedItemId(R.id.perfil)


        // Desabilita os EditTexts e o botão de salvar
        nome.isEnabled = false
        senha.isEnabled = false
        email.isEnabled = false
        btn_salvar.isEnabled = false
        btn_salvar.setBackgroundResource(R.drawable.btn_roundeddisable)

// Adiciona listeners aos elementos de imagem de edição
        editar_nome.setOnClickListener {
            nome.isEnabled = true
            btn_salvar.isEnabled = true
            btn_salvar.setBackgroundResource(R.drawable.btn_roundeddisable)
        }

        editar_senha.setOnClickListener {
            senha.isEnabled = true
            btn_salvar.isEnabled = true
            btn_salvar.setBackgroundResource(R.drawable.btn_roundeddisable)
        }

        editar_email.setOnClickListener {
            email.isEnabled = true
            btn_salvar.isEnabled = true
            btn_salvar.setBackgroundResource(R.drawable.btn_roundeddisable)
        }

// Adiciona TextWatchers aos EditTexts
        nome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Verifica se há texto no EditText e habilita/desabilita o botão de salvar
                btn_salvar.isEnabled = s.toString().isNotEmpty() || senha.text.toString()
                    .isNotEmpty() || email.text.toString().isNotEmpty()
                btn_salvar.setBackgroundResource(if (btn_salvar.isEnabled) R.drawable.btn_rounded else R.drawable.btn_roundeddisable)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        senha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Verifica se há texto no EditText e habilita/desabilita o botão de salvar
                btn_salvar.isEnabled = s.toString().isNotEmpty() || nome.text.toString()
                    .isNotEmpty() || email.text.toString().isNotEmpty()
                btn_salvar.setBackgroundResource(if (btn_salvar.isEnabled) R.drawable.btn_rounded else R.drawable.btn_roundeddisable)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Verifica se há texto no EditText e habilita/desabilita o botão de salvar
                btn_salvar.isEnabled = s.toString().isNotEmpty() || nome.text.toString()
                    .isNotEmpty() || senha.text.toString().isNotEmpty()
                btn_salvar.setBackgroundResource(if (btn_salvar.isEnabled) R.drawable.btn_rounded else R.drawable.btn_roundeddisable)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btn_salvar.setOnClickListener {
            Toast.makeText(this, "Edição realizada com sucesso!", Toast.LENGTH_SHORT).show()
        }

        esconde_svg.setOnClickListener {
            val cursorPosition = senha.selectionEnd

            if (senha.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            senha.setSelection(cursorPosition)
        }
    }
}



