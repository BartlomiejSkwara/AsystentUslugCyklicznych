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
        <th>Status</th>
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
        <td>{{ cycle.statusBitmask }}</td>
        <td>{{ cycle.business.businessName }}</td>
        <td>{{ cycle.serviceUser.name + ' ' + cycle.serviceUser.getSurname }}</td>
        <td>
          {{ cycle.certificate.idCertificate }}
        </td>
        <td>{{ formatDate(cycle.certificate.validTo) }}</td>
        <td>{{ cycle.certificate.cardType }}</td>
        <td>
          <!-- Przycisk usuwania wyświetla się tylko dla roli admin lub editor -->


          <button v-if="isAdminOrEditor" class="action-button cancel-button" @click="switchRequestModalVisibility(cycle.getIdCyclicalService,STATUS_TYPES.BLANK,cycle.statusBitmask)">Zmiana Statusu</button>
          <button v-if="cancelRequestElligable(cycle.accountUsername,cycle.statusBitmask)" class="action-button cancel-button" @click="switchRequestModalVisibility(cycle.getIdCyclicalService,STATUS_TYPES.CANCEL_REQUEST,cycle.statusBitmask)">Anulowanie Prośba</button>
          <button v-if="requestRenewalElligable(cycle.accountUsername,cycle.statusBitmask)" class="action-button edit-button" @click="switchRequestModalVisibility(cycle.getIdCyclicalService,STATUS_TYPES.AWAITING_RENEWAL,cycle.statusBitmask)">Odnowienie Prośba</button>
          <button v-if="isAdmin" class="action-button delete-button" @click="deleteCycle(cycle.getIdCyclicalService)">Usuń</button>
          <button v-if="!cycle.oneTime" class="action-button renew-button"><router-link :to="`/renew-cycle/${cycle.getIdCyclicalService}`" class="renew">Odnów</router-link></button>

        </td>
      </tr>
      </tbody>
    </table>
  </div>

      <!-- Comment modal -->
      <!--  -->
<!--  -->
    <div v-if="showRequestModal" class="modal" tabindex="-1" style="display: block ;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Doprecyzuj szczegóły operacji {{requestType.desc}} na usłudze nr. {{serviceReferencedByModal}}</h2>

          </div>
          <div class="modal-body ">

              <label for="Komentarz">Komentarz:</label>
              <br>
              <textarea name="Komentarz" v-model="comment"></textarea>
              <span v-if="requestType == STATUS_TYPES.BLANK">
                <br>
                <label for="Stan">Stan:</label>
                <br>
                <select id="Stan" v-model="newStatus">
                  <option v-for="(status,key) in availableStatuses" :key="key" :value="status.mVal">
                    {{ status.desc }}
                  </option>
                </select>
              </span>
              <p class="text-danger">{{ errorMessage }}</p>
          </div>

          <div class="modal-footer">
            <button @click="submitRequest">Submit</button>
            <button @click="switchRequestModalVisibility(-1,-1,-1)">Cancel</button>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
// import { eventBus } from '@/eventBus.js'; // Import eventBus
import { getCookie } from '@/utility';

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
      showRequestModal: false,
      serviceReferencedByModal:0,
      bitmaskReferencedByModal:0,
      newStatus:0,
      errorMessage:'',
      requestType:{
          mVal : -1,
          desc : "BLANK"
      },
      STATUS_TYPES: {
        BLANK: {
          mVal : -1,
          desc : "Zmiana Stanu"
        },
        AWAITING_RENEWAL: {
          mVal : 1,
          desc : "Prośba o Odnowienie"
        },
        PRO_FORM_SENT: {
          mVal : 2,
          desc : "Pro Forma Wysłana "
        },
        CANCEL_REQUEST: {
          mVal : 4,
          desc : "Prośba o Anulowanie"
        },
        CANCELED:{
          mVal : 8,
          desc : "Anulowane"
        },
        MARKED_AS_NON_RENEWABLE:{
          mVal : 16,
          desc : "Nieodnawialny"
        },
        RENEWED_ELSEWHERE:{
          mVal : 32,
          desc : "Odnowiony gdzie indziej"
        },
        PAYMENT_DONE:{
          mVal : 64,
          desc : "Zapłata Otrzymana"
        },
        INVOICE_SENT:{
          mVal : 128,
          desc : "Faktura wystawiona kod otrzymany"
        },
        RENEWED:{
          mVal : 256,
          desc : "Odnowione"
        },

      },
      comment:'',
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

    requestRenewalElligable(uname,statusBitmask){
      return (uname == this.$store.state.username)&&(this.hasStatus(statusBitmask,this.STATUS_TYPES.RENEWED.mVal))
    },
    cancelRequestElligable(uname,statusBitmask){
      return (uname == this.$store.state.username)&&
        (!this.hasStatus(statusBitmask,this.STATUS_TYPES.CANCEL_REQUEST.mVal)&&!this.hasStatus(statusBitmask,this.STATUS_TYPES.CANCELED.mVal))
    },
    hasStatus(statusBitmask, status) {
      return (statusBitmask & status) !== 0;
    },
    async submitRequest() {

      // if (confirm("Are you sure you want to delete this cycle?")) {


      try {
        const cookie = getCookie("XSRF-TOKEN");
        let operation = "";
        switch (this.requestType.mVal) {
          case 1:
            operation = "renewalRequest";
            break;
          case 4:
            operation = "cancelRequest";
            break;
          default:
            operation = "statusChange";
            break;
        }

        let payload = {
            b:"322"
        };
        if(this.comment!=''){
          payload.comment = this.comment;

        }
        if(operation == "statusChange"){

          payload.requestedStateChange = this.newStatus;
          if(this.newStatus<=0){
            this.errorMessage = "Nie uzupełniono pola stan !!!"
            return;

          }
        }
        console.log(payload);

        /// todo poprawka
        const response = await fetch(`/api/cyclicalservice/${operation}/${this.serviceReferencedByModal}`, {
          method: 'POST',
          headers:{
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN':cookie
          },
          body: JSON.stringify(payload)
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }


        const cycle = this.cycles.find(cycle => cycle.getIdCyclicalService === this.serviceReferencedByModal);

        if (cycle) {
          let newMask = parseInt(await response.text(), 10);
          cycle.statusBitmask = newMask;
        }
        // this.cycles = this.cycles.filter(cycle => cycle.getIdCyclicalService !== id);

        alert(`Z powodzeniem dokonano operacji ${this.requestType.desc}!`);
        this.switchRequestModalVisibility(-1,-1,-1);

      } catch (error) {
        // próba odświeżenia a w wypadku X
        console.error('There has been a problem with your fetch operation:', error);
        alert('There has been a problem with your fetch operation: ' + error.message);
      }

      // }
    },

    switchRequestModalVisibility(serviceId,requestType,bitmask){
      if(this.showRequestModal){
        this.showRequestModal = false;
      }else{
        this.showRequestModal = true;
      }
      this.serviceReferencedByModal=serviceId;
      this.bitmaskReferencedByModal = bitmask;
      this.requestType = requestType;
      this.comment = '';
      this.errorMessage = '';
    },

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
      try {
        const response = await fetch(`/api/cyclicalservice/getAll`, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
        }

        const role = response.headers.get('frontRole');
        this.$store.commit('setRole', role);
        const data = await response.json();
        this.cycles = data;
      } catch (error) {
          console.error("There has been a problem with your fetch operation:", error);
      }


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
    availableStatuses(){
      let nonAvailable = [];
      nonAvailable.push(this.STATUS_TYPES.BLANK);
      nonAvailable.push(this.STATUS_TYPES.CANCEL_REQUEST);
      nonAvailable.push(this.STATUS_TYPES.AWAITING_RENEWAL);
      nonAvailable.push(this.STATUS_TYPES.RENEWED);
      let bitmask = this.bitmaskReferencedByModal;

      return Object.values(this.STATUS_TYPES).filter(curr => {
        if(nonAvailable.findIndex(blocked => {return blocked==curr}) == -1)
          return true;

        return !this.hasStatus(bitmask,curr.mVal);

      })
    },
    isAdminOrEditor() {
      return this.$store.state.role !== "ROLE_user";
    },
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
