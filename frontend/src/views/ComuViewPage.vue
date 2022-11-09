<template>
    <div>
        <p> id : {{this.id}}</p>
        <P> title : {{this.info.title}}</P>
        <P> content : {{this.info.content}}</P>

        <button @click="Previos"> previos</button>
        
        <button @click="Next"> next</button>

        <button @click="Main"> main</button>

        <button @click="Delete"> delete</button>

        <button @click="Update"> Update</button>
    </div>
</template>

<script>
import Axios from 'axios'

export default{
    name:'ComuViewPage',
    data(){
        return{
            id:null,
            info:[],
        }
    },
    mounted(){
        var vm = this;

        const index  = this.$route.params.contentId
        console.log("i got :" , index)
        vm.id = index

        Axios.get('/api/community/view',{
            params:{
                id:index,
            }
        })
        .then(function(response){
                console.log(response.data)
                vm.info = response.data;
                console.log(vm.info)
        })
        .catch(function(error) {
                console.log(error);
        })
    },
    methods:{
        Previos(){
            var vm = this;
            console.log("Previos id" , vm.id)
            Axios.get('/api/community/view/previos',{
                params:{
                    id:vm.id,
                }
            })
            .then(function(response){
                    console.log("Previos data ",response.data[0].id)
                    vm.id = response.data[0].id
                    console.log(vm.id)
                    vm.info = response.data[0]
            })
            .catch(function(error) {
                    console.log(error);
                    alert("No more page")
            })
        },
        Next(){
            var vm = this
            Axios.get('/api/community/view/next',{
                params:{
                    id:vm.id,
                }
            })
            .then(function(response){
                    console.log("Next data ", response.data)
                    vm.id = response.data[0].id
                    console.log(vm.id)
                    vm.info = response.data[0]
            })
            .catch(function(error) {
                    console.log(error);
                    alert("No more page")
            })
        },
        Main(){
            this.$router.push({
                path:"/community"
            })
        },
        Delete(){
            var vm = this;
            console.log("Delete id" , vm.id)
            Axios.get('/api/community/delete',{
                params:{
                    id:vm.id,
                }
            })
            .then(function(response){
                    console.log(response.data)
                    if(response.data === true){
                        alert(vm.id+"삭제 성공")
                    }else{
                        alert(vm.id+"삭제 실패")
                    }
                    vm.$router.push({
                        path:"/community"
                    })
            })
            .catch(function(error) {
                    console.log(error);
            })
        },
        Update(){
            var vm = this
            this.$router.push({
                name: "UpdatePage",
                params:{
                    updateId:vm.id,
                }
            })
        }
    }
}

</script>
