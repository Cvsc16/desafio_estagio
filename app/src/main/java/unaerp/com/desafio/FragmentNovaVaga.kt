package unaerp.com.desafio

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentNovaVaga : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_novavaga, container, false)
        val back = view.findViewById<ImageView>(R.id.back)

        val spinner_areaConhecimento: Spinner = view.findViewById(R.id.spinner_areaConhecimento)
        val spinner_localidade: Spinner = view.findViewById(R.id.spinner_localidade)
        val spinner_tipoVaga: Spinner = view.findViewById(R.id.spinner_tipoVaga)

        val editText = view.findViewById<EditText>(R.id.nome_anunciante)
        editText.keyListener = null

        val adapter_areaConhecimento = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opcoes_spinner_areaConhecimento,
            R.layout.spinner_item
        )
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_areaConhecimento.adapter = adapter_areaConhecimento

        val adapter_localidade = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opcoes_spinner_localidade,
            R.layout.spinner_item
        )
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_localidade.adapter = adapter_localidade

        val adapter_tipoVaga = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opcoes_spinner_tipoVaga,
            R.layout.spinner_item
        )
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tipoVaga.adapter = adapter_tipoVaga

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
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}



