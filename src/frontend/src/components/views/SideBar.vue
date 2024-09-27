<template>
  <div class="d-flex flex-column flex-shrink-0 p-3 text-bg-dark">
    <router-link to="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
      <svg class="bi pe-none me-2" width="40" height="32">
      </svg>
      <span class="fs-4">Your Virtual Assistant</span>
    </router-link>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
      <li>
        <router-link to="/ServiceUser" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Users
        </router-link>
      </li>
      <li>
        <router-link to="/Business" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Businesses
        </router-link>
      </li>
      <li>
        <router-link to="/Cycles" class="nav-link text-white">
          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>
          Cycles
        </router-link>
      </li>
<!--      <li>-->
<!--        <router-link to="/certificates" class="nav-link text-white">-->
<!--          <svg class="bi pe-none me-2" width="16" height="16"><use xlink:href="#grid"></use></svg>-->
<!--          Certificates-->
<!--        </router-link>-->
<!--      </li>-->
    </ul>
<!--    <hr>-->
<!--    <div class="dropdown">-->
<!--      <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">-->
<!--        <img src="https://github.com/mdo.png" alt="" width="32" height="32" class="rounded-circle me-2">-->
<!--        <strong>Current User</strong>-->
<!--      </a>-->
<!--      <ul class="dropdown-menu dropdown-menu-dark text-small shadow">-->
<!--        <li><a class="dropdown-item" href="#">Add new payment...</a></li>-->
<!--        <li><hr class="dropdown-divider"></li>-->
<!--        <li><a class="dropdown-item" href="#">Sign out</a></li>-->
<!--      </ul>-->
<!--    </div>-->
  </div>
</template>

<script>
export default {
  name:'SideBar',
}
</script>

<style>
</style>



