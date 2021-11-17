package com.ori.server

import android.os.Parcel
import android.os.Parcelable

class Person() : Parcelable {
    var name = ""
    var age = 0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        age = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}