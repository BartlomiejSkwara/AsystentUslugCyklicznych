<template>
  <div>
    <SideBar class="col-3 sidebar"></SideBar>
    <div class="container-fluid ms-0 me-0">
      <div class="row">
        <div class="col-3">test</div>
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
import SideBar from './components/views/SideBar.vue';
import FooterComponent from './components/views/FooterComponent.vue';
import HeaderComponent from './components/views/HeaderComponent.vue';

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
