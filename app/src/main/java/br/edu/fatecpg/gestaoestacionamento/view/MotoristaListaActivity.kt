package br.edu.fatecpg.gestaoestacionamento.view
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.adapter.VagaAdapter
import br.edu.fatecpg.gestaoestacionamento.model.Vaga
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MotoristaListaActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var txtSaudacao: TextView
    private lateinit var btnSair: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var vagaAdapter: VagaAdapter
    private lateinit var vagasList: MutableList<Vaga>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vagas_motorista)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Referências aos componentes
        txtSaudacao = findViewById(R.id.txtSaudacao)
        btnSair = findViewById(R.id.btnSair)
        recyclerView = findViewById(R.id.recyclerViewVagas)

        // Configuração inicial
        recyclerView.layoutManager = LinearLayoutManager(this)
        vagasList = mutableListOf()
        vagaAdapter = VagaAdapter(vagasList) { vagaId ->
            Toast.makeText(this, "Selecionada a vaga: $vagaId", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = vagaAdapter

        // Configura saudação
        val usuarioNome = auth.currentUser?.displayName ?: "Motorista"
        txtSaudacao.text = "Olá, $usuarioNome"

        // Botão para sair
        btnSair.setOnClickListener {
            startActivity(Intent(this, MotoristaBotoesActivity::class.java))
            finish()
        }
        // Carregar vagas
        loadVagas()
    }
    // Função para carregar vagas criadas pelo administrador
    private fun loadVagas() {
        firestore.collection("vagas")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                vagasList.clear()
                for (document in documents) {
                    try {
                        val vaga = document.toObject(Vaga::class.java)
                        vaga.id = document.id // Adiciona o ID do documento ao objeto Vaga
                        vagasList.add(vaga)
                    } catch (e: Exception) {
                        Log.e("MotoristaListaActivity", "Erro ao converter documento", e)
                    }
                }
                vagaAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar vagas: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}