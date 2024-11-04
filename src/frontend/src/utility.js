export function getCookie(cname){
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');

    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            console.log("Ciastka: ",document.cookie);

            return c.substring(name.length, c.length);

        }
    }
    
    return "";
}

export  function refreshCSRF(){
     fetchWrapper(this,`/api/authentication/requestToken`)
}

export function translateCardType(id){
  switch (id) {
    case 1:
      return "SIMPLYSIGN Podpis"
    case 2:
      return "SIMPLYSIGN Pieczęć"
    case 3:
      return "Fizyczny Podpis"
    case 4:
      return "Fizyczna Pieczęć"  
    default:
      return "null"
  }
}

export async function fetchWrapper(context, url, init={}){

    const response = await fetch(url,init)
    if(response.status === 403){
        context.$store.commit('setRole', 'ROLE_nobody');
        context.$store.commit('setUsername', '');
        context.$router.push(
            {
                path: '/login',
                query: { message: 'Nie masz uprawnień do korzystania z danej usługi lub twój żeton stracił ważność spróbuj ponownie się zalogować.' }
            }
        )
        return;
    }
    return response;
}



export function decodeStatus(decodedStatus){
    let bitmask = decodedStatus;
    let statusesList = [];


    Object.values(STATUS_TYPES_LIST).forEach(curr => {
    //   console.log(curr.mVal," ",bitmask);
    //   console.log(this.hasStatus(bitmask,curr.mVal));
      
      if(hasStatus(bitmask,curr.mVal)){
        bitmask-=curr.mVal;
        statusesList.push(curr.desc);
      }
    })
    return statusesList;
}

export function hasStatus(statusBitmask, statusValue) {
    return (statusBitmask & statusValue) !== 0;
}


export const STATUS_TYPES_LIST = {
    BLANK: {
      mVal : 0,
      desc : "Zmiana Stanu"
    },
    AWAITING_RENEWAL: {
      mVal : 1,
      desc : "Prośba o Odnowienie"
    },
    PRO_FORM_SENT: {
      mVal : 2,
      desc : "Pro Forma Wysłana "
    },
    CANCEL_REQUEST: {
      mVal : 4,
      desc : "Prośba o Anulowanie"
    },
    CANCELED:{
      mVal : 8,
      desc : "Anulowane"
    },
    MARKED_AS_NON_RENEWABLE:{
      mVal : 16,
      desc : "Nieodnawialny"
    },
    RENEWED_ELSEWHERE:{
      mVal : 32,
      desc : "Odnowiony gdzie indziej"
    },
    PAYMENT_DONE:{
      mVal : 64,
      desc : "Zapłata Otrzymana"
    },
    INVOICE_SENT:{
      mVal : 128,
      desc : "Faktura wystawiona, kod wysłany"
    },
    RENEWED:{
      mVal : 256,
      desc : "Odnowione"
    },    
    NEW:{
      mVal : 512,
      desc: "Nowy"
    },
    IGNORED:{
      mVal : 1024,
      desc: "Ignorowany"
    },
    EXPIRED:{
      mVal : 2048,
      desc: "Wygaszony"
    },

  }