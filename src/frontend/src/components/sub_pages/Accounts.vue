<template>
    <div>
      <h1 style="margin-bottom: 20px;">Panel administratora</h1>
      <div class="container ">
        <!-- <button  class="add-button" @click="switchRequestModalVisibility()"
        data-bs-toggle="modal" data-bs-target="#registerModal"        
        >Dodaj nowe konto</button> -->
        <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.username" placeholder="Username" style="margin-bottom: 10px; margin-right: 10px;">
          <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
            <input type="text" class="input" v-model="searchFields.role" placeholder="Rola" style="margin-bottom: 10px; margin-right: 10px;">
            <!-- <input type="text" class="input" v-model="searchFields.id" placeholder="REGON" style="margin-bottom: 10px; margin-right: 10px;"> -->
          </div>
          <button class="btn btn-primary"  @click="fetchAccounts">
                Odśwież Użytkowników
            </button>
          <button class="btn btn-warning btn1" style="margin-left: 5px" type="button" @click="dbWipe">
            Usuń bazę
          </button>
          <button class="btn btn-warning" style="margin-left: 5px" type="button" @click="forceDailyCheck">
            Aktualizuj Stany
          </button>
          <!-- <button @click="toggleSearchFields" style="margin-left: 10px;">+</button> -->
        </div>
      </div>


      <table class=" custom-container">
        <thead>
        <tr>
          <th>Login</th>
          <th>Ostatnio aktywny</th>
          <!-- <th>Rola</th> -->
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="account in filteredAccounts" :key="account.idLoginCredentials">
          <td>{{ account[0] }}</td>
          <td>{{ account[1] == 0 ? "teraz" : account[1]+" min temu"}} </td>
          <!-- <td>{{ account.role }}</td> -->
          <td>
            <button class="btn btn-danger "  type="button" @click="forceLogout(account[0])">
                Wygaś Żeton
            </button>

          </td>
        </tr>
        </tbody>
      </table>
  
      <!-- Modal rejestracji -->
      <div  id="registerModal" class="modal fade" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h2>Rejestracja nowego konta </h2>

          </div>
          <div class="modal-body ">

            <label for="username" >Username:</label>
            <br>
            <input name="username" v-model="registerForm.username" maxlength="40" class="form-control">
            <br>
            <label for="password">Hasło:</label>
            <br>
            <input name="password" v-model="registerForm.password" maxlength="80" class="form-control">
            <br>
            <label for="role">Rola:</label>
            <br>
            <select id="role"  v-model="registerForm.role"  class="form-select">                
                <option value="ROLE_user" selected >user</option>
                <option value="ROLE_editor">editor</option>
                <option value="ROLE_admin">admin</option>
            </select >

            <br> 
            <p class="text-danger">{{ registerForm.errorMessage }}</p>
          </div>

          <div class="modal-footer">
            <button @click="register" class="btn btn-outline-success">Submit</button>
            <button id="closeRegister" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>

    </div>
  </template>
  
  <script>
  import { getCookie,fetchWrapper} from '@/utility';
  export default {
    name: 'AccountsList',
    data() {
      return {
        accounts: [],
        searchFields: {
          id: '',
          username: '',
          role: ''
        },
        registerForm:{
            username:'',
            role:'ROLE_user',
            password:'',
            errorMessage:""
        },
        showAdditionalFields: false,
        // contactDataDetails: null,
  
      };
    },
    computed: {
      filteredAccounts() {
        return this.accounts.filter(account => {
          return (
            account[0].toLowerCase().includes(this.searchFields.username.toLowerCase())
            
            //&&
            // account.role.toLowerCase().includes(this.searchFields.role.toLowerCase())
          );
        });
      }
    },
    mounted() {
      this.fetchAccounts();
    },
    methods: {

        async forceLogout(login){
          try {
                const cookie = getCookie('XSRF-TOKEN');

                const response = await fetchWrapper(this,`/api/authentication/forceLogout?login=${login}`, {
                    method: 'POST',
                    headers:{
                      'X-XSRF-TOKEN': cookie

                    }
                });

                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
                }

                const role = response.headers.get('frontRole');
                this.$store.commit('setRole', role);
                
                this.accounts.splice(this.accounts.findIndex(ac=>ac[0]==login),1)
                alert(`Z powodzeniem dokonano operacji wygaszenia żetonu użytkownika ${login}!`);

                // const data = await response.json();
                

            } catch (error) {
                console.error("There has been a problem with your fetch operation:", error);
            }
        },
        async fetchAccounts(){
            try {
                const response = await fetchWrapper(this,`/api/authentication/getRecentActivities`, {
                    method: 'GET',
                });

                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
                }

                const role = response.headers.get('frontRole');
                this.$store.commit('setRole', role);
                const data = await response.json();
                this.accounts = Object.entries(data);
                // const serverTimezone = parseInt(response.headers.get("timezone"),10);
                // let localTime = new Date(Date.now())
                // console.log(localTime);
                
                // console.log(localTime.getTimezoneOffset()*1000*60," vs ",serverTimezone);
                

                // this.accounts.forEach(account=>{
                  
                //   const minutes = -1*((new Date(account[1])).getTime()-Date.now())/1000/60;
                //   account[1] = minutes.toFixed(0);
                // })


            } catch (error) {
                console.error("There has been a problem with your fetch operation:", error);
            }
            
        },
      async dbWipe() {

        const confirmed = window.confirm("Czy na pewno chcesz usunąć całą bazę danych? Tej operacji nie można cofnąć!");

        if (!confirmed) {
          return;
        }

        try {
          const cookie = getCookie('XSRF-TOKEN');
          const response = await fetchWrapper(this, '/api/specialActions/dbWipe', {
            method: 'POST',
            headers: {
              'X-XSRF-TOKEN': cookie
            }
          });

          if (!response.ok) {
            throw new Error('Wystąpił błąd podczas usuwania bazy danych');
          }

          alert('Z powodzeniem usunięto bazę danych');
          this.fetchAccounts();
        } catch (error) {
          console.error('Wystąpił błąd:', error);
          alert(error.message);
        }
      },

      async forceDailyCheck() {

        const confirmed = window.confirm("Czy na pewno odświeżyć stany usług cyklicznych ?");

        if (!confirmed) {
          return;
        }

        try {
          const cookie = getCookie('XSRF-TOKEN');
          const response = await fetchWrapper(this, '/api/cyclicalservice/forceDailyCheck', {
            method: 'POST',
            headers: {
              'X-XSRF-TOKEN': cookie
            }
          });

          if (!response.ok) {
            throw new Error('Wystąpił błąd podczas usuwania bazy danych');
          }

          alert('Z powodzeniem odświeżono stany usług cyklicznych');
          this.fetchAccounts();
        } catch (error) {
          console.error('Wystąpił błąd:', error);
          alert(error.message);
        }
      },
        toggleSearchFields() {
            this.showAdditionalFields = !this.showAdditionalFields;
        },
        

        switchRequestModalVisibility(){
            this.registerForm.username = '';
            this.registerForm.role = 'ROLE_user';
            this.registerForm.password = '';
        },



        async register() {

          let role = 0;
          switch (this.registerForm.role) {
            case "ROLE_editor":
              role = 1;
              break;
            case "ROLE_admin":
              role = 2;
              break;
            default:
              role = 0;
              break;
          }


          let payload = {
            login: this.registerForm.username,
            password: this.registerForm.password,
            role:role,

          };
          try{
            if(payload.login.trim() === "")
              throw new Error("Nie określiłeś loginu użytkownika")
            if(payload.password.trim() === "")
              throw new Error("Nie określiłeś hasła użytkownika")

            const cookie = getCookie("XSRF-TOKEN");
            const response = await fetchWrapper(this,`/api/accountData/register`, {
                method: 'POST',
                headers:{
                  'Content-Type': 'application/json',
                  'X-XSRF-TOKEN':cookie
                },
                body: JSON.stringify(payload)

            })
            
            //.then(response => {
            if (!response.ok) {
                if (response.status === 409) {
                throw new Error('Cannot register user');
                } else {
                throw new Error(await response.text());
                }
            }

            this.switchRequestModalVisibility();
            document.getElementById("closeRegister").click();
            
            this.fetchAccounts();
            //})
          }catch(error){
            // .catch(error => {
              this.registerForm.errorMessage = error.message;

          }
        
        },
          
        closeModal() {
        this.showModal = false;
        }
    }
  };
  </script>
  
  <style src="@/assets/style.css"></style>
  
  <style>
  @media (min-width: 1200px) { 
      .custom-container {
          max-width: 720px; 
      }
  }
  .close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
  }
  
  .close:hover,
  .close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
  }
  .view-button {
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 2px 8px;
    font-size: 12px;
    cursor: pointer;
    outline: none;
    margin: 0;
  }
  
  .view-button:hover {
    background-color: #0056b3;
  }
  </style>
  