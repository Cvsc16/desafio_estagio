package unaerp.com.desafio

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import unaerp.com.desafio.databinding.ActivityDetalhesvagaBinding
import unaerp.com.desafio.databinding.ActivityEdicaoVagaBinding
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
lateinit var sharedPrefs: SharedPreferences
private var fullName: String = ""

class ActivityEdicaoVagas : AppCompatActivity() {

    private lateinit var binding: ActivityEdicaoVagaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdicaoVagaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        val vaga = intent.getSerializableExtra("vaga") as? ClassVaga

        tituloVaga = binding.textareaTitulo
        tituloVaga.setText(vaga?.titulo)

        spinnerAreaConhecimento = binding.spinnerAreaConhecimento
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

        descricaoVar = binding.textareaDescricao
        descricaoVar.setText(vaga?.descricao)

        remuneracao = binding.textareaRemuneracao
        remuneracao.setText(vaga?.pagamento)

        telefoneVar = binding.textareaTelefone
        telefoneVar.setText(vaga?.telefone)

        emailVar = binding.textareaEmail
        emailVar.setText(vaga?.emailEmpresa)

        nomeEmpresa = binding.nomeAnunciante
        nomeEmpresa.setText(vaga?.empresa)

        spinnerLocalidade = binding.spinnerLocalidade
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

        val editTextPesquisa: EditText = binding.editTextPesquisa
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

        spinnerTipoVaga = binding.spinnerTipoVaga
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

        dataFimVar = binding.textareaDatafim
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

        dataInicioVar = binding.textareaDatainicio
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
        val switch = binding.switchAnunciante

// Define a cor para quando o Switch estiver desativado (à esquerda)
        val corDesativado = Color.parseColor("#93BCFB")

// Define a cor para quando o Switch estiver ativado (à direita)
        val corAtivado = Color.parseColor("#172B4D")

// Define a cor inicial do Switch
        switch.thumbTintList = ColorStateList.valueOf(corDesativado)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch.thumbTintList = ColorStateList.valueOf(corDesativado)
                nomeEmpresa.setText("Oculto")
            } else {
                switch.thumbTintList = ColorStateList.valueOf(corAtivado)
                nomeEmpresa.setText(fullName)
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val database = FirebaseDatabase.getInstance().reference.child("users").child(userId!!)



        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(ClassUser::class.java)
                    userData?.let { user ->
                        val fullName = user.nome
                        val names = fullName.split(" ")
                        var formattedName = fullName // Definir o nome completo como valor padrão

                        if (names.size >= 2) {
                            val firstName = names[0]
                            val lastName = names[1]
                            formattedName = "$firstName $lastName"
                        }

                        nomeEmpresa.setText(formattedName)

                        // Atualizar a variável global fullName
                        atualizarFullName(formattedName)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tratar o erro, se necessário
            }
        })



        val back : ImageView = binding.back
        val btn_atualizarVaga : Button = binding.btnAtualizarVaga

        back.setOnClickListener {
            finish()
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
        switchVisibilidade = binding.switchAnunciante
    }



    private fun atualizarFullName(value: String) {
        fullName = value
    }
    private fun atualizarVaga() {
        val vaga = intent.getSerializableExtra("vaga") as? ClassVaga
        val vagaId = vaga?.id

        if (vagaId == null) {
            Toast.makeText(this, "ID da vaga não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val titulo = tituloVaga.text.toString()
        val empresa = if (switchVisibilidade.isChecked) "Oculto" else nomeEmpresa.text.toString()
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
        if (!pagamento.isBlank()) {
            atualizacaoVagaMap["pagamento"] = pagamento
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
        if (!dataFim.isBlank()) {
            atualizacaoVagaMap["dataFim"] = dataFim
        }
        if (switchVisibilidade.isChecked) {
            atualizacaoVagaMap["empresa"] = null
        } else {
            atualizacaoVagaMap["empresa"] = nomeEmpresa.text.toString()
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
            Toast.makeText(this, "Nenhum campo foi alterado", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance().reference
        database.child("vagas").child(vagaId).updateChildren(atualizacaoVagaMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sucesso ao atualizar a vaga
                    Toast.makeText(this, "Vaga atualizada com sucesso", Toast.LENGTH_SHORT).show()

                    // Retornar para a MainActivity após a atualização
                    onBackPressed()
                } else {
                    // Falha ao atualizar a vaga
                    val errorMessage = task.exception?.message ?: "Erro ao atualizar a vaga"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnCanceledListener {
                // Operação cancelada
                Toast.makeText(this, "Atualização da vaga cancelada", Toast.LENGTH_SHORT).show()
            }
    }
}