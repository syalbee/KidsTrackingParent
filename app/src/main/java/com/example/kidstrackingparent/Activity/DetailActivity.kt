package com.example.kidstrackingparent.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.kidstrackingparent.BuildConfig
import com.example.kidstrackingparent.R
import com.example.kidstrackingparent.dataClass.Childuserdata
import com.example.kidstrackingparent.dataClass.CompanionString.Companion.ICON_ID
import com.example.kidstrackingparent.dataClass.CompanionString.Companion.LAYER_ID
import com.example.kidstrackingparent.dataClass.CompanionString.Companion.SOURCE_ID
import com.google.firebase.database.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: MapboxMap
    private lateinit var dbref : DatabaseReference
    private lateinit var listAnak:ArrayList<Childuserdata>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.activity_detail)
        listAnak = ArrayList()
        
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)

    }

    private fun getChilddata(uid: String?, map: MapboxMap) {
        dbref = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child("Childs")
            .child(uid.toString())
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val childData = snapshot.getValue(Childuserdata::class.java)
                    listAnak.add(childData!!)

                    tvPosition.text = childData?.position.toString()
                    tvLastupdate.text = childData?.lastUpdate.toString()
                    println(childData?.lon)

                    var lat : Double = childData?.lat!!.toDouble()
                    var lon : Double = childData?.lon!!.toDouble()

                    val symbolLayers = ArrayList<Feature>()
                    symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(lon, lat))) //remember to reverse LatLng
                    map.setStyle(
                        Style.Builder().fromUri(Style.MAPBOX_STREETS)
                            .withImage(ICON_ID, BitmapUtils
                                .getBitmapFromDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.mapbox_marker_icon_default))!!)
                            .withSource(GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(symbolLayers)))
                            .withLayer(
                                SymbolLayer(LAYER_ID, SOURCE_ID)
                                    .withProperties(iconImage(ICON_ID),
                                        PropertyFactory.iconSize(1.0f),
                                        PropertyFactory.iconAllowOverlap(true),
                                        PropertyFactory.iconIgnorePlacement(true)
                                    ))
                    )
                    {
                        //Here is style loaded
                    }

                    //DEFAULT ZOOM , YOU CAN CUSTOME THIS
                    val latLng = LatLng(lat,lon)
                    val position = CameraPosition.Builder().target(latLng).zoom(13.0).tilt(10.0).build()
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position))


                    Log.d("DetailActivity", "onDataChange: {${childData.toString()}}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DetailActivity", error.toException().toString())
            }
        })

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        initAddMarker(map)
    }

    private fun initAddMarker(map: MapboxMap) {

        val intentUid = intent.getStringExtra("uid")
        println(getChilddata(intentUid, map))

    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }
}