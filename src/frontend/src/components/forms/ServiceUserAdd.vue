<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edytuj użytkownika' : 'Dodaj nowego użytkownika usługi' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="name">Imię:</label>
        <input type="text" id="name" v-model="form.name" required />
      </div>
      <div>
        <label for="surname">Nazwisko:</label>
        <input type="text" id="surname" v-model="form.surname" required />
      </div>
      <div>
        <label>Emaile:</label>
        <div v-for="(email, index) in form.emails" :key="index">
          <input type="email" v-model="form.emails[index]" placeholder="Wpisz email" required />
          <button type="button" @click="removeEmail(index)">Usuń</button>
        </div>
        <button type="button" @click="addEmail">Dodaj nowy email</button>
      </div>
      <div>
        <label>Numery telefonów: (max 16 znaków)</label>
        <div v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <input type="tel" v-model="form.phoneNumbers[index]" placeholder="Wpisz numer telefonu" required />
          <button type="button" @click="removePhoneNumber(index)">Usuń</button>
        </div>
        <button type="button" @click="addPhoneNumber">Dodaj nowy telefon</button>
      </div>
      <div>
        <label for="hasPolishPESEL">Polski PESEL?:</label>
        <select id="hasPolishPESEL" v-model="form.hasPolishPESEL">
          <option :value="true">Tak</option>
          <option :value="false">Nie</option>
        </select>
      </div>
      <div v-if="form.hasPolishPESEL">
        <label for="taxIdentificationNumber">PESEL (dokładnie 11 znaków):</label>
        <input type="text" id="taxIdentificationNumber" v-model="form.taxId" />
      </div>
      <div>
        <label for="comments">Dodatkowy opis:</label>
        <input type="text" id="comments" v-model="form.comments" />
      </div>
      <button type="submit">Zapisz</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>
</template>

<script>
import { getCookie,fetchWrapper } from '@/utility';
export default {
  name: 'UserForm',
  data() {
    return {
      formMode: 'add',
      form: {
        idServiceUser: null,
        name: '',
        surname: '',
        emails: [''],
        phoneNumbers: [''],
        hasPolishPESEL: false,
        taxId: '',
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
        
        this.form.taxId = null;
      }
    }
  },
  methods: {
    fetchUser() {
      fetchWrapper(this,`/api/serviceUser/get/${this.$route.query.idServiceUser}`)
          .then(response => response.json())
          .then(user => {
            this.form.idServiceUser = user.idServiceUser || null;
            this.form.name = user.name || '';
            this.form.surname = user.surname || '';
            this.form.hasPolishPESEL = user.hasPolishPESEL !== undefined ? user.hasPolishPESEL : false;
            this.form.taxId = user.taxId || null;
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
      const payload = {
        name: this.form.name,
        surname: this.form.surname,
        hasPolishPESEL: this.form.hasPolishPESEL ? 1 : 0,
        comments: this.form.comments,
        emails: this.form.emails,
        phoneNumbers: this.form.phoneNumbers,
        taxId: this.form.hasPolishPESEL ? this.form.taxId : null
      };
      
      const url = this.formMode === 'add'
          ? '/api/serviceUser/insertBody'
          : `/api/serviceUser/update/${this.form.idServiceUser}`;

      const cookie = getCookie("XSRF-TOKEN");
      // console.log("Fetched cookie:");
      // console.log(document.cookie);
      // console.log(cookie);

    
      fetchWrapper(this,url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN':cookie
        },
        body: JSON.stringify(payload)
      })
          .then(response => response.text())
          .then(() => {
            this.$router.push('/ServiceUser');
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
