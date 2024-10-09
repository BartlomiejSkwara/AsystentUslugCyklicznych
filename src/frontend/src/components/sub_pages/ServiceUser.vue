<template>
  <div>



    <h1 style="margin-bottom: 20px;">Lista użytkowników</h1>
    <div class="container">
      <router-link to="/add-user" class="add-button">Dodaj nowego użytkownika</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.surname" placeholder="Nazwisko" style="margin-bottom: 10px; margin-right: 10px;">
        <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.name" placeholder="Imię" style="margin-bottom: 10px; margin-right: 10px;">
        </div>
        <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
      </div>
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Imię</th>
        <th>Nazwisko</th>
        <th>Kontakt</th>
        <th>Polski PESEL?</th>
        <th>PESEL</th>
        <th>Dodatkowy opis</th>
        <th>Działania</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in paginatedUsers" :key="user.idServiceUser">
        <td>{{ user.idServiceUser }}</td>
        <td>{{ user.name }}</td>
        <td>{{ user.surname }}</td>
        <td>
          {{ user.contactData ? user.contactData.idContactData : 'N/A' }}
          <button v-if="user.contactData" @click="viewContactData(user.idServiceUser)" class="view-button">...</button>
        </td>
        <td>{{ user.hasPolishPesel }}</td>
        <td>{{ user.taxIdentificationNumber }}</td>
        <td>{{ user.comments }}</td>
        <td>
          <button class="action-button edit-button" @click="editUser(user.idServiceUser)">Edytuj</button>
          <button class="action-button delete-button" @click="deleteUser(user.idServiceUser)">Usuń</button>
<!--          <button class="action-button data-button" @click="toggleCycles(user.idServiceUser)">Cykle</button>-->
<!--          <button class="action-button data-button" @click="toggleBusinesses(user.idServiceUser)">Firmy</button>-->
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button :disabled="currentPage === 1" @click="prevPage">Poprzednia</button>
      <span>Strona {{ currentPage }} z {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="nextPage">Następna</button>
    </div>

    <div v-if="showContactDataModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeContactDataModal">&times;</span>
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
          <p>No data available</p>
        </div>
      </div>
    </div>


    <div v-if="selectedUser && cyclicalServices.length">
      <h2 style="margin-top: 30px;">Cyclical Services for User ID: {{ selectedUser }}</h2>
      <table>
        <thead>
        <tr>
          <th>Agreement Number</th>
          <th>Description</th>
          <th>Price</th>
          <th>Business Name</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="service in cyclicalServices" :key="service.agreementNumber">
          <td>{{ service.agreementNumber }}</td>
          <td>{{ service.description }}</td>
          <td>{{ service.price }}</td>
          <td>{{ service.businessName }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-if="selectedBusinessUser && businesses.length">
      <h2 style="margin-top: 30px;">Businesses for User ID: {{ selectedBusinessUser }}</h2>
      <table>
        <thead>
        <tr>
          <th>Name</th>
          <th>NIP</th>
          <th>Address</th>
          <th>REGON</th>
          <th>Contact</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="business in businesses" :key="business.idBusiness">
          <td>{{ business.name }}</td>
          <td>{{ business.nip }}</td>
          <td>{{ business.adres }}</td>
          <td>{{ business.regon }}</td>
          <td>
            <button @click="viewBusinessContact(business.idBusiness)" class="view-button">View Contact</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showBusinessContactModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeBusinessContactModal">&times;</span>
        <h2>Business Contact Details</h2>
        <div v-if="businessContactDetails">
          <p><strong>Emails:</strong></p>
          <ul>
            <li v-for="email in businessContactDetails.emails" :key="email.idEmail">{{ email.email }}</li>
          </ul>
          <p><strong>Phone Numbers:</strong></p>
          <ul>
            <li v-for="phone in businessContactDetails.phoneNumbers" :key="phone.idPhoneNumber">{{ phone.number }}</li>
          </ul>
        </div>
        <div v-else>
          <p>No data available</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCookie } from '@/utility';
export default {
  name: 'ServiceUserList',
  data() {
    return {
      users: [],
      searchFields: {
        surname: '',
        name: '',
      },
      showAdditionalFields: false,
      selectedUser: null,
      cyclicalServices: [],
      selectedBusinessUser: null,
      businesses: [],
      currentPage: 1,
      usersPerPage: 8,
      showContactDataModal: false,
      contactDataDetails: null,
      showBusinessContactModal: false,
      businessContactDetails: null,
    };
  },
  computed: {
    filteredUsers() {
      return this.users.filter(user => {
        return (
            user.surname.toLowerCase().includes(this.searchFields.surname.toLowerCase()) &&
            (!this.showAdditionalFields || (
                user.name.toLowerCase().includes(this.searchFields.name.toLowerCase())
            ))
        );
      });
    },
    paginatedUsers() {
      const start = (this.currentPage - 1) * this.usersPerPage;
      const end = start + this.usersPerPage;
      return this.filteredUsers.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.usersPerPage);
    }
  },
  methods: {
    toggleSearchFields() {
      this.showAdditionalFields = !this.showAdditionalFields;
    },
    editUser(id) {
      const user = this.users.find(u => u.idServiceUser === id);
      if (user) {
      
        this.$router.push({
          path: '/add-user',
          query: {
            idServiceUser: user.idServiceUser,
            name: user.name,
            surname: user.surname,
            contactDataId: user.contactData ? user.contactData.idContactData : null,
            hasPolishPesel: user.hasPolishPesel,
            taxIdentificationNumber: user.taxIdentificationNumber,
            comments: user.comments
          }
        });
      }
    },

    async deleteUser(id) {

      if (confirm("Are you sure you want to delete this user?")) {
        
        try{
          const cookie = getCookie("XSRF-TOKEN");
          const response = await fetch(`/api/serviceUser/delete/${id}`, {
            method: 'DELETE',
            headers:{
              'X-XSRF-TOKEN':cookie
            }
          });
                  
          // .then(response => {
          if (!response.ok) {
            if (response.status === 409) {
              throw new Error('Cannot delete, assigned cycle');
            } else {
              throw new Error('Network response was not ok');
            }
          }
          this.users = this.users.filter(user => user.idServiceUser !== id);
          alert('Service user deleted successfully!');

            // })
        }catch (error){
          console.error('There has been a problem with your fetch operation:', error);
          alert(error.message);
        }


            // .catch(error => {

            // });
      
      
      }


    },
    // TO JUŻ BEZ SENSU JAK NIE MA /api/cyclicalservice/getAllByUser?userID=${userId}
    // toggleCycles(userId) {
    //   console.log('Toggling cycles for user ID:', userId); // Debugging
    //   if (this.selectedUser === userId) {
    //     this.selectedUser = null;
    //     this.cyclicalServices = [];
    //   } else {
    //     this.selectedUser = userId;
    //     fetch(`/api/cyclicalservice/getAllByUser?userID=${userId}`)
    //         .then(response => response.json())
    //         .then(data => {
    //           console.log('Cyclical services data:', data); // Debugging
    //           // Map the services to include businessName directly from the response
    //           this.cyclicalServices = data.map(service => ({
    //             ...service,
    //             businessName: service.business.businessName
    //           }));
    //         })
    //         .catch(error => {
    //           console.error('Error fetching cyclical services:', error);
    //         });
    //   }
    // },
    // toggleBusinesses(userId) {
    //   console.log('Toggling businesses for user ID:', userId); // Debugging
    //   if (this.selectedBusinessUser === userId) {
    //     this.selectedBusinessUser = null;
    //     this.businesses = [];
    //   } else {
    //     this.selectedBusinessUser = userId;
    //     fetch(`/api/business/getAllByUser?userID=${userId}`)
    //         .then(response => response.json())
    //         .then(data => {
    //           console.log('Businesses data:', data); // Debugging
    //           this.businesses = data;
    //         })
    //         .catch(error => {
    //           console.error('Error fetching businesses:', error);
    //         });
    //   }
    // },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    viewContactData(contactDataId) {
      console.log('Fetching contact data for ID:', contactDataId); // Debugging
      this.showContactDataModal = true;
      this.contactDataDetails = null; // Reset details
      fetch(`/api/serviceUser/get/${contactDataId}`)
          .then(response => response.json())
          .then(data => {
            console.log('Contact data received:', data); // Debugging
            this.contactDataDetails = data.contactData;
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    closeContactDataModal() {
      this.showContactDataModal = false;
    },
    viewBusinessContact(businessId) {
      console.log('Fetching contact data for business ID:', businessId); // Debugging
      this.showBusinessContactModal = true;
      this.businessContactDetails = null; // Reset details
      fetch(`/api/business/get/${businessId}`)
          .then(response => response.json())
          .then(data => {
            console.log('Business contact data received:', data); // Debugging
            this.businessContactDetails = data.contactData;
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
    closeBusinessContactModal() {
      this.showBusinessContactModal = false;
    }
  },
  mounted() {
    fetch("/api/serviceUser/getAll")
        .then(response => {
          if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
          }
          return response.json();
        })
        .then(data => {
          this.users = data;
        })
        .catch(error => {
          console.error("There has been a problem with your fetch operation:", error);
        });
  }
}
</script>

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
