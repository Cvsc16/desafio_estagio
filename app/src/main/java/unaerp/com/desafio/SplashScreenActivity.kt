package unaerp.com.desafio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreenActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance("https://desafio5semestre-default-rtdb.firebaseio.com/")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageViewGif = findViewById<ImageView>(R.id.img_gif)

        Glide.with(this)
            .asGif()
            .load(R.drawable.splash_gif)
            .into(imageViewGif)

        Log.d("ABRIRAPP", "REALIZOU O LOGIN")

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val userRef = database.getReference("users").child(uid!!)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userData = dataSnapshot.getValue() as Map<String, Object>
                    val tipo = userData["tipo"] as String
                    Log.d("TAGRECEBIDO", "Tipo de conta recebido: $tipo")

                    val intent = if (tipo == "Interessado") {
                        Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                            putExtra("tipo_conta", "Interessado")
                        }
                    } else {
                        Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                            putExtra("tipo_conta", "Anunciante")
                        }
                    }
                    startActivity(intent)
                    finish()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadUser:onCancelled", databaseError.toException())
                    // Em caso de erro, navegar para a tela de login
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        } else {
            // Navegar para a tela de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Usuário NÃO logado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}





