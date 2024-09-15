<template>
  <div>
    <h1 style="margin-bottom: 20px;">Certificates List</h1>
    <div class="container">
      <button @click="openAddCertificateDialog" class="add-button">Add new Certificate</button>
      <input type="text" class="input" v-model="search" placeholder="Insert certificate serial number" style="margin-bottom: 10px;">
    </div>
    <!-- Add Certificate Dialog -->
    <div v-if="showAddCertificateDialog" class="dialog-overlay">
      <div class="dialog">
        <button class="margin" @click="chooseManualEntry">Manual Entry</button>
        <button class="margin" @click="chooseFileUpload">Upload from File</button>
        <button class="margin" @click="closeAddCertificateDialog">Cancel</button>
      </div>
    </div>
    <table>
      <thead>
      <tr>
        <th>ID Certificate</th>
        <th>ID Service</th>
        <th>Valid From</th>
        <th>Valid To</th>
        <th>Card Number</th>
        <th>Card Type</th>
        <th>Certificate Serial Number</th>
        <th>Name in Organisation</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="certificate in filteredCertificates" :key="certificate.id_certificate">
        <td>{{ certificate.id_certificate }}</td>
        <td>{{ certificate.id_service }}</td>
        <td>{{ formatDate(certificate.valid_from) }}</td>
        <td>{{ formatDate(certificate.valid_to) }}</td>
        <td>{{ certificate.card_number }}</td>
        <td>{{ certificate.card_type }}</td>
        <td>{{ certificate.certificate_serial_number }}</td>
        <td>{{ certificate.name_in_organisation }}</td>
        <td>
          <button class="action-button edit-button" @click="editCertificate(certificate.id_certificate)">Edit</button>
          <button class="action-button delete-button" @click="deleteCertificate(certificate.id_certificate)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'CertificateList',
  data() {
    return {
      certificates: [],
      search: '',
      showAddCertificateDialog: false
    };
  },
  computed: {
    filteredCertificates() {
      return this.certificates.filter(certificate =>
          certificate.certificate_serial_number.toLowerCase().includes(this.search.toLowerCase())
      );
    }
  },
  mounted() {
    fetch("/api/certificate/getAll")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
          }
          return response.json();
        })
        .then((data) => {
          this.certificates = data;
        })
        .catch((error) => {
          console.error("There has been a problem with your fetch operation:", error);
        });

    this.certificates = [
      {
        id_certificate: 1,
        id_service: 101,
        valid_from: '2023-01-01T00:00:00Z',
        valid_to: '2024-01-01T00:00:00Z',
        card_number: '1234567890',
        card_type: 'Type A',
        certificate_serial_number: 'ABC123',
        name_in_organisation: 'John Doe'
      },
      {
        id_certificate: 2,
        id_service: 102,
        valid_from: '2022-05-15T00:00:00Z',
        valid_to: '2023-05-15T00:00:00Z',
        card_number: '0987654321',
        card_type: 'Type B',
        certificate_serial_number: 'DEF456',
        name_in_organisation: 'Jane Smith'
      },
      {
        id_certificate: 3,
        id_service: 103,
        valid_from: '2021-08-20T00:00:00Z',
        valid_to: '2022-08-20T00:00:00Z',
        card_number: '1122334455',
        card_type: 'Type C',
        certificate_serial_number: 'GHI789',
        name_in_organisation: 'Jim Beam'
      }
    ];
  },
  methods: {
    formatDate(date) {
      return new Intl.DateTimeFormat('en-GB', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      }).format(new Date(date));
    },
    editCertificate(id_certificate) {
      const certificate = this.certificates.find(c => c.id_certificate === id_certificate);
      if (certificate) {
        this.$router.push({
          path: '/add-certificate',
          query: {
            id_certificate: certificate.id_certificate,
            id_service: certificate.id_service,
            valid_from: certificate.valid_from,
            valid_to: certificate.valid_to,
            card_number: certificate.card_number,
            card_type: certificate.card_type,
            certificate_serial_number: certificate.certificate_serial_number,
            name_in_organisation: certificate.name_in_organisation
          }
        });
      }
    },
    deleteCertificate(id_certificate) {
      if (confirm("Are you sure you want to delete this certificate?")) {
        fetch(`/api/certificate/delete/${id_certificate}`, {
          method: 'DELETE'
        })
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok');
              }
              this.certificates = this.certificates.filter(certificate => certificate.id_certificate !== id_certificate);
            })
            .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
            });
      }
    },
    openAddCertificateDialog() {
      this.showAddCertificateDialog = true;
    },
    closeAddCertificateDialog() {
      this.showAddCertificateDialog = false;
    },
    chooseManualEntry() {
      this.$router.push('/add-certificate');
    },
    chooseFileUpload() {
      this.$router.push('/upload-certificate');
    }
  }
};
</script>

<style src="@/assets/style.css"></style>
