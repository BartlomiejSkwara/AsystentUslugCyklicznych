<template>
  <header class="d-flex align-items-center pb-3 mb-5 border-bottom">
    <div class="ms-auto">
      <span style="font-size: large; margin-right: 20px" v-if="hasRole"  class="me-3">Witaj, {{ this.$store.state.username }}!</span>
    </div>
    <div class="ms-3">
      <router-link v-if="!hasRole" to="/login" class="btn btn-primary me-2">Zaloguj</router-link>
      <button v-if="hasRole" @click="logout" class="btn btn-danger">Wyloguj się</button>
    </div>
  </header>
</template>

<script>
import { fetchWrapper } from '@/utility';

// import { eventBus } from '@/eventBus.js';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      // isLoggedIn: this.checkLoginStatus(),
      // username: '',
      // userRole: ''
    };
  },
  created() {
    // eventBus.on('roleUpdate', this.updateUserRole);
    // eventBus.on('usernameUpdate', this.updateUsername);

  },
  computed:{
    hasRole (){
      console.log(this.$store.state.role);
      
      return this.$store.state.role!='ROLE_nobody'
    }
  },
  methods: {
    // checkLoginStatus() {
    //   return document.cookie.split(';').some((item) => item.trim().startsWith('XSRF-TOKEN='));
    // },
    getRole() {
      fetchWrapper(this,'/api/authentication/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include'
      })
      // .then(response => {
      //   const role = response.headers.get('frontRole');
      //   if (role) {
      //     this.userRole = role;
      //     eventBus.on('roleUpdate', role); // Emitowanie roli przez event bus
      //     console.log("User Role:", role);
      //   } else {
      //     console.error('Role not found in the response headers');
      //   }
      // })
      // .catch(error => {
      //   console.error('Error fetching role:', error);
      // });
    },
    logout() {
      fetchWrapper(this,'/api/authentication/logout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        isLoggedIn: false
      })
          .then(response => {
            if (response.ok) {
              document.cookie = 'XSRF-TOKEN=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
              this.isLoggedIn = false;
              this.$store.commit('setRole', 'ROLE_nobody');
              this.$store.commit('setUsername', '');
              // eventBus.emit('roleUpdate', this.userRole); // Wyczyszczenie roli przy wylogowaniu
              // eventBus.emit('usernameUpdate', this.username); // Wyczyszczenie username przy wylogowaniu

              this.$router.push('/login');
            } 
          })
          .catch(error => {
            console.error('Logout error:', error);
          });
    },
    // updateUserRole(role) {
    //   this.userRole = role;
    //   console.log("Header User role update", this.userRole);

    // },
    // updateUsername(username) {
    //   this.username = username;
    //   console.log("Header User name update", this.username);
    // }
  },
  watch: {
    $route() {
      this.isLoggedIn = this.hasRole;
      document.cookie = 'XSRF-TOKEN=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    }
  }
};
</script>
