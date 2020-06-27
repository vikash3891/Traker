package com.home.traker.base

import java.util.*

open class BaseViewModel<N> : Observable() {
    var mNavigator: N? = null
}