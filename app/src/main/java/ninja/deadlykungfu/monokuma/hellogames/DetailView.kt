package ninja.deadlykungfu.monokuma.hellogames

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_detail_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailView : AppCompatActivity() {
    val apiBaseURL = "https://androidlessonsapi.herokuapp.com/api/"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val gsonInstance = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder()
            .baseUrl(apiBaseURL)
            .addConverterFactory(gsonInstance)
            .build()

        val sv = retrofit.create(WSInterface::class.java)

        val fetcherCallback = object: Callback<ExtendedGame> {
            override fun onFailure(call: Call<ExtendedGame>, t: Throwable?) {
                Log.e("WSError", "WS call failed")
            }

            override fun onResponse(call: Call<ExtendedGame>, response: Response<ExtendedGame>) {
                Log.e("WS", "Got but assertions failed")
                if (response != null) {
                    Log.e("WS", "Response code : "+response.code())
                    if (response.code() == 200) {
                        val bdy = response.body()
                        if (bdy != null) {
                            description.setText(bdy.description_en)
                            Log.e("WS", "HTTP OK")
                        }
                    }
                }
            }
        }

        // TODO : Change with ID from Intent
        sv.getGame(1).enqueue(fetcherCallback)
        // sv.getGames().execute()
        setContentView(R.layout.activity_games_main)
    }
}
