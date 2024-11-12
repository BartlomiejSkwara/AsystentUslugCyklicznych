<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edytuj dane firmy' : 'Dodaj nową firmę' }}</h1>
    <form @submit.prevent="submitForm" :style="styleModifier">
      <div>
        <label for="name">Nazwa: <span class="text-danger">*</span></label>
        <input type="text" id="name" v-model="form.name" class="form-control" required>
      </div>
      <div>
        <label for="locality">Miejscowość: <span class="text-danger"> *</span></label>
        <input type="text" id="locality" v-model="form.locality" class="form-control" required>
      </div>
      <div>
        <label for="countryCode">Kod kraju:<span class="text-danger"> *</span></label>
        <select id="countryCode" v-model="form.countryCode" @change="setPostalCodePattern" class="form-control">
          <option v-for="country in countries" :key="country.countryCode" :value="country.countryCode">
            {{ country.countryCode }}
          </option>
        </select>

        <label for="postalCode">Kod pocztowy:<span class="text-danger"> *</span></label>
        <input
            type="text"
            id="postalCode"
            v-model="form.postalCode"
            :pattern="postalCodePattern"
            class="form-control"
            required>
      </div>
      <div>
        <label for="street">Ulica: <span class="text-danger">*</span></label>
        <input type="text" id="street" v-model="form.street" class="form-control" required>
      </div>
      <div>
        <label for="propertyNumber">Numer posesji:</label>
        <input type="text" id="propertyNumber" v-model="form.propertyNumber" class="form-control">
      </div>
      <div>
        <label for="apartmentNumber">Numer lokalu:</label>
        <input type="text" id="apartmentNumber" v-model="form.apartmentNumber" class="form-control">
      </div>
      <div>
        <label for="nip">NIP: <span class="text-danger">*</span></label>
        <input type="text" maxlength="10" id="nip" v-model="form.nip" class="form-control" @input="formatNIP" required>
      </div>
      <div>
        <label for="regon">REGON: </label>
        <input type="text" id="regon" v-model="form.regon" class="form-control" >
      </div>
      <div>
        <label for="commentsBusiness">Opis:</label>
        <textarea type="text" id="commentsBusiness" v-model="form.comments" class="form-control" />
      </div>
      <div>
        <label>Emaile:</label>
        <div class="input_button_place" v-for="(email, index) in form.emails" :key="index">
          <input type="text" v-model="form.emails[index]" placeholder="Wpisz email" class="form-control input_size" >
          <button type="button"  class="btn1" @click="removeEmail(index)">Usuń</button>
        </div>
        <button type="button"  class="btn1 button_add_contact" @click="addEmail">Dodaj nowy email</button>
      </div>
      <div>
        <p>Numery telefonów:</p>
        <div class="input_button_place" v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <!-- <input type="text" maxlength="16" v-model="form.phoneNumbers[index]" placeholder="Wpisz numer telefonu" class="form-control input_size" > -->
          <IntlTelInput class="input_size form-control"
            v-model="form.phoneNumbers[index]"
            ref=phoneNumberB
            @changeCountry="countryChange"
            :options="{
              initialCountry: 'pl',
              strictMode: true

            }" />
          <button   type="button" class="btn1" @click="removePhoneNumber(index)">Usuń</button>
        </div>
        <button type="button"  class="btn1 button_add_contact" @click="addPhoneNumber">Dodaj nowy numer telefonu</button>


      </div>
      <div class="form-check form-switch">
        <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" v-model="ignoreDup" >
        <label class="form-check-label" for="flexSwitchCheckDefault">Ignoruj Duplikaty Danych Kontaktowych</label>
      </div>
      <p class="text-danger" style="font-size: 0.9em">* pozycje obowiązkowe</p>
      <p class="text-danger">{{ errorMessage }}</p>

      <button v-if="standalone" type="submit">Zapisz</button>
      <button v-if="standalone" class="btn1" style="float: right" @click="checkFormAndOpenModal">Powrót</button>
      <br>
    </form>
  </div>
</template>


<script>
import { fetchWrapper, getCookie, refreshCSRF } from '@/utility';
import IntlTelInput from "intl-tel-input/vueWithUtils";
import "intl-tel-input/styles";
import { postalCodeData } from "@monarkit/postal-code-data";

export default {
  name: 'BusinessForm',
  components:{
    IntlTelInput
  },
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
        emails: [],
        phoneNumbers: [],
        countryCode: 'PL'
      },
      errorMessage: "",
      ignoreDup: false,
      showRequestModal: false,
      countries: postalCodeData,
      postalCodePattern: '^[0-9]{2}-?[0-9]{3}$',

    };
  },
  computed:{
    styleModifier(){
      return !this.standalone? {width: "100%"}:{};
    },

    isFormFilled() {
      return this.form.name || this.form.locality || this.form.postalCode || this.form.street ||
          this.form.propertyNumber || this.form.apartmentNumber || this.form.nip || this.form.regon ||
          this.form.comments || this.form.emails.some(email => email) || this.form.phoneNumbers.some(phone => phone);
    },

  },
  mounted() {
    refreshCSRF();
    this.setPostalCodePattern();
    if (this.$route.query.idBusiness) {
      this.formMode = 'edit';
      this.fetchBusiness();
    }
  },
  methods: {
    setPostalCodePattern() {
      const countryCode = this.form.countryCode;
      const countryData = this.countries.find(country => country.countryCode === countryCode);
      this.postalCodePattern = countryData ? countryData.pattern.source : '';
      

      console.log(this.postalCodePattern)
      return this.postalCodePattern
    },
    formatNIP() {
      this.form.nip=this.form.nip.replace(/\D/g, '');
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
              this.form.emails = business.contactData.emails.map(email => email.email) || [];
              this.form.phoneNumbers = business.contactData.phoneNumbers.map(phone => phone.number) || [];
              console.log(this.form.phoneNumbers);

            } else {
              this.form.emails = [];
              this.form.phoneNumbers = [];
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
      if (this.form.emails.length > 0) {
        this.form.emails.splice(index, 1);
      }
    },
    addPhoneNumber() {
      this.form.phoneNumbers.push('');
    },
    removePhoneNumber(index) {
      if (this.form.phoneNumbers.length > 0) {
        this.form.phoneNumbers.splice(index, 1);
      }
    },
    async submitForm() {

      let emailsCheck = this.form.emails.filter(str=> str.trim().length !==0)


      let numbersCheck = [];
      for (let i = 0; i <  this.form.phoneNumbers.length; i++) {
        let instance  = this.$refs.phoneNumberB[i].instance;
        numbersCheck.push(`+${instance.selectedCountryData.dialCode} ${this.form.phoneNumbers[i].replace(/\+\d+/,'')}`)

      }

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
        emails: emailsCheck.length === 0 ? null : emailsCheck,
        phoneNumbers: numbersCheck
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
    checkFormAndOpenModal() {
      if (this.isFormFilled) {
        const confirmation = window.confirm('Czy na pewno chcesz opuścić formularz? Stracisz niezapisane postępy!');
        if (confirmation) {
          this.goBack();
        }
      } else {
        this.goBack();
      }
    },
    goBack() {
      this.$router.push('/Business');
    }
  }
};
</script>



<style src="@/assets/style.css"></style>