package unaerp.com.desafio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import java.text.NumberFormat
import java.util.Locale

class ActivityNovaVaga : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novavaga)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val spinner_areaConhecimento: Spinner = findViewById(R.id.spinner_areaConhecimento)
        val spinner_localidade: Spinner = findViewById(R.id.spinner_localidade)
        val spinner_tipoVaga: Spinner = findViewById(R.id.spinner_tipoVaga)

        val adapter_areaConhecimento = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_areaConhecimento, R.layout.spinner_item)
        adapter_areaConhecimento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_areaConhecimento.adapter = adapter_areaConhecimento

        val adapter_localidade = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_localidade, R.layout.spinner_item)
        adapter_localidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_localidade.adapter = adapter_localidade

        val adapter_tipoVaga = ArrayAdapter.createFromResource(this, R.array.opcoes_spinner_tipoVaga, R.layout.spinner_item)
        adapter_tipoVaga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tipoVaga.adapter = adapter_tipoVaga

        val telefoneEditText = findViewById<EditText>(R.id.textarea_telefone)
        telefoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())


            }
        }