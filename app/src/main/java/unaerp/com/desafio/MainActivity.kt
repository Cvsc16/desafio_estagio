package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val esconde_svg = findViewById<ImageView>(R.id.esconde_svg)
        val senha = findViewById<EditText>(R.id.senha)

        val semContaTextView = findViewById<TextView>(R.id.sem_conta)
        trocacor(semContaTextView, ContextCompat.getColor(this, R.color.destaque), View.OnClickListener {
            val intent = Intent(this, CadastroInteressado::class.java)
            startActivity(intent)
        })


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