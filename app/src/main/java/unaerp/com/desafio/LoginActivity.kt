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
import com.google.firebase.firestore.auth.User as FirebaseUser
import unaerp.com.desafio.User as LocalbaseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val esconde_svg = findViewById<ImageView>(R.id.esconde_svg)
        val senha = findViewById<EditText>(R.id.senha)
        val email = findViewById<EditText>(R.id.email)
        val btn_login= findViewById<Button>(R.id.btn_login)

        val tipoConta = intent.getStringExtra("tipo_conta")

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

            val mAuth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance("https://desafio5semestre-default-rtdb.firebaseio.com/")

            mAuth.signInWithEmailAndPassword( email.text.toString(),
                senha.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login bem-sucedido

                        val user = mAuth.currentUser
                        val uid = user?.uid

                        // Busca informações do usuário no Firebase Realtime Database
                        val userRef = database.getReference("users").child(uid!!)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val user = dataSnapshot.getValue(LocalbaseUser::class.java)
                                if (user?.tipo == "Anunciante") {
                                    // Login bem-sucedido para anunciante
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                        putExtra("tipo_conta", tipoConta)
                                    }
                                    startActivity(intent)
                                    finish()
                                } else if (user?.tipo == "Interessado") {
                                    // Login bem-sucedido para interessado
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                        putExtra("tipo_conta", tipoConta)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Ocorreu um erro ao buscar as informações do usuário
                                Log.w(TAG, "loadUser:onCancelled", databaseError.toException())
                            }
                        })
                    } else {
                        // Login mal-sucedido
                        Toast.makeText(this@LoginActivity, "Dados incorretos.", Toast.LENGTH_SHORT)
                            .show()
                    }
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