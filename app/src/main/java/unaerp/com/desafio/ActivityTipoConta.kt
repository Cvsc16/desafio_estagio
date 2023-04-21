package unaerp.com.desafio

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ActivityTipoConta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipoconta)
        val layoutTipoConta = findViewById<ConstraintLayout>(R.id.layout_tipoConta)

        layoutTipoConta.setOnClickListener {
            // altera a cor de fundo para mostrar que foi selecionado
            layoutTipoConta.setBackgroundResource(R.drawable.fundo_vaga1)
        }

        // define o método onClick para mudar a cor de volta quando clicado novamente
        fun onClick(view: View) {
            val layoutTipoConta = view as ConstraintLayout
            if (layoutTipoConta.background.constantState == resources.getDrawable(R.drawable.fundo_vaga1).constantState) {
                layoutTipoConta.setBackgroundResource(R.drawable.fundo_tipovaga)
            } else {
                layoutTipoConta.setBackgroundResource(R.drawable.fundo_tipovaga)
            }
        }

    }
        }