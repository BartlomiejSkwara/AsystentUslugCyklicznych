<template>
  <div>
    <h1 style="margin-bottom: 20px;">Customer list</h1>
    <div style="margin-bottom: 20px;">
      <input type="text" v-model="search" placeholder="Insert surname" style="margin-bottom: 10px;">
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
        </tr>
        </thead>
        <tbody>
        <tr v-for="customer in filteredCustomers" :key="customer.id">
          <td>{{ customer.id }}</td>
          <td>{{ customer.name }}</td>
          <td>{{ customer.surname }}</td>
          <td>{{ customer.email }}</td>
          <td>{{ customer.phoneNumber }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div>
<!--Pod zapytania z bazy danych-->
<!--    <h1>Lista Użytkowników</h1>-->
<!--    <ul>-->
<!--      <li v-for="user in users" :key="user.id">-->
<!--        <strong>ID:</strong> {{ user.id }} <br>-->
<!--        <strong>Email:</strong> {{ user.email }} <br>-->
<!--        <strong>Name:</strong> {{ user.name }} <br>-->
<!--        <strong>Phone Number:</strong> {{ user.phone_number }} <br>-->
<!--        <strong>Surname:</strong> {{ user.surname }} <br>-->
<!--      </li>-->
<!--    </ul>-->
  </div>
</template>

<script>
export default {
  name: 'CustomerList',
  data() {
    return {
      customers: [
        { id: 1, name: 'Jan', surname: 'Kowalski', email: 'jan.kowalski@example.com', phoneNumber: '123456789' },
        { id: 2, name: 'Anna', surname: 'Nowak', email: 'anna.nowak@example.com', phoneNumber: '987654321' },

      ],
      msg: "",
      users: [],
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
          this.users = data;
        })
        .catch((error) => {
          console.error("There has been a problem with your fetch operation:", error);
        });
  }
}
</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
}

table, th, td {
  border: 1px solid black;
}

th, td {
  padding: 8px;
  text-align: left;
}
</style>
