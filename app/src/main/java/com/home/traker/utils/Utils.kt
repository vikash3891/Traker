package com.home.traker.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.home.traker.R
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created
 * by anoop on 4/1/18.
 */

object Utils {
    private val PREF_AUTH_TOKEN = "auth_token"
    private val COMPANY_NAME = "company_name"
    private val PREF_USER_ID = "User-Id"
    private val PREF_USER_IMAGE = "image"
    private val PREF_QUES_ANSWER_JSON = "question_answer_data"
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
     var isRegisterSuccess = false


    /*
    Using in Dialog Screen's for custom width
     */
    fun getScreenWidth(activity: Activity): Int {
        val p = Point()
        activity.windowManager.defaultDisplay.getSize(p)
        return p.x
    }

    /*
    Using in Dialog Screen's for custom height
     */
    fun getScreenheight(activity: Activity): Int {
        val p = Point()
        activity.windowManager.defaultDisplay.getSize(p)
        return p.y
    }

    /**
     * This method is used for retrofit conversion from string to RequestBody
     *
     * @param s specify string data
     * @return return object of RequestBody
     */
    fun getRequestBody(s: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), s)
    }

    /**
     * This method is used to get RequestBody from file
     *
     * @param file specify file object
     * @return return object of RequestBody
     */
    fun getRequestBody(file: File): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file)
    }

    /*
    Using in Activities for JSON Body
     */
    fun getJSONRequestBody(stringHashMap: HashMap<String, String>?): RequestBody {
        val jsonObject = JSONObject()
        if (stringHashMap != null && stringHashMap.size > 0) {
            try {

                for ((key, value) in stringHashMap) {
                    jsonObject.put(key, value)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())
    }

    /*
    For Checking the Internet Connectivity
     */
    fun isConnected(context: Context): Boolean {
        val cm = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /*
    For Checking Permission While accessing Gallery
     */
    fun checkPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    /*
    Hide KeyBoard
     */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.findViewById<View>(android.R.id.content).getWindowToken(), 0)
    }

    fun showProgressDialog1(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        try {
            progressDialog.show()
            if (progressDialog.window != null) {
                progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            progressDialog.setContentView(R.layout.progress_layout)
            progressDialog.isIndeterminate = true
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return progressDialog
    }


    /*
    Show Snack Bar
     */


    //        if (activity != null) {
    //            final AlertDialog alertDilog = new AlertDialog.Builder(activity, R.style.AlertDialogStyle).create();
    //            alertDilog.setTitle(activity.getString(R.string.app_name));
    //            alertDilog.setMessage(message);
    //            alertDilog.setCancelable(false);
    //            alertDilog.setButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
    //                @Override
    //                public void onClick(DialogInterface dialog, int which) {
    //                    alertDilog.dismiss();
    //                }
    //            });
    //
    //            alertDilog.show();
    //            alertDilog.getWindow().setLayout((int) (Utils.getScreenWidth(activity) * 0.9f), LinearLayout.LayoutParams.WRAP_CONTENT);
    //        }


    fun convertDateFormat(oldFormat: String, newFormat: String, dateString: String): String {
        var sdf = SimpleDateFormat(oldFormat)
        try {
            val date = sdf.parse(dateString)
            sdf = SimpleDateFormat(newFormat)
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun convertDateFormatWithSuffix(oldFormat: String, dateString: String): String {
        var sdf = SimpleDateFormat(oldFormat)
        try {
            val date = sdf.parse(dateString)
            val day = date.date
            val dayWithSuffix = getDayNumberSuffix(day)
            sdf = SimpleDateFormat(" dd'$dayWithSuffix' MMMM yyyy")
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    fun parseDateToddMMyyyy(time: String): String {

        var format = SimpleDateFormat("MMM dd,yyyy");
        var date = format.format(Date.parse(time));

        /* var originalFormat = SimpleDateFormat("MM dd yyyy", Locale.ENGLISH);
         var targetFormat = SimpleDateFormat("dd-MMM-yyyy");
         var date = originalFormat.parse(time);
         var formattedDate = targetFormat.format(date);  // 20120821*/

//        var inputPattern = "dd-MM-yyyy "
//        var outputPattern = "dd-MMM-yyyy"
//        var inputFormat = SimpleDateFormat(inputPattern)
//        var outputFormat = SimpleDateFormat(outputPattern)
//
//        var date: Date? = null
//        var str: String = ""
//
//        try {
//            date = inputFormat.parse(time);
//            str = outputFormat.format(date);
//        } catch (e: ParseException) {
//            e.printStackTrace();
//        }
//        return formattedDate.toString();
        Log.d("NewDate", date)
        return date.toString();
    }

    fun getDayNumberSuffix(day: Int): String {
        if (day >= 11 && day <= 13) {
            return "th"
        }
        when (day % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th"
        }
    }

    fun getNameOfMonth(no: String): String {
        var name = ""
        if (no.equals("1"))
            name = "Jan"
        if (no.equals("2"))
            name = "Feb"
        if (no.equals("3"))
            name = "Mar"
        if (no.equals("4"))
            name = "Apr"
        if (no.equals("5"))
            name = "May"
        if (no.equals("6"))
            name = "Jun"
        if (no.equals("7"))
            name = "Jul"
        if (no.equals("8"))
            name = "Aug"
        if (no.equals("9"))
            name = "Sep"
        if (no.equals("10"))
            name = "Oct"
        if (no.equals("11"))
            name = "Nov"
        if (no.equals("12"))
            name = "Dec"

        return name

    }


    fun watchYoutubeVideo(activity: Activity, youTubeUrl: String) {
        //        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youTubeUrl)));
        if (youTubeUrl != "") {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubeUrl))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubeUrl))
            try {
                activity.startActivity(appIntent)
            } catch (e: Exception) {
                activity.startActivity(webIntent)
            }

        }
    }

    fun itemsPopUp(context: Context, title: String, items: Array<String>): String {
        var selectedItem = ""
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setItems(items) { dialog, item ->
            selectedItem = items[item]

        }
        builder.setCancelable(false).setNegativeButton(
            "CANCEL"
        ) { dialog, id -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
        return selectedItem
    }

    fun getProgressValue(total: Double, current: Double): Double {
        var value = 0.0
        try {
            value = current / total
            value *= 100
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }
     // Code to darken the color supplied (mostly color of toolbar)

}
