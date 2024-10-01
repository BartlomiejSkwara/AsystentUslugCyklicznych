<template>
  <div>
    <h1>Add New Cyclical Service</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="agreementNumber">Agreement Number:</label>
        <input type="text" id="agreementNumber" v-model="form.agreementNumber" required>
      </div>
      <div>
        <label for="price">Price:</label>
        <input type="number" id="price" v-model="form.price" step="0.01" required>
      </div>
      <div>
        <label for="description">Description:</label>
        <input type="text" id="description" v-model="form.description" required>
      </div>
      <div>
        <label for="oneTime">One Time:</label>
        <select id="oneTime" v-model="form.oneTime">
          <option :value="true">Yes</option>
          <option :value="false">No</option>
        </select>
      </div>
      <div>
        <label for="cycleStart">Cycle Start:</label>
        <input type="datetime-local" id="cycleStart" v-model="form.cycleStart" required>
      </div>
      <div>
        <label for="cycleEnd">Cycle End:</label>
        <input type="datetime-local" id="cycleEnd" v-model="form.cycleEnd" required>
      </div>
      <div>
        <label for="cardNumber">Card Number:</label>
        <input type="text" id="cardNumber" v-model="form.cardNumber" required>
      </div>
      <div>
        <label for="cardType">Card Type:</label>
        <select id="cardType" v-model="form.cardType" required>
          <option value="PHYSICAL">PHYSICAL</option>
          <option value="SIMPLYSIGN">SIMPLYSIGN</option>
        </select>
      </div>
      <div>
        <label for="certSerialNumber">Certificate Serial Number:</label>
        <input type="text" id="certSerialNumber" v-model="form.certSerialNumber" required>
      </div>
      <div>
        <label for="nameInOrganisation">Name in Organisation: (optional)</label>
        <input type="text" id="nameInOrganisation" v-model="form.nameInOrganisation">
      </div>
      <div>
        <label for="businessId">Business ID:</label>
        <input type="number" id="businessId" v-model="form.businessId" required>
      </div>
      <div>
        <label for="serviceUserId">Service User ID:</label>
        <input type="number" id="serviceUserId" v-model="form.serviceUserId" required>
      </div>
      <button type="submit">Save</button>
      <button type="button" @click="goBack">Back</button>
    </form>
  </div>
</template>

<script>
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

      fetch('/api/cyclicalservice/insertBody', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
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
