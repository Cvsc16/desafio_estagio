package unaerp.com.desafio

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CadastroActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        supportActionBar?.hide()


        val esconde_svg = findViewById<ImageView>(R.id.esconde_svg)
        val back = findViewById<ImageView>(R.id.back)
        val senha = findViewById<EditText>(R.id.cadastro_senha)
        val nome = findViewById<EditText>(R.id.cadastro_nome)
        val email = findViewById<EditText>(R.id.cadastro_email)
        val btn_cadastro= findViewById<Button>(R.id.btn_cadastro)

        val tipoConta = intent.getStringExtra("tipo_conta")

        val semContaTextView = findViewById<TextView>(R.id.sem_conta)
        trocacor(semContaTextView, ContextCompat.getColor(this, R.color.destaque), View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        val termosTextView = findViewById<TextView>(R.id.termos)
        trocacor1(termosTextView, ContextCompat.getColor(this, R.color.destaque))

        val termos2TextView = findViewById<TextView>(R.id.termos)
        trocacor2(termos2TextView, ContextCompat.getColor(this, R.color.destaque))

        val loginTextView = findViewById<TextView>(R.id.cadastro_)

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

        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_cadastro.setOnClickListener {
            // Verifica se todos os campos foram preenchidos
            if (senha.text.isEmpty() || nome.text.isEmpty() || email.text.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT)
                    .show()
            } else if (senha.text.length < 6) {
                Toast.makeText(
                    this,
                    "A senha deve conter no mínimo 6 caracteres!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isConnected()) {
                Toast.makeText(
                    this,
                    "É necessário estar conectado à internet para realizar o cadastro!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                Toast.makeText(this, "Por favor, insira um email válido!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Cadastra o usuário
                val database =
                    FirebaseDatabase.getInstance("https://desafio5semestre-default-rtdb.firebaseio.com/")
                val mAuth = FirebaseAuth.getInstance()

                mAuth.fetchSignInMethodsForEmail(email.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods ?: emptyList()
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                // O email já está associado com uma conta existente
                                Toast.makeText(
                                    this,
                                    "Esse email já foi cadastrado no aplicativo!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // O email ainda não foi cadastrado
                                mAuth.createUserWithEmailAndPassword(
                                    email.text.toString(),
                                    senha.text.toString()
                                )
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            // Cadastro do usuário foi bem sucedido
                                            val user = mAuth.currentUser
                                            // Armazena informações do usuário no Firebase Realtime Database
                                            val uid = user?.uid
                                            val userRef = database.getReference("users").child(uid!!)
                                            userRef.child("nome").setValue(nome.text.toString())
                                            userRef.child("email").setValue(email.text.toString())
                                            userRef.child("tipo").setValue(tipoConta)

                                            user.sendEmailVerification()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        // E-mail de verificação enviado com sucesso
                                                    } else {
                                                        // Ocorreu um erro ao enviar o e-mail de verificação
                                                    }
                                                }
                                            // Exibe mensagem de sucesso e volta para a tela de login
                                            Toast.makeText(
                                                this,
                                                "Cadastro realizado com sucesso!" +
                                                        "Confirme o cadastro no seu email",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            val intent = Intent(this, LoginActivity::class.java)
                                            intent.putExtra("tipo_conta", tipoConta)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // Cadastro do usuário falhou
                                            Log.w(
                                                TAG,
                                                "createUserWithEmail:failure",
                                                task.exception
                                            )
                                            Toast.makeText(
                                                this,
                                                "Cadastro falhou.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        } else {
                            // Erro ao verificar se o email já foi cadastrado
                            Log.w(TAG, "fetchSignInMethodsForEmail:failure", task.exception)
                            Toast.makeText(
                                this,
                                "Erro ao verificar se o email já foi cadastrado.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }

    fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun trocacor(textView: TextView, color: Int, onClickListener: View.OnClickListener): SpannableString {
        val text = textView.text
        val startIndex = text.indexOf("Entre")
        val endIndex = startIndex + "Entre".length

        val spannable = SpannableString(text)
        spannable.setSpan(LoginActivity.CustomClickableSpan(onClickListener), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
        return spannable
    }

    fun trocacor1(textView: TextView, color: Int) {
        val text = textView.text
        val startIndex = text.indexOf("Termos e Condições")
        val endIndex = startIndex + "Termos e Condições".length

        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannable
    }

    fun trocacor2(textView: TextView, color: Int) {
        val text = textView.text
        val startIndex = text.indexOf("Política de Privacidade")
        val endIndex = startIndex + "Política de Privacidade".length

        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannable
    }

    fun trocacor_ult(textView: TextView, color: Int) {
        val text = textView.text
        val lastChar = text[text.length - 1]
        val lastCharSpannable = SpannableString("$lastChar")
        lastCharSpannable.setSpan(ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = SpannableStringBuilder(text.subSequence(0, text.length - 1)).append(lastCharSpannable)
    }

}