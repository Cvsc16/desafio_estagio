package unaerp.com.desafio

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class FragmentPerfil : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        val back = view.findViewById<ImageView>(R.id.back)
        val senha = view.findViewById<EditText>(R.id.cadastro_senha)
        val nome = view.findViewById<EditText>(R.id.cadastro_nome)
        val email = view.findViewById<EditText>(R.id.cadastro_email)
        val btn_salvar = view.findViewById<Button>(R.id.btn_salvar)
        val editar_nome = view.findViewById<ImageView>(R.id.editar_nome)
        val editar_senha = view.findViewById<ImageView>(R.id.editar_senha)
        val editar_email = view.findViewById<ImageView>(R.id.editar_email)
        val esconde_svg = view.findViewById<ImageView>(R.id.esconde_svg)

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
            Toast.makeText(context, "Alterações realizadas com sucesso!", Toast.LENGTH_SHORT).show()
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
        back.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}


