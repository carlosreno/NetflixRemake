package co.tiagoaguiar.netflixremake.util

import android.util.Log
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask {
    fun execute(url: String){
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {


            val requestURL = URL(url)
            val urlConnection = requestURL.openConnection() as HttpsURLConnection
            urlConnection.readTimeout = 2000
            urlConnection.connectTimeout = 2000
            val statusCode = urlConnection.responseCode
            if (statusCode > 400) {
                throw IOException("Erro na comunicacao")
                Log.i("deu ruim","deu ruim")
            }
            val stream = urlConnection.inputStream
            val jsomStream = stream.bufferedReader().use {it.readText()
            }
            Log.i("teste",jsomStream)

            }catch (e: Exception){
                Log.e("teste","${e.message}")
            }
        }
    }
}