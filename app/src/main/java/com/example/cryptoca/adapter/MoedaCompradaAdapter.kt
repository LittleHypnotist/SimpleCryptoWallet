package com.example.cryptoca.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.R
import com.example.cryptoca.databinding.ListaMoedasCompraVerificaLayoutBinding
import com.example.cryptoca.fragment.MoedaCompradaFragmentDirections

//Adapter da lista de criptos com botões da compra e de verificar
class MoedaCompradaAdapter(var context: Context, var list: List<Data>, var type: String) : RecyclerView.Adapter<MoedaCompradaAdapter.MercadoViewHolder>() {

    inner class MercadoViewHolder (view : View) : RecyclerView.ViewHolder(view){
        var binding = ListaMoedasCompraVerificaLayoutBinding.bind(view)

        init {
            //Clique do botão de compra
            binding.buCompra.setOnClickListener {
                // Passagem de um ecrã para outro
                val data = list[adapterPosition] //obter info do item clicado
                val action = MoedaCompradaFragmentDirections.actionMoedaCompradaFragmentToComprasFragment(data)
                Navigation.findNavController(it).navigate(action)
            }

            binding.buVerificar.setOnClickListener {
                val data = list[adapterPosition]
                val action = MoedaCompradaFragmentDirections.actionMoedaCompradaFragmentToVisualizarComprasFragment(data)
                Navigation.findNavController(it).navigate(action)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MercadoViewHolder {
        return MercadoViewHolder(LayoutInflater.from(context).inflate(R.layout.lista_moedas_compra_verifica_layout, parent, false))
    }

    //actualiza a lista de dados utilizados no adaptador
    //notificaDataSetChanged() para actualizar a visualização
    fun updateData(dataItem : List<Data>){
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MercadoViewHolder, position: Int) {
        val item = list[position]
        holder.binding.CriptoNomeTextView .text = item.name
        holder.binding.abreviacaoCriptoTextView.text = item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.carregar))

            .into(holder.binding.CriptoImageView)

    }


    override fun getItemCount(): Int {
        return list.size
    }

}