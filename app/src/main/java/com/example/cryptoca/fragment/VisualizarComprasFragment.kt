package com.example.cryptoca.fragment

import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.CryptocurrencyPurchasesDbHelper
import com.example.cryptoca.PurchaseColumns.Companion.COLUMN_VALOR_COMPRA
import com.example.cryptoca.PurchaseColumns.Companion.COLUMN_ID_CRIPTO
import com.example.cryptoca.PurchaseColumns.Companion.COLUMN_NOME
import com.example.cryptoca.PurchaseColumns.Companion.COLUMN_VALOR_COMPRA_CRIPTO
import com.example.cryptoca.TABLE_NAME
import com.example.cryptoca.adapter.VisualizarAdapter
import com.example.cryptoca.databinding.FragmentVisualizarComprasBinding


class VisualizarComprasFragment : Fragment() {

    private lateinit var binding: FragmentVisualizarComprasBinding
    private var idCripto: Int? = null
    private lateinit var list: List<ComprasFragment.Compras>
    private lateinit var lista_info_cripto: List<Data>
    private lateinit var adapter: VisualizarAdapter

    private val item: VisualizarComprasFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentVisualizarComprasBinding.inflate(layoutInflater)

        val args = VisualizarComprasFragmentArgs.fromBundle(requireArguments())
        idCripto = args.data.id


        //Obter as compras na base de dados
        list = getPurchases(idCripto!!.toString())

        val data: Data = item.data

        lista_info_cripto = getPrecoAtual(data)


        adapter = VisualizarAdapter(requireContext(), list, lista_info_cripto)
        binding.listaCriptoRecyclerView.adapter = adapter
        binding.SpinKitViewThreeBounce.visibility = View.GONE


        return binding.root
    }

    //Trazer o preço atual da cripto
    private fun getPrecoAtual(data: Data): List<Data> {
        data.quote.EUR.price.toInt()
        return listOf(data)
    }


    private fun getPurchases(id_cripto: String): List<ComprasFragment.Compras> {
        val purchases = mutableListOf<ComprasFragment.Compras>()

        val dbHelper = CryptocurrencyPurchasesDbHelper(requireContext())
        val db = dbHelper.readableDatabase

        //Definir uma projecção que especifique as colunas da tabela em que estamos interessados
        val projection = arrayOf(BaseColumns._ID, COLUMN_ID_CRIPTO, COLUMN_NOME, COLUMN_VALOR_COMPRA, COLUMN_VALOR_COMPRA_CRIPTO)

        //Definir um critério de selecção para filtrar os resultados de modo a incluir apenas o item com o id_cripto especificado
        val selection = "$COLUMN_ID_CRIPTO = ?"
        val selectionArgs = arrayOf(id_cripto)


        val sortOrder = "${COLUMN_NOME} ASC"


        val cursor = db.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

        //através do cursor e adicionar as compras à lista
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val id_cripto = getInt(getColumnIndexOrThrow(COLUMN_ID_CRIPTO))
                val nome = getString(getColumnIndexOrThrow(COLUMN_NOME))
                val valor_compra = getDouble(getColumnIndexOrThrow(COLUMN_VALOR_COMPRA))
                val valor_compra_cripto = getDouble(getColumnIndexOrThrow(COLUMN_VALOR_COMPRA_CRIPTO))
                purchases.add(ComprasFragment.Compras(id, id_cripto , nome, valor_compra, valor_compra_cripto))
            }
        }


        cursor.close()

        //Devolver a lista de compras
        return purchases
    }

}