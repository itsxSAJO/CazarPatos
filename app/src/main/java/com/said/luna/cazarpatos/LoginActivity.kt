package com.said.luna.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.said.luna.cazarpatos.EXTRA_LOGIN
import com.said.luna.cazarpatos.R
import java.io.File

class LoginActivity : AppCompatActivity() {
    lateinit var prefsManager: FileHandler
    lateinit var encryptedPrefsManager: FileHandler
    lateinit var internalPrefsManager : FileHandler
    lateinit var externalPrefsManager : FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser: Button
    lateinit var checkBoxRecordarme: CheckBox
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicialización de variables
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        LeerDatosDePreferencias()

        // Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()

            if (!validateRequiredData()) return@setOnClickListener

            GuardarDatosEnPreferencias()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, email)
            startActivity(intent)
            finish()
        }

        buttonNewUser.setOnClickListener {
            // Acción para nuevo usuario
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
    }

    private fun validateRequiredData(): Boolean {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty()) {
            editTextEmail.error = getString(R.string.error_email_required)
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.error = getString(R.string.error_password_required)
            editTextPassword.requestFocus()
            return false
        }
        if (password.length < 3) {
            editTextPassword.error = getString(R.string.error_password_min_length)
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    private fun LeerDatosDePreferencias() {
        var listadoLeido: Pair<String, String>

        // SharedPreferences
        prefsManager = SharedPreferencesManager(this)
        listadoLeido = prefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }

        // EncryptedSharedPreferences
        encryptedPrefsManager = EncriptedSharedPreferencesManager(this)
        listadoLeido = encryptedPrefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }

        //FileInternalPreferences
        internalPrefsManager = FileInternalManager(this)
        listadoLeido = internalPrefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }

        //FileExternalManager
        externalPrefsManager = FileExternalManager(this)
        listadoLeido = externalPrefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }
    }

    private fun GuardarDatosEnPreferencias() {
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        val listadoAGrabar: Pair<String, String> =
            if (checkBoxRecordarme.isChecked) email to clave else "" to ""

        // SharedPreferences
        prefsManager = SharedPreferencesManager(this)
        prefsManager.SaveInformation(listadoAGrabar)

        // EncryptedSharedPreferences
        encryptedPrefsManager = EncriptedSharedPreferencesManager(this)
        encryptedPrefsManager.SaveInformation(listadoAGrabar)

        //FileInternalPreferences
        internalPrefsManager = FileInternalManager(this)
        internalPrefsManager.SaveInformation(listadoAGrabar)

        //FileExternalManager
        externalPrefsManager = FileExternalManager(this)
        externalPrefsManager.SaveInformation(listadoAGrabar)
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}
