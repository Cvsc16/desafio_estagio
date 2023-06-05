package unaerp.com.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate

class ActivityFiltragem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtragem)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        back.setOnClickListener {
            onBackPressed()
        }

        btn_filtro.setOnClickListener {
            val areaConhecimento = spinner_areaConhecimento.selectedItem.toString()
            val localidade = spinner_localidade.selectedItem.toString()
            val anunciante = spinner_anunciante.selectedItem.toString()
            val tipoVaga = spinner_tipoVaga.selectedItem.toString()
            val remuneracao = spinner_remuneracao.selectedItem.toString()

            val fragmentVagas = supportFragmentManager.findFragmentByTag("FragmentVagasTag") as? FragmentVagas
            fragmentVagas?.atualizarFiltro(areaConhecimento, localidade, anunciante, tipoVaga, remuneracao)

            onBackPressed()
        }

    }
}