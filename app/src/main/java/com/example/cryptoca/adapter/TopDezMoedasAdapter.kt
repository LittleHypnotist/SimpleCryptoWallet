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
import com.example.cryptoca.databinding.TopMoedasLayoutBinding
import com.example.cryptoca.fragment.HomeFragmentDirections

//Adapter para as top 10 moedas segundo market cap
class TopDezMoedasAdapter(var context: Context, val list: List<Data>) :
    RecyclerView.Adapter<TopDezMoedasAdapter.TopMoedasHolder>() {

    inner class TopMoedasHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = TopMoedasLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMoedasHolder {

        return TopMoedasHolder(
            LayoutInflater.from(context).inflate(R.layout.top_moedas_layout, parent, false)
        )

    }

    //carregar info
    //item contem o indice do item atual
    override fun onBindViewHolder(holder: TopMoedasHolder, position: Int) {

        val item = list[position]

        holder.binding.topNomeCripto.text = item.name

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.carregar))

            .into(holder.binding.topMoedaIcon)

        holder.binding.topMoedaPreco.setTextColor(context.resources.getColor(R.color.preto_1))
        holder.binding.topMoedaPreco.text =
            "€${String.format("%.02f", item.quote.EUR.price)}"


        holder.itemView.setOnClickListener{
            Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToDetalhesFragment(item))
        }

    }


    override fun getItemCount(): Int {
        return 10
    }

}