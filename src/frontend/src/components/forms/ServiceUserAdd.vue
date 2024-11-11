<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edytuj użytkownika' : 'Dodaj nowego użytkownika usługi' }}</h1>
    <form  @submit.prevent="submitForm" :style="styleModifier">
      <div>
        <label for="serviceUserName">Imię: <span class="text-danger">*</span></label>
        <input type="text" id="serviceUserName" v-model="form.name" class="form-control" required />
      </div>
      <div>
        <label for="surname">Nazwisko: <span class="text-danger">*</span></label>
        <input type="text" id="surname" v-model="form.surname" class="form-control" required />
      </div>
      <div>
        <label for="login">Login: <span class="text-danger">*</span></label>
        <input type="text" id="login" v-model="form.login" class="form-control" required />
      </div>
      <div>
        <span v-if="formMode==='edit'">
          <label for="password">Hasło: </label>
          <input type="password" id="password" v-model="form.password" class="form-control" />
        </span>
        <span v-else>
          <label for="password">Hasło: <span  class="text-danger" >* </span></label>
          <input type="password" id="password" v-model="form.password" class="form-control" required/>
        </span>
      </div>
      <div>
        <p>Emaile:</p>
        <div class="input_button_place" v-for="(email, index) in form.emails" :key="index">
          <input type="email" v-model="form.emails[index]" placeholder="Wpisz email" class="input_size form-control"   />
          <button class="btn1"  @click="removeEmail(index)">Usuń</button>
        </div>
        <button  class="btn1 button_add_contact" type="button" @click="addEmail">Dodaj nowy email</button>
      </div>
      <div>
        <p>Numery telefonów:</p>
        <div class="input_button_place" v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <IntlTelInput class="input_size form-control"
            v-model="form.phoneNumbers[index]"
            ref=phoneNumber
            @changeCountry="countryChange"
            :options="{
              initialCountry: 'pl',
              strictMode: true

            }" />
          <!-- <input type="tel" maxlength="16" v-model="form.phoneNumbers[index]" placeholder="Wpisz numer telefonu" class="input_size form-control"   /> -->
          <button class="btn1" type="button" @click="removePhoneNumber(index)">Usuń</button>
        </div>
        <button  class="btn1 button_add_contact" type="button" @click="addPhoneNumber">Dodaj nowy telefon</button>
      </div>
      <div>
        <label for="hasPolishPESEL">Polski PESEL?:</label>
        <br>
        <select id="hasPolishPESEL" v-model="form.hasPolishPESEL">
          <option :value="true">Tak</option>
          <option :value="false">Nie</option>
        </select>
      </div>
      <div v-if="form.hasPolishPESEL">
        <label for="taxIdentificationNumber">PESEL:</label>
        <input type="text" maxlength="11" id="taxIdentificationNumber" v-model="form.taxId" class="form-control" />
      </div>
      <div>
        <label for="commentsUser">Dodatkowy opis:</label>
        <textarea type="text" id="commentsUser" v-model="form.comments" class="form-control" />
      </div>

      <div class="form-check form-switch">
        <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefaultUser" v-model="ignoreDup">
        <label class="form-check-label" for="flexSwitchCheckDefaultUser">Ignoruj Duplikaty Danych Kontaktowych</label>
      </div>
      <p class="text-danger" style="font-size: 0.9em">* pozycje obowiązkowe</p>
      <p class="text-danger">{{ errorMessage }}</p>

      <button v-if="standalone" type="submit">Zapisz</button>
      <button v-if="standalone" class="btn1" style="float: right" @click="checkFormAndOpenModal">Powrót</button>
    </form>
  </div>
</template>

<script>
import { getCookie,fetchWrapper, refreshCSRF } from '@/utility';
import IntlTelInput from "intl-tel-input/vueWithUtils";
import "intl-tel-input/styles";
export default {
  name: 'UserForm',
  components: {
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
      formMode: 'add',
      form: {
        idServiceUser: null,
        name: '',
        surname: '',
        login: '',
        password: '',
        emails: [],
        phoneNumbers: [],
        hasPolishPESEL: true,
        taxId: "",
        comments: ''
      },
      errorMessage: "",
      ignoreDup: false,
      showRequestModal: false,
    };
  },
  computed:{
    styleModifier(){
      return !this.standalone? {width: "100%"}:{};
    },

    isFormFilled() {
      return this.form.name || this.form.surname || this.form.login || this.form.password ||
          this.form.taxId || this.form.comments || this.form.emails.some(email => email) ||
          this.form.phoneNumbers.some(phone => phone);
    },


  },
  mounted() {
    refreshCSRF();
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
    countryChange(country){

    // console.log(ref(1).intlTelInput);
    console.log(this.$refs.phoneNumber);
    console.log(this.$refs.phoneNumber[0].instance.getNumber());




    console.log("zmiana",country,"....");

    },
    switchRequestModalVisibility() {
      this.showRequestModal = !this.showRequestModal;
    },
    fetchUser() {
      fetchWrapper(this,`/api/serviceUser/get/${this.$route.query.idServiceUser}`)
          .then(response => response.json())
          .then(user => {
            this.form.idServiceUser = user.idServiceUser || null;
            this.form.name = user.name || '';
            this.form.surname = user.surname || '';
            this.form.login = user.accountDataEntity.username || '';
            this.form.password = '';
            this.form.hasPolishPESEL = user.hasPolishPesel !== undefined ? user.hasPolishPesel : false;
            this.form.taxId = user.taxIdentificationNumber || null;
            this.form.comments = user.comments || '';

            if (user.contactData) {
              this.form.emails = user.contactData.emails.map(email => email.email) || [];
              this.form.phoneNumbers = user.contactData.phoneNumbers.map(phone => phone.number!=null?phone.number:null) || [];

              console.log(this.form.phoneNumbers);

            } else {
              this.form.phoneNumbers =[];

              this.form.emails = [];
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
      //JEDNAK NADAL NIE DZIAŁA
      let payload = {};


      let numbersCheck = [];

      for (let i = 0; i <  this.form.phoneNumbers.length; i++) {
        let instance  = this.$refs.phoneNumber[i].instance;
        numbersCheck.push(`+${instance.selectedCountryData.dialCode} ${this.form.phoneNumbers[i].replace(/\+\d+/,'')}`)

      }

      let taxIdCheck = this.form.taxId;
      if(this.form.taxId != null)
        taxIdCheck = this.form.taxId.trim().length>0?this.form.taxId:null;

      let passwordCheck = this.form.password;
      if(this.form.password != null)
        passwordCheck = this.form.password.trim().length>0?this.form.password:null;


      let emailsCheck = this.form.emails.filter(str=> str.trim().length!==0 )

      payload = {
        name: this.form.name,
        surname: this.form.surname,
        login: this.form.login,
        password: passwordCheck,
        hasPolishPESEL: this.form.hasPolishPESEL ? 1 : 0,
        comments: this.form.comments,
        emails:  emailsCheck.length === 0 ? null : emailsCheck,
        phoneNumbers: numbersCheck.length>0?numbersCheck:null,
        taxId: taxIdCheck
      };

      // if (this.form.newPassword) {
      //   payload.password = this.form.newPassword; // Dodaj hasło tylko, jeśli jest ustawione
      // }
      
      let url = this.formMode === 'add'
          ? '/api/serviceUser/insertBody'
          : `/api/serviceUser/update/${this.form.idServiceUser}`;
      if(this.ignoreDup)
          url+="?checkForDuplicates=false";
      const cookie = getCookie("XSRF-TOKEN");
      // console.log("Fetched cookie:");
      // console.log(document.cookie);
      // console.log(cookie);
      try {
        const response = await fetchWrapper(this,url,{
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN':cookie
          },
          body: JSON.stringify(payload)
        })
        if(response.status == 400){
          this.errorMessage = await response.text();
        }else if(response.status == 200){
          if(this.standalone){
            this.$router.push('/ServiceUser');
          }
          else{
            return  this.form.name + " " + this.form.surname;              
          }
        }

      } catch (error) {
        console.error('Error saving user:', error);

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
      this.$router.push('/ServiceUser');
    }


  }
};
</script>

<style src="@/assets/forms.css"></style>
