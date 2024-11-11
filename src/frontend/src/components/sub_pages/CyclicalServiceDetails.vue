<template>
    <h1>Szczegóły Usługi cyklicznej {{cycleInfo.getIdCyclicalService}}</h1>
        <div class=" pt-4">
          <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h3>Dane Usługi: </h3>
                    <span><b>Powiązane Konto Użytkownika Systemu: </b>{{ cycleInfo.accountUsername }}</span><br>
<!--                    <span><b>Cena: </b>{{ cycleInfo.price }}</span><br>-->
                    <span><b>Usługa: </b>{{ cycleInfo.oneTime === true ? 'Jednorazowa' : 'Cykliczna'}}</span><br>
                    <span><b>Numer Umowy: </b>{{ cycleInfo.agreementNumber }}</span><br>

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
                  <span><b>Typ Karty: </b>{{ translateCardType(cycleInfo.certificate.cardType) }}</span><br>
                  <span><b>Numer Karty: </b>{{ cycleInfo.certificate.cardNumber }}</span><br>
                  <button class="btn btn-primary"  data-bs-toggle="collapse" data-bs-target="#certHistory" >Historia certyfikatów ... </button>

                </div>

                <div class="col-md-4">
                  <h3>Użytkownik Usługi i Firma:</h3>
                  <span><b>Imię użytkownika usługi: </b>{{ cycleInfo.serviceUser.name }}</span><br>
                  <span><b>Nazwisko użytkownika usługi: </b>{{ cycleInfo.serviceUser.getSurname }}</span><br>
                  <span><b>Maile użytkownika: </b></span>
                  <ul>
                    <li v-for="(email, index) in serviceUserContactData.emails" :key="index">
                      {{ email.email }}
                    </li>
                  </ul>
                  <span><b>Numery telefonów użytkownika: </b></span>
                  <ul>
                    <li v-for="(phone, index) in serviceUserContactData.phoneNumbers" :key="index">
                      {{ phone.number }}
                    </li>
                  </ul>
                  <span><b>Stanowisko w firmie: </b>{{ cycleInfo.certificate.nameInOrganisation || 'brak danych' }}</span><br>
                  <span><b>----------------</b></span><br>
                  <span><b>Nazwa Firmy: </b>{{ cycleInfo.business.businessName }}</span><br>
                  <span><b>Maile firmy: </b></span>
                  <ul>
                    <li v-for="(email, index) in businessContactData.emails" :key="index">
                      {{ email.email }}
                    </li>
                  </ul>
                  <span><b>Numery telefonów firmy: </b></span>
                  <ul>
                    <li v-for="(phone, index) in businessContactData.phoneNumbers" :key="index">
                      {{ phone.number }}
                    </li>
                  </ul>
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
                    <td>{{ decodeStatus(change.statusTypeName)[0] }}</td>
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
                    <td>{{ translateCardType(cert.cardType) }}</td>
                    <td>{{ cert.cardNumber }}</td>
                    <td>{{ cert.nameInOrganisation }}</td>

                  </tr>
                </tbody>
              </table>
            </div>
        </div>
        
        <br>
        <button class="btn1 btn btn-danger" type="button" @click="goBack" style="float: right">Powrót</button>
    </div>
  </template>
  
  <script>  
  import { fetchWrapper, translateCardType,decodeStatus } from '@/utility';
  export default {
    name: 'CyclicalServiceDetails',
    data() {
      return {
        cycleId: null,
        cycleInfo: this.$store.state.passedValue,
        statusList: [],
        statusChangeHistory: [],
        certHistory:[],
        serviceUserContactData: { emails: [], phoneNumbers: [] },
        businessContactData: { emails: [], phoneNumbers: [] }
      };
    },
    mounted() {
      this.statusList = decodeStatus(this.cycleInfo.statusBitmask);
      this.fetchStatusChangeHistory();
      this.fetchCertHistory();
      this.fetchBusinessContactData(this.cycleInfo.business.idBusiness);
      this.fetchServiceUserContactData(this.cycleInfo.serviceUser.idServiceUser);
      // this.cycleId = this.$route.params.id; 
    },
    computed:{

    },
    methods: {
      decodeStatus,
      translateCardType,
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

      async fetchBusinessContactData(idBusiness) {
        try {
          const response = await fetchWrapper(this, `/api/business/get/${idBusiness}`, {
            method: 'GET',
          });
          const businessData = await response.json();
          this.businessContactData = businessData.contactData || { emails: [], phoneNumbers: [] };
        } catch (error) {
          console.error("Błąd pobierania danych firmy:", error);
        }
      },
      async fetchServiceUserContactData(idServiceUser) {
        try {
          const response = await fetchWrapper(this, `/api/serviceUser/get/${idServiceUser}`, {
            method: 'GET',
          });
          const userData = await response.json();
          this.serviceUserContactData = userData.contactData || { emails: [], phoneNumbers: [] };
        } catch (error) {
          console.error("Błąd pobierania danych użytkownika:", error);
        }
      },
      
      
        goBack() {
          this.$router.push('/Cycles');
        }
      }
  };
  </script>
  
  <!-- <style src="@/assets/style.css"></style> -->
  