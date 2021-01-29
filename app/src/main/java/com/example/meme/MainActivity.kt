package com.example.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentImageUrl:String? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    fun loadMeme() {

// ...
        progressbar.visibility = View.VISIBLE;
// Add a request (in this example, called stringRequest) to your RequestQueue.

        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
              currentImageUrl = response.getString("url")
                Glide
                    .with(
                        this
                    )
                    .load(currentImageUrl).listener(object:RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressbar.visibility = View.GONE;
                            return false;
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressbar.visibility = View.GONE;
                            return false;
                        }

                    })
                    .into(image)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    fun shareMeme(view: View) {
        var intent = Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"check out this cool memes $currentImageUrl")
        intent.type = "text/plain"
        var chooser = Intent.createChooser(intent,"this meme is awesome..")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}