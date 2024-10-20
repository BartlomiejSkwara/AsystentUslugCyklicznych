<template>
  <div>
    <h1>Odnowienie płatności cyklicznej</h1>
    <form @submit.prevent="submitForm">
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
        <select id="cardType" v-model="form.cardType" required disabled>
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
      <button type="submit">Zapisz</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>
</template>

<script>
import { getCookie,fetchWrapper, refreshCSRF } from '@/utility';

export default {
  name: 'RenewCycle',
  data() {
    return {
      form: {
        cycleStart: '',
        cycleEnd: '',
        cardNumber: '',
        cardType: 'PHYSICAL',
        certSerialNumber: '',
        nameInOrganisation: ''
      },
      cycleId: null
    };
  },
  mounted() {
    refreshCSRF();
    this.cycleId = this.$route.params.id; // Pobieramy ID cyklu z parametrów trasy
    this.setDefaultData();
  },
  methods: {
    setDefaultData() {
      const now = new Date();
      const offset = 2 * 60 + 2; // CEST UTC+2
      const localDate = new Date(now.getTime() + offset * 60 * 1000);
      this.form.cycleStart = localDate.toISOString().slice(0, 16); // Ustawiamy domyślną datę rozpoczęcia
      // this.form.cardNumber = cycle.certificate.cardNumber;
    },
    submitForm() {
      const payload = {
        cycleStart: this.form.cycleStart,
        cycleEnd: this.form.cycleEnd,
        cardNumber: this.form.cardNumber,
        cardType: this.form.cardType,
        certSerialNumber: this.form.certSerialNumber,
        nameInOrganisation: this.form.nameInOrganisation || null
      };
      const cookie = getCookie("XSRF-TOKEN");


      fetchWrapper(this,`/api/cyclicalservice/renew/${this.cycleId}`, {
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
          console.error('Error renewing cyclical service:', error);
        });
    },
    goBack() {
      this.$router.push('/Cycles');
    }
  }
};
</script>

<style src="@/assets/style.css"></style>
