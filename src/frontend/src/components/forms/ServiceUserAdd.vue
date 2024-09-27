<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edit User' : 'Add New User' }}</h1>
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
        <label>Emails:</label>
        <div v-for="(email, index) in form.emails" :key="index">
          <input type="email" v-model="form.emails[index]" placeholder="Enter email" required />
          <button type="button" @click="removeEmail(index)">Remove</button>
        </div>
        <button type="button" @click="addEmail">Add Email</button>
      </div>
      <div>
        <label>Phone Numbers: (max 16 digits)</label>
        <div v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <input type="tel" v-model="form.phoneNumbers[index]" placeholder="Enter phone number" required />
          <button type="button" @click="removePhoneNumber(index)">Remove</button>
        </div>
        <button type="button" @click="addPhoneNumber">Add Phone Number</button>
      </div>
      <div>
        <label for="hasPolishPESEL">Has Polish PESEL:</label>
        <select id="hasPolishPESEL" v-model="form.hasPolishPESEL">
          <option :value="true">Yes</option>
          <option :value="false">No</option>
        </select>
      </div>
      <div v-if="form.hasPolishPESEL">
        <label for="taxIdentificationNumber">Tax ID (exactly 11 digits):</label>
        <input type="text" id="taxIdentificationNumber" v-model="form.taxId" />
      </div>
      <div>
        <label for="comments">Comments:</label>
        <input type="text" id="comments" v-model="form.comments" />
      </div>
      <button type="submit">Save</button>
      <button type="button" @click="goBack">Back</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'UserForm',
  data() {
    return {
      formMode: 'add', // 'edit' for editing existing user
      form: {
        idServiceUser: null,
        name: '',
        surname: '',
        emails: [''], // Initialize with one empty field
        phoneNumbers: [''], // Initialize with one empty field
        hasPolishPESEL: false, // Default value for select dropdown (boolean)
        taxId: '', // Initialize as empty string for consistency
        comments: ''
      }
    };
  },
  mounted() {
    if (this.$route.query.idServiceUser) {
      this.formMode = 'edit';
      this.fetchUser();
    }
  },
  watch: {
    'form.hasPolishPESEL': function(newValue) {
      if (!newValue) {
        this.form.taxId = null; // Set taxId to null if hasPolishPESEL is set to false
      }
    }
  },
  methods: {
    fetchUser() {
      fetch(`/api/serviceUser/get/${this.$route.query.idServiceUser}`)
          .then(response => response.json())
          .then(user => {
            this.form.idServiceUser = user.idServiceUser || null;
            this.form.name = user.name || '';
            this.form.surname = user.surname || '';
            this.form.hasPolishPESEL = user.hasPolishPESEL !== undefined ? user.hasPolishPESEL : false;
            this.form.taxId = user.taxId || null; // Ensure taxId is properly handled as null or actual value
            this.form.comments = user.comments || '';

            if (user.contactData) {
              this.form.emails = user.contactData.emails.map(email => email.email) || [''];
              this.form.phoneNumbers = user.contactData.phoneNumbers.map(phone => phone.number) || [''];
            } else {
              this.form.emails = [''];
              this.form.phoneNumbers = [''];
            }
          })
          .catch(error => {
            console.error('Error fetching user data:', error);
          });
    },
    addEmail() {
      this.form.emails.push('');
    },
    removeEmail(index) {
      if (this.form.emails.length > 1) {
        this.form.emails.splice(index, 1);
      }
    },
    addPhoneNumber() {
      this.form.phoneNumbers.push('');
    },
    removePhoneNumber(index) {
      if (this.form.phoneNumbers.length > 1) {
        this.form.phoneNumbers.splice(index, 1);
      }
    },
    submitForm() {
      // Prepare the payload
      const payload = {
        name: this.form.name,
        surname: this.form.surname,
        hasPolishPESEL: this.form.hasPolishPESEL ? 1 : 0, // Convert boolean to bit (1/0)
        comments: this.form.comments,
        emails: this.form.emails,
        phoneNumbers: this.form.phoneNumbers,
        taxId: this.form.hasPolishPESEL ? this.form.taxId : null // Ensure taxId is null if hasPolishPESEL is false
      };

      const url = this.formMode === 'add'
          ? '/api/serviceUser/insertBody'
          : `/api/serviceUser/update/${this.form.idServiceUser}`;

      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })
          .then(response => response.text())
          .then(() => {
            this.$router.push('/ServiceUser'); // Redirect to user list
          })
          .catch(error => {
            console.error('Error saving user:', error);
          });
    },
    goBack() {
      this.$router.push('/ServiceUser');
    }
  }
};
</script>

<style src="@/assets/forms.css"></style>
