package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val esconde_svg = findViewById<ImageView>(R.id.esconde_svg)
        val senha = findViewById<EditText>(R.id.senha)
        val email = findViewById<EditText>(R.id.email)
        val btn_login= findViewById<Button>(R.id.btn_login)

        val semContaTextView = findViewById<TextView>(R.id.sem_conta)
        trocacor(semContaTextView, ContextCompat.getColor(this, R.color.destaque), View.OnClickListener {
            val intent = Intent(this, ActivityTipoConta::class.java)
            startActivity(intent)
        })

        val esqueciSenha = findViewById<TextView>(R.id.esqueci_minha_senha)
        esqueciSenha.setOnClickListener {
            val intent = Intent(this, ActivityEsqueceuSenha::class.java)
            startActivity(intent)
        }

        val loginTextView = findViewById<TextView>(R.id.login_)
        trocacor_ult(loginTextView, ContextCompat.getColor(this, R.color.principal))

        esconde_svg.setOnClickListener {
            val cursorPosition = senha.selectionEnd

            if (senha.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                senha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            senha.setSelection(cursorPosition)
        }

        btn_login.setOnClickListener {
            if (email.text.toString() == "teste@gmail.com" && senha.text.toString() == "teste123") {
                // Login bem-sucedido
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Login mal-sucedido
                Toast.makeText(this, "Dados incorretos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun trocacor(textView: TextView, color: Int, onClickListener: View.OnClickListener): SpannableString {
        val text = textView.text
        val startIndex = text.indexOf("Registre-se")
        val endIndex = startIndex + "Registre-se".length

        val spannable = SpannableString(text)
        spannable.setSpan(CustomClickableSpan(onClickListener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
        return spannable
    }

    fun trocacor_ult(textView: TextView, color: Int) {
        val text = textView.text
        val lastChar = text[text.length - 1]
        val lastCharSpannable = SpannableString("$lastChar")
        lastCharSpannable.setSpan(ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = SpannableStringBuilder(text.subSequence(0, text.length - 1)).append(lastCharSpannable)
    }

    class CustomClickableSpan(private val onClickListener: View.OnClickListener) : ClickableSpan() {
        override fun onClick(widget: View) {
            onClickListener.onClick(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

}