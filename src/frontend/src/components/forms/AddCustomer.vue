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
      },
      isEdit: false,
      customerId: null
    };
  },
  mounted() {
    this.prefillForm();
  },
  methods: {
    prefillForm() {
      if (this.$route.query.id) {
        this.isEdit = true;
        this.customerId = this.$route.query.id;
        this.form.name = this.$route.query.name;
        this.form.surname = this.$route.query.surname;
        this.form.email = this.$route.query.email;
        this.form.phoneNumber = this.$route.query.phoneNumber;
      }
    },
    async submitForm() {
      try {
        const url = this.isEdit ? `/api/customer/update/${this.customerId}` : '/api/customer/insertBody';
        const method = this.isEdit ? 'POST' : 'POST';

        const response = await fetch(url, {
          method: method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.form)
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        alert(this.isEdit ? 'Customer updated successfully!' : 'Customer added successfully!');
        this.$router.push('/Customer');
      } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
        alert('There has been a problem with your fetch operation: ' + error.message);
      }
    },
  }
};
</script>

<style src="@/assets/forms.css"></style>
