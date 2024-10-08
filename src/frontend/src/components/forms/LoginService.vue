<template>
  <div class="login-container">
    <div class="form-container">
      <div class="button-group">
        <button @click="toggleForm('login')" :class="{ active: formType === 'login' }">Login</button>
        <button @click="toggleForm('register')" :class="{ active: formType === 'register' }">Register</button>
      </div>
      <form v-if="formType === 'login'" @submit.prevent="submitLogin">
        <h2>Zaloguj</h2>
        <div>
          <label for="loginUsername">Login:</label>
          <input type="text" id="loginUsername" v-model="loginForm.login" required>
        </div>
        <div>
          <label for="loginPassword">Hasło:</label>
          <input type="password" id="loginPassword" v-model="loginForm.password" required>
        </div>
        <button type="submit">Zaloguj</button>
      </form>
      <form v-if="formType === 'register'" @submit.prevent="submitRegister">
        <h2>Zarejestruj</h2>
        <div>
          <label for="registerLogin">Login:</label>
          <input type="text" id="registerLogin" v-model="registerForm.login" required>
        </div>
        <div>
          <label for="registerPassword">Hasło:</label>
          <input type="password" id="registerPassword" v-model="registerForm.password" required>
        </div>
        <div>
          <label for="registerConfirmPassword">Powtórz hasło:</label>
          <input type="password" id="registerConfirmPassword" v-model="registerForm.confirmPassword" required>
        </div>
        <button type="submit">Zarejestruj się</button>
      </form>
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
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
        login: '',
        password: ''
      },
      registerForm: {
        login: '',
        password: '',
        confirmPassword: ''
      },
      errorMessage: null
    };
  },
  methods: {
    toggleForm(type) {
      this.formType = type;
      this.$router.push(`/${type}`);
    },
    async submitLogin() {
      try {
        const response = await fetch('/api/authentication/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.loginForm)
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Network response was not ok: ${errorText}`);
        }

        const result = await response.text();
        console.log(result);

        const role = response.headers.get('frontRole');
        const username = response.headers.get('username'); // Pobranie username z nagłówka
        console.log("Login service sets", role, username);
        
        this.$store.commit('setUsername', username);
        this.$store.commit('setRole', role);

        // eventBus.emit('roleUpdate', role); // Emitowanie roli i nazwy użytkownika
        // eventBus.emit('usernameUpdate', username );
        //eventBus.emit('roleBlock', role );

        this.$router.push('/Cycles');
      } catch (error) {
        console.error('Login error:', error);
        this.errorMessage = 'Złe dane logowania. Spróbuj ponownie.';
      }
    },
    async submitRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        alert('Passwords do not match');
        return;
      }
      try {
        const response = await fetch('/api/authentication/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            login: this.registerForm.login,
            password: this.registerForm.password
          })
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Network response was not ok: ${errorText}`);
        }

        const result = await response.json();
        console.log('Registration successful', result);
        // Optional: Redirect to login page after successful registration
        this.$router.push('/login');
      } catch (error) {
        console.error('Registration error:', error);
        this.errorMessage = 'Registration failed. Please try again.';
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.button-group {
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-bottom: 20px;
}

.button-group button {
  flex: 1;
  padding: 10px;
  cursor: pointer;
}

.button-group button.active {
  background-color: #007bff;
  color: white;
}

form {
  width: 100%;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.error-message {
  color: red;
  margin-top: 15px;
}
</style>
