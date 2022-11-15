const state ={
    is_login:0
};

const getters = {
    getIsLogin : state => state.is_login
};

const mutations ={
    setIsLogin(state, value){
        state.is_login = value;
        console.log(state.is_login)
    }
}

export default {namespaced: true, state, mutations, getters}
