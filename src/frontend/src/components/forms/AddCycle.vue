<template>
  <div>
    <h1>{{ formMode === 'edit' ? 'Edytuj płatność cykliczną' : 'Dodaj nową płatność cykliczną' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="agreementNumber">Numer umowy: <span class="text-danger">*</span></label>
        <input type="text" id="agreementNumber" v-model="form.agreementNumber" required>
      </div>
      <div>
        <label for="oneTime">Jednorazowe: <span class="text-danger"> *</span></label>
        <br>
        <select id="oneTime" v-model="form.oneTime">
          <option :value="false">Nie</option>
          <option :value="true">Tak</option>
        </select>
      </div>
      <div>
        <label for="cycleStart">Data rozpoczęcia: <span class="text-danger">*</span></label>
        <input type="datetime-local" id="cycleStart" v-model="form.cycleStart" required>
      </div>
      <div>
        <label for="cycleEnd">Okres ważności certyfikatu w latach: <span class="text-danger"> *</span></label>
        <br>
        <select id="cycleEnd" v-model="form.cycleEnd" required>
          <option :value="1">1</option>
          <option :value="2">2</option>
          <option :value="3">3</option>
        </select>
      </div>
      <div>
        <label for="cardNumber">Numer karty: <span class="text-danger">*</span></label>
        <input type="text" id="cardNumber" v-model="form.cardNumber" @input="formatCardNumber" required>
      </div>
      <div>
        <label for="cardType">Typ karty: <span class="text-danger"> *</span></label>
        <br>
        <select id="cardType" v-model="form.cardType" required>
          <option value=1>{{translateCardType(1)}}</option>
          <option value=2>{{translateCardType(2)}}</option>
          <option value=3>{{translateCardType(3)}}</option>
          <option value=4>{{translateCardType(4)}}</option>
        </select>
      </div>
      <div>
        <label for="certSerialNumber">Numer certyfikatu: <span class="text-danger">*</span></label>
        <input type="text" id="certSerialNumber" v-model="form.certSerialNumber" required>
      </div>
      <div>
        <label for="nameInOrganisation">Stanowisko w organizacji: </label>
        <input type="text" id="nameInOrganisation" v-model="form.nameInOrganisation">
      </div>

      <div>
        <label for="businessId">Firma: <span class="text-danger">*</span></label>
        <input list="businesses" name="businesses" v-model="form.businessId" />
        <datalist id="businesses">
          <option v-for="business in businesses" :key="business.id">
            {{ business.name }}
          </option>

        </datalist>

        <button class="btn btn-primary"   data-bs-toggle="modal" data-bs-target="#businessModal">Dodaj Nową</button>
      </div>

      <div>
        <label for="serviceUserId">Użytkownik usługi: <span class="text-danger">*</span></label>

        <input list="serviceUsers" name="serviceUsers" v-model="form.serviceUserId" />
        <datalist id="serviceUsers">
          <option v-for="user in serviceUsers" :key="user.id">
            {{ user.name + " " + user.surname }}
          </option>
        </datalist>

        <button class="btn btn-primary"   data-bs-toggle="modal" data-bs-target="#userModal">Dodaj Nowego</button>

      </div>



      <div>
        <label for="description">Opis:</label>
        <input type="text" id="description" v-model="form.description" required>
      </div>
      <p class="text-danger" style="font-size: 0.9em">* pozycje obowiązkowe</p>
      <button type="submit">Zapisz</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>




  <div id="userModal" class="modal fade" tabindex="-1" aria-labelledby="userModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-body ">
          <ServiceUserAdd  ref="userForm" :standalone="false"></ServiceUserAdd>  
        </div>
        <div class="modal-footer">
          <button @click="submitUserForm" class="btn btn-outline-success" >Zapisz</button>
          <button  id="closeUserForm" class="btn btn-outline-secondary" data-bs-dismiss="modal">Powrót</button>
        </div>
      </div>
    </div>
  </div>  

  <div id="businessModal" class="modal fade" tabindex="-1" aria-labelledby="businessModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-body ">
          <AddBusiness  ref="businessForm" :standalone="false"></AddBusiness>  
        </div>
        <div class="modal-footer">
          <button @click="submitBusinessForm" class="btn btn-outline-success" >Zapisz</button>
          <button  id="closeBusinessForm" class="btn btn-outline-secondary" data-bs-dismiss="modal">Powrót</button>
        </div>
      </div>
    </div>
  </div> 
</template>

<script>
import {fetchWrapper, getCookie, refreshCSRF, translateCardType } from '@/utility';
import ServiceUserAdd from './ServiceUserAdd.vue';
import AddBusiness from './AddBusiness.vue';

export default {
  components:{
    ServiceUserAdd,
    AddBusiness
  },
  data() {
    return {
      formMode: 'add',
      businesses: [],
      serviceUsers: [],
      form: {
        agreementNumber: '',
        oneTime: false,
        cycleStart: '',
        cycleEnd: 1,
        cardNumber: '',
        cardType: 1,
        certSerialNumber: '',
        nameInOrganisation: '',
        businessId: null,
        serviceUserId: null,
        accountDataUsername: null,
        description: '',
        signatureType: 0,
        idCyclicalService:null
      }
    };
  },
  computed: {
  },
  mounted() {
    console.log(this.form);
    
    refreshCSRF();
    // console.log(this.$route.query.serviceUserId)
    if (this.$route.query.serviceUserId) {
      this.formMode = 'edit';
      this.fetchCycle();
    }
    this.fetchBusinesses();
    this.fetchServiceUsers();
    console.log(this.form);

  },
  methods: {
    translateCardType,
    formatCardNumber() {
      let cardNumber = this.form.cardNumber.replace(/\D/g, '');

      cardNumber = cardNumber.replace(/(\d{4})(?=\d)/g, '$1 ');

      this.form.cardNumber = cardNumber.trim();
    },

    async submitUserForm(){
      let newUserName = await this.$refs.userForm.submitForm()
      if(newUserName!=null){
        document.getElementById("closeUserForm").click();
        await this.fetchServiceUsers();

        this.form.serviceUserId = newUserName;
      }
    },
    async submitBusinessForm(){  

      let newName = await this.$refs.businessForm.submitForm()
      if(newName!=null){
        document.getElementById("closeBusinessForm").click();
        await this.fetchBusinesses();
        

        this.form.businessId = newName;


      }
    },
    fetchCycle() {
      const userId = this.$route.query.serviceUserId || 1; // Default userID or from route params
      const targetAgreementNumber = this.$route.query.agreementNumber; // Get agreementNumber from route

      fetchWrapper(this, `/api/cyclicalservice/getAllByUser?userID=${userId}`)
          .then(response => response.json())
          .then(cycles => {
            
            if (cycles.length > 0) {
              let cycle;
              if (targetAgreementNumber) {
                // Find the cycle with the matching agreementNumber
                cycle = cycles.find(cycle => cycle.agreementNumber === targetAgreementNumber);
              }
              

              if (cycle) {

                ///fsaf
                
                this.form.agreementNumber = cycle.agreementNumber || '';
                this.form.cycleStart = cycle.certificate ? cycle.certificate.validFrom : '';
                this.form.cycleEnd = this.calculateCertificateYears(cycle.certificate.validFrom, cycle.certificate.validTo);
                this.form.oneTime = cycle.oneTime || false;
                this.form.cardNumber = cycle.certificate ? cycle.certificate.cardNumber : '';
                this.form.cardType = cycle.certificate ? cycle.certificate.cardType : 1;
                this.form.certSerialNumber = cycle.certificate ? cycle.certificate.certificateSerialNumber : '';
                this.form.nameInOrganisation = cycle.certificate ? cycle.certificate.nameInOrganisation : '';
                this.form.businessId = cycle.business ? cycle.business.businessName : '';
                this.form.serviceUserId = cycle.serviceUser ? `${cycle.serviceUser.name} ${cycle.serviceUser.getSurname}` : '';
                this.form.description = cycle.description || '';
                this.form.idCyclicalService = cycle.getIdCyclicalService;
                console.log(cycle);
                console.log(this.form);
              } else {
                console.log('No cycle with matching agreementNumber found.');
              }
            } else {
              console.log('No cyclical service data found for the given user.');
            }
          })
          .catch(error => {
            console.error('Error fetching user data:', error);
          });
    },

    calculateCertificateYears(validFrom, validTo) {
      if (validFrom && validTo) {
        const startYear = new Date(validFrom).getFullYear();
        const endYear = new Date(validTo).getFullYear();
        return endYear - startYear;
      }
      return 1; // Default to 1 year if dates are invalid
    },

    async fetchBusinesses() {
      try {
        const response = await fetchWrapper(this, '/api/business/getAll', {
          method: 'GET'
        });

        if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();
        this.businesses = data;
      } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
      }
    },
    async fetchServiceUsers() {
      try {
        const response = await fetchWrapper(this, '/api/serviceUser/getAll', {
          method: 'GET'
        });

        if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();
        this.serviceUsers = data;
      } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
      }
    },
    submitForm() {
      const payload = {
        agreementNumber: this.form.agreementNumber,
        description: this.form.description,
        oneTime: this.form.oneTime,
        cycleStart: this.form.cycleStart,
        certificateLengthInYears: this.form.cycleEnd,
        cardNumber: this.form.cardNumber,
        cardType: this.form.cardType,
        certSerialNumber: this.form.certSerialNumber,
        nameInOrganisation: this.form.nameInOrganisation || null,
        businessId: this.getBusinessID(this.form.businessId),
        serviceUserId: this.getUserID(this.form.serviceUserId),
        // signatureType: this.form.signatureType
      };
      const cookie = getCookie('XSRF-TOKEN');

      let url = this.formMode === 'add'
          ? '/api/cyclicalservice/insertBody'
          : `/api/cyclicalservice/update/${this.form.idCyclicalService}`;

      fetchWrapper(this, url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': cookie
        },
        body: JSON.stringify(payload)
      })
          .then(response => response.text())
          .then(() => {
            this.$router.push('/Cycles');
          })
          .catch(error => {
            console.error('Error saving cyclical service:', error);
          });
    },
    goBack() {
      this.$router.push('/Cycles');
    },
    getBusinessID(name) {
      const business = this.businesses.find(bus => bus.name === name);
      return business ? business.idBusiness : null;
    },
    getUserID(fullName) {
      const user = this.serviceUsers.find(
          user => `${user.name} ${user.surname}` === fullName
      );
      return user ? user.idServiceUser : null;
    },

  }
};
</script>

<style src="@/assets/style.css"></style>
