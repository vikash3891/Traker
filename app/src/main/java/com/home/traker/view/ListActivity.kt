package com.home.traker.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.holder.ListItemAdapter
import com.home.traker.model.ListItemModel
import com.home.traker.utils.AppPrefences
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : BaseActivity() {

    var adapter: ListItemAdapter? = null
    var foodsList = ArrayList<ListItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        topbar.imageSetting.visibility = View.VISIBLE
        topbar.imageSetting.background = resources.getDrawable(R.drawable.ic_logout_white_50dp)


        addDriver.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        topbar.imageSetting.setOnClickListener {
            logoutAlertDialog()
        }
    }

    private fun logoutAlertDialog() {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("Are you sure you want to logout? ")
        alertDialog.setNeutralButton("Cancel") { _, _ ->
        }

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            //AppPrefences.clearAll(this)

            AppPrefences.setLogin(this, false)

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialog.show()

    }


    override fun onResume() {
        super.onResume()
        getDriverListAPI()
    }

    private fun getDriverListAPI() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.DriverListResponseModel> =
                apiService.getDriverList()
            call.enqueue(object : Callback<ResponseModelClasses.DriverListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.DriverListResponseModel>,
                    response: Response<ResponseModelClasses.DriverListResponseModel>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                //showSuccessPopup(response.body()!!.message)
                            } else {
                                foodsList = response.body()!!.data

                                for (i in foodsList) {
                                    Log.d(
                                        "ResDriverList:",
                                        foodsList[0].lat + " " + foodsList[0].lang
                                    )
                                }
                                adapter = ListItemAdapter(this@ListActivity, foodsList)

                                listRecyc.apply {
                                    layoutManager = LinearLayoutManager(this@ListActivity)
                                    adapter = ListItemAdapter(this@ListActivity, foodsList)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.DriverListResponseModel>,
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