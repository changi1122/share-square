<template>
    <LogoutTopTitle/>
    <div class="none"></div>
    <div class="layout">
        <p>Community</p> 

        <div class="box">
            <p>Title</p>
            <input v-model="title" name="title" type = "text" class="input"/>
        </div>

        <div class="box">
            <p>Content</p>
            <textarea v-model="content" name="content"></textarea>
        </div>

        <div class="btn">
            <button class="Submit-btn" @click="write">Submit</button>
            <div class="filebox">
                <input class="upload-name" value="첨부파일" placeholder="첨부파일">
                <label for="file">Upload</label> 
                <input type="file" id="file" @change="upload">
            </div>
        </div>

    </div>
</template>

<script>
import Axios from 'axios';
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import $ from 'jquery';


export default{
    name:'WritePage',
    components: {
            LogoutTopTitle,
    },
    data: function(){
        return{
                id:-1,
                title: '',
                content:'',
                visiter:0,
                fileinfo :'No image Data',
                key :'',
            }  
    },
    methods:{
        write(){
            var vm = this
            var formdata = new FormData();
            //var photoFile = document.getElementById("file");
            // console.log(photoFile);
            // console.log(photoFile.files[0]);
            // frm.append("file", photoFile.files[0]);
            // console.log(frm);
            // console.log(111);

            console.log(process.env.VUE_APP_BACKEND_URL);

            console.log("FFFFFF : ",this.title.replace(/\s/g,'').length)

            if( this.title.replace(/\s/g,'').length != 0 && this.content.replace(/\s/g,'').length != 0){
                this.id = this.$store.state.Userid.userid;
    
                var url2 ='/api/community/write/test';
                var url ='/api/community/write';

                var data={
                    user_id: this.id,
                    title : this.title,
                    content : this.content,
                    visiter:this.visiter
                }
                console.log(data.title)
                console.log(data.content)
    
                Axios.post(url,data).then(res => {
                    console.log("res: ",res.data);
                    vm.key = res.data;
                    console.log("DSsdsdsd", vm.key)

                    formdata.append('files', vm.fileinfo);
                    formdata.append('key', vm.key)

                    console.log(formdata);

                    Axios.post(url2,formdata, {
                    headers:{
                        'Content-Type' : 'application/json'
                    }
                    }).then(res => {
                    console.log(res);
                    })
                })

                //formdata.append('title', vm.title);
                //formdata.append('content', vm.content);

                alert("작성완료")
                this.$router.push({
                    path:'/community'
                })
            }else{
                alert("Error : Check Title or Content")
            }

        },
        upload(e){
            let imageFile = e.target.files;
            console.log("imageFile: " , imageFile);
            this.fileinfo = imageFile[0]
            console.log("fileinfo : ", this.fileinfo);
        }
    },
    mounted(){
        $("#file").on('change',function(){
        var fileName = $("#file").val();
        $(".upload-name").val(fileName);
        });
    }
}
</script>

<style scoped>
    p{
        margin: 0px 0px;
    }

    .btn{
        display: flex;
        width: 502px;
        flex-wrap: wrap;
        flex-direction: row;
        align-items: center;
    }

    .filebox label,
    .Submit-btn {
        font-size: 13px;
        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        transition-duration: 0.4s;        
    }

    .Submit-btn{
        margin-right: 15px;
        padding: 10px 15px;
        border-radius: 10px;
    }

    .filebox label{
        padding: 8px 15px;
        border-radius: 10px;
    }

    .filebox label:hover,
    .Submit-btn:hover{
        color: white;
        background-color: #5EDB97;
        cursor: pointer;
    }

    .box{
        margin-top : 10px;
    }

    .none{
        height: 50px;
    }

    .layout{
        margin : 0px 0px;
        margin-top : 10px;
        display: flex;
        flex-direction: column;
        align-items: center;
        flex-wrap: wrap;
    }

    .layout>p{
        font-size: 25px;
        margin-top: 30px;
    }

    .input{
        width: 502px;
        box-sizing: border-box;
        border: 1px solid #000000;
        margin : 0px 0px;
        padding : 3px 0px;
    }

    textarea{
        width: 500px;
        height: 300px;
        resize: none;
        margin : 0px 0px;
        padding : 0px 0px;
        font-size: 11px;
        font-family: "Inter", 'Noto Sans KR', "Helvetica Neue", Helvetica, Arial, "맑은 고딕", malgun gothic, "돋움", Dotum, sans-serif, "Apple Color Emoji";
    }

    textarea:focus {
        outline: none;
    }

    .filebox{
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
    }

    .filebox .upload-name {
        display: inline-block;
        padding: 0 10px;
        vertical-align: middle;
        border: 1px solid #dddddd;
        width: 200px;
        color: #999999;
        padding: 10px 0px;
        margin-left: 5px;
    }

    .upload-name{
        font-size: 13px;
    }

    .filebox input[type="file"] {
        position: absolute;
        width: 0;
        height: 0;
        padding: 0;
        overflow: hidden;
        border: 0;
    }

</style>