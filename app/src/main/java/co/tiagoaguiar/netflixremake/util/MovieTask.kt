package co.tiagoaguiar.netflixremake.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MovieTask(private val callback: Callback) {
    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute()
        fun onResult(movieDetail: MovieDetail)
        fun onFailure(message: String)
    }

    fun execute(url: String) {
        callback.onPreExecute()
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null

            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000
                val statusCode = urlConnection.responseCode
                if (statusCode==400){
                    stream = urlConnection.errorStream
                    val jsomStream = stream.bufferedReader().use {
                        it.readText()
                    }
                    val json = JSONObject(jsomStream)
                    val message = json.getString("message")
                    throw IOException(message)
                }else if (statusCode > 400) {
                    throw IOException("Erro na comunicacao")
                }
                stream = urlConnection.inputStream
                val jsomStream = stream.bufferedReader().use {
                    it.readText()
                }
                val movieDetail = toMovieDetail(jsomStream)
                handler.post {
                    callback.onResult(movieDetail)
                }

            } catch (e: Exception) {
                val message = e.message ?: "Erro desconhecido"
                handler.post {
                    callback.onFailure(message)
                }
            } finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }

    private fun toMovieDetail(jsonAsString: String): MovieDetail{
        val json = JSONObject(jsonAsString)
        val id= json.getInt("id")
        val title = json.getString("title")
        val desc = json.getString("desc")
        val cast = json.getString("cast")
        val coverUrl = json.getString("cover_url")
        val jsonMovies = json.getJSONArray("movie")
        val similares = mutableListOf<Movie>()
        for(i in 0 until jsonMovies.length()){
            val jsonMovie = jsonMovies.getJSONObject(i)

            val similarId = jsonMovie.getInt("id")
            val similarCoverId =  jsonMovie.getString("cover_url")
            val mov = Movie(similarId,similarCoverId)
            similares.add(mov)
        }
        val movie = Movie(id, coverUrl, title, desc, cast)
        return MovieDetail(movie,similares)
    }

}