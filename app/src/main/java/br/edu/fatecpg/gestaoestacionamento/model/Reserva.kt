package br.edu.fatecpg.gestaoestacionamento.model

import com.google.firebase.Timestamp

data class Reserva(
    var id: String = "", // Adicionando o campo id
    val nome: String = "",
    val telefone: String = "",
    val data: String = "",
    val endereco: Map<String, String> = emptyMap(),
    val placa: String = ""
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

