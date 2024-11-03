<template>
  <div>
    <h1 style="margin-bottom: 20px;">Lista firm oferujących płatności cykliczne</h1>
    <div class="container">
      <router-link to="/add-business" class="add-button">Dodaj nową firmę</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.name" placeholder="Nazwa" style="margin-bottom: 10px; margin-right: 10px;">
        <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.nip" placeholder="NIP" style="margin-bottom: 10px; margin-right: 10px;">
          <input type="text" class="input" v-model="searchFields.regon" placeholder="REGON" style="margin-bottom: 10px; margin-right: 10px;">
        </div>
        <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
      </div>
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Nazwa</th>
        <th>Adres</th>
        <th>Dane kontaktowe</th>
        <th>Opis</th>
        <th>NIP</th>
        <th>REGON</th>
        <th>Działania</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="business in filteredBusinesses" :key="business.idBusiness">
        <td>{{ business.idBusiness }}</td>
        <td @click="toggleDetails(business.idBusiness)" class="clickable">{{ business.name }}</td>
        <td>{{ business.adres }}</td>
        <td>
          {{ business.contactData ? business.contactData.idContactData : 'N/A' }}
          <button v-if="business.contactData" @click="viewContactData(business.idBusiness)" class="view-button" data-bs-toggle="modal" data-bs-target="#contactModal">...</button>
        </td>
        <td>{{ business.comments }}</td>
        <td>{{ business.nip }}</td>
        <td>{{ business.regon }}</td>
        <td>
          <button class="action-button edit-button" @click="editBusiness(business.idBusiness)">Edytuj</button>
          <button v-if="isAdmin" class="action-button delete-button" @click="deleteBusiness(business.idBusiness)">Usuń</button>
        </td>
      </tr>
      </tbody>
    </table>


    <div v-if="expandedBusiness !== null">
      <h3 style="margin-top: 10px">Szczegóły dla firmy: {{ getBusinessName(expandedBusiness) }}</h3>
      <div v-if="sortedExpandedBusinessDetails.length">
        <table>
          <thead>
          <tr>
            <th>Imię</th>
            <th>Nazwisko</th>
            <th>Typ Usługi</th>
            <th>Data ważności</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="detail in sortedExpandedBusinessDetails" :key="detail.name">
            <td>{{ detail.name }}</td>
            <td @click="viewUserData(detail.id_user)" class="clickable" data-bs-toggle="modal" data-bs-target="#userContactData">
              {{ detail.surname }}
            </td>
            <td>{{translateCardType(detail.cardType)}} ważny {{calculateCertLen(detail.validFrom,detail.validTo)}} lata</td>
            <td>{{ formatDate(detail.validTo) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <p v-else>Brak przypisanych użytkowników i cykli.</p>
    </div>

    <div id="contactModal" class="modal fade" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Dane kontaktowe firmy</h2>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div v-if="contactDataDetails">
              <p><strong>Emaile:</strong></p>
              <ul>
                <li v-for="email in contactDataDetails.emails" :key="email.idEmail">{{ email.email }}</li>
              </ul>
              <p><strong>Numery telefonów:</strong></p>
              <ul>
                <li v-for="phone in contactDataDetails.phoneNumbers" :key="phone.idPhoneNumber">{{ phone.number }}</li>
              </ul>
            </div>
            <div v-else>
              <p>Brak danych</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id="userContactData" class="modal fade" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h3>Dane kontaktowe użytkownika przypisanego do firmy</h3>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedUserData">
              <p><strong>Imię:</strong> {{ selectedUserData.name }}</p>
              <p><strong>Nazwisko:</strong> {{ selectedUserData.surname }}</p>
              <p><strong>PESEL:</strong> {{ selectedUserData.taxIdentificationNumber === null ? "brak" : selectedUserData.taxIdentificationNumber}}</p>
              <p><strong>Emaile:</strong></p>
              <ul>
                <li v-for="email in selectedUserData.contactData.emails" :key="email.idEmail">{{ email.email }}</li>
              </ul>
              <p><strong>Numery telefonów:</strong></p>
              <ul>
                <li v-for="phone in selectedUserData.contactData.phoneNumbers" :key="phone.idPhoneNumber">{{ phone.number }}</li>
              </ul>
            </div>
            <div v-else>
              <p>Brak danych</p>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { getCookie, fetchWrapper, translateCardType } from '@/utility';

export default {
  name: 'BusinessService',
  data() {
    return {
      businesses: [],
      searchFields: {
        name: '',
        nip: '',
        regon: ''
      },
      showAdditionalFields: false,
      showModal: false,
      contactDataDetails: null,
      expandedBusiness: null,
      selectedUserData: null,
      expandedBusinessDetails: [],
    };
  },
  computed: {
    isAdmin() {
      return this.$store.state.role === "ROLE_admin";
    },
    filteredBusinesses() {
      return this.businesses.filter(business => {
        return (
            business.name.toLowerCase().includes(this.searchFields.name.toLowerCase()) &&
            business.nip.toLowerCase().includes(this.searchFields.nip.toLowerCase()) &&
            business.regon.toLowerCase().includes(this.searchFields.regon.toLowerCase())
        );
      });
    },
    sortedExpandedBusinessDetails() {
      return this.expandedBusinessDetails.slice().sort((a, b) => {
        return new Date(a.validTo) - new Date(b.validTo);
      });
    }
  },
  mounted() {
    this.fetchBusinesses();
  },
  methods: {
    translateCardType,
    calculateCertLen(sDate,eDate){
      const d1 = new Date(sDate);
      const d2 = new Date(eDate);
      // console.log(this.expandedBusinessDetails,eDate);
      
      return d2.getFullYear()-d1.getFullYear();
    },

    toggleDetails(businessId) {
      if (this.expandedBusiness === businessId) {
        this.expandedBusiness = null;
        this.expandedBusinessDetails = [];
      } else {
        this.expandedBusiness = businessId;
        this.fetchDetailsForBusiness(businessId);
      }
    },
    fetchDetailsForBusiness(businessId) {
      fetchWrapper(this, `/api/cyclicalservice/getAllByBusiness?businessID=${businessId}`)
          .then(response => response.json())
          .then(data => {
            const details = data.map(cycle => ({
              id_user: cycle.serviceUser.idServiceUser,
              name: cycle.serviceUser.name,
              surname: cycle.serviceUser.getSurname,
              agreementNumber: cycle.agreementNumber,
              validTo: cycle.certificate.validTo,
              validFrom: cycle.certificate.validFrom,
              id: cycle.getIdCyclicalService,
              cardType: cycle.certificate.cardType
            }));
            this.expandedBusinessDetails = details;
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    getBusinessName(businessId) {
      const business = this.businesses.find(b => b.idBusiness === businessId);
      return business ? business.name : 'N/A';
    },
    toggleSearchFields() {
      this.showAdditionalFields = !this.showAdditionalFields;
    },
    fetchBusinesses() {
      fetchWrapper(this, "/api/business/getAll")
          .then(response => response.json())
          .then(data => {
            this.businesses = data;
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    editBusiness(idBusiness) {
      this.$router.push({
        path: '/add-business',
        query: {
          idBusiness: idBusiness
        }
      });
    },
    async deleteBusiness(idBusiness) {
      if (confirm('Are you sure you want to delete this business service?')) {
        try {
          const cookie = getCookie("XSRF-TOKEN");
          const response = await fetchWrapper(this, `/api/business/delete/${idBusiness}`, {
            method: 'DELETE',
            headers: {
              'X-XSRF-TOKEN': cookie
            }
          });

          if (!response.ok) {
            if (response.status === 409) {
              throw new Error('Cannot delete, assigned cycle');
            } else {
              throw new Error('Network response was not ok');
            }
          }

          this.businesses = this.businesses.filter(business => business.idBusiness !== idBusiness);
          alert('Business deleted successfully!');
        } catch (error) {
          console.error('There has been a problem with your fetch operation:', error);
          alert(error.message);
        }
      }
    },
    viewContactData(id) {
      this.showModal = true;
      this.contactDataDetails = null;
      fetchWrapper(this, `/api/business/get/${id}`)
          .then(response => response.json())
          .then(data => {
            this.contactDataDetails = data.contactData;
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    //////
    viewUserData(UserDataId) {
      this.selectedUserData = null; // Reset details
      fetchWrapper(this, `/api/serviceUser/get/${UserDataId}`)
          .then(response => response.json())
          .then(data => {
            this.selectedUserData = data;
          })
          .catch(error => {
            console.error("Problem z operacją:", error);
          });
    },
    ////////
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
    closeModal() {
      this.showModal = false;
    }
  }
};
</script>

<style src="@/assets/style.css"></style>

<style>
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

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

.clickable {
  text-decoration: underline;
  cursor: pointer;
}

.clickable:hover {
  color: #0056b3;
}

</style>
