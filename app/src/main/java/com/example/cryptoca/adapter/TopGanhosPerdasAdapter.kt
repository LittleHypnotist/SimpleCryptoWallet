package com.example.cryptoca.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptoca.fragment.TopMoedasGanhosPerdasFragment

//Adapter para as moedas de top 10 ganhos e perdas
class TopGanhosPerdasAdapter(fragment_TGP: Fragment) : FragmentStateAdapter(fragment_TGP) {

    override fun getItemCount(): Int {
        return 2
    }

    //criado objetos e atribuir a variavel position com a chave "position"
    //Obtem informação da posição e utiliza para mostrar a informação correta
    override fun createFragment(position: Int): Fragment {
        val fragment_TGP = TopMoedasGanhosPerdasFragment()
        val bundle = Bundle()
        bundle.putInt("position", position)
        fragment_TGP.arguments = bundle
        return fragment_TGP
    }
}