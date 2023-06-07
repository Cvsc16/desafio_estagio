package unaerp.com.desafio


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private val FragmentNovaVaga = FragmentNovaVaga()
    private val FragmentPerfil = FragmentPerfil()
    private val FragmentVagas = FragmentVagas()
    private var selectedItemId = R.id.vagas
    private var userEmail: String? = null
    private var tipoConta: String? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height
            val keyboardVisibleThreshold = 200 // Ajuste conforme necessário

            if (heightDiff > keyboardVisibleThreshold) {
                // Teclado visível
                bottomNavigationView.visibility = View.GONE
            } else {
                // Teclado oculto
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val menuFlutuante = resources.getDrawable(R.drawable.ic_novavaga_svg, null)

        // Recebe o email do usuário por meio do Intent
        userEmail = intent.getStringExtra("email")

        tipoConta = intent.getStringExtra("tipo_conta")
        Log.d("TAGCONTA", "Tipo de conta recebido: $tipoConta")

        FragmentVagas.arguments = Bundle().apply {
            putString("email", userEmail)
            putString("tipo_conta", tipoConta)
        }

// Define o ícone do item de menu para o menu flutuante
        bottomNavigationView.menu.findItem(R.id.novaVaga).icon = menuFlutuante

// Define a cor do ícone do item de menu como nula
        bottomNavigationView.itemIconTintList = null

// Define o item selecionado
        bottomNavigationView.selectedItemId = R.id.vagas

// Adiciona um listener para a seleção de item de menu
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.novaVaga && tipoConta == "Interessado") {
                return@setOnNavigationItemSelectedListener false
            }

            if (bottomNavigationView.selectedItemId == item.itemId) {
                return@setOnNavigationItemSelectedListener false
            }

            when (item.itemId) {
                R.id.vagas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentVagas, "FragmentVagas")
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

            // Retorna verdadeiro para indicar que o item de menu foi selecionado
            true
        }

// Adiciona um listener para a transação do fragmento
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val itemId = when (fragment) {
                is FragmentVagas -> R.id.vagas
                is FragmentPerfil -> R.id.perfil
                is FragmentNovaVaga -> R.id.novaVaga
                else -> R.id.vagas
            }
            bottomNavigationView.selectedItemId = itemId
        }



        if (tipoConta == "Interessado") {
            bottomNavigationView.menu.removeItem(R.id.novaVaga)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentVagas)
            .commit()
    }
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        // Verifica se o fragmento atual é o fragmento de vagas
        if (currentFragment?.id == R.id.vagas) {
            finish()
        } else {
            // Se não for o fragmento de vagas, chama o método padrão
            super.onBackPressed()
        }
    }
}
