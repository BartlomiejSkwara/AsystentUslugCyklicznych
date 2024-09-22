<template>
  <div class="login-container">
    <div class="form-container">
      <div class="button-group">
        <button @click="toggleForm('login')" :class="{ active: formType === 'login' }">Login</button>
        <button @click="toggleForm('register')" :class="{ active: formType === 'register' }">Register</button>
      </div>
      <form v-if="formType === 'login'" @submit.prevent="submitLogin">
        <h2>Login</h2>
        <div>
          <label for="loginUsername">Username:</label>
          <input type="text" id="loginUsername" v-model="loginForm.username" required>
        </div>
        <div>
          <label for="loginPassword">Password:</label>
          <input type="password" id="loginPassword" v-model="loginForm.password" required>
        </div>
        <button type="submit">Login</button>
      </form>
      <form v-if="formType === 'register'" @submit.prevent="submitRegister">
        <h2>Register</h2>
        <div>
          <label for="registerUsername">Username:</label>
          <input type="text" id="registerUsername" v-model="registerForm.username" required>
        </div>
        <div>
          <label for="registerPassword">Password:</label>
          <input type="password" id="registerPassword" v-model="registerForm.password" required>
        </div>
        <div>
          <label for="registerConfirmPassword">Confirm Password:</label>
          <input type="password" id="registerConfirmPassword" v-model="registerForm.confirmPassword" required>
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginService',
  data() {
    return {
      formType: this.$route.path === '/login' ? 'login' : 'register',
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      }
    };
  },
  methods: {
    toggleForm(type) {
      this.formType = type;
      this.$router.push(`/${type}`);
    },
    submitLogin() {
      const payload = {
        email: this.loginForm.email,
        password: this.loginForm.password
      };
      fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })
          .then(response => response.json())
          .then(data => {
            console.log('Login successful', data);
          })
          .catch(error => {
            console.error('Login error:', error);
          });
    },
    submitRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        alert('Passwords do not match');
        return;
      }
      const payload = {
        email: this.registerForm.email,
        password: this.registerForm.password
      };
      fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })
          .then(response => response.json())
          .then(data => {
            console.log('Registration successful', data);
          })
          .catch(error => {
            console.error('Registration error:', error);
          });
    }
  }
};
</script>

<style scoped>
form {
  width: 100%;
}
</style>
<style src="@/assets/forms.css"></style>
