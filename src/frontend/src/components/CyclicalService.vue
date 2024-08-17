<template>
  <div>
    <h1 style="margin-bottom: 20px;">Cycles list</h1>
    <div>
      <div class="container">
        <router-link to="/add-cycle" class="add-button">Add new cycle</router-link>
        <input type="text" class="input" v-model="search" placeholder="Insert dysponent name" style="margin-bottom: 10px;">
      </div>
      <table>
        <thead>
        <tr>
          <th @click="sortBy('id')">
            <u>ID</u>
            <span :class="getSortIcon('id')"></span>
          </th>
          <th>Description</th>
          <th>Price</th>
          <th>Renewal Message Sent</th>
          <th>First Cycle Start</th>
          <th @click="sortBy('nextRenewal')">
            <u>Next Renewal</u>
            <span :class="getSortIcon('nextRenewal')"></span>
          </th>
          <th>Renewal Period</th>
          <th @click="sortBy('dysponentEntity.name')">
            <u>Dysponent</u>
            <span :class="getSortIcon('dysponentEntity.name')"></span>
          </th>
          <th>Customer</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="cycle in sortedAndFilteredCycles" :key="cycle.id">
          <td>{{ cycle.id }}</td>
          <td>{{ cycle.description }}</td>
          <td>{{ cycle.price }}</td>
          <td>{{ cycle.renewalMessageSent }}</td>
          <td>{{ formatDate(cycle.firstCycleStart) }}</td>
          <td>{{ formatDate(cycle.nextRenewal) }}</td>
          <td>{{ formatPeriod(cycle.renewalPeriod) }}</td>
          <td>{{ cycle.dysponentEntity.name }}</td>
          <td>{{ cycle.customerEntity.name }}</td>
          <td>
            <button @click="renewCycle(cycle.id)">Renew</button>
            <div v-if="cycle.renewedAt">
              Renewed recently at {{ formatDate(cycle.renewedAt) }}
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cycles: [],
      sortKey: 'id',
      search: '',
      sortOrders: {
        id: 1,
        nextRenewal: 1,
        'dysponentEntity.name': 1
      }
    };
  },
  mounted() {
    this.fetchCycles();
  },
  methods: {
    fetchCycles() {
      fetch("/api/cyclicalservice/getAll")
          .then(response => {
            if (!response.ok) {
              throw new Error("Network response was not ok " + response.statusText);
            }
            return response.json();
          })
          .then(data => {
            this.cycles = data.map(cycle => ({
              ...cycle,
              firstCycleStart: new Date(cycle.firstCycleStart),
              nextRenewal: new Date(cycle.nextRenewal),
              renewedAt: null
            }));
          })
          .catch(error => {
            console.error("There has been a problem with your fetch operation:", error);
          });
    },
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
      return period.replace('P', '').replace('Y', ' years ').replace('M', ' months ').replace('D', ' days ');
    },
    renewCycle(id) {
      fetch(`/api/cyclicalservice/renew/${id}`, {
        method: 'GET',
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
          })
          .then(() => {
            this.updateCycleNextRenewal(id);
            this.setRenewedAt(id);
            this.saveUpdatedCycle(id);
          })
          .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
          });
    },

    saveUpdatedCycle(id) {
      const cycle = this.cycles.find(c => c.id === id);
      if (cycle) {
        fetch(`/api/cyclicalservice/update/${id}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            id: cycle.id,
            description: cycle.description,
            price: cycle.price,
            renewalMessageSent: cycle.renewalMessageSent,
            firstCycleStart: cycle.firstCycleStart.toISOString(),
            nextRenewal: cycle.nextRenewal.toISOString(),
            renewalPeriod: cycle.renewalPeriod,
            customerId: cycle.customerEntity.id,
            dysponentId: cycle.dysponentEntity.id
          })
        })
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
              }
              return response.text();
            })
            .then(() => {
              console.log('Cycle successfully updated in the database');
            })
            .catch(error => {
              console.error('There has been a problem with your fetch operation:', error);
            });
      }
    },
    updateCycleNextRenewal(id) {
      const cycle = this.cycles.find(c => c.id === id);
      if (cycle) {
        cycle.nextRenewal = new Date(new Date(cycle.nextRenewal).getTime() + this.periodToMilliseconds(cycle.renewalPeriod));
      }
    },
    setRenewedAt(id) {
      const cycle = this.cycles.find(c => c.id === id);
      if (cycle) {
        cycle.renewedAt = new Date();
      }
    },
    periodToMilliseconds(period) {
      const matches = period.match(/P(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)D)?/);
      const years = parseInt(matches[1] || 0, 10);
      const months = parseInt(matches[2] || 0, 10);
      const days = parseInt(matches[3] || 0, 10);
      const totalDays = years * 365 + months * 30 + days;
      return totalDays * 24 * 60 * 60 * 1000;
    }
  },
  computed: {
    filteredCycles() {
      return this.cycles.filter(cycle =>
          cycle.dysponentEntity.name.toLowerCase().includes(this.search.toLowerCase())
      );
    },
    sortedAndFilteredCycles() {
      return this.filteredCycles.slice().sort((a, b) => {
        let order = this.sortOrders[this.sortKey] || 1;
        let aValue = this.sortKey === 'dysponentEntity.name' ? a.dysponentEntity.name : a[this.sortKey];
        let bValue = this.sortKey === 'dysponentEntity.name' ? b.dysponentEntity.name : b[this.sortKey];
        if (aValue < bValue) return -1 * order;
        if (aValue > bValue) return 1 * order;
        return 0;
      });
    }
  }
};
</script>

<style src="@/assets/style.css"></style>
