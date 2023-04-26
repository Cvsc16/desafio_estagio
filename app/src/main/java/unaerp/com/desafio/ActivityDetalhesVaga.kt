package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

import unaerp.com.desafio.DescricaoFragment
import unaerp.com.desafio.ContatoFragment

class ActivityDetalhesVaga : AppCompatActivity() {

    private val detalhesFragment=DescricaoFragment()
    private val contatosFragment=ContatoFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhesvaga)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val buttonDetalhes = findViewById<Button>(R.id.button_detalhes)
        val buttonDetalhes2 = findViewById<Button>(R.id.button_detalhes2)

        // Adicionar o fragmento padrão
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detalhesFragment)
            .commit()

        buttonDetalhes.setOnClickListener {
            buttonDetalhes2.setBackgroundResource(0)
            buttonDetalhes.setBackgroundResource(R.drawable.button_detalhesvaga)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detalhesFragment)
                .commit()
        }

        buttonDetalhes2.setOnClickListener {
            buttonDetalhes2.setBackgroundResource(R.drawable.button_detalhesvaga)
            buttonDetalhes.setBackgroundResource(0)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, contatosFragment)
                .commit()
        }
    }
}