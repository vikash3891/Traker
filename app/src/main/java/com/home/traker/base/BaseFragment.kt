package com.home.traker.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

public abstract class BaseFragment : Fragment() {


    private var mActivity: BaseActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view)
    }

    /*override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.mActivity = activity
            activity!!.onFragmentAttached()
        }
    }*/

    protected abstract fun setUp(view: View)

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    /*fun swapFragment(fragment: Fragment, tag: String) {
        try {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.content_frame, fragment, tag)
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE)
            //        fragmentTransaction.addToBackStack(null);
            //        fragmentTransaction.commit();

            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }*/


//    fun beginAddtoBackStackFragment(fragment: Fragment, tag: String) {
//        try {
//            val fragmentTransaction = fragmentManager!!.beginTransaction()
//            fragmentTransaction.replace(R.id.content_frame, fragment, tag)
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE)
//            fragmentTransaction.addToBackStack(tag)
//            //        fragmentTransaction.commit();
//
//            fragmentTransaction.commitAllowingStateLoss()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

    fun hideSystemUI() {
        mActivity!!.getWindow().decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}