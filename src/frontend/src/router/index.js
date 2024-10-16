import { createMemoryHistory, createRouter } from 'vue-router'
import HomePage from '../components/views/Home.vue'
// database pages
import ServiceUserList from "../components/sub_pages/ServiceUser.vue"
import CyclesList from "../components/sub_pages/CyclicalService.vue";
import BusinessService from "../components/sub_pages/BusinessService.vue";
import CertificateList from "../components/sub_pages/CertificateList.vue";
import Accounts from "../components/sub_pages/Accounts.vue";
import CyclicalServiceDetails from '../components/sub_pages/CyclicalServiceDetails.vue';
// forms
import AddCycle from '../components/forms/AddCycle.vue';
import ServiceUserAdd from '../components/forms/ServiceUserAdd.vue';
import AddBusiness from '../components/forms/AddBusiness.vue';
import AddCertificate from "../components/forms/AddCertificate.vue";
import UploadCertificate from "../components/forms/UploadCertificate.vue";
import LoginComponent from "../components/forms/LoginService.vue";
import RenewCycle from "../components/forms/RenewCycle.vue";

const routes = [
    {
        path: '/',
        name: 'HomePage',
        component: HomePage
    },
    // database pages
    {
        path: '/ServiceUser',
        name: 'ServiceUserList',
        component: ServiceUserList
    },
    {
        path: '/Cycles',
        name: 'CyclesList',
        component: CyclesList
    },
    {
        path: '/Business',
        name: 'BusinessService',
        component: BusinessService
    },
    {
        path: '/Accounts',
        name: 'AccountsList',
        component: Accounts
    },
    {
        path: '/certificates',
        name: 'CertificateList',
        component: CertificateList
    },
    // forms
    {
        path: '/add-cycle',
        name: AddCycle,
        component: AddCycle
    },
    {
        path: '/add-user',
        name: ServiceUserAdd,
        component: ServiceUserAdd
    },
    {
        path: '/add-business',
        name: AddBusiness,
        component: AddBusiness
    },
    {
        path: '/add-certificate',
        name: AddCertificate,
        component: AddCertificate
    },
    {
        path: '/upload-certificate',
        name: UploadCertificate,
        component: UploadCertificate
    },
    {
        path: '/login',
        name: 'Login',
        component: LoginComponent
    },
    {
        path: '/register',
        name: 'Register',
        component: LoginComponent
    },
    {
        path: '/renew-cycle/:id',
        name: 'RenewCycle',
        component: RenewCycle,
    },
    {
        path: '/cycleDetails',
        name: 'CyclicalServiceDetails',
        component: CyclicalServiceDetails,
        props: true

    }



]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router

