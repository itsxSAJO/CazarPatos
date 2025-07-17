package com.said.luna.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.said.luna.cazarpatos.EXTRA_LOGIN
import com.said.luna.cazarpatos.R
import java.io.File

class LoginActivity : AppCompatActivity() {
    // lateinit var prefsManager: FileHandler
    // lateinit var encryptedPrefsManager: FileHandler
    // lateinit var internalPrefsManager: FileHandler
    // lateinit var externalPrefsManager: FileHandler

    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser: Button
    lateinit var checkBoxRecordarme: CheckBox
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var auth: FirebaseAuth

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

        // auth Firebase
        auth = Firebase.auth

        // LeerDatosDePreferencias() // 🔒 Comentado para deshabilitar uso de preferencias

        // Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (!validateRequiredData()) return@setOnClickListener

            // GuardarDatosEnPreferencias() // 💾 Comentado para no guardar datos localmente

            AutenticarUsuario(email, password)
        }

        buttonNewUser.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (!validateRequiredData()) return@setOnClickListener

            SignUpNewUser(email, password)
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
    }

    private fun AutenticarUsuario(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "signInWithEmail:success")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(EXTRA_LOGIN, auth.currentUser?.email)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(EXTRA_LOGIN, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        task.exception?.message ?: "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun SignUpNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Usuario creado correctamente.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(EXTRA_LOGIN, auth.currentUser?.email)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(EXTRA_LOGIN, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        task.exception?.message ?: "No se pudo crear el usuario.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
        if (password.length < 6) {
            editTextPassword.error = getString(R.string.error_password_min_length)
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    /*
    private fun LeerDatosDePreferencias() {
        var listadoLeido: Pair<String, String>

        prefsManager = SharedPreferencesManager(this)
        listadoLeido = prefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }

        encryptedPrefsManager = EncriptedSharedPreferencesManager(this)
        listadoLeido = encryptedPrefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }

        internalPrefsManager = FileInternalManager(this)
        listadoLeido = internalPrefsManager.ReadInformation()
        if (listadoLeido.first.isNotEmpty()) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        }

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

        prefsManager = SharedPreferencesManager(this)
        prefsManager.SaveInformation(listadoAGrabar)

        encryptedPrefsManager = EncriptedSharedPreferencesManager(this)
        encryptedPrefsManager.SaveInformation(listadoAGrabar)

        internalPrefsManager = FileInternalManager(this)
        internalPrefsManager.SaveInformation(listadoAGrabar)

        externalPrefsManager = FileExternalManager(this)
        externalPrefsManager.SaveInformation(listadoAGrabar)
    }
    */

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}
