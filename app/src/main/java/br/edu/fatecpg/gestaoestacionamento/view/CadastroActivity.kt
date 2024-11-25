package br.edu.fatecpg.gestaoestacionamento.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.MainActivity
import br.edu.fatecpg.gestaoestacionamento.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnCadastro.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()
            val nome = binding.edtNome.text.toString()

            // Verificar qual radio button foi selecionado
            val tipo = when {
                binding.btnRd1.isChecked -> "Administrador"
                binding.btnRd2.isChecked -> "Motorista"
                else -> {
                    Toast.makeText(this, "Selecione um tipo de usuário!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Verificar se os campos estão vazios
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Erro! Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cria o usuário no Firebase Auth
            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            // Após a criação do usuário, salvamos os dados no Firestore
                            val userMap = hashMapOf(
                                "nome" to nome , // Você pode pegar o nome do usuário de outro campo de entrada, se necessário
                                "email" to email,
                                "tipoUsuario" to tipo
                            )

                            // Salva os dados do usuário no Firestore com o UID
                            firestore.collection("users")
                                .document(it.uid)  // Usando o UID como chave única
                                .set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                                    // Redireciona para a tela principal
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erro ao salvar os dados no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Erro! ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
