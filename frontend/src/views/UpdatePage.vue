<template>
    <div class="layout">
        <p>Update</p> 
        <input name="title" type = "text" v-model="this.info.title"/> 
        <textarea name="content" v-model="this.info.content"> </textarea>
        <input type="file" name="file"/> 
        <button @click="update">수정</button>
    </div>
</template>

<script>
import Axios from "axios"

export default{
    name:'UpdatePage',
    data(){
        return{
            id:null,
            info:[],
        }
    },

    mounted(){
        var vm = this;

        const index  = this.$route.params.updateId
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
        update(){
            var vm =this
            var url ='/api/community/update';
            var data={
                title : vm.info.title,
                content : vm.info.content,
                id: vm.id,
                visiter: vm.info.visiter
            }
    
            console.log(data.title)
            console.log(data.content)

            Axios.post(url,data).then(res => {
                console.log(res);
                if(res.data=== true){
                    alert(vm.id + " 변경 성공")
                }else{
                    alert(vm.id+" 변경 실패")
                }
                vm.$router.push({
                        path:"/community"
                })
            })
        },
    }
}
</script>

<style scoped>
    .layout{
        width : 500px;
        margin : 0 auto;
        margin-top : 10px;
    }

    .layout > input{
        width: 100px;
        box-sizing: border-box;
        margin-top: 10px;
    }

    .layout > textarea{
        width: 100px;
        margin-top: 10px;
    }
</style>