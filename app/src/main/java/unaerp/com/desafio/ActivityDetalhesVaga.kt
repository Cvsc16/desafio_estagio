package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class ActivityDetalhesVaga : AppCompatActivity() {

    private val detalhesFragment=DescricaoFragment()
    private val contatosFragment=ContatoFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhesvaga)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        val back = findViewById<ImageView>(R.id.back)

        val vaga = intent.getSerializableExtra("vaga") as ClassVaga

        val nomeEmpresaTextView = findViewById<TextView>(R.id.nome_empresa)
        nomeEmpresaTextView.text = vaga.empresa

        val nomeVagaTextView = findViewById<TextView>(R.id.nomeVaga)
        nomeVagaTextView.text = vaga.titulo

        val nomeCidadeTextView = findViewById<TextView>(R.id.tv_cidade)
        nomeCidadeTextView.text = vaga.cidadeEmpresa

        val tipoVagaTextView = findViewById<TextView>(R.id.tv_tipo_vaga)
        tipoVagaTextView.text = vaga.tipoTrabalho

        val salarioTextView = findViewById<TextView>(R.id.tv_valorSalario)
        salarioTextView.text = "R$ ${vaga.pagamento}"


        val buttonDetalhes = findViewById<Button>(R.id.button_detalhes)
        val buttonDetalhes2 = findViewById<Button>(R.id.button_detalhes2)

        back.setOnClickListener {
            onBackPressed()
        }

        val bundle = Bundle().apply {
            putSerializable("vaga", vaga) // Substitua 'vaga' pelo objeto ClassVaga correto
        }
        detalhesFragment.arguments = bundle

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