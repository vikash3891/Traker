package com.home.traker.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.RequestModel
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.databinding.ActivityDriverActivityLogBinding
import com.home.traker.navigators.OnItemSelectionListener
import com.home.traker.model.ListItemModel
import com.home.traker.network.LocationTrackingService
import com.home.traker.utils.AppPrefences
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_driver_activity_log.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DriverActivityLogScreen : BaseActivity(),
    OnItemSelectionListener {
    override fun getPosition(position: Int, isForTeamA: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClickListener(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var foodsList = ArrayList<String>()
    private lateinit var binding: ActivityDriverActivityLogBinding
    private lateinit var viewModel: ListItemModel
    private var isPunchedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_activity_log)
        topbar.imageSetting.background = resources.getDrawable(R.drawable.ic_account_white_24dp)

        viewModel = ListItemModel("", "", "", "", "", "", "", "", "", "")
        binding.viewmodel = viewModel
        try {
            topbar.setBackgroundColor(resources.getColor(R.color.colorComparePreviousYear))
            topbar.imageSetting.visibility = View.VISIBLE
            foodsList.add("Share")
            foodsList.add("Logout")

            val resultTypeAdapter = ArrayAdapter(this, R.layout.spinner_row, foodsList)
            resultTypeAdapter.setDropDownViewResource(R.layout.spinner_row)
            topbar.spinner.adapter = resultTypeAdapter

            try {
                topbar.imageSetting.setOnClickListener {
                    startActivity(
                        Intent(
                            this@DriverActivityLogScreen,
                            ShareMyLocationActivity::class.java
                        )
                    )
                }

                textLogout.setOnClickListener {
                    logoutAlertDialog()
                }

                textPunchIn.setOnClickListener {

                    if (!isPunchedIn) {
                        punchInOutApi(isPunchedIn)
                        isPunchedIn = true
                        Toast.makeText(this, "Punch IN", Toast.LENGTH_SHORT).show()
                        textPunchIn.alpha = 0.2f
                        textPunchIn.animate().apply {
                            interpolator = LinearInterpolator()
                            startDelay = 1000
                            duration = 500
                            alpha(1f)
                            start()
                        }
                        val jobScheduler =
                            getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

                        val jobInfo = JobInfo.Builder(
                            11,
                            ComponentName(
                                this@DriverActivityLogScreen,
                                LocationTrackingService::class.java
                            )
                        )
                            // only add if network access is required
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build()
                        jobScheduler.schedule(jobInfo)
                    } else {
                        Toast.makeText(this, "Already Punch IN", Toast.LENGTH_SHORT).show()
                    }
                }

                textPunchOut.setOnClickListener {

                    if (isPunchedIn) {
                        punchInOutApi(isPunchedIn)
                        isPunchedIn = false
                        Toast.makeText(this, "Punch OUT", Toast.LENGTH_SHORT).show()
                        textPunchOut.alpha = 0.2f
                        textPunchOut.animate().apply {
                            interpolator = LinearInterpolator()
                            startDelay = 1000
                            duration = 500
                            alpha(1f)
                            start()
                        }
                        val stopIntent = Intent(this, LocationTrackingService::class.java)
                        stopService(stopIntent)
                    } else {
                        Toast.makeText(this, "Already Punch OUT", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun punchInOutApi(isChecked: Boolean) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            var inTime = getTime()
            var outTime = getTime()

            if (!isChecked)
                outTime = ""
            else
                inTime = ""

            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.setPunchInOut(
                    RequestModel.PunchInOuRequestModel(
                        viewModel.user_id!!,
                        getDate(),
                        inTime,
                        outTime
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                var msg = ""
                                if (isPunchedIn)
                                    msg = "User Punched IN Successfully"
                                else
                                    msg = "User Punched Out Successfully"
                                showSuccessPopup(msg)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    t: Throwable
                ) {
                    Log.d("Throws:", t.message.toString())
                    dismissDialog()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }

    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }

    fun getTime(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Time is: $date.time")
        return inputFormat.format(date.time)
    }

    fun getDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Date is: $date.date")
        return inputFormat.format(date.time)
    }

    private fun logoutAlertDialog() {
        var alertDialog = android.app.AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("Are you sure you want to logout? ")
        alertDialog.setNeutralButton("Cancel") { _, _ ->
        }

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()

            AppPrefences.setLogin(this, false)

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialog.show()

    }
}