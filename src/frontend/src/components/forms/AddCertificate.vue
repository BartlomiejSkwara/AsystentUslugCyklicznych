<template>
  <div>
    <h1>{{ isEdit ? 'Edytuj certyfikat' : 'Dodaj nowy certyfikat' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="id_service">ID Płatności:</label>
        <input type="number" id="id_service" v-model.number="form.id_service" class="form-control" required />
      </div>
      <div>
        <label for="valid_from">Data rozpoczęcia:</label>
        <input type="datetime-local" id="valid_from" v-model="form.valid_from" class="form-control" required />
      </div>
      <div>
        <label for="valid_to">Data zakończenia:</label>
        <input type="datetime-local" id="valid_to" v-model="form.valid_to" class="form-control"  required />
      </div>
      <div>
        <label for="card_number">Numer karty:</label>
        <input type="text" id="card_number" v-model="form.card_number" class="form-control" required />
      </div>
      <div>
        <label for="card_type">Typ karty:</label>
        <select id="card_type" v-model="form.card_type" required>
          <option value="Type A">Typ A</option>
          <option value="Type B">Typ B</option>
          <option value="Type C">Typ C</option>
        </select>
      </div>
      <div>
        <label for="certificate_serial_number">Numer seryjny certyfikatu:</label>
        <input type="text" id="certificate_serial_number" v-model="form.certificate_serial_number" class="form-control"  required />
      </div>
      <div>
        <label for="name_in_organisation">Dane osobowe:</label>
        <input type="text" id="name_in_organisation" v-model="form.name_in_organisation" class="form-control" required />
      </div>
      <button type="submit">{{ isEdit ? 'Zapisz' : 'Zapisz' }}</button>
      <button type="button" @click="goBack">Powrót</button>
    </form>
  </div>
</template>

<script>
import { refreshCSRF } from '@/utility';

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
    refreshCSRF();
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
