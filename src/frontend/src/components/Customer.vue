<template>
  <div>
    <h1 style="margin-bottom: 20px;">Customer list</h1>
    <div class="container">
      <router-link to="/add-customer" class="add-button">Add new Customer</router-link>
      <input type="text" class="input" v-model="search" placeholder="Insert surname" style="margin-bottom: 10px;">
    </div>
    <div style="margin-top: 20px;">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Surname</th>
          <th>Email</th>
          <th>Phone number</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="customer in filteredCustomers" :key="customer.id">
          <td>{{ customer.id }}</td>
          <td>{{ customer.name }}</td>
          <td>{{ customer.surname }}</td>
          <td>{{ customer.email }}</td>
          <td>{{ customer.phoneNumber }}</td>
          <td>
            <button class="action-button edit-button" @click="editCustomer(customer.id)">Edit</button>
            <button class="action-button delete-button" @click="deleteCustomer(customer.id)">Delete</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CustomerList',
  data() {
    return {
      customers: [],
      msg: "",
      search: ''
    };
  },
  computed: {
    filteredCustomers() {
      return this.customers.filter(customer =>
          customer.surname.toLowerCase().includes(this.search.toLowerCase())
      );
    }
  },
  mounted() {
    fetch("/api/customer/getAll")
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok " + response.statusText);
          }
          return response.json();
        })
        .then((data) => {
          this.customers = data;
        })
        .catch((error) => {
          console.error("There has been a problem with your fetch operation:", error);
        });
  },
  methods: {
    editCustomer(id) {
      const customer = this.customers.find(c => c.id === id);
      if (customer) {
        this.$router.push({
          path: '/add-customer',
          query: {
            id: customer.id,
            name: customer.name,
            surname: customer.surname,
            email: customer.email,
            phoneNumber: customer.phoneNumber
          }
        });
      }
    },

    deleteCustomer(id) {
      if (confirm("Are you sure you want to delete this customer?")) {
        fetch(`/api/customer/delete/${id}`, {
          method: 'DELETE'
        })
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok');
              }
              this.customers = this.customers.filter(customer => customer.id !== id);
            })
            .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
            });
      }
    }
  }
}
</script>

<style src="@/assets/style.css"></style>
