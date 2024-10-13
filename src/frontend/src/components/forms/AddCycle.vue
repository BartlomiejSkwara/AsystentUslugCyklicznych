<template>
  <div>
    <h1>Dodaj nową płatność cykliczną</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="agreementNumber">Numer dokumentu:</label>
        <input type="text" id="agreementNumber" v-model="form.agreementNumber" required>
      </div>
<!--      ZOSTAWIAMY CZY USUWAMY CENĘ?-->
      <div>
        <label for="price">Cena:</label>
        <input type="number" id="price" v-model="form.price" step="0.01" required>
      </div>
      <div>
        <label for="description">Opis:</label>
        <input type="text" id="description" v-model="form.description" required>
      </div>
      <div>
        <label for="oneTime">Jednorazowe:</label>
        <select id="oneTime" v-model="form.oneTime">
          <option :value="true">Tak</option>
          <option :value="false">Nie</option>
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
        <label for="nameInOrganisation">Stawnowisko w organizacji: (opcjonalne)</label>
        <input type="text" id="nameInOrganisation" v-model="form.nameInOrganisation">
      </div>
      <div>
        <label for="businessId">ID firmy:</label>
        <input type="number" id="businessId" v-model="form.businessId" required>
      </div>
      <div>
        <label for="serviceUserId">ID użytkownika:</label>
        <input type="number" id="serviceUserId" v-model="form.serviceUserId" required>
      </div>
      <button type="submit">Zapisz</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>
</template>

<script>
import { getCookie, refreshCSRF } from '@/utility';

export default {
  data() {
    return {
      form: {
        agreementNumber: '',
        price: null,
        description: '',
        oneTime: true,
        cycleStart: '',
        cycleEnd: '',
        cardNumber: '',
        cardType: 'PHYSICAL',
        certSerialNumber: '',
        nameInOrganisation: '',
        businessId: null,
        serviceUserId: null
      }
    };
  },
  mounted() {
    refreshCSRF()
  },
  methods: {
    submitForm() {
      const payload = {
        agreementNumber: this.form.agreementNumber,
        price: parseFloat(this.form.price),
        description: this.form.description,
        oneTime: this.form.oneTime,
        cycleStart: this.form.cycleStart,
        cycleEnd: this.form.cycleEnd,
        cardNumber: this.form.cardNumber,
        cardType: this.form.cardType,
        certSerialNumber: this.form.certSerialNumber,
        nameInOrganisation: this.form.nameInOrganisation || null,
        businessId: this.form.businessId,
        serviceUserId: this.form.serviceUserId
      };
      const cookie = getCookie("XSRF-TOKEN");

      fetch('/api/cyclicalservice/insertBody', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN':cookie
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
    }
  }
};
</script>


<style src="@/assets/style.css"></style>
