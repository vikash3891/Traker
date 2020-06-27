package com.home.traker.navigators

interface OnItemSelectionListener {
    fun onItemClickListener(position: Int)
    fun getPosition(position: Int, isForTeamA: Boolean)
}