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
        <th>One Time</th>
        <th>Business ID</th>
        <th>Service User ID</th>
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
        <td>{{ cycle.oneTime }}</td>
        <td>{{ cycle.business.idBusiness }}</td>
        <td>{{ cycle.serviceUser.idServiceUser }}</td>
        <td>{{ cycle.certificate.idCertificate }}</td>
        <td>
          <button class="action-button delete-button" @click="deleteCycle(cycle.getIdCyclicalService)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
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
      selectedDays: 60
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

<style src="@/assets/style.css"></style>
