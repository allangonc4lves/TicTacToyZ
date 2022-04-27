package com.ags.tiktaktoy

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*
import android.media.MediaPlayer as MediaPlayer1

class MainActivity : Activity() {

    var musicafundo: MediaPlayer1? = null
    var clique: android.media.MediaPlayer? = null

    var listaPersonagensA = arrayListOf<String>()
    var listaPersonagensB = arrayListOf<String>()
    var imgPersonagensA = arrayListOf<Int>()
    var imgPersonagensB = arrayListOf<Int>()

    var modoDeJogo = ""
    var nomeP1selecionado = ""
    var nomeP2selecionado = ""
    var imgP1selecionado = ""
    var imgP2selecionado = ""

    val nP1:TextView by lazy{
        findViewById<TextView>(R.id.nomeP1)
    }
    val nP2:TextView by lazy{
        findViewById<TextView>(R.id.nomeP2)
    }

    val fButton:TextView by lazy{
        findViewById<Button>(R.id.fButton)
    }

    val nButton:TextView by lazy{
        findViewById<Button>(R.id.nButton)
    }

    val dButton:TextView by lazy{
        findViewById<Button>(R.id.dButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBackPressed()

        findViewById<Button>(R.id.fButton)

        fButton.visibility = View.GONE
        nButton.visibility = View.GONE
        dButton.visibility = View.GONE

        clique = android.media.MediaPlayer.create(applicationContext, R.raw.clique)

        imagensP()

        val PxP: Button = findViewById(R.id.PxP)
        val PxC: Button = findViewById(R.id.PxC)

        val imgP1: ImageView = findViewById(R.id.imgP1)
        val imgP2: ImageView = findViewById(R.id.imgP2)
        val sP1: Spinner = findViewById(R.id.selecionarP1)
        val sP2: Spinner = findViewById(R.id.selecionarP2)

        listaPersonagensA.addAll(listOf("Goku", "Goku SSJ", "Goku SSJ3", "Goku SSJ4",
            "Goku God", "Goku God SSJ", "Goku IS incompleto", "Goku IS completo", "Kid Goku",
            "Vegeta", "Vegeta SSJ", "Vegeta Buu", "Vegeta God", "Vegeta God SSJ","Kid Gohan", "Gohan", "Gohan(Cell)",
            "Curirin", "Freeza", "Gold Freeza", "Picolo", "Cell", "Gotenks", "Gotenks SSJ", "Gotenks SSJ 3",
            "Majin buu", "Super Buu", "Super buu(Gohan)", "Kid Buu", "Dabura", "Goku black",
            "Bills", "Whis", "Hitto", "Jiren", "Vados", "Mestre Kame", "N 17", "N 18", "Trunks",
            "Trunks SSJ", "Brolly"))

        listaPersonagensB.addAll(listOf("Vegeta", "Vegeta SSJ", "Vegeta Buu", "Vegeta God", "Vegeta God SSJ",
            "Goku", "Goku SSJ", "Goku SSJ3", "Goku SSJ4", "Goku God", "Goku God SSJ", "Goku IS incompleto",
            "Goku IS completo", "Kid Goku", "Kid Gohan", "Gohan", "Gohan(Cell)",  "Curirin", "Freeza",
            "Gold Freeza", "Picolo", "Cell", "Gotenks", "Gotenks SSJ", "Gotenks SSJ 3",
            "Majin buu", "Super Buu", "Super buu(Gohan)", "Kid Buu", "Dabura", "Goku black",
            "Bills", "Whis", "Hitto", "Jiren", "Vados", "Mestre Kame", "N 17", "N 18", "Trunks",
            "Trunks SSJ", "Brolly"))

        val array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPersonagensA)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sP1!!.setAdapter(array_adapter)

        sP1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }

            override fun onItemSelected(parent: AdapterView<*>?,  view: View?, position: Int, id: Long)
            {
                if( listaPersonagensA.get(id.toInt()) != nP2.text.toString() ) {
                    nP1.setText(listaPersonagensA.get(id.toInt()))
                    imgP1.setImageResource( imgPersonagensA.get( id.toInt()) )
                    imgP1selecionado = id.toInt().toString()
                    nomeP1selecionado = nP1.text.toString()
                } else {
                    makeText()
                }
            }
        }
        //*****************

        val array_adapt = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPersonagensB )
        array_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sP2!!.setAdapter(array_adapt)

        sP2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }

            override fun onItemSelected(parent: AdapterView<*>?,  view: View?, position: Int, id: Long)
            {
                if( listaPersonagensB.get(id.toInt()) != nP1.text.toString() ) {
                    nP2.setText(listaPersonagensB.get(id.toInt()))
                    imgP2.setImageResource( imgPersonagensB.get( id.toInt()) )
                    imgP2selecionado = id.toInt().toString()
                    nomeP2selecionado = nP2.text.toString()
                } else {
                    makeText()
                }


            }
        }


        musicafundo = MediaPlayer1.create(applicationContext, R.raw.m1)
        musicafundo?.isLooping = true



        PxP.setOnClickListener(View.OnClickListener {
                clique?.start()
                modoDeJogo = "pxp"

                val bundle = Bundle()
                bundle.putString("p1selecionado", nomeP1selecionado)
                bundle.putString("p2selecionado", nomeP2selecionado)
                bundle.putString("imgP1", imgP1selecionado)
                bundle.putString("imgP2", imgP2selecionado)
                bundle.putString("mDeJogo", modoDeJogo)

                intent = Intent(this@MainActivity, CarregamentoJogo::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

                musicafundo?.stop()

        })

        PxC.setOnClickListener(View.OnClickListener {
            clique?.start()
            fButton.visibility = View.VISIBLE
            nButton.visibility = View.VISIBLE
            dButton.visibility = View.VISIBLE

        })

        fButton.setOnClickListener(View.OnClickListener {
            clique?.start()
            modoDeJogo = "facil"

            val bundle = Bundle()
            bundle.putString("p1selecionado", nomeP1selecionado)
            bundle.putString("p2selecionado", nomeP2selecionado)
            bundle.putString("imgP1", imgP1selecionado)
            bundle.putString("imgP2", imgP2selecionado)
            bundle.putString("mDeJogo", modoDeJogo)

            intent = Intent(this@MainActivity, CarregamentoJogo::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            musicafundo?.stop()

        })

        nButton.setOnClickListener(View.OnClickListener {
            clique?.start()
            modoDeJogo = "normal"

            val bundle = Bundle()
            bundle.putString("p1selecionado", nomeP1selecionado)
            bundle.putString("p2selecionado", nomeP2selecionado)
            bundle.putString("imgP1", imgP1selecionado)
            bundle.putString("imgP2", imgP2selecionado)
            bundle.putString("mDeJogo", modoDeJogo)

            intent = Intent(this@MainActivity, CarregamentoJogo::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            musicafundo?.stop()


        })

        dButton.setOnClickListener(View.OnClickListener {
            clique?.start()
            modoDeJogo = "dificil"

            val bundle = Bundle()
            bundle.putString("p1selecionado", nomeP1selecionado)
            bundle.putString("p2selecionado", nomeP2selecionado)
            bundle.putString("imgP1", imgP1selecionado)
            bundle.putString("imgP2", imgP2selecionado)
            bundle.putString("mDeJogo", modoDeJogo)

            intent = Intent(this@MainActivity, CarregamentoJogo::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            musicafundo?.stop()
        })

    }

    @Override
    override fun onResume(){
        super.onResume();
        musicafundo?.start()

    }

    @Override
    override fun onPause() {
        super.onPause()
        musicafundo?.pause()
    }

    fun imagensP(){
        for (i in 1..42){
            imgPersonagensA.add( resources.getIdentifier( "a" + i, "drawable", packageName ) )
        }
        for (i in 1..42){
            imgPersonagensB.add( resources.getIdentifier( "b" + i, "drawable", packageName ) )
        }
    }

    override fun onBackPressed() {
        fButton.visibility = View.GONE
        nButton.visibility = View.GONE
        dButton.visibility = View.GONE
    }

    fun makeText(){
        Toast.makeText(this, "Os mesmos personagens não podem se enfrentar", Toast.LENGTH_SHORT).show()
    }

    fun avisoModoDificilText(){
        Toast.makeText(this, "Este modo de jogo ainda esta em desenvolvimento", Toast.LENGTH_SHORT).show()
    }

}

