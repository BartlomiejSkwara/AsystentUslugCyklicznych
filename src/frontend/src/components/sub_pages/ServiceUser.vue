<template>
  <div>
    <h1 style="margin-bottom: 20px;">Lista użytkowników usług</h1>
    <div class="container">
      <router-link to="/add-user" class="add-button">Dodaj nowego użytkownika usługi</router-link>
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
<!--        <th>Polski PESEL?</th>-->
        <th>PESEL</th>
        <th>Dodatkowy opis</th>
        <th>Działania</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in paginatedUsers" :key="user.idServiceUser">
        <td>{{ user.idServiceUser }}</td>
        <td>{{ user.name }}</td>
        <td @click="toggleCyclicalServices(user.idServiceUser)" class="clickable">{{ user.getSurname || user.surname }}</td>
        <td>
          {{ user.contactData ? user.contactData.idContactData : 'N/A' }}
          <button v-if="user.contactData" @click="viewContactData(user.idServiceUser)" class="view-button"
                  data-bs-toggle="modal" data-bs-target="#userContactData"
          >...</button>
        </td>
<!--        <td>{{ user.hasPolishPesel }}</td>-->
        <td>{{ user.taxIdentificationNumber }}</td>
        <td>{{ user.comments }}</td>
        <td>
          <button class="action-button edit-button" @click="editUser(user.idServiceUser)">Edytuj</button>
          <button v-if="isAdmin" class="action-button delete-button" @click="deleteUser(user.idServiceUser)">Usuń</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination" v-if="totalPages > 1">
      <button @click="prevPage" :disabled="currentPage === 1">Poprzednia</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">Następna</button>
    </div>

    <div v-if="selectedUser" style="margin-top: 20px;">
      <h2>Szczegóły dla użytkownika: {{ selectedUserFullName }}</h2>
      <div v-if="cyclicalServices.length">
        <table>
          <thead>
          <tr>
            <th>Nazwa firmy</th>
<!--            <th>Numer dokumentu</th>-->
            <th>Typ Usługi</th>
            <th>Ważne do:</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="service in cyclicalServices" :key="service.agreementNumber">
            <td @click="viewBusinessData(service.business.idBusiness)" class="clickable" data-bs-toggle="modal" data-bs-target="#businessData">
              {{ service.business.businessName }}
            </td>
<!--            <td>{{ service.agreementNumber }}</td>-->
            <td>{{service.certificate.cardType}} ważny {{calculateCertLen(service.certificate.validFrom,service.certificate.validTo)}} lata</td>
            <td>{{ formatDate(service.certificate.validTo) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <p v-else>Brak przypisanych firm i cykli.</p>
    </div>

    <div id="userContactData" class="modal fade" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h3>Dane kontaktowe użytkownika usługi</h3>
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

    <div id="businessData" class="modal fade" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h3>Dane przypisanej firmy</h3>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedBusiness">
              <p><strong>Nazwa firmy:</strong> {{ selectedBusiness.name }}</p>
              <p><strong>NIP:</strong> {{ selectedBusiness.nip }}</p>
              <p><strong>REGON:</strong> {{ selectedBusiness.regon }}</p>
              <p><strong>Adres:</strong> {{ selectedBusiness.adres }}</p>
              <p><strong>Emaile:</strong></p>
              <ul>
                <li v-for="email in selectedBusiness.contactData.emails" :key="email.idEmail">{{ email.email }}</li>
              </ul>
              <p><strong>Numery telefonów:</strong></p>
              <ul>
                <li v-for="phone in selectedBusiness.contactData.phoneNumbers" :key="phone.idPhoneNumber">{{ phone.number }}</li>
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
import { getCookie, fetchWrapper, decodeSignatureType } from '@/utility';

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
      currentPage: 1,
      usersPerPage: 8,
      contactDataDetails: null,
      selectedBusiness: null,
    };
  },
  computed: {
    isAdmin() {
      return this.$store.state.role === "ROLE_admin";
    },
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
    },
    selectedUserFullName() {
      const user = this.users.find(u => u.idServiceUser === this.selectedUser);
      return user ? `${user.name} ${user.getSurname || user.surname}` : '';
    }
  },
  methods: {
    calculateCertLen(sDate,eDate){
      const d1 = new Date(sDate);
      const d2 = new Date(eDate);
      // console.log(sDate,eDate);
      
      return d2.getFullYear()-d1.getFullYear();
    },
    decodeSignature(sig){
      return decodeSignatureType(sig);
    },
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
      if (confirm("Czy na pewno chcesz usunąć tego użytkownika?")) {
        try {
          const cookie = getCookie("XSRF-TOKEN");
          const response = await fetchWrapper(this, `/api/serviceUser/delete/${id}`, {
            method: 'DELETE',
            headers: {
              'X-XSRF-TOKEN': cookie
            }
          });

          if (!response.ok) {
            if (response.status === 409) {
              throw new Error('Nie można usunąć, przypisany cykl.');
            } else {
              throw new Error('Błąd sieci.');
            }
          }

          this.users = this.users.filter(user => user.idServiceUser !== id);
          alert('Użytkownik usunięty pomyślnie!');
        } catch (error) {
          console.error('Problem z operacją:', error);
          alert(error.message);
        }
      }
    },
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
      this.contactDataDetails = null; // Reset details
      fetchWrapper(this, `/api/serviceUser/get/${contactDataId}`)
          .then(response => response.json())
          .then(data => {
            this.contactDataDetails = data.contactData;
          })
          .catch(error => {
            console.error("Problem z operacją:", error);
          });
    },
    viewBusinessData(businessId) {
      this.selectedBusiness = null; // Resetowanie poprzednich danych firmy
      fetchWrapper(this, `/api/business/get/${businessId}`)
          .then(response => response.json())
          .then(data => {
            this.selectedBusiness = data; // Poprawione przypisanie danych firmy
          })
          .catch(error => {
            console.error("Problem z operacją:", error);
          });
    },
    toggleCyclicalServices(userId) {
      if (this.selectedUser === userId) {
        this.selectedUser = null;
        this.cyclicalServices = [];
      } else {
        this.selectedUser = userId;
        fetchWrapper(this, `/api/cyclicalservice/getAllByUser?userID=${userId}`)
            .then(response => response.json())
            .then(data => {
              this.cyclicalServices = data;
            })
            .catch(error => {
              console.error("Problem z operacją:", error);
            });
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
  mounted() {
    fetchWrapper(this, '/api/serviceUser/getAll')
        .then(response => response.json())
        .then(data => {
          this.users = data;
        })
        .catch(error => {
          console.error("Problem z operacją:", error);
        });
  }
};
</script>

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
