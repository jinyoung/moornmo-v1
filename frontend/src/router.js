
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import EmployeeManager from "./components/listers/EmployeeCards"
import EmployeeDetail from "./components/listers/EmployeeDetail"

import OrderManager from "./components/listers/OrderCards"
import OrderDetail from "./components/listers/OrderDetail"

import RegisterDeptManager from "./components/listers/RegisterDeptCards"
import RegisterDeptDetail from "./components/listers/RegisterDeptDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/employees',
                name: 'EmployeeManager',
                component: EmployeeManager
            },
            {
                path: '/employees/:id',
                name: 'EmployeeDetail',
                component: EmployeeDetail
            },

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
                path: '/registerDepts',
                name: 'RegisterDeptManager',
                component: RegisterDeptManager
            },
            {
                path: '/registerDepts/:id',
                name: 'RegisterDeptDetail',
                component: RegisterDeptDetail
            },



    ]
})
