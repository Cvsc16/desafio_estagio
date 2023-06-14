package unaerp.com.desafio

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private lateinit var nomeEmpresa: EditText
private lateinit var tituloVaga: EditText
private lateinit var descricaoVar: EditText
private lateinit var remuneracao: EditText
private lateinit var telefoneVar: EditText
private lateinit var emailVar: EditText
private lateinit var dataInicioVar: EditText
private lateinit var dataFimVar: EditText
private lateinit var switchVisibilidade: Switch
private lateinit var spinnerAreaConhecimento: Spinner
private lateinit var spinnerLocalidade: Spinner
private lateinit var spinnerTipoVaga: Spinner

class ActivityEdicaoVagas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicao_vaga)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        val vaga = intent.getSerializableExtra("vaga") as? ClassVaga

        tituloVaga = findViewById(R.id.textarea_titulo)
        tituloVaga.setText(vaga?.titulo)

        spinnerAreaConhecimento = findViewById(R.id.spinner_areaConhecimento)
        val areaConhecimento = vaga?.areaConhecimento

        val areaConhecimentoOptions =
            resources.getStringArray(R.array.opcoes_spinner_areaConhecimento)
                .toMutableList()
                .apply {
                    remove("Todos")
                }

        val adapter_areaConhecimento = ArrayAdapter(
            this,
            R.layout.spinner_item,
            areaConhecimentoOptions
        )
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAreaConhecimento.adapter = adapter_areaConhecimento

        val index_areaConhecimento = adapter_areaConhecimento.getPosition(areaConhecimento)
        if (index_areaConhecimento != -1) {
            spinnerAreaConhecimento.setSelection(index_areaConhecimento)
        }

        descricaoVar = findViewById(R.id.textarea_descricao)
        descricaoVar.setText(vaga?.descricao)

        remuneracao = findViewById(R.id.textarea_remuneracao)
        remuneracao.setText(vaga?.pagamento)

        telefoneVar = findViewById(R.id.textarea_telefone)
        telefoneVar.setText(vaga?.telefone)

        emailVar = findViewById(R.id.textarea_email)
        emailVar.setText(vaga?.emailEmpresa)

        nomeEmpresa = findViewById(R.id.nome_anunciante)
        nomeEmpresa.setText(vaga?.empresa)

        spinnerLocalidade = findViewById(R.id.spinner_localidade)
        val localidade = vaga?.cidadeEmpresa

// Ler o arquivo de texto contendo as cidades brasileiras
        val cidadeInputStream = resources.openRawResource(R.raw.cidades_brasileiras)
        val cidadeBufferedReader = BufferedReader(InputStreamReader(cidadeInputStream))

        val cidadesBrasileiras = mutableListOf<String>()
        var line: String?
        while (cidadeBufferedReader.readLine().also { line = it } != null) {
            cidadesBrasileiras.add(line!!)
        }
        cidadeBufferedReader.close()

// Criar o adapter para o spinner com as cidades brasileiras
        val adapter_localidade = ArrayAdapter(
            this,
            R.layout.spinner_item,
            cidadesBrasileiras
        )
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocalidade.adapter = adapter_localidade

        if (localidade != null) {
            val index_localidade = adapter_localidade.getPosition(localidade)
            if (index_localidade != -1) {
                spinnerLocalidade.setSelection(index_localidade)
            }
        } else {
            spinnerLocalidade.setSelection(0)
        }

        val editTextPesquisa: EditText = findViewById(R.id.editTextPesquisa)
        editTextPesquisa.setText(vaga?.cidadeEmpresa)

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

        spinnerTipoVaga = findViewById(R.id.spinner_tipoVaga)
        val spinner_tipoVaga = vaga?.tipoTrabalho

        val tipoVagaOptions =
            resources.getStringArray(R.array.opcoes_spinner_tipoVaga).toMutableList()
                .apply {
                    remove("Todos")
                }

        val adapter_tipoVaga = ArrayAdapter(
            this,
            R.layout.spinner_item,
            tipoVagaOptions
        )
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVaga.adapter = adapter_tipoVaga

        val index_tipoVaga = adapter_tipoVaga.getPosition(spinner_tipoVaga)
        if (index_tipoVaga != -1) {
            spinnerTipoVaga.setSelection(index_tipoVaga)
        }

        dataFimVar = findViewById(R.id.textarea_datafim)
        dataFimVar.setText(vaga?.dataFim)

// Define o listener para o clique no EditText
        dataFimVar.setOnClickListener {
            // Obtém a data atual para exibir no DatePicker
            val calendar = Calendar.getInstance()
            val ano = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            // Obtém a data atualmente exibida no EditText
            val dataAtual = dataFimVar.text.toString()

            // Converte a data atual para o formato do DatePicker
            val dataSelecionada =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dataAtual)

            // Cria um novo DatePickerDialog para exibir o calendário com a data atualmente exibida
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    // Atualiza o texto do EditText com a nova data selecionada
                    val novaData = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%d",
                        dayOfMonth,
                        (month + 1),
                        year
                    )
                    dataFimVar.setText(novaData)
                }, ano, mes, dia
            )

            // Define a data atualmente exibida como a data inicial do DatePickerDialog
            datePickerDialog.datePicker.updateDate(
                dataSelecionada.year + 1900,
                dataSelecionada.month,
                dataSelecionada.date
            )

            // Exibe o DatePickerDialog
            datePickerDialog.show()
        }

        dataInicioVar = findViewById(R.id.textarea_datainicio)
        dataInicioVar.setText(vaga?.dataInicio)

// Cria um SimpleDateFormat para formatar a data atual
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataAtual = Date()

// Formata a data atual
        val dataFormatada = formato.format(dataAtual)

// Define o valor formatado como texto padrão da EditText
        dataInicioVar.setText(dataFormatada)

// Define a propriedade focusable da EditText para false
        dataInicioVar.setFocusable(false)
        dataInicioVar.setFocusableInTouchMode(false)


        // Obtém a referência do Switch
        switchVisibilidade = findViewById(R.id.switch_anunciante)

// Define a cor para quando o Switch estiver desativado (à esquerda)
        val corDesativado = Color.parseColor("#93BCFB")

// Define a cor para quando o Switch estiver ativado (à direita)
        val corAtivado = Color.parseColor("#172B4D")

// Define a cor inicial do Switch
        switchVisibilidade.thumbTintList = ColorStateList.valueOf(corDesativado)

// Define o Listener para o Switch
        switchVisibilidade.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchVisibilidade.thumbTintList = ColorStateList.valueOf(corAtivado)
            } else {
                switchVisibilidade.thumbTintList = ColorStateList.valueOf(corDesativado)
            }
        }

        val fullName = vaga?.empresa
        val names = fullName?.split(" ")

        val formattedName = if (names?.size ?: 0 >= 2) {
            val firstName = names?.get(0)
            val lastName = names?.get(1)
            "$firstName $lastName"
        } else {
            fullName
        }

        nomeEmpresa.setText(formattedName)

        switchVisibilidade.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchVisibilidade.thumbTintList = ColorStateList.valueOf(corDesativado)
                nomeEmpresa.setText("oculto")
            } else {
                switchVisibilidade.thumbTintList = ColorStateList.valueOf(corAtivado)
                nomeEmpresa.setText(formattedName)
            }
        }

        val back : ImageView = findViewById(R.id.back)
        val btn_atualizarVaga : Button = findViewById(R.id.btn_atualizarVaga)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_atualizarVaga.setOnClickListener {
            Log.d("MeuBotao", "Botão clicado")
            try {
                atualizarVaga()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao cadastrar a vaga", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun atualizarVaga() {
        val vaga = intent.getSerializableExtra("vaga") as? ClassVaga
        val vagaId = vaga?.id

        if (vagaId == null) {
            Toast.makeText(this, "ID da vaga não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val titulo = tituloVaga.text.toString()
        val empresa = if (switchVisibilidade.isChecked) "" else nomeEmpresa.text.toString()
        val cidadeEmpresa = spinnerLocalidade.selectedItem.toString()
        val tipoTrabalho = spinnerTipoVaga.selectedItem.toString()
        val dataInicio = dataInicioVar.text.toString()
        val dataFim = dataFimVar.text.toString()
        val pagamento = if (remuneracao.text.isBlank()) "A Combinar" else remuneracao.text.toString()
        val areaConhecimento = spinnerAreaConhecimento.selectedItem.toString()
        val descricao = descricaoVar.text.toString()
        val telefone = telefoneVar.text.toString()
        val emailEmpresa = emailVar.text.toString()

        // Verificar se algum campo obrigatório foi alterado
        if (titulo.isBlank() && cidadeEmpresa == "Selecionar" &&
            tipoTrabalho == "Selecionar" && dataInicio.isBlank() &&
            areaConhecimento == "Selecionar" && descricao.isBlank() &&
            telefone.isBlank() && emailEmpresa.isBlank()
        ) {
            Toast.makeText(this, "Nenhum campo alterado", Toast.LENGTH_SHORT).show()
            return
        }

        // Crie um mapa para armazenar os campos atualizados da vaga
        val atualizacaoVagaMap: MutableMap<String, Any?> = mutableMapOf()

        // Adicione apenas os campos que foram alterados
        if (!titulo.isBlank()) {
            atualizacaoVagaMap["titulo"] = titulo
        }
        if (cidadeEmpresa != "Selecionar") {
            atualizacaoVagaMap["cidadeEmpresa"] = cidadeEmpresa
        }
        if (tipoTrabalho != "Selecionar") {
            atualizacaoVagaMap["tipoTrabalho"] = tipoTrabalho
        }
        if (!dataInicio.isBlank()) {
            atualizacaoVagaMap["dataInicio"] = dataInicio
        }
        if (areaConhecimento != "Selecionar") {
            atualizacaoVagaMap["areaConhecimento"] = areaConhecimento
        }
        if (!descricao.isBlank()) {
            atualizacaoVagaMap["descricao"] = descricao
        }
        if (!telefone.isBlank()) {
            atualizacaoVagaMap["telefone"] = telefone
        }
        if (!emailEmpresa.isBlank()) {
            atualizacaoVagaMap["emailEmpresa"] = emailEmpresa
        }

        // Verifique se algum campo foi alterado
        if (atualizacaoVagaMap.isEmpty()) {
            Toast.makeText(this, "Nenhum campo alterado", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance().reference
        database.child("vagas").child(vagaId).updateChildren(atualizacaoVagaMap)
            .addOnSuccessListener {
                // Sucesso ao atualizar a vaga
                Toast.makeText(this, "Vaga atualizada com sucesso", Toast.LENGTH_SHORT).show()

                // Retornar para a MainActivity após a atualização
                onBackPressed()
            }
            .addOnFailureListener { exception ->
                // Falha ao atualizar a vaga
                Toast.makeText(this, "Erro ao atualizar a vaga: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
            .addOnCanceledListener {
                // Operação cancelada
                Toast.makeText(this, "Atualização da vaga cancelada", Toast.LENGTH_SHORT).show()
            }
    }
}