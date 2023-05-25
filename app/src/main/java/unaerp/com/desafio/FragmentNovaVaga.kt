package unaerp.com.desafio

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
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
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentNovaVaga : Fragment() {

    private lateinit var descricaoVar: EditText
    private lateinit var remuneracao: EditText
    private lateinit var telefoneVar: EditText
    private lateinit var email: EditText
    private lateinit var dataInicioVar: EditText
    private lateinit var dataFimVar: EditText
    private lateinit var switchVisibilidade: Switch
    private lateinit var spinnerAreaConhecimento: Spinner
    private lateinit var spinnerLocalidade: Spinner
    private lateinit var spinnerTipoVaga: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_novavaga, container, false)
        val back = view.findViewById<ImageView>(R.id.back)

        spinnerAreaConhecimento = view.findViewById(R.id.spinner_areaConhecimento)
        spinnerLocalidade = view.findViewById(R.id.spinner_localidade)
        spinnerTipoVaga = view.findViewById(R.id.spinner_tipoVaga)


        val editText = view.findViewById<EditText>(R.id.nome_anunciante)
        editText.keyListener = null

        val adapter_areaConhecimento = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opcoes_spinner_areaConhecimento,
            R.layout.spinner_item
        )
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAreaConhecimento.adapter = adapter_areaConhecimento

        val adapter_localidade = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opcoes_spinner_localidade,
            R.layout.spinner_item
        )
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocalidade.adapter = adapter_localidade

        val adapter_tipoVaga = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opcoes_spinner_tipoVaga,
            R.layout.spinner_item
        )
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoVaga.adapter = adapter_tipoVaga

        val telefoneEditText = view.findViewById<EditText>(R.id.textarea_telefone)
        telefoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val etData = view.findViewById<EditText>(R.id.textarea_datafim)

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

        val etData2 = view.findViewById<EditText>(R.id.textarea_datainicio)

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
        val switch = view.findViewById<Switch>(R.id.switch_anunciante)

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

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        // Criando uma nova vaga
        val btnNovaVaga = view.findViewById<Button>(R.id.btn_novaVaga)

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
        descricaoVar = view.findViewById(R.id.textarea_descricao)
        remuneracao = view.findViewById(R.id.textarea_remuneracao)
        telefoneVar = view.findViewById(R.id.textarea_telefone)
        email = view.findViewById(R.id.textarea_email)
        dataInicioVar = view.findViewById(R.id.textarea_datainicio)
        dataFimVar = view.findViewById(R.id.textarea_datafim)
        switchVisibilidade = view.findViewById(R.id.switch_anunciante)

        return view
    }

    private fun cadastrarNovaVaga() {
        val titulo = "desenvolvedor front-end"
        val empresa = "engenharia ramos"
        val cidadeEmpresa = spinnerLocalidade.selectedItem.toString()
        val tipoTrabalho = spinnerTipoVaga.selectedItem.toString()
        val dataInicio = dataInicioVar.text.toString()
        val dataFim = dataFimVar.text.toString()
        val pagamento = remuneracao.text.toString()
        val areaConhecimento = spinnerAreaConhecimento.selectedItem.toString()
        val descricao = descricaoVar.text.toString()
        val telefone = telefoneVar.text.toString()
        val emailEmpresa = email.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

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
                    descricaoVar.setText("")
                    remuneracao.setText("")
                    telefoneVar.setText("")
                    email.setText("")
                    dataFimVar.setText("")
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



