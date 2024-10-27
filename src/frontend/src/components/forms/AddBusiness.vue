<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edytuj dane firmy' : 'Dodaj nową firmę' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="name">Nazwa:</label>
        <input type="text" id="name" v-model="form.name" required>
      </div>
      <div>
        <label for="address">Adres:</label>
        <input type="text" id="address" v-model="form.adres" required>
      </div>
      <div>
        <label for="nip">NIP:</label>
        <input type="text" id="nip" v-model="form.nip" required>
      </div>
      <div>
        <label for="regon">REGON:</label>
        <input type="text" id="regon" v-model="form.regon" required>
      </div>
      <div>
        <label for="comments">Opis:</label>
        <input type="text" id="comments" v-model="form.comments">
      </div>
      <div>
        <label>Emaile:</label>
        <div v-for="(email, index) in form.emails" :key="index">
          <input type="text" v-model="form.emails[index]" placeholder="Wpisz email">
          <button type="button" @click="removeEmail(index)">Usuń</button>
        </div>
        <button type="button" @click="addEmail">Dodaj nowy email</button>
      </div>
      <div>
        <label>Numer telefonu: (max 16 cyfr)</label>
        <div v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <input type="text" v-model="form.phoneNumbers[index]" placeholder="Wpisz numer telefonu">
          <button type="button" @click="removePhoneNumber(index)">Usuń</button>
        </div>
        <button type="button" @click="addPhoneNumber">Dodaj nowy numer telefonu</button>
      
      
      </div>
      <div class="form-check form-switch">
        <label class="form-check-label" for="flexSwitchCheckDefault">Ignoruj Duplikaty Danych Kontaktowych</label>
        <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" v-model="ignoreDup">
      </div>
      <p class="text-danger">{{ errorMessage }}</p>

      <button type="submit">Zapisz</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>
</template>


<script>
import { fetchWrapper, getCookie, refreshCSRF } from '@/utility';
export default {
  name: 'BusinessForm',
  data() {
    return {
      formMode: 'add', // 'edit' for editing existing business
      form: {
        idBusiness: null,
        name: '',
        adres: '',
        nip: '',
        regon: '',
        comments: '',
        emails: [''], // Initialize with one empty field
        phoneNumbers: [''] // Initialize with one empty field
      },
      errorMessage: "",
      ignoreDup: false,
    };
  },
  mounted() {
    refreshCSRF();

    if (this.$route.query.idBusiness) {
      this.formMode = 'edit';
      this.fetchBusiness();
    }
  },
  methods: {
    fetchBusiness() {
      fetchWrapper(this,`/api/business/get/${this.$route.query.idBusiness}`)
          .then(response => response.json())
          .then(business => {
            this.form.idBusiness = business.idBusiness;
            this.form.name = business.name;
            this.form.adres = business.adres;
            this.form.nip = business.nip;
            this.form.regon = business.regon;
            this.form.comments = business.comments;

            if (business.contactData) {
              this.form.emails = business.contactData.emails.map(email => email.email);
              this.form.phoneNumbers = business.contactData.phoneNumbers.map(phone => phone.number);
            } else {
              this.form.emails = [''];
              this.form.phoneNumbers = [''];
            }
          })
          .catch(error => {
            console.error('Error fetching business data:', error);
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
    async submitForm() {

        
      const payload = {
        name: this.form.name,
        adres: this.form.adres,
        nip: this.form.nip,
        regon: this.form.regon,
        comments: this.form.comments,
        emails: this.form.emails,
        phoneNumbers: this.form.phoneNumbers
      };

      let url = this.formMode === 'add'
          ? '/api/business/insertBody'
          : `/api/business/update/${this.form.idBusiness}`;


      if(this.ignoreDup)
        url+="?checkForDuplicates=false";
      console.log(url);
      
      const cookie = getCookie("XSRF-TOKEN");
      
      
      try {
        const response = await fetchWrapper(this,url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN':cookie
          },
          body: JSON.stringify(payload)
        })

        // if (!response.ok) {
        //   throw new Error('Network response was not ok ' + response.statusText);
        // }
        if(response.status == 400){
          this.errorMessage = await response.text();
        }else if(response.status == 200){
          this.$router.push('/Business'); 
        }
      
      } catch (error) {
        console.error('Error saving business:', error);
      }
      
         
    },
    goBack() {
      this.$router.push('/Business');
    }
  }
};
</script>



<style src="@/assets/style.css"></style>
