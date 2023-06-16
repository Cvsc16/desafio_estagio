package unaerp.com.desafio

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import unaerp.com.desafio.databinding.FragmentContatoBinding
import unaerp.com.desafio.databinding.FragmentNovavagaBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
class FragmentNovaVaga : Fragment() {

    private lateinit var binding: FragmentNovavagaBinding
    private lateinit var nomeEmpresa: EditText
    private lateinit var tituloVaga: EditText
    private lateinit var descricaoVar: EditText
    private lateinit var remuneracao: EditText
    private lateinit var telefoneVar: EditText
    private lateinit var email: EditText
    private lateinit var dataInicioVar: EditText
    private lateinit var dataFimVar: EditText
    private lateinit var pesquisa: EditText
    private lateinit var switchVisibilidade: Switch
    private lateinit var spinnerAreaConhecimento: Spinner
    private lateinit var spinnerLocalidade: Spinner
    private lateinit var spinnerTipoVaga: Spinner
    private lateinit var fullName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNovavagaBinding.inflate(inflater, container, false)
        val view = binding.root

        val back = binding.back

        spinnerAreaConhecimento = binding.spinnerAreaConhecimento
        spinnerLocalidade = binding.spinnerLocalidade
        spinnerTipoVaga = binding.spinnerTipoVaga


        val editText = binding.nomeAnunciante
        editText.keyListener = null

        val areaConhecimentoOptions = resources.getStringArray(R.array.opcoes_spinner_areaConhecimento)
            .toMutableList()
            .apply {
                val index = indexOf("Todos")
                if (index != -1) {
                    set(index, "Selecione")
                }
            }

        val adapter_areaConhecimento = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            areaConhecimentoOptions
        )
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAreaConhecimento.adapter = adapter_areaConhecimento
        spinnerAreaConhecimento.setSelection(0)


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
        cidadesBrasileiras.add(0, "Selecione")

// Criar o adapter para o spinner com as cidades brasileiras
        val adapter_localidade = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            cidadesBrasileiras
        )
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocalidade.adapter = adapter_localidade
        spinnerLocalidade.setSelection(0) // Define "Selecione" como o item selecionado

        val editTextPesquisa = binding.editTextPesquisa

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

        val tipoVagaOptions = resources.getStringArray(R.array.opcoes_spinner_tipoVaga)
            .toMutableList()
            .apply {
                val index = indexOf("Todos")
                if (index != -1) {
                    set(index, "Selecione")
                }
            }

        val adapter_tipoVaga = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            tipoVagaOptions
        )
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVaga.adapter = adapter_tipoVaga

        val telefoneEditText = binding.textareaTelefone
        telefoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val etData = binding.textareaDatafim

// Define o listener para o clique no EditText
        etData.setOnClickListener {
            // Obtém a data atual para exibir no DatePicker
            val calendar = Calendar.getInstance()
            val ano = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            // Cria um novo DatePickerDialog para exibir o calendário
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth ->
                    // Atualiza o texto do EditText com a data selecionada
                    val data = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%d",
                        dayOfMonth,
                        (month + 1),
                        year
                    )
                    etData.setText(data)
                }, ano, mes, dia
            )

            // Exibe o DatePickerDialog
            datePickerDialog.show()
        }

        val etData2 = binding.textareaDatainicio

// Cria um SimpleDateFormat para formatar a data atual
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataAtual = Date()

// Formata a data atual
        val dataFormatada = formato.format(dataAtual)

// Define o valor formatado como texto padrão da EditText
        etData2.setText(dataFormatada)

// Define a propriedade focusable da EditText para false
        etData2.setFocusable(false)
        etData2.setFocusableInTouchMode(false)


        // Obtém a referência do Switch
        val switch = binding.switchAnunciante

// Define a cor para quando o Switch estiver desativado (à esquerda)
        val corDesativado = Color.parseColor("#93BCFB")

// Define a cor para quando o Switch estiver ativado (à direita)
        val corAtivado = Color.parseColor("#172B4D")

// Define a cor inicial do Switch
        switch.thumbTintList = ColorStateList.valueOf(corDesativado)

// Define o Listener para o Switch
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch.thumbTintList = ColorStateList.valueOf(corAtivado)
            } else {
                switch.thumbTintList = ColorStateList.valueOf(corDesativado)
            }
        }

        val editText_nome = binding.nomeAnunciante
        val editText_email = binding.textareaEmail

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val database = FirebaseDatabase.getInstance().reference.child("users").child(userId!!)

        var emailUser: String? = null

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
                            editText_nome.setText(formattedName)

                        atualizarFullName(formattedName)

                        editText_email.setText(user.email)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tratar o erro, se necessário
            }
        })

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch.thumbTintList = ColorStateList.valueOf(corDesativado)
                editText_nome.setText("oculto")
            } else {
                switch.thumbTintList = ColorStateList.valueOf(corAtivado)
                editText_nome.setText(fullName)
            }
        }

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        // Criando uma nova vaga
        val btnNovaVaga = binding.btnNovaVaga

        btnNovaVaga.setOnClickListener {
            Log.d("MeuBotao", "Botão clicado")
            try {
                cadastrarNovaVaga()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Erro ao cadastrar a vaga", Toast.LENGTH_SHORT).show()
            }
        }

        // Inicialize as referências aos elementos da interface aqui:
        tituloVaga = binding.textareaTitulo
        nomeEmpresa = binding.nomeAnunciante
        descricaoVar = binding.textareaDescricao
        remuneracao = binding.textareaRemuneracao
        telefoneVar = binding.textareaTelefone
        email = binding.textareaEmail
        pesquisa = binding.editTextPesquisa
        dataInicioVar = binding.textareaDatainicio
        dataFimVar = binding.textareaDatafim
        switchVisibilidade = binding.switchAnunciante

        return view
    }

    private fun atualizarFullName(value: String) {
        fullName = value
    }

    private fun cadastrarNovaVaga() {
        val titulo = tituloVaga.text.toString()
        val empresa = if (switchVisibilidade.isChecked) " " else nomeEmpresa.text.toString()
        val cidadeEmpresa = spinnerLocalidade.selectedItem.toString()
        val tipoTrabalho = spinnerTipoVaga.selectedItem.toString()
        val dataInicio = dataInicioVar.text.toString()
        val dataFim = dataFimVar.text.toString()
        val pagamento = if (remuneracao.text.isBlank()) "A Combinar" else remuneracao.text.toString()
        val areaConhecimento = spinnerAreaConhecimento.selectedItem.toString()
        val descricao = descricaoVar.text.toString()
        val telefone = telefoneVar.text.toString()
        val emailEmpresa = email.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Verificar se algum campo obrigatório está vazio ou igual a "Selecionar"
        if (titulo.isBlank() ||
            cidadeEmpresa == "Selecionar" ||
            tipoTrabalho == "Selecionar" ||
            dataInicio.isBlank() ||
            areaConhecimento == "Selecionar" ||
            descricao.isBlank() ||
            telefone.isBlank() ||
            emailEmpresa.isBlank()
        ) {
            Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val novaVaga = ClassVaga(
            "",
            userId,
            titulo,
            empresa,
            cidadeEmpresa,
            tipoTrabalho,
            dataInicio,
            pagamento,
            areaConhecimento,
            descricao,
            dataFim,
            telefone,
            emailEmpresa
        )


        val database = FirebaseDatabase.getInstance().reference

        val vagaId = database.child("vagas").push().key
        if (vagaId != null) {
            novaVaga.id = vagaId
            database.child("vagas").child(vagaId).setValue(novaVaga)
                .addOnSuccessListener {
                    // Sucesso ao cadastrar a vaga
                    Toast.makeText(requireContext(), "Vaga cadastrada com sucesso", Toast.LENGTH_SHORT).show()
                    // Limpar os campos após cadastrar a vaga
                    tituloVaga.setText("")
                    nomeEmpresa.setText("")
                    descricaoVar.setText("")
                    remuneracao.setText("")
                    telefoneVar.setText("")
                    email.setText("")
                    dataFimVar.setText("")
                    pesquisa.setText("")
                    spinnerAreaConhecimento.setSelection(0)
                    spinnerLocalidade.setSelection(0)
                    spinnerTipoVaga.setSelection(0)
                    switchVisibilidade.isChecked = false
                }
                .addOnFailureListener { exception ->
                    // Falha ao cadastrar a vaga
                    Toast.makeText(requireContext(), "Erro ao cadastrar a vaga: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                .addOnCanceledListener {
                    // Operação cancelada
                    Toast.makeText(requireContext(), "Cadastro da vaga cancelado", Toast.LENGTH_SHORT).show()
                }
        }
    }

    }



