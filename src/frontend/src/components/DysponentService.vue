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
  }
};
</script>

<style src="@/assets/style.css"></style>

