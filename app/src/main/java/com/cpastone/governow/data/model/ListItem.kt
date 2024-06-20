package com.cpastone.governow.data.model

sealed class ListItem {
    data class PollItem(val title: String, val description: String, val options: List<String>) : ListItem()
    data class TextItem(
        val avatarResId: Int,
        val name: String,
        val username: String,
        val imageResId: Int
    ) : ListItem()
}

