<template>
  <div>
    <SideBar class="col-3 sidebar"></SideBar>
    <div class="container-fluid ms-0 me-0">
      <div class="row">
        <div class="col-3">s</div>
        <div class="col-9 mx-auto p-4 py-md-5">
          <HeaderComponent></HeaderComponent>
            <router-view>
            </router-view>
          <FooterComponent></FooterComponent>
        </div>
      </div>
    </div>
  </div>



</template>

<script>
import SideBar from './components/SideBar.vue';
import FooterComponent from './components/FooterComponent.vue';
import HeaderComponent from './components/HeaderComponent.vue';

export default {
  name: 'App',
  components: {
    SideBar,
    FooterComponent,
    HeaderComponent,
  },
  methods: {
  },


  data(){
    return{
      msg:''
    }
  },
  mounted() {
    // fetch("/api/customer/getAll")
    //   .then((response) => response.text())
    //   .then((data) => {
    //       this.msg = data;
    //   });
  }
}
</script>

<style>



.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0; 
  overflow-y: auto; 
  z-index: 1000; 
}

</style>
