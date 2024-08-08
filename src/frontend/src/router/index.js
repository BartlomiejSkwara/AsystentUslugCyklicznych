import { createMemoryHistory, createRouter } from 'vue-router'
import ProductList from '../components/ProductList.vue'
import HomePage from '../components/Home.vue'
import CustomerList from "@/components/Customer.vue";

const routes = [
    {
        path: '/',
        name: 'HomePage',
        component: HomePage
    },
    {
        path: '/Products',
        name: 'ProductList',
        component: ProductList
    },
    {
        path: '/Customer',
        name: 'CustomerList',
        component: CustomerList
    },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router

