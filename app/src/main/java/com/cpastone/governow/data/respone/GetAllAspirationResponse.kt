package com.cpastone.governow.data.respone

import com.cpastone.governow.data.model.Aspiration

data class GetAllAspirationResponse(
    val message: String,
    val data: List<Aspiration>
)
