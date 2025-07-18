package com.said.luna.cazarpatos

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.said.luna.cazarpatos.database.RankingPlayerDBHelper

class RankingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ranking)
        /*OperacionesSqLite()
        GrabarRankingSQLite()
        LeerRankingsSQLite()*/
        consultarPuntajeJugadores()

        /*
        var jugadores = arrayListOf<Player>()
        jugadores.add(Player("Said.Luna",10))
        jugadores.add(Player("Jugador2",6))
        jugadores.add(Player("Jugador3",3))
        jugadores.add(Player("Jugador4",9))
        jugadores.sortByDescending { it.huntedDucks }

        val recyclerViewRanking: RecyclerView = findViewById(R.id.recyclerViewRanking);
        recyclerViewRanking.layoutManager = LinearLayoutManager(this);
        recyclerViewRanking.adapter = RankingAdapter(jugadores);
        recyclerViewRanking.setHasFixedSize(true);
    */
    }

    fun consultarPuntajeJugadores() {
        val db = Firebase.firestore

        db.collection("ranking")
            .orderBy("huntedDucks", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                Log.d(EXTRA_LOGIN, "Success getting documents")

                val jugadores = ArrayList<Player>()
                for (document in result) {
                    val jugador = document.toObject(Player::class.java)
                    // Alternativamente: val jugador = document.toObject<Player>()
                    jugadores.add(jugador)
                }

                // Poblar en RecyclerView información usando el adaptador
                val recyclerViewRanking: RecyclerView = findViewById(R.id.recyclerViewRanking)
                recyclerViewRanking.layoutManager = LinearLayoutManager(this)
                recyclerViewRanking.adapter = RankingAdapter(jugadores)
                recyclerViewRanking.setHasFixedSize(true)
            }
            .addOnFailureListener { exception ->
                Log.w(EXTRA_LOGIN, "Error getting documents.", exception)
                Toast.makeText(this, "Error al obtener datos de jugadores", Toast.LENGTH_LONG).show()
            }
    }
    /*
    fun OperacionesSqLite(){
        RankingPlayerDBHelper(this).deleteAllRanking()
        RankingPlayerDBHelper(this).insertRankingByQuery(Player("Jugador9",10))
        val patosCazados = RankingPlayerDBHelper(this).readDucksHuntedByPlayer("Jugador9")
        RankingPlayerDBHelper(this).updateRanking(Player("Jugador9",5))
        RankingPlayerDBHelper(this).deleteRanking("Jugador9")
        RankingPlayerDBHelper(this).insertRanking(Player("Jugador9",7))
        val players = RankingPlayerDBHelper(this).readAllRankingByQuery()
    }
    fun GrabarRankingSQLite(){
        val jugadores = arrayListOf(
            Player("Said.Luna", 10),
            Player("Jugador2", 6),
            Player("Jugador3", 3),
            Player("Jugador4", 9)
        )
        jugadores.sortByDescending { it.huntedDucks }
        for(jugador in jugadores){
            RankingPlayerDBHelper(this).insertRanking(jugador)
        }
    }
    fun LeerRankingsSQLite(){
        val jugadoresSQLite = RankingPlayerDBHelper(this).readAllRanking()
        val recyclerViewRanking: RecyclerView = findViewById(R.id.recyclerViewRanking)
        recyclerViewRanking.layoutManager = LinearLayoutManager(this)
        recyclerViewRanking.adapter = RankingAdapter(jugadoresSQLite)
        recyclerViewRanking.setHasFixedSize(true)
    }
     */

}
