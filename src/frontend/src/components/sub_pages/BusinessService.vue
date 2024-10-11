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
        <td>{{ business.name }}</td>
        <td>{{ business.adres }}</td>
        <td>
          {{ business.contactData ? business.contactData.idContactData : 'N/A' }}
          <button v-if="business.contactData" @click="viewContactData(business.idBusiness)" class="view-button">...</button>
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

    <!-- TESTYYY -->
    <div v-if="showModal" class="modal" tabindex="-1" style="display: block ;">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h2>Dane kontaktowe</h2>
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
</template>

<script>
import { getCookie, fetchWrapper } from '@/utility';
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
    }
  },
  mounted() {
    this.fetchBusinesses();
  },
  methods: {
    toggleSearchFields() {
      this.showAdditionalFields = !this.showAdditionalFields;
    },
    fetchBusinesses() {
      fetchWrapper(this,"/api/business/getAll")
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

        try{
          const cookie = getCookie("XSRF-TOKEN");
          const response = await fetchWrapper(this,`/api/business/delete/${idBusiness}`, {
            method: 'DELETE',
            headers:{
              'X-XSRF-TOKEN':cookie
            }
          })

          //.then(response => {
          if (!response.ok) {
            if (response.status === 409) {
              throw new Error('Cannot delete, assigned cycle');
            } else {
              throw new Error('Network response was not ok');
            }
          }
          this.businesses = this.businesses.filter(business => business.idBusiness !== idBusiness);
          alert('Business deleted successfully!');
          //})
        }catch(error){
          // .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
              alert(error.message);
            // });
        }
      }
    },
    viewContactData(id) {
      this.showModal = true;
      this.contactDataDetails = null;
      fetchWrapper(this,`/api/business/get/${id}`)
          .then(response => response.json())
          .then(data => {
            this.contactDataDetails = data.contactData;
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    closeModal() {
      this.showModal = false;
    }
  }
};
</script>

<style src="@/assets/style.css"></style>

<style>
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: page ;
  top: 20%;
  left: 30%;
  right: 0;
  bottom: 0;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  max-width: 600px;
  width: 90%;
}

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
</style>
