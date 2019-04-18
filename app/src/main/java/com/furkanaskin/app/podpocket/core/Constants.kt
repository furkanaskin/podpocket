package com.furkanaskin.app.podpocket.core

/**
 * Created by Furkan on 14.04.2019
 */

object Constants {
    object IntentName {
        val LOGIN_ACTIVITY_TYPE = "login_activity_type"
        val SUBSCRIPTION_CREATED = "subscription_created"
        val ADDRESS_ITEM = "address_item"
        val SUBSCRIPTION_ID = "subscription_id"
        val EMAIL = "email"
        val CategoryEntity = "CategoryEntity"
        val CategoryProduct = "CategoryProduct"
        val ProductId = "ProductId"
        val Plan = "Plan"
    }

    object LoginActivityType {
        val LOGIN_TYPE = 0
        val REGISTER_TYPE = 1
    }

    object NetworkService {
        val BASE_URL = "https://listen-api.listennotes.com/api/v2/"
        val ListenAPI_KEY = "82e6628b74404fb9a26a934b7d1adfa0"
    }
}