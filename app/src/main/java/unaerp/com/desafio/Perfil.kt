package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class Perfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val menuFlutuante = resources.getDrawable(R.drawable.ic_novavaga_svg, null)
        bottomNavigationView.menu.findItem(R.id.search).icon = menuFlutuante

        bottomNavigationView.itemIconTintList = null

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // tratamento para a seleção do item Home
                    true
                }
                R.id.perfil -> {
                    // tratamento para a seleção do item Perfil
                    true
                }
                R.id.search -> {
                    // tratamento para a seleção do item Search
                    true
                }
                else -> false
            }

        }

        bottomNavigationView.setSelectedItemId(R.id.perfil)
    }
}



