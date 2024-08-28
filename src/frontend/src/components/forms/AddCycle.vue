<template>
  <div>
    <h1>Add new cycle</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="description">Description:</label>
        <input type="text" id="description" v-model="form.description" required />
      </div>
      <div>
        <label for="price">Price:</label>
        <input type="text" id="price" v-model="form.price" required pattern="^\d+(\.\d{1,2})?$" title="Please enter a valid price with up to two decimal places." />
      </div>
      <div>
        <label for="firstCycleStart">First Cycle Start:</label>
        <input type="datetime-local" id="first-cycle" v-model="form.firstCycleStart" required />
      </div>
      <div>
        <label for="renewalPeriod">Renewal Period (in years, months, days):</label>
        <div>
          <input type="number" v-model.number="form.renewalPeriod.years" placeholder="Years" />
          <input type="number" v-model.number="form.renewalPeriod.months" placeholder="Months" />
          <input type="number" v-model.number="form.renewalPeriod.days" placeholder="Days" />
        </div>
      </div>
      <div>
        <label for="dysponentId">Dysponent ID:</label>
        <input type="number" id="dysponentId" v-model.number="form.dysponentId" required />
        <span v-if="dysponentName">{{ dysponentName }}</span>
      </div>
      <div>
        <label for="customerId">Customer ID:</label>
        <input type="number" id="customerId" v-model.number="form.customerId" required />
        <span v-if="customerName">{{ customerName }}</span>
      </div>
      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        description: '',
        price: null,
        firstCycleStart: '',
        renewalPeriod: {
          years: 0,
          months: 0,
          days: 0
        },
        dysponentId: null,
        customerId: null
      },
      customerName: '',
      dysponentName: '',
      nextRenewal: '',
      isEdit: false, // Flag to check if it's an edit
      cycleId: null // Store cycle id if it's an edit
    };
  },
  mounted() {
    this.prefillForm();
  },
  methods: {
    async submitForm() {
      try {
        await this.fetchCustomerName();
        await this.fetchDysponentName();

        const renewalPeriod = `P${this.form.renewalPeriod.years}Y${this.form.renewalPeriod.months}M${this.form.renewalPeriod.days}D`;
        const firstCycleStart = new Date(this.form.firstCycleStart);

        const newCycle = {
          description: this.form.description,
          price: parseFloat(this.form.price),
          firstCycleStart: firstCycleStart.toISOString(),
          renewalPeriod: renewalPeriod,
          dysponentId: this.form.dysponentId,
          customerId: this.form.customerId,
          nextRenewal: this.calculateNextRenewal(firstCycleStart, this.form.renewalPeriod)
        };
        const url = this.isEdit ? `/api/cyclicalservice/update/${this.cycleId}` : '/api/cyclicalservice/insertBody';
        const method = this.isEdit ? 'POST' : 'POST';
        console.log('Sending new cycle:', newCycle); // Debugging

        const response = await fetch(url, {
          method: method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(newCycle)
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        alert('Cycle added successfully!');
        this.$router.push('/Cycles');
      } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
        alert('There has been a problem with your fetch operation: ' + error.message);
      }
    },
    async fetchCustomerName() {
      try {
        const response = await fetch(`/api/customer/get/${this.form.customerId}`);
        if (!response.ok) {
          throw new Error('Failed to fetch customer name');
        }
        const customerData = await response.json();
        this.customerName = customerData.name;
      } catch (error) {
        console.error('Error fetching customer name:', error);
        this.customerName = 'Unknown';
      }
    },
    async fetchDysponentName() {
      try {
        const response = await fetch(`/api/dysponent/get/${this.form.dysponentId}`);
        if (!response.ok) {
          throw new Error('Failed to fetch dysponent name');
        }
        const dysponentData = await response.json();
        this.dysponentName = dysponentData.name;
      } catch (error) {
        console.error('Error fetching dysponent name:', error);
        this.dysponentName = 'Unknown';
      }
    },
    calculateNextRenewal(firstCycleStart, renewalPeriod) {
      const nextRenewal = new Date(firstCycleStart);
      nextRenewal.setFullYear(nextRenewal.getFullYear() + renewalPeriod.years);
      nextRenewal.setMonth(nextRenewal.getMonth() + renewalPeriod.months);
      nextRenewal.setDate(nextRenewal.getDate() + renewalPeriod.days);
      const nextRenewalUTC = nextRenewal.toISOString();
      return nextRenewalUTC;
    },
    prefillForm() {
      if (this.$route.query.id) {
        this.isEdit = true;
        this.cycleId = this.$route.query.id;
        this.form.description = this.$route.query.description;
        this.form.price = this.$route.query.price;
        this.form.firstCycleStart = new Date(this.$route.query.firstCycleStart).toLocaleString();
        this.form.renewalPeriod.years = parseInt(this.$route.query.renewalPeriodYears, 10);
        this.form.renewalPeriod.months = parseInt(this.$route.query.renewalPeriodMonths, 10);
        this.form.renewalPeriod.days = parseInt(this.$route.query.renewalPeriodDays, 10);
        this.form.dysponentId = parseInt(this.$route.query.dysponentId, 10);
        this.form.customerId = parseInt(this.$route.query.customerId, 10);
      }
    }
  }
};
</script>

<style src="@/assets/forms.css"></style>
