package co.tiagoaguiar.netflixremake.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.netflixremake.model.MovieDetail
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class DowloadimageTask(private val callBack: Callback) {

    interface Callback {
        fun onResult(bitmap: Bitmap)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()
    fun execute(url: String) {
        executor.execute {
            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null

            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000
                val statusCode = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicacao")
                }
                stream = urlConnection.inputStream
                val bitmap = BitmapFactory.decodeStream(stream)
                handler.post {
                    callBack.onResult(bitmap)
                }
            } catch (e: IOException) {

            }finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }
}