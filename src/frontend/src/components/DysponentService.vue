<template>
  <div>
    <h1 style="margin-bottom: 20px;">Dysponents List</h1>
    <div class="container">
      <router-link to="/add-dysponent" class="add-button">Add new Dysponent</router-link>
      <input type="text" class="input" v-model="search" placeholder="Insert serial number" style="margin-bottom: 10px;">
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Surname</th>
        <th>MFN Serial Number</th>
        <th>Email</th>
        <th>Phone Number</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="dysponent in filteredDysponents" :key="dysponent.id">
        <td>{{ dysponent.id }}</td>
        <td>{{ dysponent.name }}</td>
        <td>{{ dysponent.surname }}</td>
        <td>{{ dysponent.mfnSerialNumber }}</td>
        <td>{{ dysponent.email }}</td>
        <td>{{ dysponent.phoneNumber }}</td>
        <td>
          <button class="action-button edit-button" @click="editDysponent(dysponent.id)">Edit</button>
          <button class="action-button delete-button" @click="deleteDysponent(dysponent.id)">Delete</button>
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
      dysponents: [],
      msg: "",
      search: ''
    };
  },
  computed: {
    filteredDysponents() {
      return this.dysponents.filter(dysponent =>
              dysponent.mfnSerialNumber.toLowerCase().includes(this.search.toLowerCase())
      );
    }
  },
  mounted() {
    fetch("/api/dysponent/getAll")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
          }
          return response.json();
        })
        .then((data) => {
          this.dysponents = data;
        })
        .catch((error) => {
          console.error("There has been a problem with your fetch operation:", error);
        });
  },
  methods: {
    editDysponent(id) {
      const dysponent = this.dysponents.find(d => d.id === id);
      if (dysponent) {
        this.$router.push({
          path: '/add-dysponent',
          query: {
            id: dysponent.id,
            name: dysponent.name,
            surname: dysponent.surname,
            mfnSerialNumber: dysponent.mfnSerialNumber,
            email: dysponent.email,
            phoneNumber: dysponent.phoneNumber
          }
        });
      }
    },
    deleteDysponent(id) {
      if (confirm("Are you sure you want to delete this dysponent?")) {
        fetch(`/api/dysponent/delete/${id}`, {
          method: 'DELETE'
        })
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok');
              }
              this.dysponents = this.dysponents.filter(dysponent => dysponent.id !== id);
            })
            .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
            });
      }
    }
  }
};
</script>

<style src="@/assets/style.css"></style>

