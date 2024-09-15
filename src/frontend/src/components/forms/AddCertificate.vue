<template>
  <div>
    <h1>{{ isEdit ? 'Edit Certificate' : 'Add new Certificate' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="id_service">ID Service:</label>
        <input type="number" id="id_service" v-model.number="form.id_service" required />
      </div>
      <div>
        <label for="valid_from">Valid From:</label>
        <input type="datetime-local" id="valid_from" v-model="form.valid_from" required />
      </div>
      <div>
        <label for="valid_to">Valid To:</label>
        <input type="datetime-local" id="valid_to" v-model="form.valid_to" required />
      </div>
      <div>
        <label for="card_number">Card Number:</label>
        <input type="text" id="card_number" v-model="form.card_number" required />
      </div>
      <div>
        <label for="card_type">Card Type:</label>
        <select id="card_type" v-model="form.card_type" required>
          <option value="Type A">Type A</option>
          <option value="Type B">Type B</option>
          <option value="Type C">Type C</option>
        </select>
      </div>
      <div>
        <label for="certificate_serial_number">Certificate Serial Number:</label>
        <input type="text" id="certificate_serial_number" v-model="form.certificate_serial_number" required />
      </div>
      <div>
        <label for="name_in_organisation">Name in Organisation:</label>
        <input type="text" id="name_in_organisation" v-model="form.name_in_organisation" required />
      </div>
      <button type="submit">{{ isEdit ? 'Update' : 'Submit' }}</button>
      <button type="button" @click="goBack">Back</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        id_service: 1, // Stała wartość
        valid_from: '',
        valid_to: '',
        card_number: '',
        card_type: 'Type A',
        certificate_serial_number: '',
        name_in_organisation: ''
      },
      isEdit: false,
      certificateId: null
    };
  },
  mounted() {
    this.prefillForm();
  },
  methods: {
    async submitForm() {
      try {
        const validFromUTC = new Date(this.form.valid_from).toISOString();
        const validToUTC = new Date(this.form.valid_to).toISOString();

        const newCertificate = {
          id_service: this.form.id_service,
          valid_from: validFromUTC,
          valid_to: validToUTC,
          card_number: this.form.card_number,
          card_type: this.form.card_type,
          certificate_serial_number: this.form.certificate_serial_number,
          name_in_organisation: this.form.name_in_organisation
        };

        const url = this.isEdit ? `/api/certificate/update/${this.certificateId}` : '/api/certificate/insert';
        const method = 'POST';

        const response = await fetch(url, {
          method: method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(newCertificate)
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        this.$router.push('/certificates');
      } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
      }
    },
    prefillForm() {
      const query = this.$route.query;

      if (Object.keys(query).length) {
        this.isEdit = true;
        this.certificateId = query.id_certificate;
        this.form.id_service = query.id_service;
        this.form.valid_from = new Date(query.valid_from).toISOString().slice(0, 16);
        this.form.valid_to = new Date(query.valid_to).toISOString().slice(0, 16);
        this.form.card_number = query.card_number;
        this.form.card_type = query.card_type;
        this.form.certificate_serial_number = query.certificate_serial_number;
        this.form.name_in_organisation = query.name_in_organisation;
      }
    },
    goBack() {
      this.$router.push('/certificates');
    }
  }
};
</script>

<style src="@/assets/style.css"></style>
