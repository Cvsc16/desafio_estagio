package unaerp.com.desafio
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import unaerp.com.desafio.databinding.ActivitySplashBinding
import unaerp.com.desafio.databinding.ActivityTipocontaBinding


class TipoContaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipocontaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipocontaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val contaInteressado = binding.contaInteressado
        val contaAnunciante = binding.contaAnunciante
        val btn_prosseguir = binding.btnProsseguir
        val back = binding.back

        var tipoConta = "" // variável para guardar o tipo de conta selecionado

        btn_prosseguir.setOnClickListener {
            if (contaInteressado.isSelected) {
                tipoConta = "Interessado"
            } else if (contaAnunciante.isSelected) {
                tipoConta = "Anunciante"
            }

            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("tipo_conta", tipoConta)
            startActivity(intent)
            finish()
        }

        back.setOnClickListener {
            finish()
        }

        // Variável para armazenar o ID do layout selecionado
        var layoutSelecionadoId: Int? = null

        // Método para selecionar o layout clicado e atualizar o background
        fun selecionarLayout(layoutId: Int) {
            if (layoutSelecionadoId == layoutId) {
                // O layout já está selecionado, então não faz nada
                return
            }

            // Restaurando o background do layout desmarcado, se houver um
            layoutSelecionadoId?.let { id ->
                val layoutDesmarcado = findViewById<View>(id)
                layoutDesmarcado.setBackgroundResource(R.drawable.fundo_tipovaga)
                layoutDesmarcado.isSelected = false // adicionando essa linha
            }

            // Selecionando o novo layout e atualizando o background
            layoutSelecionadoId = layoutId
            val layoutSelecionado = findViewById<View>(layoutId)
            layoutSelecionado.setBackgroundResource(R.drawable.fundo_tipovagaselecionado)
            layoutSelecionado.isSelected = true // adicionando essa linha

            // Habilitando o botão de filtro
            btn_prosseguir.isEnabled = true
            btn_prosseguir.setBackgroundResource(R.drawable.btn_rounded)
        }

        // Definindo os ouvintes de clique dos layouts
        contaInteressado.setOnClickListener {
            selecionarLayout(contaInteressado.id)
        }

        contaAnunciante.setOnClickListener {
            selecionarLayout(contaAnunciante.id)
        }

        // Desabilitando o botão de filtro inicialmente
        btn_prosseguir.isEnabled = false
        btn_prosseguir.setBackgroundResource(R.drawable.btn_roundeddisable)
    }
}
