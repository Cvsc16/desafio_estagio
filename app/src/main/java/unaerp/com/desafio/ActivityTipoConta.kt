package unaerp.com.desafio
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout


class ActivityTipoConta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipoconta)
        supportActionBar?.hide()

        val contaInteressado = findViewById<ConstraintLayout>(R.id.conta_interessado)
        val contaAnunciante = findViewById<ConstraintLayout>(R.id.conta_anunciante)
        val btn_prosseguir = findViewById<Button>(R.id.btn_prosseguir)

        var tipoConta = "" // variável para guardar o tipo de conta selecionado

        btn_prosseguir.setOnClickListener {
            if (contaInteressado.isSelected) {
                tipoConta = "interessado"
            } else if (contaAnunciante.isSelected) {
                tipoConta = "anunciante"
            }

            val intent = Intent(this, ActivityCadastro::class.java)
            intent.putExtra("tipo_conta", tipoConta)
            startActivity(intent)
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
            }

            // Selecionando o novo layout e atualizando o background
            layoutSelecionadoId = layoutId
            val layoutSelecionado = findViewById<View>(layoutId)
            layoutSelecionado.setBackgroundResource(R.drawable.fundo_tipovagaselecionado)

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
