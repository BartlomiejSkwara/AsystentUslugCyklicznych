<template>
  <div>
    <h1>Add New Dysponent</h1>
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
        <label for="mfnSerialNumber">MFN Serial Number:</label>
        <input type="text" id="mfn" v-model="form.mfnSerialNumber" required />
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
  name: 'AddDysponent',
  data() {
    return {
      form: {
        name: '',
        surname: '',
        mfnSerialNumber: '',
        email: '',
        phoneNumber: ''
      }
    };
  },
  methods: {
    submitForm() {
      const newDysponent = {
        name: this.form.name,
        surname: this.form.surname,
        mfnSerialNumber: this.form.mfnSerialNumber,
        email: this.form.email,
        phoneNumber: this.form.phoneNumber
      };

      fetch('/api/dysponent/insertBody', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newDysponent)
      })
          .then(response => {
            if (!response.ok) {
              console.error('Server response:', response); // Debugging
              throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
          })
          .then(() => {
            alert('Dysponent added successfully!');
            this.$router.push('/Dysponents'); // Redirect to the main page after successful submission
          })
          .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
          });
    }
  }
};
</script>

<style src="@/assets/forms.css"></style>
