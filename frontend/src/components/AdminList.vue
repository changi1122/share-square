<template>
    <div class="data" v-for="(item, idx) in list" :key="idx">
        <p class="text"> {{item.title}}</p>
        <p class="text"> {{item.content}}</p>
        <p class="text">{{item.latitude}}</p>
        <p class="text">{{item.longtitude}}</p>
        <p class="text">{{item.created_at}}</p>
        <p class="text">{{item.is_deleted}}</p>
        <button @click="Deleted(item.id)"> 삭제 </button>
        <button @click="Update(item.id)"> 수정 </button>
    </div>
</template>

<script>
import Axios from 'axios'

export default{
    data(){
        return{
            list:[],
        }
    },
    mounted(){
        this.Get()
    },  
    methods:{
        Get(){
            var vm = this
            Axios.get("/api/extendinfo/getAll")
            .then(rep=>{
                console.log(rep);
                vm.list = rep.data;
                console.log(vm.list)
                
            })
        },
        Deleted(id){
            Axios.get("/api/extendinfo/delete", {
                params:{
                    id  : id
                }
            })
            .then(rep=>{
                console.log(rep);
            })
        },
        Update(id){
            this.$emit('updateID', id);
        }
    }

    
}

</script>

<style scoped>
.data{
    display: flex;

}

.text{
    margin-right: 10px;
}
</style>
