package unaerp.com.desafio

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import unaerp.com.desafio.databinding.ActivityEsqueceusenhaBinding
import unaerp.com.desafio.databinding.ActivityLoginBinding
import unaerp.com.desafio.ClassUser as LocalbaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val esconde_svg = binding.escondeSvg
        val senha = binding.senha
        val email = binding.email
        val btn_login = binding.btnLogin

        val tipoConta = intent.getStringExtra("tipo_conta")

        val semContaTextView = binding.semConta
        trocacor(
            semContaTextView,
            ContextCompat.getColor(this, R.color.destaque),
            View.OnClickListener {
                val intent = Intent(this, TipoContaActivity::class.java)
                startActivity(intent)
            })

        val esqueciSenha = binding.esqueciMinhaSenha
        esqueciSenha.setOnClickListener {
            val intent = Intent(this, EsqueceuSenhaActivity::class.java)
            startActivity(intent)
        }

        val loginTextView = binding.login
        trocacor_ult(loginTextView, ContextCompat.getColor(this, R.color.principal))

        esconde_svg.setOnClickListener {
            val cursorPosition = senha.selectionEnd

            if (senha.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                senha.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                senha.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            senha.setSelection(cursorPosition)
        }

        btn_login.setOnClickListener {

            val email = email.text.toString()
            val senha = senha.text.toString()

            val mAuth = FirebaseAuth.getInstance()
            val database =
                FirebaseDatabase.getInstance("https://desafio5semestre-default-rtdb.firebaseio.com/")

            if (email.isEmpty() || senha.isEmpty()) {
                // Exibir mensagem de campos vazios
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.signInWithEmailAndPassword(
                    email,
                    senha
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            if (user != null && user.isEmailVerified) {
                                // Login bem-sucedido e email verificado
                                val uid = user.uid

                                // Busca informações do usuário no Firebase Realtime Database
                                val userRef = database.getReference("users").child(uid!!)
                                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val userData =
                                            dataSnapshot.getValue() as Map<String, Object>
                                        val email = userData["email"] as String
                                        val nome = userData["nome"] as String
                                        val tipo = userData["tipo"] as String
                                        val user = LocalbaseUser(email, nome, tipo)
                                        Log.d("TAGRECEBIDO", "Tipo de conta recebido: ${user.tipo}")
                                        if (user.tipo == "Interessado") {
                                            // Login bem-sucedido para interessado
                                            val intent = Intent(
                                                this@LoginActivity,
                                                MainActivity::class.java
                                            ).apply {
                                                putExtra("tipo_conta", user.tipo)
                                            }
                                            startActivity(intent)
                                            finish()
                                        } else if (user.tipo == "Anunciante") {
                                            // Login bem-sucedido para anunciante
                                            val intent = Intent(
                                                this@LoginActivity,
                                                MainActivity::class.java
                                            ).apply {
                                                putExtra("tipo_conta", user.tipo)
                                            }
                                            startActivity(intent)
                                            finish()
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Ocorreu um erro ao buscar as informações do usuário
                                        Log.w(
                                            TAG,
                                            "loadUser:onCancelled",
                                            databaseError.toException()
                                        )
                                    }
                                })
                            } else {
                                // Login mal-sucedido (email não verificado)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "E-mail não verificado! Por favor verifique!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Login mal-sucedido
                            Toast.makeText(
                                this@LoginActivity,
                                "Dados incorretos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                Log.d("LOGINVAGAS", "REALIZOU O LOGIN")
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