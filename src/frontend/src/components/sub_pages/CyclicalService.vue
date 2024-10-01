<template>
  <div>
    <h1 style="margin-bottom: 20px;">Cycles list</h1>
    <div class="container">
      <router-link to="/add-cycle" class="add-button">Add new cycle</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.agreementNumber" placeholder="Search by agreement number" style="margin-bottom: 10px; margin-right: 10px">
        <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.description" placeholder="Insert description" style="margin-bottom: 10px; margin-right: 10px;">
        </div>
        <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
      </div>
    </div>
    <div class="days-filter">
      <button @click="fetchCycles(7)" :class="{ active: selectedDays === 7 }">7 Days</button>
      <button @click="fetchCycles(14)" :class="{ active: selectedDays === 14 }">14 Days</button>
      <button @click="fetchCycles(30)" :class="{ active: selectedDays === 30 }">30 Days</button>
      <button @click="fetchCycles(60)" :class="{ active: selectedDays === 60 }">60 Days</button>
      <button @click="fetchAllCycles" :class="{ active: selectedDays === 'all' }">All</button>
    </div>
    <table>
      <thead>
      <tr>
        <th @click="sortBy('getIdCyclicalService')">
          <u>ID</u>
          <span :class="getSortIcon('getIdCyclicalService')"></span>
        </th>
        <th>Agreement Number</th>
        <th>Price</th>
        <th>Description</th>
        <th>Business Name</th>
        <th>Service User</th>
        <th>Certificate ID</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cycle in filteredCycles" :key="cycle.getIdCyclicalService">
        <td>{{ cycle.getIdCyclicalService }}</td>
        <td>{{ cycle.agreementNumber }}</td>
        <td>{{ cycle.price }}</td>
        <td>{{ cycle.description }}</td>
        <td>{{ cycle.business.businessName }}</td>
        <td>{{ cycle.serviceUser.name + ' ' + cycle.serviceUser.getSurname }}</td>
        <td>
          {{ cycle.certificate.idCertificate }}
          <button @click="toggleCertificateDetails(cycle.certificate.idCertificate)" class="view-button">...</button>
        </td>
        <td>
          <button class="action-button delete-button" @click="deleteCycle(cycle.getIdCyclicalService)">Delete</button>
<!--          NIE DZIAŁA JESZCZE-->
          <!--          <button class="action-button renew-button" @click="renewCycle(cycle.getIdCyclicalService)">Renew</button>-->
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="selectedCertificate">
      <h2 style="margin-top: 30px;">Certificate Details for ID: {{ selectedCertificate.idCertificate }}</h2>
      <table>
        <thead>
        <tr>
          <th>Certificate ID</th>
          <th>Serial Number</th>
          <th>Valid From</th>
          <th>Valid To</th>
          <th>Card Type</th>
          <th>Card Number</th>
          <th>Name in Organisation</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>{{ selectedCertificate.idCertificate }}</td>
          <td>{{ selectedCertificate.certificateSerialNumber }}</td>
          <td>{{ formatDate(selectedCertificate.validFrom) }}</td>
          <td>{{ formatDate(selectedCertificate.validTo) }}</td>
          <td>{{ selectedCertificate.cardType }}</td>
          <td>{{ selectedCertificate.cardNumber }}</td>
          <td>{{ selectedCertificate.nameInOrganisation }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CyclesList',
  data() {
    return {
      cycles: [],
      sortKey: "getIdCyclicalService",
      sortOrders: {
        getIdCyclicalService: 1
      },
      searchFields: {
        agreementNumber: '',
        description: '',
      },
      showAdditionalFields: false,
      selectedDays: 60,
      selectedCertificate: null
    };
  },
  mounted() {
    this.fetchCycles(this.selectedDays);
  },
  methods: {
    toggleSearchFields() {
      this.showAdditionalFields = !this.showAdditionalFields;
    },
    sortBy(key) {
      this.sortOrders[key] = this.sortOrders[key] * 1 || -1;
      this.sortKey = key;
    },
    getSortIcon(key) {
      if (this.sortKey === key) {
        return this.sortOrders[key] === 1 ? "sort-icon asc" : "sort-icon desc";
      }
      return "sort-icon";
    },
    fetchCycles(days) {
      this.selectedDays = days;
      fetch(`/api/cyclicalservice/getAll?days=${days}`)
          .then((response) => {
            if (!response.ok) {
              throw new Error("Network response was not ok " + response.statusText);
            }
            return response.json();
          })
          .then((data) => {
            this.cycles = data;
          })
          .catch((error) => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    async fetchAllCycles() {
      this.selectedDays = 'all';
      fetch(`/api/cyclicalservice/getAll`)
          .then((response) => {
            if (!response.ok) {
              throw new Error("Network response was not ok " + response.statusText);
            }
            return response.json();
          })
          .then((data) => {
            this.cycles = data;
          })
          .catch((error) => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    async deleteCycle(id) {
      if (confirm("Are you sure you want to delete this cycle?")) {
        try {
          const response = await fetch(`/api/cyclicalservice/delete/${id}`, {
            method: 'DELETE'
          });

          if (!response.ok) {
            throw new Error('Network response was not ok');
          }

          this.cycles = this.cycles.filter(cycle => cycle.getIdCyclicalService !== id);
          alert('Cycle deleted successfully!');
        } catch (error) {
          console.error('There has been a problem with your fetch operation:', error);
          alert('There has been a problem with your fetch operation: ' + error.message);
        }
      }
    },
    // POKOMBINOWAĆ BO DATA NIE WCHODZI
    // async renewCycle(cycleId) {
    //   try {
    //     const cycle = this.cycles.find(c => c.getIdCyclicalService === cycleId);
    //     if (!cycle || !cycle.certificate) {
    //       alert('Cycle or certificate not found');
    //       return;
    //     }
    //
    //     const cycleStart = new Date(cycle.cycleStart);
    //     const cycleEnd = new Date(cycle.cycleEnd);
    //
    //     if (isNaN(cycleStart.getTime()) || isNaN(cycleEnd.getTime())) {
    //       throw new Error('Invalid date format in cycleStart or cycleEnd');
    //     }
    //
    //     const differenceInDays = Math.ceil((cycleEnd - cycleStart) / (1000 * 60 * 60 * 24));
    //
    //     const newCycleStart = new Date(cycleStart);
    //
    //     const newCycleEnd = new Date(newCycleStart);
    //     newCycleEnd.setDate(newCycleStart.getDate() + differenceInDays);
    //
    //      const requestBody = {
    //       cycleStart: newCycleStart.toISOString(),
    //       cycleEnd: newCycleEnd.toISOString(),
    //       cardNumber: cycle.certificate.cardNumber,
    //       cardType: cycle.certificate.cardType,
    //       certSerialNumber: cycle.certificate.certificateSerialNumber,
    //       nameInOrganisation: cycle.certificate.nameInOrganisation || ""
    //     };
    //
    //     const response = await fetch(`/api/cyclicalservice/renew/${cycleId}`, {
    //       method: 'POST',
    //       headers: {
    //         'Content-Type': 'application/json',
    //       },
    //       body: JSON.stringify(requestBody),
    //     });
    //
    //     if (!response.ok) {
    //       const errorText = await response.text();
    //       throw new Error(`Network response was not ok: ${errorText}`);
    //     }
    //
    //     const result = await response.text();
    //     alert(result);
    //
    //     const updatedCycles = this.cycles.map(c => {
    //       if (c.getIdCyclicalService === cycleId) {
    //         return { ...c, renewal_message_sent: true, renewed: true, cycleEnd: newCycleEnd.toISOString() };
    //       }
    //       return c;
    //     });
    //     this.cycles = updatedCycles;
    //   } catch (error) {
    //     console.error('There has been a problem with your fetch operation:', error);
    //     alert('There has been a problem with your fetch operation: ' + error.message);
    //   }
    // },
    toggleCertificateDetails(certificateId) {
      if (this.selectedCertificate && this.selectedCertificate.idCertificate === certificateId) {
        this.selectedCertificate = null;
      } else {
        this.selectedCertificate = this.cycles.find(cycle => cycle.certificate.idCertificate === certificateId).certificate;
      }
    },
    formatDate(date) {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
  },
  computed: {
    filteredCycles() {
      const searchAgreementLower = this.searchFields.agreementNumber.toLowerCase();
      const searchDescriptionLower = this.searchFields.description.toLowerCase();
      return this.cycles
          .filter(cycle =>
              cycle.agreementNumber.toLowerCase().includes(searchAgreementLower) &&
              cycle.description.toLowerCase().includes(searchDescriptionLower)
          )
          .sort((a, b) => {
            let order = this.sortOrders[this.sortKey] || 1;
            if (a[this.sortKey] < b[this.sortKey]) return -1 * order;
            if (a[this.sortKey] > b[this.sortKey]) return 1 * order;
            return 0;
          });
    }
  }
};
</script>

<style>
.view-button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 2px 8px;
  font-size: 12px;
  cursor: pointer;
  outline: none;
  margin: 0;
}

.view-button:hover {
  background-color: #0056b3;
}

.renew-button {
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 2px 8px;
  font-size: 12px;
  cursor: pointer;
  outline: none;
}

.renew-button:hover {
  background-color: #218838;
}
</style>
