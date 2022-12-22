package com.example.mywearosapplication

import android.content.Context
import android.hardware.Sensor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.example.mywearosapplication.databinding.ActivityRecycleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlinx.coroutines.*

private const val MAX_ICON_PROGRESS = 0.65f

class RecycleActivity : AppCompatActivity() {
    private var sensorList: MutableList<Sensor>? = null
    private var adapter: SensorAdapter? = null
    private var wearablerecyclerViewLayoutManager: WearableLinearLayoutManager? = null
    lateinit var binding: ActivityRecycleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "Sensores", Toast.LENGTH_SHORT).show()

        val menuItems = arrayListOf<String>()
        menuItems.add("Option 1")
        menuItems.add("Option 2")
        menuItems.add("Option 3")
        menuItems.add("Option 4")
        getSensors(this)

        val wearableRecycler = binding.recyclerLauncherView
        wearableRecycler.apply {
            isEdgeItemsCenteringEnabled = true
            layoutManager = WearableLinearLayoutManager(this@RecycleActivity, CustomScrollingLayoutCallback())
            adapter = ListAdapter(this@RecycleActivity,menuItems)
        }
        binding.sensorListRecyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            layoutManager = WearableLinearLayoutManager(this@RecycleActivity, CustomScrollingLayoutCallback())
            adapter = SensorAdapter(this@RecycleActivity,sensorList?: arrayListOf())
        }
    }

    private fun getSensors(context: Context) {
        val contextRef: WeakReference<Context> = WeakReference(context)

        try {
            val contextInner = contextRef.get()

            if (contextInner != null) {
                sensorList = InfoUtilities.getSensorDetails(contextInner)
                adapter = if (sensorList != null) {
                    val tempItemList1 = ArrayList<Sensor>()
                    tempItemList1.addAll(sensorList!!)
                    SensorAdapter(
                        contextInner,
                        tempItemList1
                    )
                } else {
                    val tempItemList = ArrayList<Sensor>()
                    SensorAdapter(
                        contextInner,
                        tempItemList
                    )
                }
            }

            //UI Thread
            binding.sensorListRecyclerView.layoutManager = wearablerecyclerViewLayoutManager
            binding.sensorListRecyclerView.setHasFixedSize(true)

            if (adapter?.itemCount!! > 0) {
                binding.sensorListRecyclerView.adapter = adapter
            } else {
                binding.noSensorsTextView.visibility = View.VISIBLE
                binding.sensorListRecyclerView.visibility = View.GONE
            }

            binding.sensorListRecyclerView.requestFocus()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

class CustomScrollingLayoutCallback : WearableLinearLayoutManager.LayoutCallback() {

    private var progressToCenter: Float = 0f

    override fun onLayoutFinished(child: View, parent: RecyclerView) {
        child.apply {
            // Figure out % progress from top to bottom
            val centerOffset = height.toFloat() / 2.0f / parent.height.toFloat()
            val yRelativeToCenterOffset = y / parent.height + centerOffset

            // Normalize for center
            progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset)
            // Adjust to the maximum scale
            progressToCenter = Math.min(progressToCenter, MAX_ICON_PROGRESS)

            scaleX = 1 - progressToCenter
            scaleY = 1 - progressToCenter
        }
    }
}