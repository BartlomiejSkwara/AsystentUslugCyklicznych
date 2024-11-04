<template>
  <div class="background">
    <SideBar class="col-3 sidebar"></SideBar>
    <div class="container-fluid ms-0 me-0">
      <div class="row">
        <div class="col-3"></div>
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


<!--<style src="@/assets/style.css"></style>-->

<style>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  overflow-y: auto;
  z-index: 1000;
}

.background {
  background-color: rgba(80,89,80,0.05)
}

html{
  background-color: rgba(80,89,80,0.05)
}

:root, [data-bs-theme=light]{

  --bs-border-color: #8e9092 !important;
/* #505950 */
  /* --moreThenSixty-days: #8e9092 !important;
  --lessThenSixty-days: #25ba54 !important;
  --lessThenThirty-days: #3db8ba !important;
  --lessThenFourteen-days: #ba973d !important;
  --lessThenSeven-days: #ba5100 !important;
  --cancelled: #ba4343 !important;
  --expired: #ba2500 !important; */
  --moreThenSixty-days: #8e9092 !important;
  --lessThenSixty-days:  forestgreen!important;
  --lessThenThirty-days: dodgerblue !important;
  --lessThenFourteen-days: orange !important;
  --lessThenSeven-days: orangered !important;
  --cancelled: crimson !important;
  --expired:  firebrick !important;
}

</style>
