package com.ags.tiktaktoy

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*

class GamePlay : Activity() {

    val lPrincipal: ConstraintLayout by lazy{
        findViewById<ConstraintLayout>(R.id.layoutPrincipal)
    }

    var modoDeJogo = ""
    var cpuComecaJogada: Int = 0
    var qPartidas: Int = 0
    var qEmpates: Int = 0

    var musicafundo: MediaPlayer? = null
    var clique: MediaPlayer? = null
    var explosao3: MediaPlayer? = null
    var fimPartida: MediaPlayer? = null

    var bundle: Bundle? = null

    var imgPersonagensA = arrayListOf<Int>()
    var imgPersonagensB = arrayListOf<Int>()
    var mBatalhas = arrayListOf<Int>()

    var casaJogar = arrayListOf<Int>()
    //var casaJogar = Random.nextInt(8)
    //var casaJogar = (0 until 9).random()
    var statusPartida = "emAndamento"
    var pecaSelecionada = ""
    var somarXeO = 1
    val placarX: TextView by lazy{
        findViewById<TextView>(R.id.PlacarX)
    }
    val placarO: TextView by lazy{
        findViewById<TextView>(R.id.PlacarO)
    }

    var vez: Boolean = true
    val a: IntArray = intArrayOf(0,0,0,0,0,0,0,0,0)
    val vencedor: TextView by lazy{
        findViewById<TextView>(R.id.vencedor)
    }

    val vCasas: TextView by lazy{
        findViewById<TextView>(R.id.vCasas)
    }

    val nP1: TextView by lazy{
        findViewById<TextView>(R.id.nomeP1)
    }
    val nP2: TextView by lazy{
        findViewById<TextView>(R.id.nomeP2)
    }

    val tipoP1: TextView by lazy{
        findViewById<TextView>(R.id.tipoP1)
    }
    val tipoP2: TextView by lazy{
        findViewById<TextView>(R.id.tipoP2)
    }

    val quantidadePartidas: TextView by lazy{
        findViewById<TextView>(R.id.quantidadePartidas)
    }

    val quantidadeEmpates: TextView by lazy{
        findViewById<TextView>(R.id.quantidadeEmpates)
    }

    val imgP1: ImageView by lazy{
        findViewById<ImageView>(R.id.imgP1)
    }
    val imgP2: ImageView by lazy{
        findViewById<ImageView>(R.id.imgP2)
    }

    val sairButton: Button by lazy{
        findViewById<Button>(R.id.sairButton)
    }
    val continuarButton: Button by lazy{
        findViewById<Button>(R.id.continuarButton)
    }

    val p: Array<ImageView?> = arrayOfNulls(9)

    val cpuComeca: TextView by lazy{
        findViewById<TextView>(R.id.cpuComeca)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        //Variaveis de testes
        vCasas.visibility = View.GONE
        cpuComeca.visibility = View.GONE

        imagensP()
        musicBatalha()

        sairButton.visibility = View.GONE
        continuarButton.visibility = View.GONE

        sairButton.setOnClickListener(View.OnClickListener {
            val sair = Intent(this, MainActivity::class.java)
            musicafundo?.stop()
            startActivity(sair)
        })

        continuarButton.setOnClickListener(View.OnClickListener {
            novoJogo()
        })

        bundle =  intent.extras

        modoDeJogo = bundle!!.getString("mDeJogo").toString()
        nP1.setText( bundle!!.getString("p1selecionado") )
        nP2.setText( bundle!!.getString("p2selecionado") )

        imgP1.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
        imgP2.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") ) ) )


        musicafundo = MediaPlayer.create(applicationContext, resources.getIdentifier( "batalha" + (1 until 4).random(), "raw", packageName ))
        musicafundo?.isLooping = true
        clique = MediaPlayer.create(applicationContext, R.raw.clique)
        explosao3 = MediaPlayer.create(applicationContext, R.raw.explosao3)
        fimPartida = MediaPlayer.create(applicationContext, R.raw.fimpartida)

        casaJogar.addAll(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8))
        Collections.shuffle(casaJogar)


/*
        val desenhoJogo: ConstraintLayout = findViewById(R.id.desenhojogo)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        desenhoJogo.layoutParams.height = width

 */

        p[0] = findViewById<ImageView>(R.id.p0f)
        p[1] = findViewById<ImageView>(R.id.p1f)
        p[2] = findViewById<ImageView>(R.id.p2f)

        p[3] = findViewById<ImageView>(R.id.p3f)
        p[4] = findViewById<ImageView>(R.id.p4f)
        p[5] = findViewById<ImageView>(R.id.p5f)

        p[6] = findViewById<ImageView>(R.id.p6f)
        p[7] = findViewById<ImageView>(R.id.p7f)
        p[8] = findViewById<ImageView>(R.id.p8f)

        jogarPlayer()

        if(modoDeJogo.equals("pxp")){
            vencedor.setText( nP1.text.toString() + " Começa")
            pecaSelecionada = "x"
            tipoP1.setText("P1")
            tipoP2.setText("P2")
            alertaEscolherPecaPxP()
        } else if (modoDeJogo.equals("facil")) {
            tipoP1.setText("P1")
            tipoP2.setText("CPU")
            alertaEscolherPeca()
        } else if (modoDeJogo.equals("normal")) {
            tipoP1.setText("P1")
            tipoP2.setText("CPU")
            alertaEscolherPeca()
        } else if (modoDeJogo.equals("dificil")) {
            tipoP1.setText("P1")
            tipoP2.setText("CPU")
            alertaEscolherPeca()
        }


    }

    fun imagensP(){
        for (i in 1..42){
            imgPersonagensA.add( resources.getIdentifier( "a" + i, "drawable", packageName ) )
        }
        for (i in 1..42){
            imgPersonagensB.add( resources.getIdentifier( "b" + i, "drawable", packageName ) )
        }
    }

    fun musicBatalha(){
        for (i in 0..4){
            mBatalhas.add( resources.getIdentifier( "batalha" + i, "raw", packageName ) )
        }
    }

    fun jogarPlayer(){

        cpuComeca.setText(cpuComecaJogada.toString())

        p[0]?.setOnClickListener(View.OnClickListener {
            clique?.start()

            if (vez) {
                p[0]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[0] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false


                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }


                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[0]?.isClickable = false

                } else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {

                p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[0] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[0]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            }
        })
        p[1]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[1]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[1] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[1]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {
                p[1]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[1] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[1]?.isClickable = false
                } else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            }
        })

        p[2]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[2]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[2] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[2]?.isClickable = false
                } else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }

            } else if (!vez) {
                p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[2] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[2]?.isClickable = false
                }
            }else if (modoDeJogo.equals("facil")){

                cpuComecaJogada  = cpuComecaJogada + 1
                cpuComeca.setText(cpuComecaJogada.toString())

                p[0]?.isClickable = false
                p[1]?.isClickable = false
                p[2]?.isClickable = false
                p[3]?.isClickable = false
                p[4]?.isClickable = false
                p[5]?.isClickable = false
                p[6]?.isClickable = false
                p[7]?.isClickable = false
                p[8]?.isClickable = false

                val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                    nSuperFacilRobo()

                    for(i in 0..8){
                        if( a[i] != 1 && a[i] != 30 ){
                            p[i]?.isClickable = true
                        }
                    }

                }, 1000)
            }

        })

        p[3]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[3]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[3] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[3]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {
                p[3]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[3] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[3]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }

            }
        })

        p[4]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[4]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[4] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[4]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {
                p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[4] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[4]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            }
        })

        p[5]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[5]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[5] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[5]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {
                p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[5] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[5]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }

            }
        })

        p[6]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[6]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[6] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[6]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {
                p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[6] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[6]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }

            }
        })

        p[7]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[7]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[7] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[7]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }


            } else if (!vez) {
                p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[7] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[7]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }

            }
        })

        p[8]?.setOnClickListener(View.OnClickListener {
            clique?.start()
            if (vez) {
                p[8]?.setImageResource( imgPersonagensA.get( Integer.parseInt( bundle!!.getString("imgP1") )))
                a[8] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false
                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false
                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[8]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }



            } else if (!vez) {
                p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                a[8] = somarXeO
                vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                        + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                        + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
                verificaVencedor()

                if(modoDeJogo.equals("normal")) {
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("dificil")){
                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nDificilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                } else if (modoDeJogo.equals("pxp")){
                    p[8]?.isClickable = false
                }else if (modoDeJogo.equals("facil")){

                    cpuComecaJogada  = cpuComecaJogada + 1
                    cpuComeca.setText(cpuComecaJogada.toString())

                    p[0]?.isClickable = false
                    p[1]?.isClickable = false
                    p[2]?.isClickable = false
                    p[3]?.isClickable = false
                    p[4]?.isClickable = false
                    p[5]?.isClickable = false
                    p[6]?.isClickable = false
                    p[7]?.isClickable = false
                    p[8]?.isClickable = false

                    val mainHandler = Handler(Looper.getMainLooper()).postDelayed ({
                        nSuperFacilRobo()

                        for(i in 0..8){
                            if( a[i] != 1 && a[i] != 30 ){
                                p[i]?.isClickable = true
                            }
                        }

                    }, 1000)
                }

            }
        })
    }

    fun nSuperFacilRobo(){
        var clique: MediaPlayer? = MediaPlayer.create(applicationContext, R.raw.clique)
        Collections.shuffle(casaJogar)
        for(i in 0..8) {

            if(a[casaJogar.get(i)] == 1 || a[casaJogar.get(i)] == 30){
                casaJogar + 1
            } else if(a[casaJogar.get(i)] != 1 && a[casaJogar.get(i)] != 30){
                if (!vez) {
                    if (statusPartida.equals("Encerrada" ) || statusPartida.equals("Empatada")){
                        break
                    } else{

                        if (a[0] == 30 && a[1] == 30 && a[2] == 0 ||
                            a[1] == 30 && a[2] == 30 && a[0] == 0 ||
                            a[3] == 30 && a[4] == 30 && a[5] == 0 ||
                            a[4] == 30 && a[5] == 30 && a[3] == 0 ||
                            a[6] == 30 && a[7] == 30 && a[8] == 0 ||
                            a[7] == 30 && a[8] == 30 && a[6] == 0 ||

                            a[0] == 30 && a[3] == 30 && a[6] == 0 ||
                            a[3] == 30 && a[6] == 30 && a[0] == 0 ||
                            a[1] == 30 && a[4] == 30 && a[7] == 0 ||
                            a[4] == 30 && a[7] == 30 && a[1] == 0 ||
                            a[2] == 30 && a[5] == 30 && a[8] == 0 ||
                            a[5] == 30 && a[8] == 30 && a[2] == 0 ||

                            a[0] == 30 && a[4] == 30 && a[8] == 0 ||
                            a[4] == 30 && a[8] == 30 && a[0] == 0 ||
                            a[2] == 30 && a[4] == 30 && a[6] == 0 ||
                            a[4] == 30 && a[6] == 30 && a[2] == 0 ||

                            a[0] == 30 && a[6] == 30 && a[3] == 0 ||
                            a[1] == 30 && a[7] == 30 && a[4] == 0 ||
                            a[2] == 30 && a[8] == 30 && a[5] == 0 ||
                            a[0] == 30 && a[2] == 30 && a[1] == 0 ||
                            a[3] == 30 && a[5] == 30 && a[4] == 0 ||
                            a[6] == 30 && a[8] == 30 && a[7] == 0 ||
                            a[0] == 30 && a[8] == 30 && a[4] == 0 ||
                            a[2] == 30 && a[6] == 30 && a[4] == 0) {

                            verificaPvitoriaO()
                            break

                        }  else {
                            clique?.start()
                            p[casaJogar.get(i)]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            a[casaJogar.get(i)] = somarXeO
                            p[casaJogar.get(i)]?.isClickable = false
                            verificaVencedor()
                            break
                        }



                    }

                }
            }

        }
        vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
    }

    fun nFacilRobo(){
        var clique: MediaPlayer? = MediaPlayer.create(applicationContext, R.raw.clique)
        Collections.shuffle(casaJogar)
        for(i in 0..8) {

            if(a[casaJogar.get(i)] == 1 || a[casaJogar.get(i)] == 30){
                casaJogar + 1
            } else if(a[casaJogar.get(i)] != 1 && a[casaJogar.get(i)] != 30){
                if (!vez) {
                    if (statusPartida.equals("Encerrada" ) || statusPartida.equals("Empatada")){
                        break
                    } else{

                        if (a[0] == 30 && a[1] == 30 && a[2] == 0 ||
                            a[1] == 30 && a[2] == 30 && a[0] == 0 ||
                            a[3] == 30 && a[4] == 30 && a[5] == 0 ||
                            a[4] == 30 && a[5] == 30 && a[3] == 0 ||
                            a[6] == 30 && a[7] == 30 && a[8] == 0 ||
                            a[7] == 30 && a[8] == 30 && a[6] == 0 ||

                            a[0] == 30 && a[3] == 30 && a[6] == 0 ||
                            a[3] == 30 && a[6] == 30 && a[0] == 0 ||
                            a[1] == 30 && a[4] == 30 && a[7] == 0 ||
                            a[4] == 30 && a[7] == 30 && a[1] == 0 ||
                            a[2] == 30 && a[5] == 30 && a[8] == 0 ||
                            a[5] == 30 && a[8] == 30 && a[2] == 0 ||

                            a[0] == 30 && a[4] == 30 && a[8] == 0 ||
                            a[4] == 30 && a[8] == 30 && a[0] == 0 ||
                            a[2] == 30 && a[4] == 30 && a[6] == 0 ||
                            a[4] == 30 && a[6] == 30 && a[2] == 0 ||

                            a[0] == 30 && a[6] == 30 && a[3] == 0 ||
                            a[1] == 30 && a[7] == 30 && a[4] == 0 ||
                            a[2] == 30 && a[8] == 30 && a[5] == 0 ||
                            a[0] == 30 && a[2] == 30 && a[1] == 0 ||
                            a[3] == 30 && a[5] == 30 && a[4] == 0 ||
                            a[6] == 30 && a[8] == 30 && a[7] == 0 ||
                            a[0] == 30 && a[8] == 30 && a[4] == 0 ||
                            a[2] == 30 && a[6] == 30 && a[4] == 0) {

                            verificaPvitoriaO()
                            break

                        } else if (a[0] == 1 && a[1] == 1 && a[2] == 0 ||
                            a[1] == 1 && a[2] == 1 && a[0] == 0 ||
                            a[3] == 1 && a[4] == 1 && a[5] == 0 ||
                            a[4] == 1 && a[5] == 1 && a[3] == 0 ||
                            a[6] == 1 && a[7] == 1 && a[8] == 0 ||
                            a[7] == 1 && a[8] == 1 && a[6] == 0 ||

                            a[0] == 1 && a[3] == 1 && a[6] == 0 ||
                            a[3] == 1 && a[6] == 1 && a[0] == 0 ||
                            a[1] == 1 && a[4] == 1 && a[7] == 0 ||
                            a[4] == 1 && a[7] == 1 && a[1] == 0 ||
                            a[2] == 1 && a[5] == 1 && a[8] == 0 ||
                            a[5] == 1 && a[8] == 1 && a[2] == 0 ||

                            a[0] == 1 && a[4] == 1 && a[8] == 0 ||
                            a[4] == 1 && a[8] == 1 && a[0] == 0 ||
                            a[2] == 1 && a[4] == 1 && a[6] == 0 ||
                            a[4] == 1 && a[6] == 1 && a[2] == 0 ||

                            a[0] == 1 && a[6] == 1 && a[3] == 0 ||
                            a[1] == 1 && a[7] == 1 && a[4] == 0 ||
                            a[2] == 1 && a[8] == 1 && a[5] == 0 ||
                            a[0] == 1 && a[2] == 1 && a[1] == 0 ||
                            a[3] == 1 && a[5] == 1 && a[4] == 0 ||
                            a[6] == 1 && a[8] == 1 && a[7] == 0 ||
                            a[0] == 1 && a[8] == 1 && a[4] == 0 ||
                            a[2] == 1 && a[6] == 1 && a[4] == 0) {

                            verificaPvitoriaX()
                            break
                        } else {
                            clique?.start()
                            p[casaJogar.get(i)]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            a[casaJogar.get(i)] = somarXeO
                            p[casaJogar.get(i)]?.isClickable = false
                            verificaVencedor()
                            break
                        }



                    }

                }
            }

        }
        vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
    }

    fun nDificilRobo(){
        var clique: MediaPlayer? = MediaPlayer.create(applicationContext, R.raw.clique)

        if (!vez) {

            if (statusPartida.equals("Encerrada") || statusPartida.equals("Empatada")){

            } else{

                if (a[0] == 30 && a[1] == 30 && a[2] == 0 ||
                    a[1] == 30 && a[2] == 30 && a[0] == 0 ||
                    a[3] == 30 && a[4] == 30 && a[5] == 0 ||
                    a[4] == 30 && a[5] == 30 && a[3] == 0 ||
                    a[6] == 30 && a[7] == 30 && a[8] == 0 ||
                    a[7] == 30 && a[8] == 30 && a[6] == 0 ||

                    a[0] == 30 && a[3] == 30 && a[6] == 0 ||
                    a[3] == 30 && a[6] == 30 && a[0] == 0 ||
                    a[1] == 30 && a[4] == 30 && a[7] == 0 ||
                    a[4] == 30 && a[7] == 30 && a[1] == 0 ||
                    a[2] == 30 && a[5] == 30 && a[8] == 0 ||
                    a[5] == 30 && a[8] == 30 && a[2] == 0 ||

                    a[0] == 30 && a[4] == 30 && a[8] == 0 ||
                    a[4] == 30 && a[8] == 30 && a[0] == 0 ||
                    a[2] == 30 && a[4] == 30 && a[6] == 0 ||
                    a[4] == 30 && a[6] == 30 && a[2] == 0 ||

                    a[0] == 30 && a[6] == 30 && a[3] == 0 ||
                    a[1] == 30 && a[7] == 30 && a[4] == 0 ||
                    a[2] == 30 && a[8] == 30 && a[5] == 0 ||
                    a[0] == 30 && a[2] == 30 && a[1] == 0 ||
                    a[3] == 30 && a[5] == 30 && a[4] == 0 ||
                    a[6] == 30 && a[8] == 30 && a[7] == 0 ||
                    a[0] == 30 && a[8] == 30 && a[4] == 0 ||
                    a[2] == 30 && a[6] == 30 && a[4] == 0) {

                    verificaPvitoriaO()

                } else if (a[0] == 1 && a[1] == 1 && a[2] == 0 ||
                    a[1] == 1 && a[2] == 1 && a[0] == 0 ||
                    a[3] == 1 && a[4] == 1 && a[5] == 0 ||
                    a[4] == 1 && a[5] == 1 && a[3] == 0 ||
                    a[6] == 1 && a[7] == 1 && a[8] == 0 ||
                    a[7] == 1 && a[8] == 1 && a[6] == 0 ||

                    a[0] == 1 && a[3] == 1 && a[6] == 0 ||
                    a[3] == 1 && a[6] == 1 && a[0] == 0 ||
                    a[1] == 1 && a[4] == 1 && a[7] == 0 ||
                    a[4] == 1 && a[7] == 1 && a[1] == 0 ||
                    a[2] == 1 && a[5] == 1 && a[8] == 0 ||
                    a[5] == 1 && a[8] == 1 && a[2] == 0 ||

                    a[0] == 1 && a[4] == 1 && a[8] == 0 ||
                    a[4] == 1 && a[8] == 1 && a[0] == 0 ||
                    a[2] == 1 && a[4] == 1 && a[6] == 0 ||
                    a[4] == 1 && a[6] == 1 && a[2] == 0 ||

                    a[0] == 1 && a[6] == 1 && a[3] == 0 ||
                    a[1] == 1 && a[7] == 1 && a[4] == 0 ||
                    a[2] == 1 && a[8] == 1 && a[5] == 0 ||
                    a[0] == 1 && a[2] == 1 && a[1] == 0 ||
                    a[3] == 1 && a[5] == 1 && a[4] == 0 ||
                    a[6] == 1 && a[8] == 1 && a[7] == 0 ||
                    a[0] == 1 && a[8] == 1 && a[4] == 0 ||
                    a[2] == 1 && a[6] == 1 && a[4] == 0) {

                    verificaPvitoriaX()

                } else {

                    // CPU comecando modo de jogo 01
                    if ( cpuComecaJogada == 1 ) {

                        if (a[4] == 0) {
                            //var cJogar = arrayListOf<Int>()
                            //cJogar.addAll(listOf(0, 2, 6, 7))
                            //Collections.shuffle(cJogar)

                            clique?.start()
                            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[4]?.isClickable = false
                            a[4] = somarXeO
                            verificaVencedor()

                        } else {
                            var cJogar = arrayListOf<Int>()
                            cJogar.addAll(listOf(0, 2, 6, 8))
                            Collections.shuffle(cJogar)
                            //(0 until 3).random()

                            clique?.start()
                            p[ cJogar.get(0) ]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[ cJogar.get(0) ]?.isClickable = false
                            a[ cJogar.get(0) ] = somarXeO
                            verificaVencedor()
                        }

                    }

                    if ( cpuComecaJogada == 2 ) {


                        if (a[1] == 1) {
                            clique?.start()
                            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[2]?.isClickable = false
                            a[2] = somarXeO
                            verificaVencedor()

                        } else if (a[3] == 1) {
                            clique?.start()
                            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[6]?.isClickable = false
                            a[6] = somarXeO
                            verificaVencedor()

                        } else if (a[5] == 1 || a[7] == 1) {
                            clique?.start()
                            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[8]?.isClickable = false
                            a[8] = somarXeO
                            verificaVencedor()

                        } else if ( a[0] == 1 || a[2] == 1 || a[6] == 1 || a[8] == 1 ) {

                            if( a[4] == 30 && a[0] == 1 ||  a[4] == 30 && a[2] == 1 ){
                                //empate
                                clique?.start()
                                p[1]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                                p[1]?.isClickable = false
                                a[1] = somarXeO
                                verificaVencedor()
                            } else if( a[4] == 30 && a[6] == 1 ||  a[4] == 30 && a[8] == 1){
                                //empate
                                clique?.start()
                                p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                                p[7]?.isClickable = false
                                a[7] = somarXeO
                                verificaVencedor()
                            }



                        }
                        //P1 Começando pelo centro
                        if( a[4] == 1 && a[8] == 1 && a[0] == 30 || a[4] == 1 && a[0] == 1 && a[8] == 30 ) {
                            clique?.start()
                            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[6]?.isClickable = false
                            a[6] = somarXeO
                            verificaVencedor()
                        } else if( a[4] == 1 && a[6] == 1 && a[2] == 30 || a[4] == 1 && a[2] == 1 && a[6] == 30 ) {
                            clique?.start()
                            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[0]?.isClickable = false
                            a[0] = somarXeO
                            verificaVencedor()
                        }



                    }

                    if ( cpuComecaJogada == 3 ) {

                        if( a[4] == 30 && a[2] == 30 && a[1] == 1 && a[6] == 1 || a[4] == 30 && a[6] == 30 && a[3] == 1 && a[2] == 1 ){
                            //Vitoria
                            clique?.start()
                            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[8]?.isClickable = false
                            a[8] = somarXeO
                            verificaVencedor()

                        } else if( a[4] == 30 && a[8] == 30 && a[7] == 1 && a[0] == 1){
                            //Vitoria
                            clique?.start()
                            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[2]?.isClickable = false
                            a[2] = somarXeO
                            verificaVencedor()

                        } else if ( a[4] == 30 && a[8] == 30 && a[0] == 1 && a[5] == 1 ){
                            //Vitoria
                            clique?.start()
                            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[6]?.isClickable = false
                            a[6] = somarXeO
                            verificaVencedor()

                        } else if ( a[1] == 30 && a[4] == 30 && a[7] == 1 && a[2] == 1 ) {
                            //Empate
                            clique?.start()
                            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[8]?.isClickable = false
                            a[8] = somarXeO
                            verificaVencedor()
                        } else if ( a[1] == 30 && a[4] == 30 && a[7] == 1 && a[0] == 1 ){
                            //Empate
                            clique?.start()
                            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[6]?.isClickable = false
                            a[6] = somarXeO
                            verificaVencedor()
                        } else if ( a[4] == 30 && a[7] == 30 && a[6] == 1 && a[1] == 1 ){
                            //Empate
                            clique?.start()
                            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[0]?.isClickable = false
                            a[0] = somarXeO
                            verificaVencedor()
                        } else if ( a[4] == 30 && a[7] == 30 && a[8] == 1 && a[1] == 1 ){
                            //Empate
                            clique?.start()
                            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[2]?.isClickable = false
                            a[2] = somarXeO
                            verificaVencedor()
                        } else {
                            nFacilRobo()
                        }


                    }

                    if( cpuComecaJogada == 4 ){

                        if ( a[0] == 1 && a[2] == 1 && a[7] == 1 && a[1] == 30 && a[4] == 30 && a[6] == 30 && a[3] == 0 && a[5] == 0 && a[8] == 0 || a[1] == 1 && a[6] == 1 && a[8] == 1 && a[0] == 30 && a[4] == 30 && a[7] == 30 && a[3] == 0 && a[5] == 0 && a[2] == 0 ){
                            //Empate
                            clique?.start()
                            p[3]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[3]?.isClickable = false
                            a[3] = somarXeO
                            verificaVencedor()

                        } else if ( a[0] == 1 && a[2] == 1 && a[7] == 1 && a[1] == 30 && a[4] == 30 && a[8] == 30 && a[3] == 0 && a[5] == 0 && a[6] == 0 || a[1] == 1 && a[6] == 1 && a[8] == 1 && a[2] == 30 && a[4] == 30 && a[7] == 30 && a[3] == 0 && a[5] == 0 && a[0] == 0 ){
                            //Empate
                            clique?.start()
                            p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[5]?.isClickable = false
                            a[5] = somarXeO
                            verificaVencedor()

                        }

                        //P1 Começando pelo centro
                        if( a[0] == 30 && a[2] == 1 && a[3] == 1 && a[4] == 1 && a[5] == 30 && a[6] == 30 && a[8] == 1 && a[1] == 0 && a[7] == 0 ) {
                            clique?.start()
                            p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[7]?.isClickable = false
                            a[7] = somarXeO
                            verificaVencedor()

                        } else if( a[0] == 30 && a[1] == 1 && a[2] == 30 && a[4] == 1 && a[6] == 1 && a[7] == 30 && a[8] == 1 && a[3] == 0 && a[5] == 0 ||
                            a[0] == 1 && a[1] == 30 && a[2] == 1 && a[4] == 1 && a[6] == 30 && a[7] == 1 && a[8] == 30 && a[3] == 0 && a[5] == 0 ) {
                            clique?.start()
                            p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
                            p[5]?.isClickable = false
                            a[5] = somarXeO
                            verificaVencedor()
                        } else {
                            nFacilRobo()
                        }

                    }

                }

            }
        }

        vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())
    }

    fun verificaVencedor() {
        if (vez) {
            vez = false
            vencedor.setText(" Vez do " + nP2.text.toString())
            somarXeO = 30

        } else if (!vez) {
            vez = true
            vencedor.setText(" Vez do " + nP1.text.toString())
            somarXeO = 1
        }


        // INICIO <------------------------------------------------->
        if (a[0] == 1 && a[1] == 1 && a[2] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[0]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[1]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[2]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[0] == 30 && a[1] == 30 && a[2] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[0]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[1]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[2]?.setBackgroundColor(  Color.parseColor("#C9051281") )

            trocarFundo()

        } else if (a[3] == 1 && a[4] == 1 && a[5] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[3]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[5]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[3] == 30 && a[4] == 30 && a[5] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[3]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[5]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        } else if (a[6] == 1 && + a[7] == 1 && a[8] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[6]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[7]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[6] == 30 && + a[7] == 30 && a[8] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[6]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[7]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        }

        // FIM <------------------------------------------------->

        // INICIO |

        else if (a[0] == 1 && a[3] == 1 && a[6] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[0]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[3]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[6]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[0] == 30 && a[3] == 30 && a[6] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[0]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[3]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[6]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        } else if (a[1] == 1 && a[4] == 1 && a[7] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[1]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[7]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[1] == 30 && a[4] == 30 && a[7] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[1]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[7]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        } else if (a[2] == 1 && a[5] == 1 && a[8] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[2]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[5]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[2] == 30 && a[5] == 30 && a[8] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[2]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[5]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        }

        // FIM |

        //INICIO / \

        else if (a[0] == 1 && a[4] == 1 && a[8] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[0]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[0] == 30 && a[4] == 30 && a[8] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[0]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        } else if (a[2] == 1 && a[4] == 1 && a[6] == 1) {
            vencedor.setText(nP1.text.toString() + " venceu a partida")
            var pl = placarX.getText().toString().toInt() + 1
            placarX.setText(pl.toString())
            p[2]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            p[6]?.setBackgroundColor(  Color.parseColor("#C9078E15") )
            trocarFundo()

        } else if (a[2] == 30 && a[4] == 30 && a[6] == 30) {
            vencedor.setText(nP2.text.toString() + " venceu a partida")
            var pl = placarO.getText().toString().toInt() + 1
            placarO.setText(pl.toString())
            p[2]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            p[6]?.setBackgroundColor(  Color.parseColor("#C9051281") )
            trocarFundo()

        }

        //FIM / \

        //Empate

        else {
            verificaEmpate()
        }


    }

    fun trocarFundo(){

    qPartidas = qPartidas + 1
    quantidadePartidas.setText( qPartidas.toString() )
    statusPartida = "Encerrada"
    sairButton.visibility = View.VISIBLE
    continuarButton.visibility = View.VISIBLE

    p[0]?.isEnabled = false
    p[1]?.isEnabled = false
    p[2]?.isEnabled = false
    p[3]?.isEnabled = false
    p[4]?.isEnabled = false
    p[5]?.isEnabled = false
    p[6]?.isEnabled = false
    p[7]?.isEnabled = false
    p[8]?.isEnabled = false


        if( quantidadePartidas.text.toString().equals("2")){
            lPrincipal.setBackgroundResource(R.drawable.back4)
        } else if (  quantidadePartidas.text.toString().equals("4") ){
            lPrincipal.setBackgroundResource(R.drawable.back5)
            explosao3?.start()
        } else if ( quantidadePartidas.text.toString().equals("7")){
            explosao3?.start()
            lPrincipal.setBackgroundResource(R.drawable.back6)
        }

    }

    fun verificaEmpate(){

        if (
            a[0] != 0 &&
            a[1] != 0 &&
            a[2] != 0 &&

            a[3] != 0 &&
            a[4] != 0 &&
            a[5] != 0 &&

            a[6] != 0 &&
            a[7] != 0 &&
            a[8] != 0 &&

            statusPartida.equals("emAndamento")  ) {

            statusPartida = "Empatada"
            vencedor.setText("Partida empatada")

            qEmpates = qEmpates + 1
            quantidadeEmpates.setText( qEmpates.toString() )
            qPartidas = qPartidas + 1
            quantidadePartidas.setText( qPartidas.toString() )

            sairButton.visibility = View.VISIBLE
            continuarButton.visibility = View.VISIBLE
            p[0]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[1]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[2]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[3]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[4]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[5]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[6]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[7]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[8]?.setBackgroundColor(  Color.parseColor("#C9C34907") )
            p[0]?.isClickable = false
            p[1]?.isClickable = false
            p[2]?.isClickable = false
            p[3]?.isClickable = false
            p[4]?.isClickable = false
            p[5]?.isClickable = false
            p[6]?.isClickable = false
            p[7]?.isClickable = false
            p[8]?.isClickable = false

            if( quantidadePartidas.text.toString().equals("2")){
                lPrincipal.setBackgroundResource(R.drawable.back4)
            } else if ( quantidadePartidas.text.toString().equals("4") ){
                lPrincipal.setBackgroundResource(R.drawable.back5)
                explosao3?.start()
            } else if ( quantidadePartidas.text.toString().equals("7") ){
                explosao3?.start()
                lPrincipal.setBackgroundResource(R.drawable.back6)
            }


        }
    }

    fun novoJogo(){
        //for (i in 0..a.size ) a[i] = 0
        //lPrincipal.setBackgroundResource(R.drawable.back3)
        statusPartida = "emAndamento"
        vez = true

        a[0] = 0
        a[1] = 0
        a[2] = 0

        a[3] = 0
        a[4] = 0
        a[5] = 0

        a[6] = 0
        a[7] = 0
        a[8] = 0

        vCasas.setText(a[0].toString() + "-" + a[1].toString() + "-" + a[2].toString() + "\n"
                + "-" + a[3].toString() + "-" + a[4].toString() + "-" + a[5].toString() + "\n"
                + "-" + a[6].toString() + "-" + a[7].toString() + "-" + a[8].toString())

        p[0]?.isEnabled = true
        p[1]?.isEnabled = true
        p[2]?.isEnabled = true
        p[3]?.isEnabled = true
        p[4]?.isEnabled = true
        p[5]?.isEnabled = true
        p[6]?.isEnabled = true
        p[7]?.isEnabled = true
        p[8]?.isEnabled = true

        p[0]?.isClickable = true
        p[1]?.isClickable = true
        p[2]?.isClickable = true
        p[3]?.isClickable = true
        p[4]?.isClickable = true
        p[5]?.isClickable = true
        p[6]?.isClickable = true
        p[7]?.isClickable = true
        p[8]?.isClickable = true

        sairButton.visibility = View.GONE
        continuarButton.visibility = View.GONE

        p[0]?.setImageResource(R.drawable.transparente)
        p[1]?.setImageResource(R.drawable.transparente)
        p[2]?.setImageResource(R.drawable.transparente)

        p[3]?.setImageResource(R.drawable.transparente)
        p[4]?.setImageResource(R.drawable.transparente)
        p[5]?.setImageResource(R.drawable.transparente)

        p[6]?.setImageResource(R.drawable.transparente)
        p[7]?.setImageResource(R.drawable.transparente)
        p[8]?.setImageResource(R.drawable.transparente)

        p[0]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[1]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[2]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[3]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[4]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[5]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[6]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[7]?.setBackgroundColor(  Color.parseColor("#00000000") )
        p[8]?.setBackgroundColor(  Color.parseColor("#00000000") )

        if(modoDeJogo.equals("pxp")) {
            alertaEscolherPecaPxP()
        } else {
            alertaEscolherPeca()
        }

        cpuComecaJogada = 0

    }

    override fun onBackPressed() {

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

    fun alertaEscolherPeca(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("SELECIONE QUEM COMEÇA")
        builder.setCancelable(false)
        builder.apply {
            setPositiveButton(nP2.text.toString(),
                DialogInterface.OnClickListener { dialog, id ->

                    if(modoDeJogo.equals("normal")){
                        statusPartida = "emAndamento"
                        somarXeO = 30
                        verificaVencedor()
                        vencedor.setText( nP2.text.toString() + " Começa")
                        pecaSelecionada = "o"
                        nFacilRobo()
                    } else if ( modoDeJogo.equals("dificil") ){
                        statusPartida = "emAndamento"
                        somarXeO = 30
                        verificaVencedor()
                        vencedor.setText( nP2.text.toString() + " Começa")
                        pecaSelecionada = "o"
                        cpuComecaJogada  = cpuComecaJogada + 1
                        cpuComeca.setText(cpuComecaJogada.toString())
                        nDificilRobo()
                    } else if ( modoDeJogo.equals("facil") ){
                        statusPartida = "emAndamento"
                        somarXeO = 30
                        verificaVencedor()
                        vencedor.setText( nP2.text.toString() + " Começa")
                        pecaSelecionada = "o"
                        cpuComecaJogada  = cpuComecaJogada + 1
                        cpuComeca.setText(cpuComecaJogada.toString())
                        nSuperFacilRobo()
                    }


                })
            setNegativeButton(nP1.text.toString(),
                DialogInterface.OnClickListener { dialog, id ->
                    somarXeO = 1
                    vencedor.setText( nP1.text.toString() + " Começa")
                    pecaSelecionada = "x"
                    statusPartida = "emAndamento"

                })
        }
        // Set other dialog properties

        // Create the AlertDialog
        builder.create()
        builder.show()
    }

    fun alertaEscolherPecaPxP(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("SELECIONE QUEM COMEÇA")
        builder.setCancelable(false)
        builder.apply {
            setPositiveButton(nP2.text.toString(),
                DialogInterface.OnClickListener { dialog, id ->
                    statusPartida = "emAndamento"
                    somarXeO = 30
                    verificaVencedor()
                    vencedor.setText( nP2.text.toString() + " Começa")
                    pecaSelecionada = "o"

                })
            setNegativeButton(nP1.text.toString(),
                DialogInterface.OnClickListener { dialog, id ->
                    statusPartida = "emAndamento"
                    somarXeO = 1
                    vencedor.setText( nP1.text.toString() + " Começa")
                    pecaSelecionada = "x"

                })
        }
        // Set other dialog properties

        // Create the AlertDialog
        builder.create()
        builder.show()
    }

    fun verificaPvitoriaO() {
        // Verificação de possivel vitoria do O
        if( a[0] == 30 && a[1] == 30 && a[2] == 0 ) {

            clique?.start()
            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[2]?.isClickable = false
            a[2] = somarXeO
            verificaVencedor()

        } else if( a[1] == 30 && a[2] == 30 && a[0] == 0 ) {

            clique?.start()
            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[0]?.isClickable = false
            a[0] = somarXeO
            verificaVencedor()

        } else if( a[3] == 30 && a[4] == 30 && a[5] == 0 ) {

            clique?.start()
            p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[5]?.isClickable = false
            a[5] = somarXeO
            verificaVencedor()

        } else if( a[4] == 30 && a[5] == 30 && a[3] == 0 ) {

            clique?.start()
            p[3]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[3]?.isClickable = false
            a[3] = somarXeO
            verificaVencedor()

        } else if( a[6] == 30 && a[7] == 30 && a[8] == 0 ) {

            clique?.start()
            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[8]?.isClickable = false
            a[8] = somarXeO
            verificaVencedor()

        } else if( a[7] == 30 && a[8] == 30 && a[6] == 0 ) {

            clique?.start()
            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[6]?.isClickable = false
            a[6] = somarXeO
            verificaVencedor()

        }

        else if( a[0] == 30 && a[3] == 30 && a[6] == 0 ) {

            clique?.start()
            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[6]?.isClickable = false
            a[6] = somarXeO
            verificaVencedor()

        } else if( a[3] == 30 && a[6] == 30 && a[0] == 0 ) {

            clique?.start()
            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[0]?.isClickable = false
            a[0] = somarXeO
            verificaVencedor()

        } else if( a[1] == 30 && a[4] == 30 && a[7] == 0 ) {

            clique?.start()
            p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[7]?.isClickable = false
            a[7] = somarXeO
            verificaVencedor()

        } else if( a[4] == 30 && a[7] == 30 && a[1] == 0 ) {

            clique?.start()
            p[1]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[1]?.isClickable = false
            a[1] = somarXeO
            verificaVencedor()

        } else if( a[2] == 30 && a[5] == 30 && a[8] == 0 ) {

            clique?.start()
            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[8]?.isClickable = false
            a[8] = somarXeO
            verificaVencedor()

        } else if( a[5] == 30 && a[8] == 30 && a[2] == 0 ) {

            clique?.start()
            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[2]?.isClickable = false
            a[2] = somarXeO
            verificaVencedor()

        } else if( a[0] == 30 && a[4] == 30 && a[8] == 0 ) {

            clique?.start()
            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[8]?.isClickable = false
            a[8] = somarXeO
            verificaVencedor()

        } else if( a[4] == 30 && a[8] == 30 && a[0] == 0 ) {

            clique?.start()
            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[0]?.isClickable = false
            a[0] = somarXeO
            verificaVencedor()

        } else if( a[2] == 30 && a[4] == 30 && a[6] == 0 ) {

            clique?.start()
            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[6]?.isClickable = false
            a[6] = somarXeO
            verificaVencedor()

        } else if( a[4] == 30 && a[6] == 30 && a[2] == 0 ) {

            clique?.start()
            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[2]?.isClickable = false
            a[2] = somarXeO
            verificaVencedor()

        } else if( a[0] == 30 && a[6] == 30 && a[3] == 0 ) {

            clique?.start()
            p[3]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[3]?.isClickable = false
            a[3] = somarXeO
            verificaVencedor()

        } else if( a[1] == 30 && a[7] == 30 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        } else if( a[2] == 30 && a[8] == 30 && a[5] == 0 ) {

            clique?.start()
            p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[5]?.isClickable = false
            a[5] = somarXeO
            verificaVencedor()

        } else if( a[0] == 30 && a[2] == 30 && a[1] == 0 ) {

            clique?.start()
            p[1]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[1]?.isClickable = false
            a[1] = somarXeO
            verificaVencedor()

        } else if( a[3] == 30 && a[5] == 30 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        } else if( a[6] == 30 && a[8] == 30 && a[7] == 0 ) {

            clique?.start()
            p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[7]?.isClickable = false
            a[7] = somarXeO
            verificaVencedor()

        } else if( a[0] == 30 && a[8] == 30 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        } else if( a[2] == 30 && a[6] == 30 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        }


    }

    fun verificaPvitoriaX() {
        // Verificação de possivel vitoria do X
        if( a[0] == 1 && a[1] == 1 && a[2] == 0 ) {

            clique?.start()
            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[2]?.isClickable = false
            a[2] = somarXeO
            verificaVencedor()

        } else if( a[1] == 1 && a[2] == 1 && a[0] == 0 ) {

            clique?.start()
            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[0]?.isClickable = false
            a[0] = somarXeO
            verificaVencedor()

        } else if( a[3] == 1 && a[4] == 1 && a[5] == 0 ) {

            clique?.start()
            p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[5]?.isClickable = false
            a[5] = somarXeO
            verificaVencedor()

        } else if( a[4] == 1 && a[5] == 1 && a[3] == 0 ) {

            clique?.start()
            p[3]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[3]?.isClickable = false
            a[3] = somarXeO
            verificaVencedor()

        } else if( a[6] == 1 && a[7] == 1 && a[8] == 0 ) {

            clique?.start()
            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[8]?.isClickable = false
            a[8] = somarXeO
            verificaVencedor()

        } else if( a[7] == 1 && a[8] == 1 && a[6] == 0 ) {

            clique?.start()
            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[6]?.isClickable = false
            a[6] = somarXeO
            verificaVencedor()

        }

        else if( a[0] == 1 && a[3] == 1 && a[6] == 0 ) {

            clique?.start()
            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[6]?.isClickable = false
            a[6] = somarXeO
            verificaVencedor()

        } else if( a[3] == 1 && a[6] == 1 && a[0] == 0 ) {

            clique?.start()
            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[0]?.isClickable = false
            a[0] = somarXeO
            verificaVencedor()

        } else if( a[1] == 1 && a[4] == 1 && a[7] == 0 ) {

            clique?.start()
            p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[7]?.isClickable = false
            a[7] = somarXeO
            verificaVencedor()

        } else if( a[4] == 1 && a[7] == 1 && a[1] == 0 ) {

            clique?.start()
            p[1]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[1]?.isClickable = false
            a[1] = somarXeO
            verificaVencedor()

        } else if( a[2] == 1 && a[5] == 1 && a[8] == 0 ) {

            clique?.start()
            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[8]?.isClickable = false
            a[8] = somarXeO
            verificaVencedor()

        } else if( a[5] == 1 && a[8] == 1 && a[2] == 0 ) {

            clique?.start()
            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[2]?.isClickable = false
            a[2] = somarXeO
            verificaVencedor()

        } else if( a[0] == 1 && a[4] == 1 && a[8] == 0 ) {

            clique?.start()
            p[8]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[8]?.isClickable = false
            a[8] = somarXeO
            verificaVencedor()

        } else if( a[4] == 1 && a[8] == 1 && a[0] == 0 ) {

            clique?.start()
            p[0]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[0]?.isClickable = false
            a[0] = somarXeO
            verificaVencedor()

        } else if( a[2] == 1 && a[4] == 1 && a[6] == 0 ) {

            clique?.start()
            p[6]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[6]?.isClickable = false
            a[6] = somarXeO
            verificaVencedor()

        } else if( a[4] == 1 && a[6] == 1 && a[2] == 0 ) {

            clique?.start()
            p[2]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[2]?.isClickable = false
            a[2] = somarXeO
            verificaVencedor()

        } else if( a[0] == 1 && a[6] == 1 && a[3] == 0 ) {

            clique?.start()
            p[3]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[3]?.isClickable = false
            a[3] = somarXeO
            verificaVencedor()

        } else if( a[1] == 1 && a[7] == 1 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        } else if( a[2] == 1 && a[8] == 1 && a[5] == 0 ) {

            clique?.start()
            p[5]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[5]?.isClickable = false
            a[5] = somarXeO
            verificaVencedor()

        } else if( a[0] == 1 && a[2] == 1 && a[1] == 0 ) {

            clique?.start()
            p[1]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[1]?.isClickable = false
            a[1] = somarXeO
            verificaVencedor()

        } else if( a[3] == 1 && a[5] == 1 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        } else if( a[6] == 1 && a[8] == 1 && a[7] == 0 ) {

            clique?.start()
            p[7]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[7]?.isClickable = false
            a[7] = somarXeO
            verificaVencedor()

        } else if( a[0] == 1 && a[8] == 1 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        } else if( a[2] == 1 && a[6] == 1 && a[4] == 0 ) {

            clique?.start()
            p[4]?.setImageResource( imgPersonagensB.get( Integer.parseInt( bundle!!.getString("imgP2") )))
            p[4]?.isClickable = false
            a[4] = somarXeO
            verificaVencedor()

        }


    }
}
