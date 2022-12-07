<template>
    <p>test get user</p>
    <div class="chat-user" v-for="(item, idx) in list" :key="idx" @click="View(item.id)">
        <p>id="last_act">{{item.last_act}}</p>
        <p>id="guest_name">{{item.guest_name}}</p>
        <p>id="user_id">{{item.user_id}}</p>
        <p>id="guest_id">{{item.guest_id}}</p>
        <p>id="is_deleted">{{item.is_deleted}}</p>
    </div>
</template>

<script>
import Axios from "axios"

export default{
    data(){
        return{
            list:[]
        }
    },
    mounted(){  
        this.Get();
    },
    methods:{
        Get(){
            var vm = this;
            Axios.get("/api/get/guest",{
                params:{
                    id:vm.$store.state.Userid.userid,
                }
            })
            .then(rep=>{
                console.log(rep);
                vm.list = rep.data;
            })
        }
    }
}
</script>


<style scoped>

</style>