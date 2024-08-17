<template>
  <div>
    <h1>Add New Customer</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="name">Name:</label>
        <input type="text" id="name" v-model="form.name" required />
      </div>
      <div>
        <label for="surname">Surname:</label>
        <input type="text" id="surname" v-model="form.surname" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="form.email" required />
      </div>
      <div>
        <label for="phoneNumber">Phone Number:</label>
        <input type="tel" id="phoneNumber" v-model="form.phoneNumber" pattern="[0-9]{0,12}" required />
      </div>
      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'AddCustomer',
  data() {
    return {
      form: {
        name: '',
        surname: '',
        email: '',
        phoneNumber: ''
      }
    };
  },
  methods: {
    submitForm() {
      const newCustomer = {
        email: this.form.email,
        name: this.form.name,
        phoneNumber: this.form.phoneNumber,
        surname: this.form.surname
      };

      fetch("/api/customer/insertBody", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newCustomer)
      })
          .then(response => {
            if (!response.ok) {
              if (response.status === 409) {
                throw new Error('Conflict: Customer already exists or data conflict.');
              } else {
                throw new Error('Network response was not ok: ' + response.statusText);
              }
            }
            return response.text();
          })
          .then(() => {
            alert("Customer added successfully!");
            this.$router.push('/Customer');
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
  }
};
</script>

<style src="@/assets/forms.css"></style>
