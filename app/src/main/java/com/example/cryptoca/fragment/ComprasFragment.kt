package com.example.cryptoca.fragment


import android.app.AlertDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoca.CriptoBD.Data
import com.example.cryptoca.CryptocurrencyPurchasesDbHelper
import com.example.cryptoca.PurchaseColumns
import com.example.cryptoca.R
import com.example.cryptoca.databinding.FragmentComprasBinding


class ComprasFragment : Fragment() {

    private lateinit var binding: FragmentComprasBinding

    private val item: ComprasFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentComprasBinding.inflate(layoutInflater)

        val data : Data = item.data

        val dbHelper = CryptocurrencyPurchasesDbHelper(requireContext())
        val db = dbHelper.writableDatabase

       val botaoAddCripto = binding.bAddCripto

       botaoAddCripto.setOnClickListener {
           val id_cripto = data.id
           val name = binding.abreviacaoCripto.text.toString()
           val amountString = binding.editValorCompra.text.toString()
           val totalString = binding.editValorCripto.text.toString()

           //Verifica se a info colocada é um numero valido
           val amount = try {
               amountString.toDouble()
           } catch (e: NumberFormatException) {
               //Mostra mensagem de erro
               val builder = AlertDialog.Builder(context)
               builder.setTitle("Erro")
               builder.setMessage("O valor do montante da compra deve ser um número.")

               //Botao ok
               builder.setPositiveButton("OK") { dialog, _ ->
                   dialog.dismiss()
               }


               val alertDialog = builder.create()
               alertDialog.show()
               return@setOnClickListener
           }

           //Verifica se a info colocada é um numero valido
           val total = try {
               totalString.toDouble()
           } catch (e: NumberFormatException) {

               val builder = AlertDialog.Builder(context)
               builder.setTitle("Erro")
               builder.setMessage("O valor da criptomoeda deve ser um número.")


               builder.setPositiveButton("OK") { dialog, _ ->
                   dialog.dismiss()
               }


               val alertDialog = builder.create()
               alertDialog.show()
               return@setOnClickListener
           }

           val success = compraCripto(db, id_cripto, name, amount, total)
           if (success) {
               val builder = AlertDialog.Builder(context)
               builder.setTitle("Sucesso!\n")
               builder.setMessage("Foi adicionada a sua compra!!\n\n\nPara sair basta clicar no OK.")

               builder.setPositiveButton("OK") { dialog, _ ->
                   dialog.dismiss()
                   //Limpa os campos assim que clica no botao ok
                   binding.editValorCompra.setText("")
                   binding.editValorCripto.setText("")
               }

               val alertDialog = builder.create()
               alertDialog.show()
           }
       }

        obterComprasAdicionadas(db)

        setupInfo(data)

        return binding.root
    }

    //Mostrar info
    private fun setupInfo(data: Data) {
        binding.abreviacaoCripto.text = data.name


        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.carregar))

            .into(binding.criptoImageView)

        binding.precoTextView.text = "€${String.format("%.02f", data.quote.EUR.price)}"

        if (data.quote.EUR.percent_change_24h > 0) {

            binding.percentagemTextView.text =
                "+${String.format("%.02f", data.quote.EUR.percent_change_24h)}%"
            binding.detailChangeImageView.setImageResource(R.drawable.icon_seta_ganho)

        } else {
            binding.percentagemTextView.text =
                "${String.format("%.02f", data.quote.EUR.percent_change_24h)}%"
            binding.detailChangeImageView.setImageResource(R.drawable.icon_seta_perda)
        }
    }

    data class Compras(val id: Int, val id_cripto: Int , val nome: String, val valor_compra: Double, val valor_compra_cripto: Double)

    //função para inserir a info na base de dados SQLite.
    private fun compraCripto(db: SQLiteDatabase, id_cripto: Int ,nome: String, valor_compra: Double, valor_compra_cripto: Double): Boolean {

        val values = ContentValues().apply {
            put(PurchaseColumns.COLUMN_ID_CRIPTO, id_cripto)
            put(PurchaseColumns.COLUMN_NOME, nome)
            put(PurchaseColumns.COLUMN_VALOR_COMPRA, valor_compra)
            put(PurchaseColumns.COLUMN_VALOR_COMPRA_CRIPTO, valor_compra_cripto)
        }
        val rowId = db.insert(TABLE_NAME, null, values)
        return rowId != (-1).toLong()

    }

    //Função para recuperar dados da base de dados.
    //A função cria uma lista de objectos "Compras" vazia e um objecto de cursor que executa uma consulta na tabela TABLE_NAME
    //É usado um cursor para repetir a iteração através  das linhas do conjunto de resultados, para cada linha lê os valores de cada coluna e
    //armazena-os em variáveis, depois cria um novo objecto "Compras" com os valores das variáveis e adiciona-o à lista de objectos "Compras"
    //Por fim fecha o cursor e devolve a lista de objectos "Compras".
    private fun obterComprasAdicionadas(db: SQLiteDatabase): List<Compras> {
        val compra = mutableListOf<Compras>()
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val id_crito = getInt(getColumnIndexOrThrow(PurchaseColumns.COLUMN_ID_CRIPTO))
                val nome = getString(getColumnIndexOrThrow(PurchaseColumns.COLUMN_NOME))
                val valor_compra = getDouble(getColumnIndexOrThrow(PurchaseColumns.COLUMN_VALOR_COMPRA))
                val valor_compra_cripto = getDouble(getColumnIndexOrThrow(PurchaseColumns.COLUMN_VALOR_COMPRA_CRIPTO))
                compra.add(Compras(id, id_crito ,nome, valor_compra, valor_compra_cripto))

            }
        }
        cursor.close()
        return compra
    }

    //Variável é utilizada nas funções acima referidas para especificar o nome da tabela na base de dados SQLite com a qual as funções interagem.
    companion object {
        const val TABLE_NAME = "Compras"
    }

}


