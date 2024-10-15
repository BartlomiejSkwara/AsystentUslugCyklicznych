<template>
    <div>
      <h1 style="margin-bottom: 20px;">Lista kont użytkowników systemu</h1>
      <div class="container">
        <button  class="add-button" @click="switchRequestModalVisibility()"
        data-bs-toggle="modal" data-bs-target="#registerModal"        
        >Dodaj nowe konto</button>
        <div style="display: inline-block; align-items: center; flex-wrap: wrap;">
          <input type="text" class="input" v-model="searchFields.username" placeholder="Username" style="margin-bottom: 10px; margin-right: 10px;">
          <div v-if="showAdditionalFields" style="display: inline-block; flex-wrap: wrap;">
            <input type="text" class="input" v-model="searchFields.role" placeholder="Rola" style="margin-bottom: 10px; margin-right: 10px;">
            <!-- <input type="text" class="input" v-model="searchFields.id" placeholder="REGON" style="margin-bottom: 10px; margin-right: 10px;"> -->
          </div>
          <button @click="toggleSearchFields" style="margin-left: 10px;">+</button>
        </div>
      </div>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Rola</th>
          <!-- <th>Działania</th> -->
        </tr>
        </thead>
        <tbody>
        <tr v-for="account in filteredAccounts" :key="account.idLoginCredentials">
          <td>{{ account.idLoginCredentials }}</td>
          <td>{{ account.username }}</td>
          <td>{{ account.role }}</td>
          <!-- <td>
            <button class="action-button edit-button" @click="editBusiness(business.idBusiness)">Edytuj</button>
            <button v-if="isAdmin" class="action-button delete-button" @click="deleteBusiness(business.idBusiness)">Usuń</button>
          </td> -->
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
            account.username.toLowerCase().includes(this.searchFields.username.toLowerCase()) &&
            account.role.toLowerCase().includes(this.searchFields.role.toLowerCase())
          );
        });
      }
    },
    mounted() {
      this.fetchAccounts();
    },
    methods: {
        async fetchAccounts(){
            try {
                const response = await fetchWrapper(this,`/api/accountData/getAll`, {
                    method: 'GET',
                });

                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
                }

                const role = response.headers.get('frontRole');
                this.$store.commit('setRole', role);
                const data = await response.json();
                this.accounts = data;
            } catch (error) {
                console.error("There has been a problem with your fetch operation:", error);
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
  