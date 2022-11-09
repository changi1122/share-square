import { createStore }  from 'vuex'

export default createStore({
    state:{
        id:0
    },
    getters:{
        time2(state){
            return state.id;
        }
    },
    mutations:{
        setCounter(state, value){
            state.id = value;
        }
    }
})