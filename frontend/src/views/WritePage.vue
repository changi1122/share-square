<template>
    <div class="layout">
        <p>Write</p> 
        <input v-model="title" name="title" type = "text"/>
        <textarea v-model="content" name="content"></textarea>
        <input type="file" name="file" id="file"/>
        <button @click="write">제출</button>
    </div>
</template>

<script>
import Axios from 'axios';



export default{
    name:'WritePage',
    data: function(){
        return{
                title: '',
                content:'',
                visiter:0,
            }  
    },
    methods:{
        write(){
            
            var frm = new FormData();
            var photoFile = document.getElementById("file");
            console.log(photoFile.files[0]);
            frm.append("file", photoFile.files[0]);
            console.log(frm);
            console.log(111);

            var url ='/api/community/write';
            var data={
                title : this.title,
                content : this.content,
                visiter:this.visiter
            }
    
            console.log(data.title)
            console.log(data.content)

            Axios.post(url,data).then(res => {
                console.log(res);
            })
            alert("작성완료")
            this.$router.push({
                path:'/community'
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