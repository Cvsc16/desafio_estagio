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
    private var selectedItemId = R.id.vagas
    private var userEmail: String? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuFlutuante = resources.getDrawable(R.drawable.ic_novavaga_svg, null)

        // Recebe o email do usuário por meio do Intent
        userEmail = intent.getStringExtra("email")

        FragmentVagas.arguments = Bundle().apply {
            putString("email", userEmail)
        }

        bottomNavigationView.menu.findItem(R.id.novaVaga).icon = menuFlutuante
        bottomNavigationView.itemIconTintList = null
        selectedItemId = bottomNavigationView.selectedItemId

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            selectedItemId = item.itemId
            when (item.itemId) {
                R.id.vagas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentVagas)
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.perfil -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentPerfil)
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.novaVaga -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentNovaVaga)
                        .addToBackStack(null)
                        .commit()
                    true
                }

                else -> false
            }
        }

// Define o item selecionado
        bottomNavigationView.selectedItemId = selectedItemId

        if (userEmail == "interessado@gmail.com") {
            bottomNavigationView.menu.removeItem(R.id.novaVaga)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentVagas)
            .commit()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStackImmediate()
            val fragment = fragmentManager.findFragmentById(R.id.fragment_container)
            selectedItemId = when (fragment) {
                is FragmentVagas -> R.id.vagas
                is FragmentPerfil -> R.id.perfil
                is FragmentNovaVaga -> R.id.novaVaga
                else -> R.id.vagas
            }
        } else {
            super.onBackPressed()
        }

        bottomNavigationView.selectedItemId = selectedItemId
    }
}
