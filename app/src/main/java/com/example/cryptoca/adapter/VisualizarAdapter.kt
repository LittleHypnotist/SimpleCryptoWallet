package com.example.cryptoca.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.R
import com.example.cryptoca.databinding.VisualizarComprasLayoutBinding
import com.example.cryptoca.fragment.ComprasFragment

//Adapter para vizualização das compras efetuadas
class VisualizarAdapter(var context: Context, var list: List<ComprasFragment.Compras>, var lista_info_cripto: List<Data>) :
    RecyclerView.Adapter<VisualizarAdapter.CompraViewHolder>() {

    inner class CompraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = VisualizarComprasLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompraViewHolder {
        return CompraViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.visualizar_compras_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CompraViewHolder, position: Int) {

        val lista_compras = list[position]
        val id_cripto = lista_compras.id_cripto

        // Encontra os dados para a moeda com o id_cripto correspondente
        val lista_info_cripto = lista_info_cripto.find { it.id == id_cripto }

        if (lista_info_cripto != null) {
            // Calcula o preço actual utilizando os dados da API
            val preco_corrente = lista_info_cripto.quote.EUR.price
            val preco_compra = lista_compras.valor_compra
            val preco_cripto = lista_compras.valor_compra_cripto

            val preco_atual = ((preco_compra / preco_cripto) * preco_corrente)

            holder.binding.NomeCripto.text = lista_compras.nome
            holder.binding.valorCompra.text = "€${String.format("%.02f", lista_compras.valor_compra)}"
            holder.binding.valorCripto.text = "€${String.format("%.02f", lista_compras.valor_compra_cripto)}"
            holder.binding.precoAtual.text = "€${String.format("%.02f", preco_atual)}"
        }

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + lista_compras.id_cripto + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.carregar))

            .into(holder.binding.ImagemCripto)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}