<template>
<!--  <div style="margin-bottom: 20px;">-->
<!--    <input type="text" v-model="search" placeholder="Insert dysponent name" style="margin-bottom: 10px;">-->
<!--  </div>-->
  <h1>Your cycles list</h1>
  <div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Description</th>
        <th>Price</th>
        <th>Renewal Message Sent</th>
        <th>First Cycle Start</th>
        <th @click="sortBy('nextRenewal')">
          Next Renewal
          <span :class="getSortIcon('nextRenewal')"></span>
        </th>
        <th>Renewal Period</th>
        <th>Dysponent</th>
        <th>Customer</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cycle in sortedCycles" :key="cycle.id">
        <td>{{ cycle.id }}</td>
        <td>{{ cycle.description }}</td>
        <td>{{ cycle.price }}</td>
        <td>{{ cycle.renewalMessageSent }}</td>
        <td>{{ formatDate(cycle.firstCycleStart) }}</td>
        <td>{{ formatDate(cycle.nextRenewal) }}</td>
        <td>{{ formatPeriod(cycle.renewalPeriod) }}</td>
        <td>{{ cycle.dysponentEntity.name }}</td>
        <td>{{ cycle.customerEntity.name }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style>
.sort-icon {
  margin-left: 5px;
}

.sort-icon:after {
  content: '';
  border: 4px solid transparent;
  display: inline-block;
}

.sort-icon.asc:after {
  border-bottom-color: black;
  border-top: none;
}

.sort-icon.desc:after {
  border-top-color: black;
  border-bottom: none;
}
</style>

<script>
export default {
  data() {
    return {
      cycles: [
        {
          id: 1,
          description: "Cycle 1",
          price: 100.0,
          renewalMessageSent: false,
          firstCycleStart: new Date("2024-06-01T10:00:00"),
          nextRenewal: new Date("2024-07-01T10:00:00"),
          renewalPeriod: { years: 0, months: 1, days: 0 },
          dysponentEntity: { name: "John Doe" },
          customerEntity: { name: "ACME Corp" }
        },
        {
          id: 2,
          description: "Cycle 2",
          price: 200.0,
          renewalMessageSent: false,
          firstCycleStart: new Date("2024-06-05T10:00:00"),
          nextRenewal: new Date("2024-09-05T10:00:00"),
          renewalPeriod: { years: 0, months: 3, days: 0 },
          dysponentEntity: { name: "Jane Smith" },
          customerEntity: { name: "Globex Inc" }
        },
        {
          id: 3,
          description: "Cycle 3",
          price: 150.0,
          renewalMessageSent: false,
          firstCycleStart: new Date("2024-06-10T10:00:00"),
          nextRenewal: new Date("2024-07-10T10:00:00"),
          renewalPeriod: { years: 0, months: 1, days: 0 },
          dysponentEntity: { name: "Alice Johnson" },
          customerEntity: { name: "Initech" }
        },
        {
          id: 4,
          description: "Cycle 4",
          price: 250.0,
          renewalMessageSent: false,
          firstCycleStart: new Date("2024-06-15T10:00:00"),
          nextRenewal: new Date("2024-09-15T10:00:00"),
          renewalPeriod: { years: 0, months: 3, days: 0 },
          dysponentEntity: { name: "Bob Brown" },
          customerEntity: { name: "Hooli" }
        }
      ],
      sortKey: 'nextRenewal',
      sortOrders: {
        nextRenewal: 1
      }
    };
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
          this.cycles = data;
        })
        .catch((error) => {
          console.error("There has been a problem with your fetch operation:", error);
        });
  },
  computed: {
    sortedCycles() {
      return this.cycles.slice().sort((a, b) => {
        let order = this.sortOrders[this.sortKey] || 1;
        if (a[this.sortKey] < b[this.sortKey]) return -1 * order;
        if (a[this.sortKey] > b[this.sortKey]) return 1 * order;
        return 0;
      });
    }
  },
  methods: {
    sortBy(key) {
      this.sortOrders[key] = this.sortOrders[key] * -1 || 1;
      this.sortKey = key;
    },
    getSortIcon(key) {
      if (this.sortKey === key) {
        return this.sortOrders[key] === 1 ? 'sort-icon asc' : 'sort-icon desc';
      }
      return 'sort-icon';
    },
    formatDate(date) {
      return new Intl.DateTimeFormat('en-GB', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      }).format(new Date(date));
    },
    formatPeriod(period) {
      return `${period.years} years, ${period.months} months, ${period.days} days`;
    }
  }
};
</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f2f2f2;
}
</style>
