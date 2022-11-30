const state ={
    is_admin:false
};

const getters = {
    getIsadmin : state => state.is_admin
};

const mutations ={
    setIsadmin(state, value){
        state.is_admin = value;
        console.log(state.is_admin);
    }
}

export default {namespaced: true, state, mutations, getters}
