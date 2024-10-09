<template>
  <div>
    <h1 style="margin-bottom: 20px;">Lista cykli</h1>
    <div class="container">
      <router-link to="/add-cycle" class="add-button">Dodaj nową płatność cykliczną</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.agreementNumber" placeholder="Numer dokumentu" style="margin-bottom: 10px; margin-right: 10px">
        <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.description" placeholder="Opis" style="margin-bottom: 10px; margin-right: 10px;">
        </div>
        <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
      </div>
    </div>
    <div class="days-filter">
      <button @click="fetchCycles(7)" :class="{ active: selectedDays === 7 }">7 dni</button>
      <button @click="fetchCycles(14)" :class="{ active: selectedDays === 14 }">14 dni</button>
      <button @click="fetchCycles(30)" :class="{ active: selectedDays === 30 }">30 dni</button>
      <button @click="fetchCycles(60)" :class="{ active: selectedDays === 60 }">60 dni</button>
      <button @click="fetchAllCycles" :class="{ active: selectedDays === 'all' }">Wszystkie</button>
    </div>

    <table>
      <thead>
      <tr>
        <th @click="sortBy('getIdCyclicalService')">
          <u>ID</u>
          <span :class="getSortIcon('getIdCyclicalService')"></span>
        </th>
        <th>Numer dokumentu</th>
<!--        <th>Opis</th>-->
        <th>Firma</th>
        <th>Użytkownik</th>
        <th>Certyfikat</th>
        <th>Ważne do:</th>
        <th>Typ karty </th>
        <th>Działania</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cycle in filteredCycles" :key="cycle.getIdCyclicalService">
        <td>{{ cycle.getIdCyclicalService }}</td>
        <td>{{ cycle.agreementNumber }}</td>
<!--        <td>{{ cycle.description }}</td>-->
        <td>{{ cycle.business.businessName }}</td>
        <td>{{ cycle.serviceUser.name + ' ' + cycle.serviceUser.getSurname }}</td>
        <td>
          {{ cycle.certificate.idCertificate }}
        </td>
        <td>{{ formatDate(cycle.certificate.validTo) }}</td>
        <td>{{ cycle.certificate.cardType }}</td>
        <td>
          <!-- Przycisk usuwania wyświetla się tylko dla roli admin lub editor -->
          <button v-if="cycle.accountUsername == this.$store.state.username" class="action-button cancel-button" @click="requestCancel(cycle.getIdCyclicalService)">Anulowanie Prośba</button>
          <button v-if="cycle.accountUsername == this.$store.state.username" class="action-button edit-button" @click="requestRenewal(cycle.getIdCyclicalService)">Przedłużenie Prośba</button>
          <button v-if="isAdmin" class="action-button delete-button" @click="deleteCycle(cycle.getIdCyclicalService)">Usuń</button>
          <button v-if="!cycle.oneTime" class="action-button renew-button"><router-link :to="`/renew-cycle/${cycle.getIdCyclicalService}`" class="renew">Odnów</router-link></button>

        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
// import { eventBus } from '@/eventBus.js'; // Import eventBus
import { getCookie, refreshCSRF } from '@/utility';

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
      // userRole: 'ROLE_test', // Przechowuje bieżącą rolę użytkownika
      // username: ''
    };
  },
  created() {
    // this.username = this.$store.state.username;
    // eventBus.on('roleUpdate', this.updateUserRole);
    // eventBus.on('usernameUpdate', this.updateUsername)
  },
  mounted() {
    // console.log(this.$store.state.role);

    this.fetchCycles(this.selectedDays);
  },
  beforeUnmount() {
    // eventBus.off('roleUpdate', this.updateUserRole);
    // eventBus.off('usernameUpdate', this.updateUsername)
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
            const role = response.headers.get('frontRole');
            this.$store.commit('setRole', role);
            // eventBus.emit('roleUpdate', role);
            // this.updateUserRole(role);
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
       await fetch(`/api/cyclicalservice/getAll`)
          .then((response) => {
            if (!response.ok) {
              throw new Error("Network response was not ok " + response.statusText);
            }
            const role = response.headers.get('frontRole');
            this.$store.commit('setRole', role);

            // eventBus.emit('Cyclical Service emits changes', role);
            // this.updateUserRole(role);
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
          const cookie = getCookie("XSRF-TOKEN");

          const response = await fetch(`/api/cyclicalservice/delete/${id}`, {
            method: 'DELETE',
            headers:{
            'X-XSRF-TOKEN':cookie
            }
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

       refreshCSRF()  
      }
    },

    //TODO zapytanie sie nigdy nie powiedzie bo nie ma kiedy dodać komentarza 
    async requestCancel(id) {

      if (confirm("Czy na pewno chcesz wysłać prośbę o anulowanie tej usługi cyklicznej ?")) {
      

        try {
          const cookie = getCookie("XSRF-TOKEN");

          const response = await fetch(`/api/cyclicalservice/cancelRequest/${id}`, {
            method: 'POST',
            headers:{
            'X-XSRF-TOKEN':cookie
            }
          });

          if (!response.ok) {
            throw new Error('Network response was not ok');
          }

          this.cycles = this.cycles.filter(cycle => cycle.getIdCyclicalService !== id);
          alert('Wysłano prośbę o anulowanie!');
        } catch (error) {
          console.error('There has been a problem with your fetch operation:', error);
          alert('There has been a problem with your fetch operation: ' + error.message);
        }

      refreshCSRF()  
      }
    },

    async requestRenewal(id) {

      if (confirm("Czy na pewno chcesz wysłać prośbę o odnowienie tej usługi cyklicznej ?")) {


        try {
          const cookie = getCookie("XSRF-TOKEN");

          const response = await fetch(`/api/cyclicalservice/renewalRequest/${id}`, {
            method: 'POST',
            headers:{
            'X-XSRF-TOKEN':cookie
            }
          });

          if (!response.ok) {
            throw new Error('Network response was not ok');
          }

          this.cycles = this.cycles.filter(cycle => cycle.getIdCyclicalService !== id);
          alert('Wysłano odnowienie o anulowanie!');
        } catch (error) {
          console.error('There has been a problem with your fetch operation:', error);
          alert('There has been a problem with your fetch operation: ' + error.message);
        }

      refreshCSRF()  
      }
    },
    
    // updateUserRole(role) {
    //   console.log("Cyc ser Role = ",role);
    //   this.userRole = role;
    // },
    // updateUsername(username){
    //   console.log("Cyc ser Username = ", username);
      
    //   this.username = username;
    // },
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
    isAdmin() {
      return this.$store.state.role === "ROLE_admin";
      },

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
.renew{
  color: white;
  text-decoration: none;
}
</style>
