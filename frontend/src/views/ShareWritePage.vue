<template>
    <TopHeader/>
    <div id="null"></div>
    <div class="row display">

        <p class="subtitle">나눔물품 등록</p>
        <input v-model="title" name="title" type="text" class="input" placeholder="물품 이름 입력"/>
    
        <div id="example-5" class="category">
            <select :class="{'select': true, 'gray': selected1 === ''}" :value="selected1" @change="setSelect($event)">
            <option
                v-for="(item, index) in selectList"
                :key="index"
                :value="item.value"
                >{{ item.name }}</option>
            </select>
        </div>

        <div class="box">
            <QuillEditor ref="EDITOR" v-model:content="content" />
        </div>

        <!-- 여기-->
        <!-- <div class="show_map">
            <div class="row show_size">
                <div id="map" @click="DD"></div>
                <div class="map-button-area">
                    <div class="where">
                        <button @click="findplace" class="location-button">장소 검색</button> 
                        <input id="user_location" type="text" placeholder="위치" v-model="location" @keyup.enter="findplace"/>
                    </div>
                    <button class="map-close">선택 완료</button>
                </div>
            </div>
            <div class="show_background"></div>
        </div> -->
        <SmallMap ref="CallMap"/>

        
        <template v-if="this.$route.params.func != 'push'">
            <div @click="() => { this.$refs['image'].click(); }" v-if="imageTempUrl === ''" class="image-box">
                <img :src='"/api/share/fileview/" + this.filename' alt="" id="text-img">
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
    
        <template v-if="this.$route.params.func== 'push' ">
            <div class="submit-buttons">
                <button id="submit" class="accentbutton" @click="write">물품 등록</button>
            </div>
        </template>
        <template v-else>
            <div class="submit-buttons">
                <button id="delete" class="commonbutton" style="margin-right: 10px" @click="Delete">물품 삭제</button>
                <div class="none"></div>
                <button id="update" class="accentbutton" @click="update">정보 수정</button>
            </div>
        </template> 
    </div>


</template>


<script>
import TopHeader from "@/components/TopHeader.vue";
import QuillEditor from '@/components/QuillEditor.vue';
import SmallMap from '@/components/SmallMap.vue';
import $ from "jquery"
import Axios from 'axios';

export default{
    components:{
        TopHeader,
        QuillEditor,
        SmallMap,
    },
    
    data(){
        return{
            path:"",
            index:0,
            title:"",
            content:"",
            selected1:"",
            filename:"",
            fileinfo:"",
            imageTempUrl:"",
            key:0,
            selectList:[
                { name: "카테고리 선택", value:""},
                { name: "박스/포장재", value:"박스/포장재"},
                { name: "생활용품", value:"생활용품"},
                { name: "청소도구", value:"청소도구"},
                { name: "의류", value: "의류"},
                { name: "미용", value: "미용"},
                { name: "기타 상품", value:"기타 상품"},
            ],
            send:null,
        }
    },
    mounted(){

        $("#file").on('change',function(){
            var fileName = $("#file").val();
            $(".upload-name").val(fileName);
        });

        $(document).ready(function(){
            $(".open").click(function(){
                document.querySelector("#null").classList.add("Off");
                document.querySelector(".show_map").classList.add("show");
            });
            $(".show_background").click(function(){
                document.querySelector("#null").classList.remove("Off");
                document.querySelector(".show_map").classList.remove("show");
            });
            $(".map-close").click(function(){
                document.querySelector("#null").classList.remove("Off");
                document.querySelector(".show_map").classList.remove("show");
            });
        });

        this.path = this.$route.params.func
        if(this.path != 'push'){
            this.Now();
        }

    },
    methods:{
        Now(){
            const vm = this
            console.log(vm.path)
            Axios.get('/api/share/find',{
                params:{
                    id: vm.path
                }
            })
            .then(function(response){
                console.log(response.data)
                vm.send = response.data
                vm.title = response.data.title
                vm.$refs.EDITOR.setContent(response.data.content);
                vm.$refs.CallMap.latitude = response.data.latitude
                vm.$refs.CallMap.longitude = response.data.longtitude
                vm.selected1 = response.data.category
                vm.filename = response.data.filename
            })
        },
        setSelect(event) {
            // 카테고리 변경 적용
            this.selected1 = event.target.value;
        },

        write(){
            const vm = this;
            const formdata = new FormData();

            if(this.title.replace(/\s/g,'').length != 0 && this.content.replace(/\s/g,'').length != 0 && this.fileinfo ){
                const url ="/api/share/write"
                const url2 ="/api/share/write/test"

                const data={
                    title : this.title,
                    content: this.content,
                    latitude: vm.$refs.CallMap.latitude,
                    longtitude : vm.$refs.CallMap.longitude,
                    category : this.selected1,
                    username : this.$store.state.Username.username,
                    user_id : this.$store.state.Userid.userid,
                }

                Axios.post(url, data).then(res => {
                    this.key = res.data;

                    formdata.append('files', vm.fileinfo)
                    formdata.append('key', vm.key)

                    Axios.post(url2, formdata, {
                        headers:{
                            'Content-Type' : 'multipart/form-data'
                        }
                    }).then(res => {
                        console.log(res);
                        
                        vm.$router.push({
                            path:'/map'
                        });
                    })
                }).catch(e => {
                    console.log(e)
                })
            } else {
                alert("Title or Content or File is null");
            }
        },

        upload(e){
            let imageFile = e.target.files;
            this.fileinfo = imageFile[0];
            this.imageTempUrl = URL.createObjectURL(imageFile[0]);
        },

        update(){
            const vm = this;
            const url = '/api/share/update';
            const url2 ="/api/share/write/test";

            const data = {
                id : vm.$route.params.func,
                title: vm.title,
                content : vm.content,
                latitude : vm.$refs.CallMap.latitude,
                longtitude : vm.$refs.CallMap.longitude,
                category : vm.selected1,
            } 

            const formdata = new FormData();
            formdata.append('files', vm.fileinfo);
            formdata.append('key', vm.$route.params.func);

            Axios.post(url, data).then(res => {
                console.log(res);
                
                Axios.post(url2, formdata, {
                    headers:{
                        'Content-Type' : 'multipart/form-data'
                    }
                }).then(res => {
                    console.log(res);

                    vm.$router.push({
                        path:'/map'
                    });
                }).catch(e => {
                    console.log(e);
                })
            }).catch(e => {
                console.log(e);
                alert("Fail")
            })
        },
        Delete(){

            const vm = this;

            Axios.get('/api/share/delete',{
                params:{
                    id:vm.$route.params.func
                }
            }).then(function(response){
                console.log(response.data)
                if(response.data === true){
                        alert(vm.id+"삭제 성공")
                    }else{
                        alert(vm.id+"삭제 실패")
                    }
                    vm.$router.push({
                        path:"/user/article"
                    })
            })
            .catch(function(error) {
                    console.log(error);
            })
        },

    }
}
</script>


<style scoped>

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

    #null{
        height: 150px;
    }

    .none{
        width: 10px;
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
    .category{
        width: 100%;
        margin-bottom: 20px;
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

    .Off{
        display: none;
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