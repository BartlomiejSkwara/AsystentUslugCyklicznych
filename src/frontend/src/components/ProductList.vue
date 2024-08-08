<template>
  <div>
    <h1>Lista produktów</h1>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Nazwa produktu</th>
        <th>Cena</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="product in products" :key="product.id">
        <td>{{ product.id }}</td>
        <td>{{ product.name }}</td>
        <td>{{ product.price }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ProductList',
  data() {
    return {
      // Symulacja danych z bazy danych
      products: [
        { id: 1, name: 'Produkt 1', price: 10 },
        { id: 2, name: 'Produkt 2', price: 20 },
        { id: 3, name: 'Produkt 3', price: 30 },
      ]
    };
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
