package br.edu.fatecpg.gestaoestacionamento.model
import com.google.firebase.Timestamp

data class Vaga(
    var id: String = "",
    val numero: String = "",
    val localizacao: Map<String, String> = mapOf(
        "cidade" to "",
        "estado" to "",
        "rua" to "",
        "numero" to ""
    ),
    val status: String = "Disponível",
    val timestamp: Timestamp = Timestamp.now()  // Alterado para Timestamp
)