import { createApp } from 'vue'
import { createStore } from "vuex"
import App from './App.vue'
import router from './router'
import "bootstrap/dist/css/bootstrap.css"
import "bootstrap/dist/js/bootstrap.js"    

const store = createStore({
    state () {
      return {
        username: '',
        role: "ROLE_nobody",
        passedValue: ''
      }
    },
    mutations: {
        setUsername(state,username){
            state.username = username;
        },
        setRole(state,role){
            state.role = role;
        },
        setPassedValue(state,passedValue){
            state.passedValue = passedValue;
        }
    }

})
  

createApp(App)
    .use(router)
    .use(store)
    .mount('#app')


