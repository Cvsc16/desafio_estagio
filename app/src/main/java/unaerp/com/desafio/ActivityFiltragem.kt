package unaerp.com.desafio

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate

class ActivityFiltragem : AppCompatActivity(){

    private var areaConhecimentoSelecionada: String? = null
    private var cidadeSelecionada: String? = null
    private var empresaSelecionada: String? = null
    private var tipoTrabalhoSelecionado: String? = null
    private var remuneracaoSelecionada: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtragem)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        val back = findViewById<ImageView>(R.id.back)
        val btn_filtro= findViewById<Button>(R.id.btn_filtrar)

        val spinner_areaConhecimento: Spinner = findViewById(R.id.spinner_areaConhecimento)
        val spinner_localidade: Spinner = findViewById(R.id.spinner_localidade)
        val spinner_anunciante: Spinner = findViewById(R.id.spinner_anunciante)
        val spinner_tipoVaga: Spinner = findViewById(R.id.spinner_tipoVaga)
        val spinner_remuneracao: Spinner = findViewById(R.id.spinner_remuneracao)

        val adapter_areaConhecimento = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_areaConhecimento, R.layout.spinner_item)
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_areaConhecimento.adapter = adapter_areaConhecimento

        val adapter_localidade = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_localidade, R.layout.spinner_item)
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_localidade.adapter = adapter_localidade

        val adapter_anunciante = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_anunciante, R.layout.spinner_item)
        adapter_anunciante.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_anunciante.adapter = adapter_anunciante

        val adapter_tipoVaga = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_tipoVaga, R.layout.spinner_item)
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tipoVaga.adapter = adapter_tipoVaga

        val adapter_remuneracao = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_remuneracao, R.layout.spinner_item)
        adapter_remuneracao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_remuneracao.adapter = adapter_remuneracao

        // Obtenha os valores selecionados salvos
        areaConhecimentoSelecionada = intent.getStringExtra("areaConhecimentoSelecionada")
        Log.d("LOGFILTRO", "AREA PASSADA:$areaConhecimentoSelecionada")
        cidadeSelecionada = intent.getStringExtra("cidadeSelecionada")
        Log.d("LOGFILTRO", "CIDADE PASSADA:$cidadeSelecionada")
        empresaSelecionada = intent.getStringExtra("empresaSelecionada")
        Log.d("LOGFILTRO", "EMPRESA PASSADA:$empresaSelecionada")
        tipoTrabalhoSelecionado = intent.getStringExtra("tipoTrabalhoSelecionado")
        Log.d("LOGFILTRO", "TRABALHO PASSADA:$tipoTrabalhoSelecionado")
        remuneracaoSelecionada = intent.getStringExtra("remuneracaoSelecionada")

        // Pré-selecione as opções salvas nos spinners correspondentes
        val areaConhecimentoIndex = adapter_areaConhecimento.getPosition(areaConhecimentoSelecionada)
        if (areaConhecimentoIndex >= 0) {
            spinner_areaConhecimento.setSelection(areaConhecimentoIndex)
        }

        val cidadeIndex = adapter_localidade.getPosition(cidadeSelecionada)
        if (cidadeIndex >= 0) {
            spinner_localidade.setSelection(cidadeIndex)
        }

        val empresaIndex = adapter_anunciante.getPosition(empresaSelecionada)
        if (empresaIndex >= 0) {
            spinner_anunciante.setSelection(empresaIndex)
        }

        val tipoTrabalhoIndex = adapter_tipoVaga.getPosition(tipoTrabalhoSelecionado)
        if (tipoTrabalhoIndex >= 0) {
            spinner_tipoVaga.setSelection(tipoTrabalhoIndex)
        }

        val remuneracaoIndex = adapter_remuneracao.getPosition(remuneracaoSelecionada)
        if (remuneracaoIndex >= 0) {
            spinner_remuneracao.setSelection(remuneracaoIndex)
        }

        back.setOnClickListener {
            onBackPressed()
        }

        btn_filtro.setOnClickListener {
            val areaConhecimentoSelecionada = spinner_areaConhecimento.selectedItem.toString()
            val cidadeSelecionada = spinner_localidade.selectedItem.toString()
            val empresaSelecionada = spinner_anunciante.selectedItem.toString()
            val tipoTrabalhoSelecionado = spinner_tipoVaga.selectedItem.toString()
            val remuneracaoSelecionada = spinner_remuneracao.selectedItem.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("areaConhecimentoSelecionada", areaConhecimentoSelecionada)
            resultIntent.putExtra("cidadeSelecionada", cidadeSelecionada)
            resultIntent.putExtra("empresaSelecionada", empresaSelecionada)
            resultIntent.putExtra("tipoTrabalhoSelecionado", tipoTrabalhoSelecionado)
            resultIntent.putExtra("remuneracaoSelecionada", remuneracaoSelecionada)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}