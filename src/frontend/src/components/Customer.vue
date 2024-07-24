<template>
  <div>
    <h1 style="margin-bottom: 20px;">Lista klientów</h1>
    <div style="margin-bottom: 20px;">
      <input type="text" v-model="search" placeholder="Wyszukaj po imieniu" style="margin-bottom: 10px;">
    </div>
    <div style="margin-top: 20px;">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Imię</th>
          <th>Nazwisko</th>
          <th>Email</th>
          <th>Numer telefonu</th>
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
      search: ''
    };
  },
  computed: {
    filteredCustomers() {
      return this.customers.filter(customer =>
          customer.name.toLowerCase().includes(this.search.toLowerCase())
      );
    }
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
