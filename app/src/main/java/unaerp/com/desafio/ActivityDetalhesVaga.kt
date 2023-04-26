package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

import unaerp.com.desafio.DescricaoFragment
import unaerp.com.desafio.ContatoFragment

class ActivityDetalhesVaga : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhesvaga)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val buttonDetalhes = findViewById<Button>(R.id.button_detalhes)
        val buttonDetalhes2 = findViewById<Button>(R.id.button_detalhes2)

        // Adicionar o fragmento padrão
        val fragment1 = DescricaoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment1)
            .commit()

        buttonDetalhes.setOnClickListener {
            buttonDetalhes2.setBackgroundResource(0)
            buttonDetalhes.setBackgroundResource(R.drawable.button_detalhesvaga)
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container2)
            if (currentFragment != null) {
                supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            }
            // Adicionar o novo fragmento
            val fragment1 = DescricaoFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment1)
                .addToBackStack(null)
                .commit()
        }

        buttonDetalhes2.setOnClickListener {
            buttonDetalhes2.setBackgroundResource(R.drawable.button_detalhesvaga)
            buttonDetalhes.setBackgroundResource(0)
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment != null) {
                supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            }
            val fragment2 = ContatoFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container2, fragment2)
                .addToBackStack(null)
                .commit()
        }
    }
}