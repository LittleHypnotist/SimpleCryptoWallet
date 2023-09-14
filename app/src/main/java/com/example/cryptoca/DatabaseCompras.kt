package com.example.cryptoca

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

//Definir a base de dados e a tabela
private const val DATABASE_NAME = "Compras_Cripto.db"
private const val DATABASE_VERSION = 1
internal const val TABLE_NAME = "Compras"

//Classe interna para armazenamento de nomes de colunas
class PurchaseColumns : BaseColumns {
    companion object {
        const val COLUMN_ID_CRIPTO = "id_cripto"
        const val COLUMN_NOME = "nome"
        const val COLUMN_VALOR_COMPRA = "valor_compra"
        const val COLUMN_VALOR_COMPRA_CRIPTO = "valor_compra_cripto"
    }
}

//Cria uma subclasse SQLiteOpenHelper
class CryptocurrencyPurchasesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //Cria a tabela de compras
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${PurchaseColumns.COLUMN_ID_CRIPTO} REAL," +
                "${PurchaseColumns.COLUMN_NOME} TEXT," +
                "${PurchaseColumns.COLUMN_VALOR_COMPRA} REAL," +
                "${PurchaseColumns.COLUMN_VALOR_COMPRA_CRIPTO} REAL)"
        db.execSQL(createTable)
    }

    //Gestão de actualizações da versão da base de dados
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }
}