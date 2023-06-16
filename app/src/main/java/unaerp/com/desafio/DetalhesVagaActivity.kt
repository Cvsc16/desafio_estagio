package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import unaerp.com.desafio.databinding.ActivityCadastroBinding
import unaerp.com.desafio.databinding.ActivityDetalhesvagaBinding

class DetalhesVagaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesvagaBinding
    private val detalhesFragment=DescricaoFragment()
    private val contatosFragment=ContatoFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesvagaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        val back = binding.back

        val vaga = intent.getSerializableExtra("vaga") as ClassVaga


        val nomeEmpresaTextView = binding.nomeEmpresa
        nomeEmpresaTextView.text = vaga.empresa

        val nomeVagaTextView = binding.nomeVaga
        nomeVagaTextView.text = vaga.titulo

        val nomeCidadeTextView = binding.tvCidade
        nomeCidadeTextView.text = vaga.cidadeEmpresa

        val tipoVagaTextView = binding.tvTipoVaga
        tipoVagaTextView.text = vaga.tipoTrabalho

        val salarioTextView = binding.tvValorSalario
        salarioTextView.text = "R$ ${vaga.pagamento}"


        val buttonDetalhes = binding.buttonDetalhes
        val buttonDetalhes2 = binding.buttonDetalhes2

        back.setOnClickListener {
            onBackPressed()
        }

        val bundle = Bundle().apply {
            putSerializable("vaga", vaga)
        }
        detalhesFragment.arguments = bundle
        contatosFragment.arguments = bundle

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