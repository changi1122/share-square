import { createStore } from "vuex";
import createPersistedState from 'vuex-persistedstate';
import Email from './modules/email';
import Islogin from "./modules/Islogin";
import Username from "./modules/username";
import userid from "./modules/userid";


export default createStore({
    modules:{
        Email :Email,
        Islogin :Islogin,
        Username :Username,
        Userid : userid
    },
    plugins:[
        createPersistedState()
    ]
})