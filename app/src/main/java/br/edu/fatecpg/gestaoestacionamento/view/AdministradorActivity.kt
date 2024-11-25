package br.edu.fatecpg.gestaoestacionamento.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.adapter.ReservaAdapter
import br.edu.fatecpg.gestaoestacionamento.model.Reserva

class AdministradorActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter
    private lateinit var reservasList: MutableList<Reserva>

    // Definindo uma tag para logar
    private val TAG = "AdministradorActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_administrador)

        Log.d(TAG, "onCreate: Iniciando AdministradorActivity")

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewReservas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reservasList = mutableListOf()
        reservaAdapter = ReservaAdapter(reservasList)
        recyclerView.adapter = reservaAdapter

        loadReservas()
    }

    private fun loadReservas() {
        // Recuperar as reservas da coleção "reservas"
        firestore.collection("reservas")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Ordena por timestamp
            .get()
            .addOnSuccessListener { documents ->


                reservasList.clear()
                for (document in documents) {
                    try {
                        val consulta = document.toObject(Reserva::class.java)
                        reservasList.add(consulta)
                        Log.d(TAG, "Consulta carregada: ${consulta.nome}")
                    } catch (e: Exception) {
                        Log.e(TAG, "loadConsultas: Erro ao converter documento para objeto Consulta", e)
                    }
                }

                reservaAdapter.notifyDataSetChanged() // Atualiza o RecyclerView
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "loadConsultas: Erro ao carregar as consultas", exception)
                Toast.makeText(this, "Erro ao carregar as consultas: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
