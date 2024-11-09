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
        <label>Emaile:</label>
        <div class="input_button_place" v-for="(email, index) in form.emails" :key="index">
          <input type="email" v-model="form.emails[index]" placeholder="Wpisz email" class="input_size form-control"  required />
          <button type="button" @click="removeEmail(index)">Usuń</button>
        </div>
        <button type="button" class="button_add_contact" @click="addEmail">Dodaj nowy email</button>
      </div>
      <div>
        <label>Numery telefonów:</label>
        <div class="input_button_place" v-for="(phoneNumber, index) in form.phoneNumbers" :key="index">
          <input type="tel" maxlength="16" v-model="form.phoneNumbers[index]" placeholder="Wpisz numer telefonu" class="input_size form-control"  required />
          <button type="button" @click="removePhoneNumber(index)">Usuń</button>
        </div>
        <button type="button" class="button_add_contact" @click="addPhoneNumber">Dodaj nowy telefon</button>
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
      <button v-if="standalone" type="button" style="float: right" @click="checkFormAndOpenModal">Powrót</button>
    </form>
  </div>

  <div id="requestModal" class="modal fade" tabindex="-1" aria-labelledby="requestModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content ">
        <div class="modal-header modal-bg">
          <h3>Czy na pewno chcesz opuścić formularz? Stracisz niezapisane postępy!</h3>
        </div>
        <div class="modal-footer modal-bg">
          <button  id="closeRequest" class="btn btn-outline-success" data-bs-dismiss="modal">Kontynuuj</button>
          <button class="btn btn-outline-secondary" data-bs-dismiss="modal" @click="goBack">Powrót</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCookie,fetchWrapper, refreshCSRF } from '@/utility';
export default {
  name: 'UserForm',
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
        emails: [''],
        phoneNumbers: [''],
        hasPolishPESEL: true,
        taxId: '',
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
    async submitForm() {
      const payload = {
        name: this.form.name,
        surname: this.form.surname,
        login: this.form.login,
        password: this.form.password.trim(),
        hasPolishPESEL: this.form.hasPolishPESEL ? 1 : 0,
        comments: this.form.comments,
        emails: this.form.emails,
        phoneNumbers: this.form.phoneNumbers,
        taxId: this.form.hasPolishPESEL ? this.form.taxId : null
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
        const modalElement = document.getElementById('requestModal');
        if (modalElement) {
          const modalInstance = new window.bootstrap.Modal(modalElement);
          modalInstance.show();
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
