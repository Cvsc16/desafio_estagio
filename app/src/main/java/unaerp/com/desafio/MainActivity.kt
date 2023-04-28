package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val FragmentNovaVaga=FragmentNovaVaga()
    private val FragmentPerfil=FragmentPerfil()
    private val FragmentVagas=FragmentVagas()

    private var userEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuFlutuante = resources.getDrawable(R.drawable.ic_novavaga_svg, null)

        // Recebe o email do usuário por meio do Intent
        userEmail = intent.getStringExtra("email")


        FragmentVagas.arguments = Bundle().apply {
            putString("email", userEmail)
        }

        bottomNavigationView.menu.findItem(R.id.novaVaga).icon = menuFlutuante
        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.vagas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentVagas)
                        .commit()
                    true
                }

                R.id.perfil -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentPerfil)
                        .commit()
                    true
                }

                R.id.novaVaga -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentNovaVaga)
                        .commit()
                    true
                }

                else -> false
            }


        }
        if (userEmail == "interessado@gmail.com") {
            bottomNavigationView.menu.removeItem(R.id.novaVaga)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentVagas)
            .commit()
    }
}