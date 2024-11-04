<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edytuj dane firmy' : 'Dodaj nową firmę' }}</h1>
    <form @submit.prevent="submitForm" :style="styleModifier">
      <div>
        <label for="name">Nazwa: <span class="text-danger">*</span></label>
        <input type="text" id="name" v-model="form.name" class="form-control" required>
      </div>
      <div>
        <label for="locality">Miejscowość: <span class="text-danger">*</span></label>
        <input type="text" id="locality" v-model="form.locality" class="form-control" required>
      </div>
      <div>
        <label for="postalCode">Kod pocztowy: <span class="text-danger">*</span></label>
        <input type="text" id="postalCode" v-model="form.postalCode" class="form-control" required>
      </div>
      <div>
        <label for="street">Ulica: <span class="text-danger">*</span></label>
        <input type="text" id="street" v-model="form.street" class="form-control" required>
      </div>
      <div>
        <label for="propertyNumber">Numer posesji: <span class="text-danger">*</span></label>
        <input type="text" id="propertyNumber" v-model="form.propertyNumber" class="form-control" required>
      </div>
      <div>
        <label for="apartmentNumber">Numer lokalu: <span class="text-danger">*</span></label>
        <input type="text" id="apartmentNumber" v-model="form.apartmentNumber" class="form-control" required>
      </div>
      <div>
        <label for="nip">NIP: <span class="text-danger">*</span></label>
        <input type="text" id="nip" v-model="form.nip" class="form-control" required>
      </div>
      <div>
        <label for="regon">REGON: </label>
        <input type="text" id="regon" v-model="form.regon" class="form-control" >
      </div>
      <div>
        <label for="commentsBusiness">Opis:</label>
        <input type="text" id="commentsBusiness" v-model="form.comments" class="form-control" >
      </div>
      <div>
        <label>Emaile: <span class="text-danger">*</span></label>
        <div class="input_button_place" v-for="(email, index) in form.emails" :key="index">
          <input type="text" v-model="form.emails[index]" placeholder="Wpisz email" class="form-control input_size" >
          <button type="button" @click="removeEmail(index)">Usuń</button>
        </div>
        <button type="button" class="button_add_contact" @click="addEmail">Dodaj nowy email</button>
      </div>
      <div>
        <label>Numer telefonu: (max 16 cyfr) <span class="text-danger">*</span></label>
        <div class="input_button_place" v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <input type="text" v-model="form.phoneNumbers[index]" placeholder="Wpisz numer telefonu" class="form-control" >
          <button type="button" @click="removePhoneNumber(index)">Usuń</button>
        </div>
        <button type="button" class="button_add_contact" @click="addPhoneNumber">Dodaj nowy numer telefonu</button>


      </div>
      <div class="form-check form-switch">
        <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" v-model="ignoreDup" >
        <label class="form-check-label" for="flexSwitchCheckDefault">Ignoruj Duplikaty Danych Kontaktowych</label>
      </div>
      <p class="text-danger" style="font-size: 0.9em">* pozycje obowiązkowe</p>
      <p class="text-danger">{{ errorMessage }}</p>

      <button v-if="standalone" type="submit">Zapisz</button>
      <button v-if="standalone" type="button" style="float: right" @click="handleGoBack">Powrót</button>
      <br>
    </form>
  </div>

  <div v-if="showRequestModal" id="requestModal" class="modal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Czy na pewno chcesz opuścić formularz?</h5>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="switchRequestModalVisibility">Kontynuuj</button>
          <button type="button" class="btn btn-danger" @click="goBack">Powrót</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { fetchWrapper, getCookie, refreshCSRF } from '@/utility';
export default {
  name: 'BusinessForm',
  props:{
    standalone:{
      type: Boolean,
      default: true,
    }
  },
  data() {
    return {
      formMode: 'add', // 'edit' for editing existing business
      form: {
        idBusiness: null,
        name: '',
        locality: '',
        postalCode: '',
        street: '',
        propertyNumber: '',
        apartmentNumber: '',
        nip: '',
        regon: '',
        comments: '',
        emails: [''], // Initialize with one empty field
        phoneNumbers: [''] // Initialize with one empty field
      },
      errorMessage: "",
      ignoreDup: false,
      showRequestModal: false,
      // showConfirmation: false
    };
  },
  computed:{
    styleModifier(){
      return !this.standalone? {width: "100%"}:{};
    },
    isFormFilled() {
      return Object.values(this.form).some(value =>
          Array.isArray(value) ? value.some(v => v) : value
      );
    }
  },
  mounted() {
    refreshCSRF();
    if (this.$route.query.idBusiness) {
      this.formMode = 'edit';
      this.fetchBusiness();
    }
  },
  methods: {
    handleGoBack() {
      console.log(this.isFormFilled)
      if (this.isFormFilled) {
        this.showRequestModal = true;
      } else{
        this.goBack();
      }
    },
    switchRequestModalVisibility() {
      this.showRequestModal = !this.showRequestModal;
      console.log(this.showRequestModal)
    },
    fetchBusiness() {
      fetchWrapper(this,`/api/business/get/${this.$route.query.idBusiness}`)
          .then(response => response.json())
          .then(business => {
            this.form.idBusiness = business.idBusiness;
            this.form.name = business.name;
            this.form.locality = business.address.locality;
            this.form.postalCode = business.address.postalCode;
            this.form.street = business.address.street;
            this.form.propertyNumber = business.address.propertyNumber;
            this.form.apartmentNumber = business.address.apartmentNumber;
            this.form.nip = business.nip;
            this.form.regon = business.regon || '';
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
        locality: this.form.locality,
        postalCode: this.form.postalCode,
        street: this.form.street,
        propertyNumber: this.form.propertyNumber,
        apartmentNumber: this.form.apartmentNumber,
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
          if(this.standalone){
            this.$router.push('/Business'); 
          }
          else{
            return this.form.name;            
          }
        }
      
      } catch (error) {
        console.error('Error saving business:', error);
      }
      
      return null;   
    },
    goBack() {
      this.$router.push('/Business');
    }
  }
};
</script>



<style src="@/assets/style.css"></style>