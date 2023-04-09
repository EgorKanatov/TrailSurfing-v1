package com.example.trailsurfing.fragments

import com.example.trailsurfing.R
import com.example.trailsurfing.data.Route
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trailsurfing.MainActivity
import com.example.trailsurfing.databinding.FragmentFullPathDescriptionBinding
import com.example.trailsurfing.map.ViewerMapFragmentDirections
import com.example.trailsurfing.recyclerview.OnItemClickListner

class FragmentFullPathDescription : Fragment() {
    private lateinit var binding: FragmentFullPathDescriptionBinding
    private val args by navArgs<FragmentFullPathDescriptionArgs>()
    private val item by lazy { args.item }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullPathDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.item = item

        binding.pathOpen.setOnClickListener {
            MainActivity.path = item.pathUUID
            val directions = FragmentFullPathDescriptionDirections.actionFragmentFullPathDescriptionToViewerMapFragment() // Переход на карту с прорисованным путём

            findNavController().navigate(directions)
        }
    }
}
