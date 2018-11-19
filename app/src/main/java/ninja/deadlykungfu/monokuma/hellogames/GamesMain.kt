package ninja.deadlykungfu.monokuma.hellogames

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_games_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Game(val id: Int, val name: String)
data class ExtendedGame(
    val id: Int,
    val name: String,
    val type: String,
    val players: Int,
    val year: Int,
    val url: String,
    val description_fr: String,
    val description_en: String
)

interface WSInterface {
    @GET("game/list")
    fun getGames(): Call<List<Game>>

    @GET("/game/details")
    fun getGame(@Query("game_id") gameId: Int): Call<ExtendedGame>
}

class GamesMain : AppCompatActivity() {
    val intt: Intent by lazy { Intent(this, DetailView::class.java) }

    val appData = arrayListOf<Game>()
    val apiBaseURL = "https://androidlessonsapi.herokuapp.com/api/"

    override fun onCreate(savedInstanceState: Bundle?) {

        val gsonInstance = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder()
            .baseUrl(apiBaseURL)
            .addConverterFactory(gsonInstance)
            .build()

        val sv = retrofit.create(WSInterface::class.java)

        val fetcherCallback = object: Callback<List<Game>> {
            override fun onFailure(call: Call<List<Game>>, t: Throwable?) {
                Log.e("WSError", "WS call failed")
            }

            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                Log.e("WS", "Got but assertions failed")
                if (response != null) {
                    Log.e("WS", "Response code : "+response.code())
                    if (response.code() == 200) {
                        val bdy = response.body()
                        if (bdy != null) {
                            appData.addAll(bdy)
                            renderGames()
                            Log.e("WS", "HTTP OK")
                        }
                    }
                }
            }
        }

        super.onCreate(savedInstanceState)
        sv.getGames().enqueue(fetcherCallback)
        // sv.getGames().execute()
        setContentView(R.layout.activity_games_main)

        button1.setOnClickListener {
            startActivity(intt)
        }

        button2.setOnClickListener {
            startActivity(intt)
        }

        button3.setOnClickListener {
            startActivity(intt)
        }

        button4.setOnClickListener {
            startActivity(intt)
        }
    }



    fun renderGames() {
        // FIXME : This is an abomination, but 10mn before the end
        val gotten = (0..9).random()
        button1.setText(appData.get(gotten).name)

        val gotten2 = (0..9).random()
        button2.setText(appData.get(gotten2).name)

        val gotten3 = (0..9).random()
        button3.setText(appData.get(gotten3).name)

        val gotten4 = (0..9).random()
        button4.setText(appData.get(gotten4).name)
    }
}
