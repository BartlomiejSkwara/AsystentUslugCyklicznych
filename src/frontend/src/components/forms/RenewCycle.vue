<template>
  <div>
    <h1>Odnowienie płatności cyklicznej</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="cycleEnd">Okres ważności certyfikatu w latach: </label>
        <br>
        <select id="cycleEnd" v-model="form.cycleEnd" required>
          <option :value="1">1</option>
          <option :value="2">2</option>
          <option :value="3">3</option>
        </select>
      </div>
      <div>
        <label for="cardNumber">Numer karty:</label>
        <input type="text" id="cardNumber" v-model="form.cardNumber" class="form-control" required>
      </div>
      <!-- <div>
        <label for="cardType">Typ karty:</label>
        <select id="cardType" v-model="form.cardType" required disabled>
          <option value="PHYSICAL">FIZYCZNA</option>
          <option value="SIMPLYSIGN">PODPIS W CHMURZE</option>
        </select>
      </div> -->
      <div>
        <label for="certSerialNumber">Numer certyfikatu:</label>
        <input type="text" id="certSerialNumber" v-model="form.certSerialNumber"  @input="formatCertSerialNumber" class="form-control" required>
      </div>
      <div>
        <label for="nameInOrganisation">Stanowisko w organizacji: (opcjonalne)</label>
        <input type="text" id="nameInOrganisation" v-model="form.nameInOrganisation" class="form-control" >
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
        cycleEnd: 1,
        cardNumber: '',
        cardType: 1,
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
    formatCertSerialNumber(){
      let certNumber = this.form.certSerialNumber.replace(/\D/g, '');
      this.form.certSerialNumber = certNumber.trim();

    },

    setDefaultData() {
      const now = new Date();
      const offset = 2 * 60 + 2; // CEST UTC+2
      const localDate = new Date(now.getTime() + offset * 60 * 1000);
      this.form.cycleStart = localDate.toISOString().slice(0, 16); // Ustawiamy domyślną datę rozpoczęcia
      // this.form.cardNumber = cycle.certificate.cardNumber;
    },
    submitForm() {
      const payload = {
        certificateLengthInYears: this.form.cycleEnd,
        cardNumber: this.form.cardNumber,
        // cardType: this.form.cardType,
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
