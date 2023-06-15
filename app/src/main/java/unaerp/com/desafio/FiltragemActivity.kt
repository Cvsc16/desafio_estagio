package unaerp.com.desafio

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.BufferedReader
import java.io.InputStreamReader

class FiltragemActivity : AppCompatActivity(){

    private var areaConhecimentoSelecionada: String? = null
    private var cidadeSelecionada: String? = null
    private var empresaSelecionada: String? = null
    private var tipoTrabalhoSelecionado: String? = null
    private var remuneracaoSelecionada: String? = null
    private var escolhaUsuario: String? = null
    private var tipoConta: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtragem)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        val back = findViewById<ImageView>(R.id.back)
        val btn_filtro= findViewById<Button>(R.id.btn_filtrar)
        val btn_vagasGerais= findViewById<Button>(R.id.btn_vagasGerais)
        val btn_vagasAnunciante= findViewById<Button>(R.id.btn_minhasVagas)
        val btn_resetar = findViewById<Button>(R.id.btn_resetar)

        val spinner_areaConhecimento: Spinner = findViewById(R.id.spinner_areaConhecimento)
        val spinner_localidade: Spinner = findViewById(R.id.spinner_localidade)
        val spinner_anunciante: Spinner = findViewById(R.id.spinner_anunciante)
        val spinner_tipoVaga: Spinner = findViewById(R.id.spinner_tipoVaga)
        val spinner_remuneracao: Spinner = findViewById(R.id.spinner_remuneracao)

        val adapter_areaConhecimento = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_areaConhecimento, R.layout.spinner_item)
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_areaConhecimento.adapter = adapter_areaConhecimento

        // Ler o arquivo de texto contendo as cidades brasileiras
        val cidadeInputStream = resources.openRawResource(R.raw.cidades_brasileiras)
        val cidadeBufferedReader = BufferedReader(InputStreamReader(cidadeInputStream))

        val cidadesBrasileiras = mutableListOf<String>()
        var line: String?
        while (cidadeBufferedReader.readLine().also { line = it } != null) {
            cidadesBrasileiras.add(line!!)
        }
        cidadeBufferedReader.close()

// Adicionar "Selecione" como a primeira opção
        cidadesBrasileiras.add(0, "Todos")

        val adapter_localidade = ArrayAdapter(this, R.layout.spinner_item, cidadesBrasileiras)
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_localidade.adapter = adapter_localidade
        spinner_localidade.setSelection(0)

        val editTextPesquisa = findViewById<EditText>(R.id.editTextPesquisa)

        editTextPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar esse método
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtra as opções do Spinner com base no texto digitado
                adapter_localidade.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // Não é necessário implementar esse método
            }
        })

        val adapter_anunciante = ArrayAdapter<String>(this, R.layout.spinner_item)
        adapter_anunciante.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_anunciante.adapter = adapter_anunciante

        val database = FirebaseDatabase.getInstance().reference
        val usuariosRef = database.child("users")

        usuariosRef.orderByChild("tipo").equalTo("Anunciante").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val anuncianteList = mutableListOf<String>()

                for (snapshot in dataSnapshot.children) {
                    val usuarioId = snapshot.key
                    val usuarioNome = snapshot.child("nome").getValue(String::class.java)
                    if (usuarioId != null && usuarioNome != null) {
                        val nomeDividido = usuarioNome.split(" ")
                        val primeiroNome = nomeDividido.getOrNull(0)
                        val segundoNome = nomeDividido.getOrNull(1)

                        val nomeExibicao = if (primeiroNome != null && segundoNome != null) {
                            "$primeiroNome $segundoNome"
                        } else {
                            usuarioNome // Mantém o nome completo se não houver primeiro e segundo nome
                        }

                        anuncianteList.add(nomeExibicao)
                    }
                }

                // Adicione a opção "Todos" à lista de anunciantes
                anuncianteList.add(0, "Todos")

                // Atualize o adapter com a lista atualizada de anunciantes
                adapter_anunciante.clear()
                adapter_anunciante.addAll(anuncianteList)
                adapter_anunciante.notifyDataSetChanged()

                empresaSelecionada = intent.getStringExtra("empresaSelecionada")
                val empresaIndex = adapter_anunciante.getPosition(empresaSelecionada)
                if (empresaIndex >= 0) {
                    spinner_anunciante.setSelection(empresaIndex)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate o erro, se necessário
            }
        })

        val adapter_tipoVaga = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_tipoVaga, R.layout.spinner_item)
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tipoVaga.adapter = adapter_tipoVaga

        val adapter_remuneracao = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_remuneracao, R.layout.spinner_item)
        adapter_remuneracao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_remuneracao.adapter = adapter_remuneracao

        btn_vagasGerais.setOnClickListener {
            btn_vagasGerais.isSelected = true
            btn_vagasAnunciante.isSelected = false
            btn_vagasGerais.setBackgroundColor(resources.getColor(R.color.principal))
            btn_vagasGerais.setTextColor(resources.getColor(R.color.white))
            btn_vagasAnunciante.setBackgroundColor(resources.getColor(R.color.white))
            btn_vagasAnunciante.setTextColor(resources.getColor(R.color.principal))
            atualizarCorBotaoResetar()
        }

        btn_vagasAnunciante.setOnClickListener {
            btn_vagasAnunciante.isSelected = true
            btn_vagasGerais.isSelected = false
            btn_vagasAnunciante.setBackgroundColor(resources.getColor(R.color.principal))
            btn_vagasAnunciante.setTextColor(resources.getColor(R.color.white))
            btn_vagasGerais.setBackgroundColor(resources.getColor(R.color.white))
            btn_vagasGerais.setTextColor(resources.getColor(R.color.principal))
            atualizarCorBotaoResetar()
        }

        // Verificar se há uma escolha do usuário salva
        escolhaUsuario = intent.getStringExtra("escolhaUsuario")
        tipoConta = intent.getStringExtra("tipoConta")

        if(tipoConta == "Interessado"){
            btn_vagasGerais.visibility = View.GONE
            btn_vagasAnunciante.visibility = View.GONE
        }

            Log.d("LOGESCOLHAUSUARIO", "AREA PASSADA tela2:$escolhaUsuario")
        // Verificar a escolha do usuário na inicialização
        if (escolhaUsuario == null || escolhaUsuario == "Vagas Gerais") {
            // Destacar o botão "Vagas Gerais"
            btn_vagasGerais.isSelected = true
            btn_vagasGerais.setBackgroundColor(resources.getColor(R.color.principal))
            btn_vagasGerais.setTextColor(resources.getColor(R.color.white))
            btn_vagasAnunciante.setBackgroundColor(resources.getColor(R.color.white))
            btn_vagasAnunciante.setTextColor(resources.getColor(R.color.principal))
        } else if (escolhaUsuario == "Minhas Vagas") {
            // Destacar o botão "Minhas Vagas"
            btn_vagasAnunciante.isSelected = true
            btn_vagasAnunciante.setBackgroundColor(resources.getColor(R.color.principal))
            btn_vagasAnunciante.setTextColor(resources.getColor(R.color.white))
            btn_vagasGerais.setBackgroundColor(resources.getColor(R.color.white))
            btn_vagasGerais.setTextColor(resources.getColor(R.color.principal))
        }

        atualizarCorBotaoResetar()

        btn_resetar.setOnClickListener {
            resetarValores()
        }

        // Obtenha os valores selecionados salvos
        areaConhecimentoSelecionada = intent.getStringExtra("areaConhecimentoSelecionada")
        Log.d("LOGFILTRO", "AREA PASSADA:$areaConhecimentoSelecionada")
        cidadeSelecionada = intent.getStringExtra("cidadeSelecionada")
        Log.d("LOGFILTRO", "CIDADE PASSADA:$cidadeSelecionada")
//        empresaSelecionada = intent.getStringExtra("empresaSelecionada")
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

//        val empresaIndex = adapter_anunciante.getPosition(empresaSelecionada)
//        if (empresaIndex >= 0) {
//            spinner_anunciante.setSelection(empresaIndex)
//        }

        val tipoTrabalhoIndex = adapter_tipoVaga.getPosition(tipoTrabalhoSelecionado)
        if (tipoTrabalhoIndex >= 0) {
            spinner_tipoVaga.setSelection(tipoTrabalhoIndex)
        }

        val remuneracaoIndex = adapter_remuneracao.getPosition(remuneracaoSelecionada)
        if (remuneracaoIndex >= 0) {
            spinner_remuneracao.setSelection(remuneracaoIndex)
        }

        spinner_areaConhecimento.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                atualizarCorBotaoResetar()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implemente aqui caso deseje tratar o evento de nada selecionado
            }
        })

        spinner_localidade.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                atualizarCorBotaoResetar()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implemente aqui caso deseje tratar o evento de nada selecionado
            }
        })

        spinner_anunciante.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                atualizarCorBotaoResetar()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implemente aqui caso deseje tratar o evento de nada selecionado
            }
        })

        spinner_tipoVaga.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                atualizarCorBotaoResetar()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implemente aqui caso deseje tratar o evento de nada selecionado
            }
        })

        spinner_remuneracao.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                atualizarCorBotaoResetar()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implemente aqui caso deseje tratar o evento de nada selecionado
            }
        })

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

            // Verificar a escolha do usuário entre "Vagas Gerais" e "Minhas Vagas"
            if (btn_vagasGerais.isSelected) {
                resultIntent.putExtra("escolhaUsuario", "Vagas Gerais")
            } else if (btn_vagasAnunciante.isSelected) {
                resultIntent.putExtra("escolhaUsuario", "Minhas Vagas")
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
    fun resetarValores() {
        val btn_vagasGerais= findViewById<Button>(R.id.btn_vagasGerais)
        val btn_vagasAnunciante= findViewById<Button>(R.id.btn_minhasVagas)

        val spinner_areaConhecimento: Spinner = findViewById(R.id.spinner_areaConhecimento)
        val spinner_localidade: Spinner = findViewById(R.id.spinner_localidade)
        val spinner_anunciante: Spinner = findViewById(R.id.spinner_anunciante)
        val spinner_tipoVaga: Spinner = findViewById(R.id.spinner_tipoVaga)
        val spinner_remuneracao: Spinner = findViewById(R.id.spinner_remuneracao)
        spinner_areaConhecimento.setSelection(0)
        spinner_localidade.setSelection(0)
        spinner_anunciante.setSelection(0)
        spinner_tipoVaga.setSelection(0)
        spinner_remuneracao.setSelection(0)

        btn_vagasGerais.isSelected = true
        btn_vagasGerais.setBackgroundColor(resources.getColor(R.color.principal))
        btn_vagasGerais.setTextColor(resources.getColor(R.color.white))
        btn_vagasAnunciante.setBackgroundColor(resources.getColor(R.color.white))
        btn_vagasAnunciante.setTextColor(resources.getColor(R.color.principal))

        atualizarCorBotaoResetar()
    }
    private fun atualizarCorBotaoResetar() {
        val btn_resetar = findViewById<Button>(R.id.btn_resetar)

        val spinner_areaConhecimento: Spinner = findViewById(R.id.spinner_areaConhecimento)
        val spinner_localidade: Spinner = findViewById(R.id.spinner_localidade)
        val spinner_anunciante: Spinner = findViewById(R.id.spinner_anunciante)
        val spinner_tipoVaga: Spinner = findViewById(R.id.spinner_tipoVaga)
        val spinner_remuneracao: Spinner = findViewById(R.id.spinner_remuneracao)
        val btn_vagasGerais = findViewById<Button>(R.id.btn_vagasGerais)

        val isDefaultValues = spinner_areaConhecimento.selectedItemPosition == 0 &&
                spinner_localidade.selectedItemPosition == 0 &&
                spinner_anunciante.selectedItemPosition == 0 &&
                spinner_tipoVaga.selectedItemPosition == 0 &&
                spinner_remuneracao.selectedItemPosition == 0

        if (isDefaultValues && btn_vagasGerais.isSelected) {
            btn_resetar.setBackgroundColor(ContextCompat.getColor(this, R.color.detalhe))
            btn_resetar.isEnabled = false
        } else {
            btn_resetar.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            btn_resetar.isEnabled = true
        }
    }
}