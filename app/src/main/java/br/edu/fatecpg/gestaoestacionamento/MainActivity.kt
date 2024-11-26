package br.edu.fatecpg.gestaoestacionamento

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.gestaoestacionamento.databinding.ActivityMainBinding
import br.edu.fatecpg.gestaoestacionamento.view.CadastroActivity
import br.edu.fatecpg.gestaoestacionamento.view.AdministradorActivity
import br.edu.fatecpg.gestaoestacionamento.view.MotoristaActivity
import br.edu.fatecpg.gestaoestacionamento.view.MotoristaCardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Botão de Login
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()

            // Verifica se o email e a senha estão preenchidos
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Erro! Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Realiza o login com o Firebase Auth
            auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            // Busca os dados do usuário no Firestore
                            firestore.collection("users")
                                .document(it.uid)  // Usando o UID do Firebase Auth
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        // Verificar o tipo do usuário no Firestore
                                        val tipoUsuario = document.getString("tipoUsuario")
                                        val nome = document.getString("nome")

                                        if (tipoUsuario == "Administrador") {
                                            // Redireciona para a tela do Médico
                                            val intent = Intent(this, AdministradorActivity::class.java)
                                            intent.putExtra("nome", nome)
                                            startActivity(intent)
                                            finish()
                                        } else if (tipoUsuario == "Motorista") {
                                            // Redireciona para a tela do Paciente
                                            val intent = Intent(this, MotoristaCardActivity::class.java)

                                            // Verificar se já existe uma reserva para este motorista
                                            firestore.collection("reservas")
                                                .document(it.uid)  // Usando o UID do Firebase Auth
                                                .get()
                                                .addOnSuccessListener { document ->
                                                    if (document.exists()) {
                                                        // Redireciona para a tela de visualização do card de reserva
                                                        startActivity(intent)
                                                        finish()
                                                    } else {
                                                        // Caso não tenha reserva, redireciona para a tela de criação de reserva
                                                        val intent = Intent(this, MotoristaActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                }

                                        } else {
                                            Toast.makeText(this, "Tipo de usuário desconhecido!", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(this, "Dados do usuário não encontrados!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao buscar dados do usuário", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // Caso o login falhe
                        Toast.makeText(this, "Erro! ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Botão de Cadastro
        binding.txtCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        // Botão de Redefinição de Senha
        binding.btnRedefinirSenha.setOnClickListener {
            val email = binding.edtEmail.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Envia um e-mail de redefinição de senha
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email de redefinição de senha enviado!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
