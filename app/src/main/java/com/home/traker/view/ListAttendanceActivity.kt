package com.home.traker.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.holder.ListItemAttendanceAdapter
import com.home.traker.model.ListAttendanceModel
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_attendance_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class ListAttendanceActivity : BaseActivity() {

    var adapter: ListItemAttendanceAdapter? = null
    var foodsList = ArrayList<ListAttendanceModel>()
    var driver_id = ""
    var driver_name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_attendance_list)
            if (intent != null && intent.hasExtra(Constants.DRIVER_ID)) {
                driver_id = intent.getStringExtra(Constants.DRIVER_ID).toString()
                driver_name = intent.getStringExtra(Constants.DRIVER_NAME).toString()
                driverName.text = driver_name
                getDriverListAPI(driver_id)
            }

            addDriver.setOnClickListener {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
            monthYear.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        try {
                            val date = Date(year-1900, monthOfYear, dayOfMonth)
                            val formatter = SimpleDateFormat("MM-yyyy")
                            val cdate = formatter.format(date)

                            // Display Selected date in textbox
                            monthYear.setText("" + setMonth(monthOfYear + 1) + "-" + year)
                            getDatesBetweenStartAndFinish(cdate.toString())
                        } catch (e1: ParseException) {
                            e1.printStackTrace()
                        }
                    },
                    year,
                    month,
                    day
                )

                dpd.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(ParseException::class)
    private fun getDatesBetweenStartAndFinish(selectedDate: String) {
        try {
            var selectedMonthList = ArrayList<ListAttendanceModel>()

            for (eachDate in 0 until foodsList.size) {
                if (foodsList[eachDate].currentdate != null && foodsList[eachDate].currentdate!!.contains(
                        selectedDate,
                        false
                    )
                ) {
                    selectedMonthList.add(foodsList[eachDate])
                }
            }

            loadList(selectedMonthList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadList(data: ArrayList<ListAttendanceModel>) {
        try {
            adapter = ListItemAttendanceAdapter(
                this@ListAttendanceActivity,
                data
            )
            listRecyc.apply {
                layoutManager = LinearLayoutManager(this@ListAttendanceActivity)
                adapter = ListItemAttendanceAdapter(
                    this@ListAttendanceActivity,
                    data
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setMonth(monthOfYear: Int): String {
        when (monthOfYear) {
            1 -> return "Jan"
            2 -> return "Feb"
            3 -> return "Mar"
            4 -> return "Apr"
            5 -> return "May"
            6 -> return "Jun"
            7 -> return "Jul"
            8 -> return "Aug"
            9 -> return "Sep"
            10 -> return "Oct"
            11 -> return "Nov"
            12 -> return "Dec"
            else -> { // Note the block
                return "June"
            }
        }
    }

    private fun getDriverListAPI(driverID: String) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.DriverAttendanceResponseModel> =
                apiService.getDriverAttendanceList(driverID)
            call.enqueue(object : Callback<ResponseModelClasses.DriverAttendanceResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.DriverAttendanceResponseModel>,
                    response: Response<ResponseModelClasses.DriverAttendanceResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("ResDriverAttendList:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                foodsList = response.body()!!.data
                                loadList(foodsList)
                                //showSuccessPopup(response.body()!!.message)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.DriverAttendanceResponseModel>,
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
}