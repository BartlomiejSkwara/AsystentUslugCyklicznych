<template>
  <div>
    <h1 style="margin-bottom: 20px;">Dysponents List</h1>
  <div style="margin-bottom: 20px;">
    <input type="text" v-model="search" placeholder="Insert serial number" style="margin-bottom: 10px;">
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
      dysponents: [
        { id: 1, name: "John", surname: "Doe", mfnSerialNumber: "MFN123456", email: "john.doe@example.com", phoneNumber: "123-456-7890" },
        { id: 2, name: "Jane", surname: "Smith", mfnSerialNumber: "MFN654321", email: "jane.smith@example.com", phoneNumber: "098-765-4321" },
        { id: 3, name: "Alice", surname: "Johnson", mfnSerialNumber: "MFN987654", email: "alice.johnson@example.com", phoneNumber: "567-890-1234" },
        { id: 4, name: "Bob", surname: "Brown", mfnSerialNumber: "MFN456789", email: "bob.brown@example.com", phoneNumber: "234-567-8901" }
      ],
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

