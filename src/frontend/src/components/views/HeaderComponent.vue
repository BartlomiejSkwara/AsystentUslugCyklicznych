<template>
  <header class="d-flex align-items-center pb-3 mb-5 border-bottom">
    <div class="ms-auto">
      <span style="font-size: large; margin-right: 20px" v-if="isLoggedIn" class="me-3">Witaj, {{ username }}!</span>
    </div>
    <div class="ms-3">
      <router-link v-if="!isLoggedIn" to="/login" class="btn btn-primary me-2">Zaloguj</router-link>
      <router-link v-if="!isLoggedIn" to="/register" class="btn btn-secondary">Zarejestruj się</router-link>
      <button v-if="isLoggedIn" @click="logout" class="btn btn-danger">Wyloguj się</button>
    </div>
  </header>
</template>

<script>
import { eventBus } from '@/eventBus.js';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      isLoggedIn: this.checkLoginStatus(),
      username: '',
      userRole: ''
    };
  },
  created() {
    eventBus.on('roleUpdate', this.updateUserRole);
    eventBus.on('usernameUpdate', this.updateUsername);
  },
  methods: {
    checkLoginStatus() {
      return document.cookie.split(';').some((item) => item.trim().startsWith('XSRF-TOKEN='));
    },
    getRole() {
      fetch('/api/authentication/login', {
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
      fetch('/api/authentication/logout', {
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
              this.userRole = 'ROLE_nobody';
              this.username = ''; // Resetowanie username po wylogowaniu
              eventBus.emit('roleUpdate', this.userRole); // Wyczyszczenie roli przy wylogowaniu
              eventBus.emit('usernameUpdate', this.username); // Wyczyszczenie username przy wylogowaniu
              this.$router.push('/login');
            } else {
              throw new Error('Logout failed');
            }
          })
          .catch(error => {
            console.error('Logout error:', error);
          });
    },
    updateUserRole(role) {
      this.userRole = role;
    },
    updateUsername(username) {
      this.username = username;
    }
  },
  watch: {
    $route() {
      this.isLoggedIn = this.checkLoginStatus();
      document.cookie = 'XSRF-TOKEN=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    }
  }
};
</script>
