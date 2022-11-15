const state ={
    userid:-1
};

const getters = {
    getuserid : state => state.userid
};

const mutations ={
    setuserid(state, value){
        state.userid = value;
        console.log(state.userid)
    }
}

export default {namespaced: true, state, mutations, getters}
