const state ={
    username:"tset"
};

const getters = {
    getUsername : state => state.username
};

const mutations ={
    setUsername(state, value){
        state.username = value;
        console.log(state.username);
    }
}

export default {namespaced: true, state, mutations, getters}
