package br.edu.fatecpg.gestaoestacionamento.model
import com.google.firebase.Timestamp

data class Vaga(
    var id: String = "",
    val endereco: Map<String, String> = mapOf(
        "cidade" to "",
        "estado" to "",
        "rua" to "",
        "numero" to "",
    ),
    val status: String = "Disponível",
    val timestamp: Timestamp = Timestamp.now()  // Alterado para Timestamp

) {

    // Métodos auxiliares para acessar os campos do mapa
    val cidade: String
        get() = endereco["cidade"] ?: ""
    val estado: String
        get() = endereco["estado"] ?: ""
    val rua: String
        get() = endereco["rua"] ?: ""
    val numero: String
        get() = endereco["numero"] ?: ""
}