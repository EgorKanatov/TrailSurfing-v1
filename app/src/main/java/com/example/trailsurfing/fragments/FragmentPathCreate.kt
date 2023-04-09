package com.example.trailsurfing.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import com.example.trailsurfing.R
import com.example.trailsurfing.data.Route
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trailsurfing.MainActivity
import com.example.trailsurfing.RouteService
import com.example.trailsurfing.databinding.FragmentPathCreateBinding
import com.example.trailsurfing.map.ViewerMapFragmentDirections
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask

class FragmentPathCreate: Fragment() {
    private lateinit var binding: FragmentPathCreateBinding

    private var mImageUri : Uri? = null

    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPathCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStorageRef = FirebaseStorage.getInstance().getReference("image_uploads") // Назначение начальной папки с изображениями

        binding.imageButton.setOnClickListener { // Выбор изображения из галереи при клике
            openFileChoose()
        }

        binding.addButton.setOnClickListener { // Кнопка загрузки фотографии на сервер
            uploadFile()
        }
    }

    private fun openFileChoose() { // Выбор изображения с типом jpeg
        val intent = Intent()
        intent.type = "image/jpeg"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,1)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            binding.imageButton.setImageURI(mImageUri)
        }
    }

    private fun uploadFile() { // Загрузка на сервер
        if (mImageUri != null) {

            val fileReference = mStorageRef!!.child(
                System.currentTimeMillis()
                    .toString() + ".jpeg"
            )
            mUploadTask = fileReference.putFile(mImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    fileReference.downloadUrl.addOnSuccessListener {
                        MainActivity.route = Route( // Подготовка данных для загрузки
                            title = binding.pathName.text.toString(),
                            description = binding.pathDescription.text.toString(),
                            imageUrl = it.toString(),
                            uuid = MainActivity.login
                        )

                        requireActivity().startService(Intent(context, RouteService::class.java)) // Старт сервиса записи местоположения


                        val directions = FragmentPathCreateDirections.actionFragmentPathCreateToViewerMapFragment()
                        findNavController().navigate(directions)
                    }


                }
                .addOnFailureListener { e ->
                    Log.e("data","${e.message}")
                }
        } else {
            Toast.makeText(context, "Заполните все параметры", Toast.LENGTH_SHORT).show()
        }
    }
}
