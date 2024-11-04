<template>
  <div>
    <h1 style="margin-bottom: 20px;">Lista certyfikatów</h1>
    <div class="container">
      <router-link v-if="isAdminOrEditor" to="/add-cycle" class="add-button">Dodaj nowy certyfikat</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.agreementNumber" placeholder="Numer dokumentu" style="margin-bottom: 10px; margin-right: 10px">
      </div>
    </div>
    <div style="display: inline-block;" class="days-filter">
      <button @click="fetchCycles(7)" :class="{ active: selectedDays === 7, 'dni-7': selectedDays === 7 }">7 dni</button>
      <button @click="fetchCycles(14)" :class="{ active: selectedDays === 14, 'dni-14': selectedDays === 14 }">14 dni</button>
      <button @click="fetchCycles(30)" :class="{ active: selectedDays === 30, 'dni-30': selectedDays === 30 }">30 dni</button>
      <button @click="fetchCycles(60)" :class="{ active: selectedDays === 60, 'dni-60': selectedDays === 60 }">60 dni</button>
<!--      <button @click="fetchAllCycles" :class="{ active: selectedDays === 'all', 'notRenewed': selectedDays === 'all' }">Nie Odnowione</button>-->
      <button @click="fetchAllCycles(1)" :class="{ active: selectedDays === 'getAll', 'dni-all': selectedDays === 'getAll' }">Wszystkie </button>
<!--      <button @click="fetchAllCycles(2)" :class="{ active: selectedDays === 'getAllExpired', 'expired': selectedDays === 'getAllExpired' }">Wygasłe </button>-->

      <select v-model="selectedStatus">
      <option value="all">Wszystkie statusy</option>
      <option v-for="status in filteredStatusTypes" :key="status.mVal" :value="status.mVal">
        {{ status.desc }}
      </option>
    </select>
      <button @click="fetchAllCycles" style="margin-left: 5px" :class="{ active: selectedDays === 'all', 'notRenewed': selectedDays === 'all' }">Nie Odnowione</button>
      <button @click="fetchAllCycles(2)" :class="{ active: selectedDays === 'getAllExpired', 'expired': selectedDays === 'getAllExpired' }">Wygasłe </button>
  </div>

    <table>
      <thead>
      <tr>
        <th @click="sortBy('getIdCyclicalService')">
          <u>ID</u>
          <span :class="getSortIcon('getIdCyclicalService')"></span>
        </th>
        <th>Typ Usługi</th>
        <th>Status</th>
        <th>Firma</th>
        <th>Użytkownik</th>

        <th>Ważne do:</th>
<!--        <th>Typ karty </th>-->
        <th></th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cycle in filteredCycles" :key="cycle.getIdCyclicalService">
        <td>{{ cycle.getIdCyclicalService }}</td>
        <td>{{ translateCardType(cycle.certificate.cardType) }}</td>
        <td>
          <!-- {{statusesList.length}} -->
          <!-- tak wiem, to nie jest optymalne, ale szkoda mi pamięci XD -->
          {{pickPriorityStatus(cycle.statusBitmask).result}}
          <button  v-if ="pickPriorityStatus(cycle.statusBitmask).moreThanOne"
          @click="switchStatusModalVisibility(cycle.statusBitmask)" class="view-button"
          data-bs-toggle="modal" data-bs-target="#statusDisplayModal"
          >...</button>

          
        </td>
        <td>{{ cycle.business.businessName }}</td>
        <td>{{ cycle.serviceUser.name + ' ' + cycle.serviceUser.getSurname }}</td>

        <td>{{ formatDate(cycle.certificate.validTo) }}</td>
<!--        <td>{{ cycle.certificate.cardType }}</td>-->
        <td>
          <!-- {{ cycle.certificate.idCertificate }} -->
            <button class="btn btn-primary" @click="detailsScreen(cycle)">Szczegóły</button>
        </td>
        <td>
          <!-- Przycisk usuwania wyświetla się tylko dla roli admin lub editor -->

          <div class="dropdown ">
            <button class="btn btn-primary dropdown-toggle" style="background-color: gray;" type="button" data-bs-toggle="dropdown" aria-expanded="false">
              Działania 
            </button>
            <ul class="dropdown-menu">
              <li v-if="isAdminOrEditor" >
                <a   class="dropdown-item " href="#" @click="switchRequestModalVisibility(cycle.getIdCyclicalService,STATUS_TYPES.BLANK,cycle.statusBitmask)" data-bs-toggle="modal" data-bs-target="#requestModal">Zmiana Statusu</a>
              </li>

              <li v-if="isAdminOrEditor" >
                <a   class="dropdown-item " href="#" @click="editCycle(cycle.getIdCyclicalService)">Edytuj Cykl</a>
              </li>

              <li v-if="cancelRequestElligable(cycle.accountUsername,cycle.statusBitmask)">
                <a  class="dropdown-item " href="#" @click="switchRequestModalVisibility(cycle.getIdCyclicalService,STATUS_TYPES.CANCEL_REQUEST,cycle.statusBitmask)" data-bs-toggle="modal" data-bs-target="#requestModal">Anulowanie Prośba</a>
              </li>

              <li v-if="requestRenewalElligable(cycle.accountUsername,cycle.statusBitmask)">
                <a  class="dropdown-item " href="#" @click="switchRequestModalVisibility(cycle.getIdCyclicalService,STATUS_TYPES.AWAITING_RENEWAL,cycle.statusBitmask)" data-bs-toggle="modal" data-bs-target="#requestModal">Odnowienie Prośba</a>
              </li>

              <li v-if="isAdmin">
                <a  class="dropdown-item" href="#" @click="deleteCycle(cycle.getIdCyclicalService)">Usuń</a>
              </li>

              <li v-if="(!cycle.oneTime)&&isAdminOrEditor" >
                <a class="dropdown-item " href="#" @click="renewCycle(cycle.getIdCyclicalService)">Odnów</a>
              </li>

            </ul>
          </div>

        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <!-- <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#requestModal"
    @click="switchRequestModalVisibility(0,STATUS_TYPES.BLANK,0)"
  >
    Launch demo modal
  </button> -->
  <!--  -->




    <!-- Status change modal  fade-->
    <div id="requestModal" class="modal fade" tabindex="-1" aria-labelledby="requestModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content ">
          <div class="modal-header modal-bg">
            <h2>Doprecyzuj szczegóły operacji {{requestType.desc}} na usłudze nr. {{serviceReferencedByModal}}</h2>

          </div>
          <div class="modal-body modal-bg">

              <label for="Komentarz">Komentarz:</label>
              <br>
              <textarea name="Komentarz" v-model="comment"></textarea>
              <span v-if="requestType == STATUS_TYPES.BLANK">
                <br>
                <label for="Status">Status:</label>
                <br>
                <select id="Status" v-model="newStatus">
                  <option v-for="(status,key) in availableStatuses" :key="key" :value="status.mVal">
                    {{ status.desc }}
                  </option>
                </select>
              </span>
              <p class="text-danger">{{ errorMessage }}</p>

          </div>

          <div class="modal-footer modal-bg">
            <button @click="submitRequest" class="btn btn-outline-success" >Zatwierdź</button>
            <button  id="closeRequest" class="btn btn-outline-secondary" data-bs-dismiss="modal">Powrót</button>
            <!-- @click="switchRequestModalVisibility(-1,-1,-1)" -->
          </div>
        </div>
      </div>
    </div>

    <!-- Status Display Modal -->
    <div id="statusDisplayModal" class="modal fade" tabindex="-1"  >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Lista obecnych statusów: </h2>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body ">
            
            <br>
            <ul>
              <li v-for="(status, index) in statusesList" :key="index">
                {{ status }}
              </li>
            </ul>
          </div>       
        </div>
      </div>
    </div>
</template>

<script>
// import { eventBus } from '@/eventBus.js'; // Import eventBus
import { getCookie,fetchWrapper, STATUS_TYPES_LIST, decodeStatus, hasStatus, translateCardType } from '@/utility';

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
      statusModalData:{
        showStatusModal: false,
        bitmask: 0
      },
      requestModalData:{

      },
      showAdditionalFields: false,
      selectedDays: 60,
      showRequestModal: false,
      serviceReferencedByModal:0,
      bitmaskReferencedByModal:0,
      newStatus:0,
      errorMessage:'',
      requestType:{
          mVal : 0,
          desc : "BLANK"
      },
      selectedStatus: 'all',
      STATUS_TYPES: STATUS_TYPES_LIST,
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
    this.$store.commit('setPassedValue', '');    

    this.fetchCycles(this.selectedDays);
  },
  beforeUnmount() {
    // eventBus.off('roleUpdate', this.updateUserRole);
    // eventBus.off('usernameUpdate', this.updateUsername)
  },

  methods: {
    translateCardType,
    editCycle(id){
      // console.log(id)
      const cycle = this.cycles.find(c => c.getIdCyclicalService === id)
      console.log(cycle);
      
      if (cycle) {
        this.$router.push({
          path: '/add-cycle',
          query: {
            businessId: cycle.idBusiness,
            cycleStart: cycle.certificate.valid_from,
            certificateLengthInYears: cycle.certificate.certificateLengthInYears,
            cardNumber: cycle.certificate.cardNumber,
            cardType: cycle.certificate.cardType,
            certSerialNumber: cycle.certificate.certSerialNumber,
            nameInOrganisation: cycle.certificate.nameInOrganisation,
            oneTime: cycle.oneTime,
            serviceUserId: cycle.serviceUser.idServiceUser,
            agreementNumber: cycle.agreementNumber,
            description: cycle.description
          }
        });
      }
    },


    renewCycle(cycSerId){
      this.$router.push(`/renew-cycle/${cycSerId}`)
    },
    detailsScreen(cycleData){
      this.$store.commit('setPassedValue', cycleData);    

      this.$router.push('/cycleDetails');
    },

    switchStatusModalVisibility(selectedBitmask){
      this.statusModalData.showStatusModal = !this.statusModalData.showStatusModal;
      this.statusModalData.bitmask = selectedBitmask;
    },

    requestRenewalElligable(uname,statusBitmask){
      return (uname == this.$store.state.username)&&(hasStatus(statusBitmask,this.STATUS_TYPES.RENEWED.mVal)||hasStatus(statusBitmask,this.STATUS_TYPES.NEW.mVal))
    },
    cancelRequestElligable(uname,statusBitmask){
      
      return (uname == this.$store.state.username)&&
        (!hasStatus(statusBitmask,this.STATUS_TYPES.CANCEL_REQUEST.mVal)&&!hasStatus(statusBitmask,this.STATUS_TYPES.CANCELED.mVal))
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
        // console.log(payload);

        /// todo poprawka
        const response = await fetchWrapper(this,`/api/cyclicalservice/${operation}/${this.serviceReferencedByModal}`, {
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
        document.getElementById('closeRequest').click();
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
      fetchWrapper(this,`/api/cyclicalservice/getAllAwaiting?days=${days}`)
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
    async fetchAllCycles(type) {
      
      let strType = "getAllAwaiting";
      this.selectedDays = 'all';

      if(type == 1){
        strType = "getAll"
        this.selectedDays = 'getAll'
      }
      else if (type == 2){
        strType = "getAllExpired"
        this.selectedDays = 'getAllExpired'

      }

      try {
        const response = await fetchWrapper(this,`/api/cyclicalservice/`+strType, {
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

          const response = await fetchWrapper(this,`/api/cyclicalservice/delete/${id}`, {
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
    },

    pickPriorityStatus(bitmask){
      let list = Object.values(this.STATUS_TYPES);
      let answ = {
        result: "Blank",
        moreThanOne: true
      }
      for (let i = list.length-1; i > 0; i--) {
        if(hasStatus(bitmask, list[i].mVal)){
          if(answ.moreThanOne == true){
            answ.result = list[i].desc;
            answ.moreThanOne = false;
          }
          else{
            answ.moreThanOne = true;
            break;
          } 
        } 
      }
      return answ;
    },


  },
  computed: {
    filteredStatusTypes() {
      return Object.values(this.STATUS_TYPES).filter(status => status.mVal !== 0); // 0 to wartość mVal dla BLANK
    },
    
    statusesList(){
      let bitmask = this.statusModalData.bitmask;

      return decodeStatus(bitmask);
    },
    availableStatuses(){
      let nonAvailable = [];
      nonAvailable.push(this.STATUS_TYPES.BLANK);
      nonAvailable.push(this.STATUS_TYPES.CANCEL_REQUEST);
      nonAvailable.push(this.STATUS_TYPES.AWAITING_RENEWAL);
      nonAvailable.push(this.STATUS_TYPES.RENEWED);
      nonAvailable.push(this.STATUS_TYPES.NEW);
      nonAvailable.push(this.STATUS_TYPES.IGNORED);
      nonAvailable.push(this.STATUS_TYPES.EXPIRED)

      let bitmask = this.bitmaskReferencedByModal;

      return Object.values(this.STATUS_TYPES).filter(curr => {
        let ok = true;
        if(nonAvailable.findIndex(blocked => {return blocked.mVal==curr.mVal}) != -1)
          return false;
        ok = !hasStatus(bitmask,curr.mVal);
        
        return ok;

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
          .filter(cycle => {
            const matchesSearch = cycle.agreementNumber.toLowerCase().includes(searchAgreementLower) &&
                cycle.description.toLowerCase().includes(searchDescriptionLower);


            let maxStatus = 0;
            Object.values(this.STATUS_TYPES).forEach(status => {
              if ((cycle.statusBitmask & status.mVal) === status.mVal) {
                maxStatus = Math.max(maxStatus, status.mVal);
              }
            });

            const matchesStatus = this.selectedStatus === 'all' || this.selectedStatus == maxStatus;

            return matchesSearch && matchesStatus;
          })
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
<style>
.renew{
  color: white;
  text-decoration: none;
}
</style>
