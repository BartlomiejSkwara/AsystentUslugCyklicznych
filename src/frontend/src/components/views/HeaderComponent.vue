<template>
  <header class="d-flex align-items-center pb-3 mb-5 border-bottom">
    <div class="ms-auto">
      <input type="text" placeholder="Search..." class="form-control">
    </div>
    <div class="ms-3">
      <router-link to="/login" class="btn btn-primary me-2">Login</router-link>
      <router-link to="/register" class="btn btn-secondary">Register</router-link>
    </div>
  </header>
</template>

<script>
export default {
  name: 'HeaderComponent',
}
</script>

<style lang="">
</style>
