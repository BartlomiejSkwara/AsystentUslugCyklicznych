<template>
  <div>
    <h1>Dodaj nową płatność cykliczną</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="agreementNumber">Numer dokumentu:</label>
        <input type="text" id="agreementNumber" v-model="form.agreementNumber" required>
      </div>
      <div>
        <label for="oneTime">Jednorazowe:</label>
        <select id="oneTime" v-model="form.oneTime">
          <option :value="false">Nie</option>
          <option :value="true">Tak</option>
        </select>
      </div>
      <div>
        <label for="cycleStart">Data rozpoczęcia:</label>
        <input type="datetime-local" id="cycleStart" v-model="form.cycleStart" required>
      </div>
      <div>
        <label for="cycleEnd">Data zakończenia:</label>
        <input type="datetime-local" id="cycleEnd" v-model="form.cycleEnd" required>
      </div>
      <div>
        <label for="cardNumber">Numer karty:</label>
        <input type="text" id="cardNumber" v-model="form.cardNumber" required>
      </div>
      <div>
        <label for="cardType">Typ karty:</label>
        <select id="cardType" v-model="form.cardType" required>
          <option value="PHYSICAL">FIZYCZNA</option>
          <option value="SIMPLYSIGN">PODPIS W CHMURZE</option>
        </select>
      </div>
      <div>
        <label for="certSerialNumber">Numer certyfikatu:</label>
        <input type="text" id="certSerialNumber" v-model="form.certSerialNumber" required>
      </div>
      <div>
        <label for="nameInOrganisation">Stanowisko w organizacji: (opcjonalne)</label>
        <input type="text" id="nameInOrganisation" v-model="form.nameInOrganisation">
      </div>

      <div>
        <label for="businessId">Firma:</label>
        <input list="businesses" name="businesses" v-model="form.businessId" />
        <datalist id="businesses">
          <option v-for="business in businesses" :key="business.id" :value="business.name">
            {{ business.name }}
          </option>
        </datalist>
      </div>

      <div>
        <label for="serviceUserId">Użytkownik usługi:</label>
        <input list="serviceUsers" name="serviceUsers" v-model="form.serviceUserId" />
        <datalist id="serviceUsers">
          <option v-for="user in serviceUsers" :key="user.id" :value="user.name + ' ' + user.surname">
            {{ user.name + " " + user.surname }}
          </option>
        </datalist>
      </div>

      <div>
        <label for="accounts">Konto w systemie powiązane z usługą:</label>
        <input list="accounts" name="accounts" v-model="form.accountDataUsername" />
        <datalist id="accounts">
          <option v-for="(account,key) in accounts" :key="key" :value="account.username"></option>
        </datalist>
      </div>

      <div>
        <label for="description">Opis:</label>
        <input type="text" id="description" v-model="form.description" required>
      </div>

      <button type="submit">Zapisz</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>
</template>

<script>
import { fetchWrapper, getCookie, refreshCSRF } from '@/utility';

export default {
  data() {
    return {
      accounts: [],
      businesses: [],
      serviceUsers: [],
      form: {
        agreementNumber: '',
        oneTime: false,
        cycleStart: '',
        cycleEnd: '',
        cardNumber: '',
        cardType: 'PHYSICAL',
        certSerialNumber: '',
        nameInOrganisation: '',
        businessId: null,
        serviceUserId: null,
        accountDataUsername: null,
        description: '',
      }
    };
  },
  computed: {
  },
  mounted() {
    refreshCSRF();
    this.fetchAccounts();
    this.fetchBusinesses();
    this.fetchServiceUsers();
  },
  methods: {
    async fetchAccounts() {
      try {
        const response = await fetchWrapper(this, '/api/accountData/getAll', {
          method: 'GET'
        });

        if (!response.ok) {
          throw new Error('Network response was not ok ' + response.statusText);
        }

        const role = response.headers.get('frontRole');
        this.$store.commit('setRole', role);
        const data = await response.json();
        this.accounts = data;
      } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
      }
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
        price: 10.00, // Stała cena
        description: this.form.description,
        oneTime: this.form.oneTime,
        cycleStart: this.form.cycleStart,
        cycleEnd: this.form.cycleEnd,
        cardNumber: this.form.cardNumber,
        cardType: this.form.cardType,
        certSerialNumber: this.form.certSerialNumber,
        nameInOrganisation: this.form.nameInOrganisation || null,
        businessId: this.getBusinessID(this.form.businessId),
        serviceUserId: this.getUserID(this.form.serviceUserId),
        relatedAccountId: this.getAccountID(this.form.accountDataUsername)
      };
      const cookie = getCookie('XSRF-TOKEN');

      fetchWrapper(this, '/api/cyclicalservice/insertBody', {
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
    getAccountID(username) {
      const account = this.accounts.find(acc => acc.username === username);
      return account ? account.idLoginCredentials : null;
    }
  }
};
</script>

<style src="@/assets/style.css"></style>
