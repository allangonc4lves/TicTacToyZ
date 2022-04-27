package com.ags.tiktaktoy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class CarregamentoJogo : Activity() {

    var bundle: Bundle? = null

    var imgPone = ""
    var imgPtwo = ""
    var nomeP1 = ""
    var nomeP2 = ""
    var modoDeJogo = ""

    var imgPersonagensA = arrayListOf<Int>()
    var imgPersonagensB = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carregamento_jogo)

        onBackPressed()

        val vs: ImageView = findViewById(R.id.vs)
        val p1: ImageView = findViewById(R.id.P1)
        val p2: ImageView = findViewById(R.id.P2)

        imagensP()

        bundle =  intent.extras

        modoDeJogo = bundle!!.getString("mDeJogo").toString()
        imgPone = bundle!!.getString("imgP1").toString()
        imgPtwo = bundle!!.getString("imgP2").toString()
        nomeP1 = bundle!!.getString("p1selecionado").toString()
        nomeP2 = bundle!!.getString("p2selecionado").toString()

        p1.setImageResource( imgPersonagensA.get( Integer.parseInt( imgPone ) ))
        p2.setImageResource( imgPersonagensB.get( Integer.parseInt( imgPtwo ) ))

        vs.alpha = 0f
        vs.animate().setDuration(7000).alpha(1f).withEndAction{
            val bundle = Bundle()
            bundle.putString("p1selecionado", nomeP1)
            bundle.putString("p2selecionado", nomeP2)
            bundle.putString("imgP1", imgPone)
            bundle.putString("imgP2",  imgPtwo)
            bundle.putString("mDeJogo", modoDeJogo)

            intent = Intent(this@CarregamentoJogo, GamePlay::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()

        }

    }

    override fun onBackPressed() {

    }

    fun imagensP(){
        for (i in 1..42){
            imgPersonagensA.add( resources.getIdentifier( "c" + i, "drawable", packageName ) )
        }
        for (i in 1..42){
            imgPersonagensB.add( resources.getIdentifier( "d" + i, "drawable", packageName ) )
        }
    }
}