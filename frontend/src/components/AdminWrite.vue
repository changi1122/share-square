<template>
    <div class="row display">

        <p class="subtitle">추가정보 등록</p>
        <input v-model="title" name="title" type="text" class="input" placeholder="물품 이름 입력"/>

        <div class="box">
            <QuillEditor ref="EDITOR" v-model:content="content" />
        </div>

        <SmallMap ref="toMap"/>

        <!-- 여기 조건 추가하기 -->
        <template v-if="this.is_update == true">
            <div @click="() => { this.$refs['image'].click(); }" v-if="imageTempUrl === ''" class="image-box">
                <img :src='"/api/extendinfo/fileview/" + this.filename' alt="" id="text-img">
            </div>
            <div @click="() => { this.$refs['image'].click(); }" v-else class="image-box">
                <img :src="imageTempUrl" alt="" id="text-img">
            </div>
        </template>


        <template v-else>
            <div @click="() => { this.$refs['image'].click(); }" v-if="imageTempUrl === ''" class="image-box">
                <div class="image-add-box">
                    이미지 추가
                </div>
            </div>
            <div @click="() => { this.$refs['image'].click(); }" v-else class="image-box">
                <img :src="imageTempUrl" alt="" id="text-img">
            </div>
        </template>

        <div class="filebox">
            <input class="upload-name" value="첨부파일" placeholder="첨부파일">
            <label for="file">Upload</label> 
            <input ref="image" type="file" id="file" @change="upload" accept="image/*">
        </div>

        <template v-if="this.is_update== false ">
            <div class="submit-buttons">
                <button id="submit" class="accentbutton" @click="write">등록</button>
            </div>
        </template>
        <template v-else>
            <div class="submit-buttons">
                <button id="delete" class="commonbutton" style="margin-right: 10px" @click="Cancle">취소</button>
                <div class="none"></div>
                <button id="delete" class="commonbutton" style="margin-right: 10px" @click="Delete">물품 삭제</button>
                <div class="none"></div>
                <button id="update" class="accentbutton" @click="update">정보 수정</button>
            </div>
        </template> 
    </div>

</template>


<script>
import SmallMap from './SmallMap.vue';
import QuillEditor from '@/components/QuillEditor.vue';
import Axios from 'axios';
import $ from "jquery";

export default{
    data(){
        return{
            id:0,
            key:0,
            is_update:false,
            title:"",
            content:"",
            imageTempUrl:"",
            fileinfo:"",
            filename:"",
            info:[],
        }
    },
    mounted(){

        this.$emit('isupdate',true)

        $("#file").on('change',function(){
            var fileName = $("#file").val();
            $(".upload-name").val(fileName);
        });

        $(document).ready(function(){
            $(".open").click(function(){
                document.querySelector(".null").classList.add("Off");
                document.querySelector(".show_map").classList.add("show");
            });
            $(".show_background").click(function(){
                document.querySelector(".null").classList.remove("Off");
                document.querySelector(".show_map").classList.remove("show");
            });
            $(".map-close").click(function(){
                document.querySelector(".null").classList.remove("Off");
                document.querySelector(".show_map").classList.remove("show");
            });
        });

    },
    components:{
        SmallMap,
        QuillEditor,
    },
    methods:{
        setSelect(event) {
            // 카테고리 변경 적용
            this.selected1 = event.target.value;
        },
        upload(e){
            let imageFile = e.target.files;
            this.fileinfo = imageFile[0];
            this.imageTempUrl = URL.createObjectURL(imageFile[0]);
        },
        write(){
            var vm = this
            var data = {
                title: this.title,
                content: this.content,
                latitude : this.$refs.toMap.latitude,
                longtitude : this.$refs.toMap.longitude
            }

            Axios.post("/api/extendinfo/save", data)
            .then(res=>{
                this.key = res.data;

                console.log("vm.key(글 ID) : ", vm.key)
                const formdata = new FormData();
                formdata.append('files', vm.fileinfo);
                formdata.append('key', vm.key);

                Axios.post("/api/extendinfo/save/file", formdata,{
                    'Content-Type': 'multipart/form-data'
                })

            })
        },
        Cancle(){
            var vm = this
            vm.info = []
            vm.id = 0
            vm.title = ""
            vm.content = ""
            vm.$refs.EDITOR.setContent("")
            console.log(vm.content)
            vm.$refs.toMap.latitude = 33.450701
            vm.$refs.toMap.longitude = 126.570667
            vm.filename = ""
            vm.is_update = false

            vm.$emit('cancle', true)
        },
        update(){
            var vm = this
            console.log(vm.id)
            var data={
                id: vm.id,
                title: vm.title,
                content:vm.content,
                latitude:vm.$refs.toMap.latitude,
                longtitude:vm.$refs.toMap.longitude
            }
            console.log(data)

            Axios.post("/api/extendinfo/update", data)
            .then(res=>{
                console.log(res)

                const formdata = new FormData();
                formdata.append('files', vm.fileinfo);
                formdata.append('key', vm.info.id);

                Axios.post("/api/extendinfo/save/file", formdata,{
                    'Content-Type': 'multipart/form-data'
                })
            })
        },
        Delete(){
            Axios.get("/api/extendinfo/delete", {
                params :{
                    id : this.id
                }
            }).then(res =>{
                console.log(res)
            })
        }
    }, 
    watch:{
        is_update(newdata){
            var vm = this
            if(newdata == true){
                Axios.get("/api/extendinfo/findbyId", {
                    params:{
                        id : vm.id
                    }
                }).then(res=>{
                    vm.info =  res.data
                    vm.title = vm.info.title
                    vm.$refs.EDITOR.setContent(vm.info.content)
                    vm.$refs.toMap.latitude = vm.info.latitude
                    vm.$refs.toMap.longitude = vm.info.longtitude
                    vm.filename = vm.info.filename
                })
            }
        }
    },
}

</script>



<style scoped>
    p{
        margin: 0px 0px;
    }

    .row {
        max-width: 680px;
        margin: 0 auto;
        padding: 0 20px;
        box-sizing: border-box;
    }

    p{
        margin: 0px 0px;
    }

    .subtitle {
        margin-bottom: 20px;
        font-size: 24px;
        font-weight: bold;
    }

    .display{
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .none{
        width: 10px;
    }

    .Off{
        display: none;
    }

    
    #map{
        width: 100%;
        height: 100%;
        margin: 0px 0px !important;
        z-index: 1;
    }

    #user_location {
        width:100%;
        border: 1px solid #d1d5db;
        font-size: 16px;
        font-family: inherit;
        padding: 10px 15px;
    }

    .box{
        width: 100%;
        margin-bottom: 20px;
    }

    .input{
        width: 100%;
        box-sizing: border-box;
        border: 1px solid #000000;
        margin-bottom: 20px;
        padding: 12px 15px;
        box-sizing: border-box;
        font-size: 16px;
        font-family: inherit;
        border: 1px solid #d1d5db;
    }

    .filebox label{
        font-size: 13px;
        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        transition-duration: 0.4s; 
    }

    .filebox label{
        padding: 8px 15px;
        border-radius: 10px;
    }

    .filebox label:hover{
        color: white;
        background-color: #5EDB97;
        cursor: pointer;
    }

    .filebox{
        display: none;
        flex-direction: row-reverse;
        align-items: center;
        justify-content: flex-end;
        width: 100%;
        margin-top: 10px;
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

    .filebox input[type="file"] {
        position: absolute;
        width: 0;
        height: 0;
        padding: 0;
        overflow: hidden;
        border: 0;
    }

    .map-button-area {
        position: absolute;
        bottom: 0;
        z-index: 100;
        background-color: #ffffff;
        width: 100%;
        padding: 8px 15px;
        box-sizing: border-box;
        border-radius: 8px;
    }
    .map-close {
        width: 100%;
        border: 1px solid #d1d5db;
        font-size: 14px;
        font-family: inherit;
        margin-top: 10px;
        padding: 10px 15px;
        cursor: pointer;
    }

    .select {
        background-color: #fff;
        width: 100%;
        padding: 10px 15px;
        font-family: inherit;
        font-size: 16px;
        border: 1px solid #d1d5db;
        box-sizing: border-box;
        outline: none;
    }
    .gray {
        color: rgba(0,0,0,0.6);
    }
    .find, .category{
        width: 100%;
        margin-bottom: 20px;
    }

    .location-button {
        width: 120px;
        height: 45.2px;
        margin-left: 10px;
        border: 1px solid #d1d5db;
        cursor: pointer;
        font-family: inherit;
    }

    .where{
        margin-top: 10px;
        margin-left: -1px;
    }
    
    .where,
    .find{
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-end;
    }

    .show_map {
        display: none; /*숨기기*/
        position: fixed;
        width: 100%;
        height: 100%;
    }

    .show_background{
        position: absolute;
        width: 100%;
        height: 100%;
        z-index: 1;
        background: rgba(0,0,0,0.4);
    }

    .show_size{
        position: relative;
        background: #ffffff;
        border-radius: 10px;
        width: 100%;
        height: 80%;
        margin: 0 15px;
        padding: 0 0 !important;
        z-index: 2;
        overflow: hidden;
    }

    .show{
        display: flex;
        align-items: center;
        align-content: center;
        flex-direction: row;
        justify-content: center;
    }

    .image-box {
        display: flex;
        width: 100%;
        border: 1px solid #d1d5db;
        box-sizing: border-box;
        cursor: pointer;
    }
    .image-box>img {
        width: 100%;
    }
    .image-add-box {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 120px;
        font-size: 14px;
        color: rgba(0,0,0,0.6);
    }

    .submit-buttons{
        width: 100%;
        margin: 20px 0 80px;
        display: flex;
        justify-content: flex-end;
    }

    .commonbutton {
        margin-top: 15px;
        font-size: 13px;
        font-family: inherit;
        border-radius: 30px;
        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        padding: 15px 36px;
        cursor: pointer;
    }
    .commonbutton:hover {
        color: white;
        background-color: #5EDB97;
    }
    
    .commonbutton {
        margin-top: 15px;
        font-size: 13px;
        font-family: inherit;
        border-radius: 30px;
        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        padding: 15px 36px;
        cursor: pointer;
    }
    .commonbutton:hover {
        color: white;
        background-color: #5EDB97;
    }
    
    .accentbutton {
        margin-top: 15px;
        font-size: 13px;
        font-family: inherit;
        border-radius: 30px;
        border: none;
        background-color:#5EDB97;
        color: #ffffff;
        padding: 15px 36px;
        cursor: pointer;
    }
    .accentbutton:hover {
        background-color: #48a773;
    }

    @media only screen and (max-width:738px) {
        .row {
            max-width: 100%;
            padding: 0 15px;
        }

        #null{
            height: 80px;
        }
        
    }

</style>