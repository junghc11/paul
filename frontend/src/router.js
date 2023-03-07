
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import OrderManager from "./components/listers/OrderCards"
import OrderDetail from "./components/listers/OrderDetail"

import PaymentManager from "./components/listers/PaymentCards"
import PaymentDetail from "./components/listers/PaymentDetail"

import DeliveryManager from "./components/listers/DeliveryCards"
import DeliveryDetail from "./components/listers/DeliveryDetail"

import CafeOrderManager from "./components/listers/CafeOrderCards"
import CafeOrderDetail from "./components/listers/CafeOrderDetail"


import MyPageView from "./components/MyPageView"
import MyPageViewDetail from "./components/MyPageViewDetail"

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/orders',
                name: 'OrderManager',
                component: OrderManager
            },
            {
                path: '/orders/:id',
                name: 'OrderDetail',
                component: OrderDetail
            },

            {
                path: '/payments',
                name: 'PaymentManager',
                component: PaymentManager
            },
            {
                path: '/payments/:id',
                name: 'PaymentDetail',
                component: PaymentDetail
            },

            {
                path: '/deliveries',
                name: 'DeliveryManager',
                component: DeliveryManager
            },
            {
                path: '/deliveries/:id',
                name: 'DeliveryDetail',
                component: DeliveryDetail
            },

            {
                path: '/cafeOrders',
                name: 'CafeOrderManager',
                component: CafeOrderManager
            },
            {
                path: '/cafeOrders/:id',
                name: 'CafeOrderDetail',
                component: CafeOrderDetail
            },


            {
                path: '/myPages',
                name: 'MyPageView',
                component: MyPageView
            },
            {
                path: '/myPages/:id',
                name: 'MyPageViewDetail',
                component: MyPageViewDetail
            },


    ]
})
