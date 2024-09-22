<template>
  <div>
    <h1 style="margin-bottom: 20px;">Business Services List</h1>
    <div class="container">
      <router-link to="/add-business" class="add-button">Add new Business Service</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.name" placeholder="Insert name" style="margin-bottom: 10px; margin-right: 10px;">
        <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.nip" placeholder="Insert NIP" style="margin-bottom: 10px; margin-right: 10px;">
          <input type="text" class="input" v-model="searchFields.regon" placeholder="Insert REGON" style="margin-bottom: 10px; margin-right: 10px;">
        </div>
        <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
      </div>
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Address</th>
        <th>Contact Data</th>
        <th>Comments</th>
        <th>NIP</th>
        <th>REGON</th>
        <th>Actions</th>
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
          <button class="action-button edit-button" @click="editBusiness(business.idBusiness)">Edit</button>
          <button class="action-button delete-button" @click="deleteBusiness(business.idBusiness)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- TESTYYY -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h2>Contact Data Details</h2>
        <div v-if="contactDataDetails">
          <p><strong>Emails:</strong></p>
          <ul>
            <li v-for="email in contactDataDetails.emails" :key="email.idEmail">{{ email.email }}</li>
          </ul>
          <p><strong>Phone Numbers:</strong></p>
          <ul>
            <li v-for="phone in contactDataDetails.phoneNumbers" :key="phone.idPhoneNumber">{{ phone.number }}</li>
          </ul>
        </div>
        <div v-else>
          <p>Loading...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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
      contactDataDetails: null
    };
  },
  computed: {
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
      fetch("/api/business/getAll")
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
    deleteBusiness(idBusiness) {
      if (confirm('Are you sure you want to delete this business service?')) {
        fetch(`/api/business/delete/${idBusiness}`, {
          method: 'DELETE'
        })
            .then(response => {
              if (!response.ok) {
                if (response.status === 409) {
                  throw new Error('Cannot delete, assigned cycle');
                } else {
                  throw new Error('Network response was not ok');
                }
              }
              this.businesses = this.businesses.filter(business => business.idBusiness !== idBusiness);
              alert('Business deleted successfully!');
            })
            .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
              alert(error.message);
            });
      }
    },
    viewContactData(id) {
      this.showModal = true;
      this.contactDataDetails = null;
      fetch(`/api/business/get/${id}`)
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
