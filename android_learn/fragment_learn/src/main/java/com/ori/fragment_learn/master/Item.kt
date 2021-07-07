package com.ori.fragment_learn.master

import java.io.Serializable
import java.util.*

class Item(val title: String, val body: String) : Serializable {

    override fun toString(): String {
        return title
    }

    companion object {
        private const val serialVersionUID = -6099312954099962806L
        val items: ArrayList<Item>
            get() {
                val items = ArrayList<Item>()
                items.add(Item("Item 1", "This is the first item"))
                items.add(Item("Item 2", "This is the second item"))
                items.add(Item("Item 3", "This is the third item"))
                return items
            }
    }

}