package com.home.traker.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.holder.ListItemVendorRouteAdapter
import com.home.traker.model.DriverVendorListModel
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_attendance_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ListVendorRouteActivity : BaseActivity() {

    var adapter: ListItemVendorRouteAdapter? = null
    var foodsList = ArrayList<DriverVendorListModel>()
    var driver_id = ""
    var driver_name = ""
    var attendance_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_attendance_list)
            if (intent != null && intent.hasExtra(Constants.DRIVER_ID)) {
                driver_id = intent.getStringExtra(Constants.DRIVER_ID).toString()
                driver_name = intent.getStringExtra(Constants.DRIVER_NAME).toString()
                //attendance_id = intent.getStringExtra(Constants.ATTENDANCE_ID).toString()
                getVendorListAPI(attendance_id)
            }

            driveDate.text = "Vendor Visit"
            driveDateIn.visibility = View.GONE
            driveDateOut.visibility = View.GONE
            driverName.text = driver_name

            val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val formatted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                current.format(formatter)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            monthYear.text = formatted

            monthYear.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        // Display Selected date in textbox

                        try {
                            val date = Date(year - 1900, monthOfYear, dayOfMonth)
                            val formatter = SimpleDateFormat("yyyy-MM-dd")
                            val cdate = formatter.format(date)

                            // Display Selected date in textbox
                            monthYear.setText("" + dayOfMonth + "-" + setMonth(monthOfYear + 1) + "-" + year)
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
            addDriver.text = "Route Map"
            addDriver.setOnClickListener {
                var intent1 = Intent(this, DriverRouteMapActivity::class.java)
                intent1.putExtra(Constants.LAT, "0.0")
                intent1.putExtra(Constants.ATTENDANCE_ID, attendance_id)
                intent1.putExtra(Constants.LONG, "0.0")
                intent1.putExtra(Constants.IS_ROUTE_MAP_VIEW, true)
                startActivity(intent1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(ParseException::class)
    private fun getDatesBetweenStartAndFinish(selectedDate: String) {
        try {
            var selectedMonthList = ArrayList<DriverVendorListModel>()

            /*for (eachDate in foodsList) {
                    if (eachDate.after(start) && eachDate.before(end)) {
                        datesInJanuaryList.add(eachDate)
                    }
                }*/
            for (eachDate in 0 until foodsList.size) {
                if (foodsList[eachDate].current_time != null && foodsList[eachDate].current_time!!.contains(
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

    private fun loadList(data: ArrayList<DriverVendorListModel>) {
        try {
            adapter = ListItemVendorRouteAdapter(
                this@ListVendorRouteActivity,
                data
            )
            listRecyc.apply {
                layoutManager =
                    LinearLayoutManager(this@ListVendorRouteActivity)
                adapter = ListItemVendorRouteAdapter(
                    this@ListVendorRouteActivity,
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

    private fun getVendorListAPI(driverID: String) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.VendorListResponseModel> =
                apiService.getVendorList(driverID)//driverID
            call.enqueue(object : Callback<ResponseModelClasses.VendorListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.VendorListResponseModel>,
                    response: Response<ResponseModelClasses.VendorListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("ResDriverRouteList:", response.body()!!.data.toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                foodsList = response.body()!!.data
                                loadList(foodsList)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.VendorListResponseModel>,
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