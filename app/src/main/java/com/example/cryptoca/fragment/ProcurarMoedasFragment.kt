package com.example.cryptoca.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.adapter.MainAdapter
import com.example.cryptoca.apis.ApiInterface
import com.example.cryptoca.apis.ApiUtilities
import com.example.cryptoca.databinding.FragmentProcurarMoedasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class ProcurarMoedasFragment : Fragment() {

    private lateinit var binding: FragmentProcurarMoedasBinding

    private lateinit var list: List<Data>
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProcurarMoedasBinding.inflate(layoutInflater)

        list = listOf()

        adapter = MainAdapter(requireContext(), list, "mercado")
        binding.listaCriptoRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getCriptoMercado()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data

                    adapter.updateData(list)
                    binding.SpinKitViewThreeBounce.visibility = GONE
                }
            }
        }

        procurarMoeda()

        return binding.root
    }

    lateinit var procurarNome : String

    private fun procurarMoeda() {
        binding.procurarCriptoText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                procurarNome = s.toString().toLowerCase()

                updateRecyclerView()
            }
        })
    }

    private fun updateRecyclerView() {
        val data = ArrayList<Data>()

        for (item in list){
            val nomeMoeda = item.name.lowercase(Locale.getDefault())
            val simboloMoeda = item.symbol.lowercase(Locale.getDefault())

            if (nomeMoeda.contains(procurarNome) || simboloMoeda.contains(procurarNome)){
                data.add(item)
            }
        }

        adapter.updateData(data)

    }


}