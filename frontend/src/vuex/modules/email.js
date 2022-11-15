const state ={
    email:"just@test.com"
};

const getters = {
    getEmail : state => state.email
};

const mutations ={
    setEmail(state, value){
        state.email = value;
        console.log(state.email);
    }
}

export default {namespaced: true, state, mutations, getters}
