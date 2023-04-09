package com.example.trailsurfing

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import com.example.trailsurfing.common.Utils
import com.example.trailsurfing.data.Point
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.TimeUnit

class RouteService : Service() {

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var reference: DatabaseReference

    private val list = mutableListOf<Point>()

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        reference = Firebase.database.reference
        reference = FirebaseDatabase.getInstance().getReference("ItemsList").child(MainActivity.login)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            maxWaitTime = TimeUnit.SECONDS.toMillis(15)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                list.add(Point(p0.lastLocation?.latitude, p0.lastLocation?.longitude))

                startForeground(
                    Utils.CURRENT_NOTIFICATION_ID,
                    Utils.generatingNotification(this@RouteService, p0.locations.toString())
                )
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            val repealLocationTrackingFromNotification = it.getBooleanExtra(
                Utils.EXTRA_REPEAL_LOCATION_TRACKING_FROM_NOTIFICATION,
                false
            )

            if (repealLocationTrackingFromNotification) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                MainActivity.route.pathUUID = list

                reference.push().setValue(MainActivity.route)


                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }
}