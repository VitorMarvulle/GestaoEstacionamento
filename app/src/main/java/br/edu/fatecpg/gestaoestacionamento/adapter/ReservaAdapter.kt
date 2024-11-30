package br.edu.fatecpg.gestaoestacionamento.adapter

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.gestaoestacionamento.R
import br.edu.fatecpg.gestaoestacionamento.model.Reserva

class ReservaAdapter(
    private val reservas: MutableList<Reserva>,
    private val onDeleteClickListener: (String) -> Unit // Função para o clique de exclusão
) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    // ViewHolder para a consulta
    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSaudacao: TextView = itemView.findViewById(R.id.txtSaudacao)
        val txtTimer: TextView = itemView.findViewById(R.id.txtTimer)
        val txtNome: TextView = itemView.findViewById(R.id.txtNome)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val txtPlaca: TextView = itemView.findViewById(R.id.txtPlaca)
        val txtTempo: TextView = itemView.findViewById(R.id.txtTempo)
        val txtPreco: TextView = itemView.findViewById(R.id.txtPreco)
        val txtEndereco: TextView = itemView.findViewById(R.id.txtEndereco)
        val txtRua: TextView = itemView.findViewById(R.id.txtRua)
        val txtNumero: TextView = itemView.findViewById(R.id.txtNumero)
        val txtCidade: TextView = itemView.findViewById(R.id.txtCidade)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta, parent, false)
        return ReservaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]

        // Configura os campos de texto com os dados da reserva
        holder.txtSaudacao.text = "Usuário: ${reserva.email}"
        holder.txtTimer.text = "Sua reserva:"
        holder.txtNome.text = reserva.nome
        holder.txtData.text = "Data: ${reserva.data}"
        holder.txtPlaca.text = "Placa: ${reserva.placa}"
        holder.txtTempo.text = "Tempo: ${reserva.tempo}"
        holder.txtPreco.text = "Preço: R$ ${reserva.preco}"
        holder.txtEndereco.text = "Endereço:"
        holder.txtRua.text = "Rua: ${reserva.rua}"
        holder.txtNumero.text = "Número: ${reserva.numero}"
        holder.txtCidade.text = "Cidade: ${reserva.cidade}"
        holder.txtEstado.text = "Estado: ${reserva.estado}"

        // Configura o clique do botão de excluir
        holder.btnExcluir.setOnClickListener {
            onDeleteClickListener(reserva.id)
        }


        // Configura o timer
        val inicioTimestamp = reserva.inicioTimestamp
        val duracaoMinutos = reserva.duracaoMinutos
        val tempoRestanteMillis = (inicioTimestamp + (duracaoMinutos * 60 * 1000)) - System.currentTimeMillis()

        if (tempoRestanteMillis > 0) {
            iniciarTimer(tempoRestanteMillis, holder.txtTimer)
        } else {
            holder.txtTimer.text = "Tempo esgotado!"
        }
    }

    // Método para iniciar o timer
    private fun iniciarTimer(tempoRestanteMillis: Long, txtTimer: TextView) {
        val timer = object : CountDownTimer(tempoRestanteMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val horas = millisUntilFinished / (1000 * 60 * 60)
                val minutos = (millisUntilFinished / (1000 * 60)) % 60
                val segundos = (millisUntilFinished / 1000) % 60

                if (horas > 0) {
                    txtTimer.text = String.format("%02d:%02d:%02d", horas, minutos, segundos)
                } else {
                    txtTimer.text = String.format("%02d:%02d", minutos, segundos)
                }
            }

            override fun onFinish() {
                txtTimer.text = "Tempo esgotado!"
            }
        }
        timer.start()
    }

}
