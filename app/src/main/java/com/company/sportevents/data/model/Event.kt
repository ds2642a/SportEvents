package com.company.sportevents.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event (
    var imageName: String,
    var name: String,
    var date: String,
    var text: String
) : Parcelable