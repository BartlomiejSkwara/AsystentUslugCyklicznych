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