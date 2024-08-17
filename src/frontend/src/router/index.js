import { createMemoryHistory, createRouter } from 'vue-router'
import ProductList from '../components/ProductList.vue'
import HomePage from '../components/Home.vue'
import CustomerList from "../components/Customer.vue"
import CyclesList from "../components/CyclicalService.vue";
import DysponentList from "../components/DysponentService.vue";
import AddCycle from '../components/forms/AddCycle.vue';
import AddCustomer from '../components/forms/AddCustomer.vue';
import AddDysponent from '../components/forms/AddDysponent.vue';

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
    {
        path: '/Cycles',
        name: 'CyclesList',
        component: CyclesList
    },
    {
        path: '/Dysponents',
        name: 'DysponentsList',
        component: DysponentList
    },
    {
        path: '/add-cycle',
        name: AddCycle,
        component: AddCycle
    },
    {
        path: '/add-customer',
        name: AddCustomer,
        component: AddCustomer
    },
    {
        path: '/add-dysponent',
        name: AddDysponent,
        component: AddDysponent
    },

]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router

