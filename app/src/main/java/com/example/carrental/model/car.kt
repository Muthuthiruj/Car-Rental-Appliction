package com.example.carrental.model

import android.os.Parcel
import android.os.Parcelable

class Car(
    val name: String?,
    val category: String?,
    val price: Int,
    val image: Int, // Drawable resource ID
    val acceleration: String?,
    val range: String?,
    val seats: String?,
    val camerasAndSensors: String?,
    val battery: String?,
    val autopilot: String?,
    val aboutimage: Int
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeInt(price)
        parcel.writeInt(image)
        parcel.writeInt(aboutimage)
        parcel.writeString(acceleration)
        parcel.writeString(range)
        parcel.writeString(seats)
        parcel.writeString(camerasAndSensors)
        parcel.writeString(battery)
        parcel.writeString(autopilot)
        parcel.writeInt(aboutimage)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car = Car(parcel)
        override fun newArray(size: Int): Array<Car?> = arrayOfNulls(size)
    }
}
