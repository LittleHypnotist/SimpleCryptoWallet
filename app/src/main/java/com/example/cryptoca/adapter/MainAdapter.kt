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
import com.example.cryptoca.databinding.MoedaInfoLayoutBinding
import com.example.cryptoca.fragment.*

//Adapter usado para poder passar de um ecrã para outro através da navegação e também é usado para mostrar alguma informação
class MainAdapter(var context: Context, var list: List<Data>, var type: String) : RecyclerView.Adapter<MainAdapter.MercadoViewHolder>() {

    inner class MercadoViewHolder (view : View) : RecyclerView.ViewHolder(view){
        var binding = MoedaInfoLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MercadoViewHolder {
        return MercadoViewHolder(LayoutInflater.from(context).inflate(R.layout.moeda_info_layout, parent, false))
    }

    fun updateData(dataItem : List<Data>){
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MercadoViewHolder, position: Int) {
        val item = list[position]
        holder.binding.CriptoNomeTextView.text = item.name
        holder.binding.abreviacaoCriptoTextView.text = item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.carregar))

            .into(holder.binding.CriptoImageView)

        holder.binding.criptoPrecoTextView.text = "€${String.format("%.02f", item.quote.EUR.price)}"

        if (item.quote.EUR.percent_change_24h > 0) {
            holder.binding.criptoPercentagemTextView.text = "${String.format("%.02f", item.quote.EUR.percent_change_24h)}%"
        }
        else{
            holder.binding.criptoPercentagemTextView.text = "${String.format("%.02f", item.quote.EUR.percent_change_24h)}%"
        }


        //para efetuar a navegação para um fragmento diferente quando o item é clicado
        //se o tipo for "home", navegará para o "DetalhesFragment" usando a acção de navegação "HomeFragmentDirections.actionHomeFragmentToDetalhesFragment"
        //sendo que os restantes tem um pensamento identico
        if (type == "home"){
            holder.itemView.setOnClickListener{
                Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToDetalhesFragment(item))
            }

        }else if (type == "mercado"){
            holder.itemView.setOnClickListener{
                Navigation.findNavController(it).navigate(ProcurarMoedasFragmentDirections.actionProcurarMoedasFragmentToDetalhesFragment(item))
            }
        }else if (type == "favoritos"){
            holder.itemView.setOnClickListener{
                Navigation.findNavController(it).navigate(MoedasFavoritasFragmentDirections.actionMoedasFavoritasFragmentToDetalhesFragment(item))
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}