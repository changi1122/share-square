<template>
    <LogoutTopTitle/>
    <div class="none"></div>
    <div class="row">
        <div class="layout">
            <p class="page-title">Community</p>

            <input v-model="title" class="title-input" name="title" type="text" placeholder="제목 입력"/>
            <QuillEditor ref="EDITOR" v-model:content="content" />

            <div class="filebox">
                <input class="upload-name" value="첨부파일" placeholder="첨부파일">
                <label for="file">Upload</label> 
                <input type="file" id="file" accept="image/*" @change="upload">
            </div>

            <div class="btn">
                <button class="submit-btn" @click="update">Update</button>
            </div>
        </div>
    </div>
</template>

<script>
import Axios from 'axios';
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import QuillEditor from '@/components/QuillEditor.vue';
import $ from 'jquery';


export default{
    name:'UpdatePage',
    components: {
            LogoutTopTitle,
            QuillEditor
    },
    mounted() {
        $("#file").on('change',function(){
        var fileName = $("#file").val();
        $(".upload-name").val(fileName);
        });

        const vm = this;

        const index = this.$route.params.updateId;
        vm.id = index;

        Axios.get('/api/community/getbyid',{
            params:{
                id : index,
            }
        })
        .then(function(response){
            vm.title = response.data.title;
            vm.$refs.EDITOR.setContent(response.data.content);
        })
        .catch(function(error) {
                console.log(error);
        })
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
        async update(){
            const vm = this;

            if( this.title.replace(/\s/g,'').length != 0 && this.content.replace(/\s/g,'').length != 0){
    
                const FileURL ='/api/community/write/test';
                const UpdateURL ='/api/community/update';

                const data = {
                    id: this.id,
                    title: this.title,
                    content: this.content,
                    visiter: this.visiter
                }
    
                try {
                    await Axios.post(UpdateURL, data);
                    vm.key = this.id;
                    
                    const formdata = new FormData();
                    formdata.append('files', vm.fileinfo);
                    formdata.append('key', vm.key);

                    await Axios.post(FileURL, formdata, {
                        'Content-Type': 'multipart/form-data'
                    });

                    this.$router.push({
                        path:'/community/view/' + vm.key
                    });
                } catch (err) {
                    alert("변경 실패");
                    console.log(err);
                }
            }
        },
        upload(e){
            let imageFile = e.target.files;
            console.log("imageFile: " , imageFile);
            this.fileinfo = imageFile[0]
            console.log("fileinfo : ", this.fileinfo);
        }
    }
}
</script>

<style scoped>

    .row {
        max-width: 680px;
        margin: 0 auto;
        padding: 0 20px;
        width: 100%;
        box-sizing: border-box;
    }

    p {
        margin: 0px 0px;
    }

    .page-title {
        text-align: center;
    }

    .btn {
        text-align: right;
        width: 100%;
    }

    .filebox label,
    .submit-btn {
        font-size: 13px;
        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        transition-duration: 0.4s;        
    }

    .submit-btn{
        padding: 10px 15px;
        border-radius: 10px;
    }

    .filebox label{
        padding: 8px 15px;
        border-radius: 10px;
    }

    .filebox label:hover,
    .submit-btn:hover{
        color: white;
        background-color: #5EDB97;
        cursor: pointer;
    }

    .none{
        height: 50px;
    }

    .layout{
        margin : 0px 0px;
        margin-top : 10px;
    }

    .layout>p{
        font-size: 25px;
        margin-top: 30px;
    }

    .title-input {
        width: 100%;
        box-sizing: border-box;
        border: 1px solid #000000;
        margin : 20px 0px;
        padding : 4px 15px;
        box-sizing: border-box;
        font-size: 24px;
        font-weight: bold;
        font-family: inherit;
        border: 1px solid #d1d5db;
    }

    .filebox {
        display: flex;
        margin: 20px 0;
    }

    .filebox .upload-name {
        display: inline-block;
        padding: 0 10px;
        vertical-align: middle;
        border: 1px solid #d1d5db;
        width: 200px;
        color: #999999;
        padding: 10px 15px;
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
    .filebox>label {
        margin-left: 10px;
    }

    @media only screen and (max-width:738px) {
        .row {
            max-width: 100%;
            padding: 0 15px;
        }
    }

</style>