<template>
  <div>
    <h1 style="margin-bottom: 20px;">User List</h1>
    <div class="container">
      <router-link to="/add-user" class="add-button">Add new User</router-link>
      <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
        <input type="text" class="input" v-model="searchFields.surname" placeholder="Insert surname" style="margin-bottom: 10px; margin-right: 10px;">
        <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.name" placeholder="Insert name" style="margin-bottom: 10px; margin-right: 10px;">
<!--          <input type="text" class="input" v-model="searchFields.taxIdentificationNumber" placeholder="Insert taxID" style="margin-bottom: 10px; margin-right: 10px;">-->
        </div>
        <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
      </div>
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Surname</th>
        <th>Contact Data ID</th>
        <th>Has Polish PESEL</th>
        <th>Tax ID</th>
        <th>Comments</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in filteredUsers" :key="user.idServiceUser">
        <td>{{ user.idServiceUser }}</td>
        <td>{{ user.name }}</td>
        <td>{{ user.surname }}</td>
        <td>
          {{ user.contactData ? user.contactData.idContactData : 'N/A' }}
          </td>
        <td>{{ user.hasPolishPesel }}</td>
        <td>{{ user.taxIdentificationNumber }}</td>
        <td>{{ user.comments }}</td>
        <td>
          <button class="action-button edit-button" @click="editUser(user.idServiceUser)">Edit</button>
          <button class="action-button delete-button" @click="deleteUser(user.idServiceUser)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ServiceUserList',
  data() {
    return {
      users: [],
      searchFields: {
        surname: '',
        name: '',
        // taxIdentificationNumber: ''
      },
      showAdditionalFields: false
    };
  },
  computed: {
    filteredUsers() {
      return this.users.filter(user => {
        return (
            user.surname.toLowerCase().includes(this.searchFields.surname.toLowerCase()) &&
            (!this.showAdditionalFields || (
                user.name.toLowerCase().includes(this.searchFields.name.toLowerCase())
                // && user.taxIdentificationNumber.toLowerCase().includes(this.searchFields.taxIdentificationNumber.toLowerCase())
            ))
        );
      });
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
            contact_data_id: user.contact_data_id,
            hasPolishPesel: user.hasPolishPesel,
            taxIdentificationNumber: user.taxIdentificationNumber,
            comments: user.comments
          }
        });
      }
    },
    deleteUser(id) {
      if (confirm("Are you sure you want to delete this user?")) {
        fetch(`/api/serviceUser/delete/${id}`, {
          method: 'DELETE'
        })
            .then(response => {
              if (!response.ok) {
                // Check if the error message corresponds to an assigned cycle
                if (response.status === 409) {
                  throw new Error('Cannot delete, assigned cycle');
                } else {
                  throw new Error('Network response was not ok');
                }
              }
              this.users = this.users.filter(user => user.idServiceUser !== id);
            })
            .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
              alert(error.message);
            });
      }
    }

  },
  mounted() {
    fetch("/api/serviceUser/getAll")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
          }
          return response.json();
        })
        .then((data) => {
          this.users = data;
        })
        .catch((error) => {
          console.error("There has been a problem with your fetch operation:", error);
        });
  }
}
</script>

<style src="@/assets/style.css"></style>
