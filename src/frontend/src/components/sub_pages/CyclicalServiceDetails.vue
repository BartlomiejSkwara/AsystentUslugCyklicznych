<template>
    <h1>Szczegóły Usługi cyklicznej {{cycleInfo.getIdCyclicalService}}</h1>
        <div class=" pt-4">
          <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h3>Dane Usługi: </h3>
                    <span><b>Powiązane Konto Użytkownika Systemu: </b>{{ cycleInfo.accountUsername }}</span><br>
                    <span><b>Cena: </b>{{ cycleInfo.price }}</span><br>
                    <span><b>Usługa Jednorazowa: </b>{{ cycleInfo.oneTime }}</span><br>
                    <span><b>Numer Zgody: </b>{{ cycleInfo.agreementNumber }}</span><br>
                    <!-- <span><b>Dokument: </b>{{decodedSignature}}</span><br> -->

                    <span><b>Opis usługi: </b>{{ cycleInfo.description }}</span><br>
                    <span><b>Obecnie przypisane Statusy: </b></span>
                    <ul>
                      <li v-for="(status, index) in statusList" :key="index">
                        {{ status }}
                      </li>
                    </ul>
                    <button class="btn btn-primary" data-bs-toggle="collapse" data-bs-target="#statusHistory" >Historia zmian statusów ... </button>
                </div>
                
                <div class="col-md-4">
                  <h3>Najnowszy Certyfikat: </h3>
                  <span><b>Numer Seryjny Certyfikatu: </b>{{ cycleInfo.certificate.certificateSerialNumber }}</span><br>
                  <span><b>Ważny Od: </b>{{ formatDate(cycleInfo.certificate.validFrom) }}</span><br>
                  <span><b>Ważny Do: </b>{{ formatDate(cycleInfo.certificate.validTo) }}</span><br>
                  <span><b>Typ Karty: </b>{{ cycleInfo.certificate.cardType }}</span><br>
                  <span><b>Numer Karty: </b>{{ cycleInfo.certificate.cardNumber }}</span><br>
                  <button class="btn btn-primary"  data-bs-toggle="collapse" data-bs-target="#certHistory" >Historia certyfikatów ... </button>

                </div>

                <div class="col-md-4">
                  <h3>Użytkownik Usługi i Firma:</h3>
                  <span><b>Nazwa Firmy: </b>{{ cycleInfo.business.businessName }}</span><br>
                  <span><b>Imię użytkownika usługi: </b>{{ cycleInfo.serviceUser.name }}</span><br>
                  <span><b>Nazwisko użytkownika usługi: </b>{{ cycleInfo.serviceUser.getSurname }}</span><br>
                  <span><b>Stanowisko w firmie: </b>{{ cycleInfo.certificate.nameInOrganisation }}</span><br>
                </div>
            </div>
        </div>
        <div class="collapse" id="statusHistory">
            <div class="card card-body">
              <h3>Historia Zmian Statusu</h3>

              <table>
                <thead>
                <tr>
                  <th>Typ Statusu</th>
                  <th>Data zmiany</th>
                  <th>Komentarz</th>
                </tr>
                </thead>
                <tbody>
                  <tr v-for="(change, index) in statusChangeHistory" :key="index">
                    <td>{{ change.statusTypeName }}</td>
                    <td>{{ formatDate(change.changeDate) }}</td>
                    <td>{{ change.comment }}</td>
                  </tr>
                </tbody>
              </table>
  
            </div>
        </div>









        
        <div class="collapse" id="certHistory">
            <div class="card card-body">
              <h3>Historia Certyfikatów</h3>
              <table>
                <thead>
                <tr>
                  <th>Numer Seryjny</th>
                  <th>Ważny od</th>
                  <th>Ważny do</th>

                  <th>Typ Karty</th>
                  <th>Nr. Karty</th>
                  <th>Stanowisko w firmie</th>
                </tr>
                </thead>
                <tbody>
                  <tr  v-for="(cert, index) in this.certHistory" :key="index">
                    <td>{{ cert.certificateSerialNumber }}</td>
                    <td>{{  formatDate(cert.validFrom) }}</td>
                    <td>{{ formatDate(cert.validTo) }}</td>
                    <td>{{ cert.cardType }}</td>
                    <td>{{ cert.cardNumber }}</td>
                    <td>{{ cert.nameInOrganisation }}</td>

                  </tr>
                </tbody>
              </table>
            </div>
        </div>
        
        <br>
        <button class="btn btn-danger" type="button" @click="goBack">Powrót</button>
    </div>
  </template>
  
  <script>  
  import { decodeSignatureType, decodeStatus, fetchWrapper } from '@/utility';
  export default {
    name: 'CyclicalServiceDetails',
    data() {
      return {
        cycleId: null,
        cycleInfo: this.$store.state.passedValue,
        statusList: [],
        statusChangeHistory: [],
        certHistory:[]
      };
    },
    mounted() {
      this.statusList = decodeStatus(this.cycleInfo.statusBitmask);
      this.fetchStatusChangeHistory();
      this.fetchCertHistory();
      // this.cycleId = this.$route.params.id; 
    },
    computed:{
      decodedSignature(){
        return decodeSignatureType(this.cycleInfo.signatureType);
      }
    },
    methods: {

      formatDate(date) {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
      
      async fetchStatusChangeHistory() {
        try {
          
          const response = await fetchWrapper(this,`/api/cyclicalservice/statusChangeHistory/${this.cycleInfo.getIdCyclicalService}`, {
              method: 'GET',
          });

          if (!response.ok) {
              throw new Error("Network response was not ok " + response.statusText);
          }

          const role = response.headers.get('frontRole');
          this.$store.commit('setRole', role);
          const statusHistoryData = await response.json();
          this.statusChangeHistory = statusHistoryData;
        } catch (error) {
            console.error("There has been a problem with your fetch operation:", error);
        }
        
      },

      async fetchCertHistory() {
        try {
          
          const response = await fetchWrapper(this,`/api/cyclicalservice/certificateHistory/${this.cycleInfo.getIdCyclicalService}`, {
              method: 'GET',
          });

          if (!response.ok) {
              throw new Error("Network response was not ok " + response.statusText);
          }

          const role = response.headers.get('frontRole');
          this.$store.commit('setRole', role);
          this.certHistory = await response.json();
        } catch (error) {
            console.error("There has been a problem with your fetch operation:", error);
        }
        
      },
      
      
        goBack() {
          this.$router.push('/Cycles');
        }
      }
  };
  </script>
  
  <!-- <style src="@/assets/style.css"></style> -->
  