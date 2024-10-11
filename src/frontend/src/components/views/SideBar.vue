<template>
  <div class="d-flex flex-column flex-shrink-0 p-3 text-bg-dark">
    <router-link to="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
      <svg class="bi pe-none me-2" width="40" height="32"></svg>
      <span class="fs-4">Twój wirtualny asystent</span>
    </router-link>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
      <!-- Link do Cycles widoczny dla wszystkich zalogowanych użytkowników -->
      <li v-if="hasRole(['ROLE_admin', 'ROLE_editor', 'ROLE_user'])">
        <router-link to="/Cycles" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Cykle
        </router-link>
      </li>
      <!-- Link do ServiceUser widoczny tylko dla ROLE_admin i ROLE_editor -->
      <li v-if="hasRole(['ROLE_admin', 'ROLE_editor'])">
        <router-link to="/ServiceUser" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Użytkownicy Usług
        </router-link>
      </li>
      <!-- Link do Business widoczny tylko dla ROLE_admin i ROLE_editor -->
      <li v-if="hasRole(['ROLE_admin', 'ROLE_editor'])">
        <router-link to="/Business" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Firmy
        </router-link>
      </li>
      <li v-if="hasRole(['ROLE_admin'])">
        <router-link to="/Accounts" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Konta użytkowników systemu
        </router-link>
      </li>
<!--  PODRZUCAMY PODSTRONĘ POD TO CZY NIE? (tyle że trzeba by było mnóstwo przerobić)-->
<!--      <li v-if="hasRole(['ROLE_admin', 'ROLE_editor'])">-->
<!--        <router-link to="/certificates" class="nav-link text-white">-->
<!--          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>-->
<!--          Certyfikaty-->
<!--        </router-link>-->
<!--      </li>-->

    </ul>
  </div>
</template>

<script>
// import { eventBus } from '@/eventBus';

export default {
  name: 'SideBar',
  data() {
    return {
      // userRole: 'ROLE_nobody' // Domyślna rola
    };
  },
  methods: {
    hasRole(roles) {
      //console.log('Checking roles:', roles, 'User role:', this.userRole); // Debugging role check
      return roles.includes(this.$store.state.role);
    }
  },
  created() {
    //console.log('Sidebar created');
    // Nasłuchuj zmiany roli z eventBus
    // eventBus.on('roleUpdate', (newRole) => {
      // this.userRole = newRole;
      // console.log('Sidebar Role updated to:', this.userRole); // Debugging role update
    // });
    // eventBus.emit('roleUpdate', this.userRole)
    // console.log('Role emit:', this.userRole);
  }
};
</script>

<style scoped>
/* Twoje style */
</style>
