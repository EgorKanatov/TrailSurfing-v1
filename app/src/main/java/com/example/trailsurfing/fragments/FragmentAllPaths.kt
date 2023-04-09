package com.example.trailsurfing.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trailsurfing.MainActivity
import com.example.trailsurfing.R
import com.example.trailsurfing.data.Route
import com.example.trailsurfing.databinding.FragmentAllPathsBinding
import com.example.trailsurfing.recyclerview.OnItemClickListner
import com.example.trailsurfing.recyclerview.UniversalRecyclerViewAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.log


class FragmentAllPaths : Fragment() {
    private lateinit var binding: FragmentAllPathsBinding
    private lateinit var reference: DatabaseReference
    private val routes = MutableLiveData<List<Route>>()

    private val Itemslist = mutableListOf<Route>()
    private val Savedlist = mutableListOf<Route>()
    private val Recomendlist = mutableListOf<Route>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllPathsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("rewrwer", Recomendlist.size.toString())
        super.onViewCreated(view, savedInstanceState)

        //переход из фрагмента со всеми путями в описание путя по клику
        val onClick = object : OnItemClickListner<Route> {
            override fun onClick(view: View, item: Route) {
                val directions = FragmentAllPathsDirections.actionFragmentAllPathsToFragmentFullPathDescription2(item)

                findNavController().navigate(directions)
            }
        }

        routes.observe(viewLifecycleOwner, Observer {
            println("Done!")

        })
        routes.observe(viewLifecycleOwner, Observer {
            println("Done!")

            binding.pathsList.adapter = UniversalRecyclerViewAdapter<Route>(R.layout.cardview_path_layout, it, onClick)
        })


        //ссылаемся к базе данных
        reference = Firebase.database.reference
        reference = FirebaseDatabase.getInstance().getReference("ItemsList")
        //получение данных из базы данных
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Route>()
                for (item in snapshot.children) {
                    for (item2 in item.children) {
                        val route = item2.getValue(Route::class.java)
                        items.add(route!!)
                    }
                }
                routes.postValue(items)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
