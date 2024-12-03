package br.edu.fatecpg.gestaoestacionamento.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.model.Vaga
import br.edu.fatecpg.gestaoestacionamento.view.MotoristaCardActivity
import com.google.firebase.auth.FirebaseAuth

class VagaAdapter(
    private val vagas: List<Vaga>,
    private val onClick: (Vaga) -> Unit
) : RecyclerView.Adapter<VagaAdapter.VagaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vaga, parent, false)
        return VagaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        val vaga = vagas[position]
        holder.bind(vaga, onClick)
    }

    override fun getItemCount(): Int = vagas.size

    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtLocalizacao: TextView = itemView.findViewById(R.id.txtLocalizacao)
        private val txtCidade: TextView = itemView.findViewById(R.id.txtCidade)
        private val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        private val txtNumero: TextView = itemView.findViewById(R.id.txtNumero)
        private val txtRua: TextView = itemView.findViewById(R.id.txtRua)
        private val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        private val edtPlacaVeiculo: EditText = itemView.findViewById(R.id.edtPlacaVeiculo)  // EditText para a placa

        private val btnReservar: Button = itemView.findViewById(R.id.btnReservar)

        fun bind(vaga: Vaga, onClick: (Vaga) -> Unit) {
            txtLocalizacao.text = "Localização"
            txtCidade.text = "Cidade: ${vaga.cidade}"
            txtEstado.text = "Estado: ${vaga.estado}"
            txtRua.text = "Rua: ${vaga.rua}"
            txtNumero.text = "Numero: ${vaga.numero}"
            txtStatus.text = "Status: ${vaga.status}"

            // Ação de clique para o botão de reservar
            btnReservar.setOnClickListener {
                val context = itemView.context

                // Recuperar o nome do usuário logado
                val user = FirebaseAuth.getInstance().currentUser
                val nomeUsuario = user?.displayName ?: "Usuário não disponível"  // Caso não tenha nome disponível, usa o valor padrão.

                // Recuperar a placa do veículo digitada pelo usuário
                val placaVeiculo = edtPlacaVeiculo.text.toString()

                // Passar os dados para a MotoristaCardActivity via Intent
                val intent = Intent(context, MotoristaCardActivity::class.java)
                intent.putExtra("cidade", vaga.cidade)
                intent.putExtra("estado", vaga.estado)
                intent.putExtra("rua", vaga.rua)
                intent.putExtra("numero", vaga.numero.toString()) // Garantir que é uma String
                intent.putExtra("status", vaga.status)
                intent.putExtra("nomeUsuario", nomeUsuario)  // Enviando o nome do usuário
                intent.putExtra("placaVeiculo", placaVeiculo)  // Enviando a placa do veículo

                // Iniciar a MotoristaCardActivity
                context.startActivity(intent)
            }

            itemView.setOnClickListener { onClick(vaga) }
        }
    }
}
