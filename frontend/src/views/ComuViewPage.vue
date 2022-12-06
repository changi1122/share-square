<template>
    <LogoutTopTitle/>
    <div class="null"> </div>
    <div>
        <div class="arti">
            <p class="arti-title"> {{this.info.title}}</p>
            
            <div class="arti-mid1">
                <div class="under-title-left">
                    <img class="user-img" :src="'/api/user/' + this.info.username + '/profileImage'" alt=""/>
                    
                    <div class="name-time">
                        <p class="arti-username"> {{this.info.username}} </p>
                        <p class="arti-date"> {{this.info.created_at}}</p>
                    </div>
                    
                </div>
        
                <div class="under-title-right1">
                    <img src="../assets/sprout.png" alt=""/>
                    <p class="arti-rel">  {{this.info.reliability}}</p>
                </div>
        
                <div class="under-title-right2">
                    <img src="../assets/carbon-footprint.png" alt=""/>
                    <p class="arti-visi"> {{this.info.visiter}} </p>
                </div>

                <template v-if="this.seen">
                    <div style="position: relative;">
                        <img @click="dot" class="dot" src="../assets/dot-menu-more.png" alt="">
                        <div class="dropdown-content2">
                            <P @click="Delete" class="delete">Delete</P>
                            <P @click="Update" class="update">Update</P>
                            <P @click="MyPage" class="myPage">Mypage</P>
                        </div>
                    </div>
                </template>
            </div>

            <hr class="arti-hr">

    
            <img v-if="image !== ''" class="arti-content-img" v-bind:src="image" alt=""/>

            <div class="arti-mid2">
                <div class="arti-content" v-html="this.info.content"></div>
            </div>
    
            <hr class="arti-hr"/>

            <img class="main" @click="Main" src="../assets/home.png" alt="">

            <div class="arti-move">

                <template v-if="this.previous.id != this.info.id">
                    <div class="arti-move-child" @click="Previos">
                        <img class="previous" src="../assets/go_previous_icon.png" alt=""/>
                        <div class="move-button">
                            <p class="move-title"> {{this.previous.title}} </p>
                            <p class="move-time"> {{this.previous.created_at}}</p>
                        </div>
                    </div>
                </template>
                <template v-else>
                    <div class="none"></div>
                </template>
                
                <div class="chat">
                    <div class="chat-button">
                        <img class="chat-img" src="../assets/chat.png" alt="" @click="tochat">
                        <p class="chat-num" @click="tochat">{{this.rownumber}}</p>
                    </div>
                </div>

                <template v-if="this.next.id != this.info.id">
                    <div class="arti-move-child" @click="Next">
                        <div class="move-button">
                            <p class="move-title"> {{this.next.title}} </p>
                            <p class="move-time"> {{this.next.created_at}}</p>
                        </div>
                        <img class="next" src="../assets/go_next_icon.png" alt=""/>
                    </div>
                </template>

                <template v-else>
                    <div class="none"></div>
                </template>

            </div>
        </div>

    </div>
    <div class="null"> </div>


</template>

<script>
import Axios from 'axios';
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';

export default{
    name:'ComuViewPage',
    data(){
        return{
            id:null,
            info:[],
            next: [],
            previous: [],
            seen: 0,
            image:"",
            rownumber:0,
        }
    }, 
    components: {
            LogoutTopTitle,
    },
    mounted(){
        const index  = this.$route.params.contentId

        this.Now(index);
    },
    methods:{
        tochat(){

            if(this.$store.state.Islogin.is_login == 0){
                alert("Please log in")
                this.$router.push({
                    path:'/login'
                })
            }else{
                this.$router.push({
                    name:"CommentPage",
                    params:{
                        contentId: this.$route.params.contentId
                    }
                })
            }
        },  
        getRow(idx){
            var vm=this
            Axios.get("/api/comment/rownum",{
                params:{
                    id : idx,
                }
            })
            .then(res=>{
                console.log(res)
                vm.rownumber = res.data
            })

        },
        Now(idx){
            var vm = this;
            vm.getRow(idx)
            console.log("i got :" , idx)
            vm.id = idx

            Axios.get('/api/community/view',{
                params:{
                    id:idx,
                }
            })
            .then(function(response){
                    console.log(response.data)
                    vm.info = response.data[0];
                    vm.next = response.data[1];
                    vm.previous = response.data[2];
                    console.log(vm.info)

                    vm.Incl(vm.id);

                    console.log("dd", vm.$store.state.Islogin.is_login);
                    console.log(vm.info.user_id);
                    console.log(vm.$store.state.Userid.userid);
                    if(vm.$store.state.Islogin.is_login && vm.info.user_id == vm.$store.state.Userid.userid){
                        vm.seen=1;
                    }else{
                        vm.seen=0;
                    }
                    
                    if (vm.info.filename) {
                        var url = '/api/community/fileview/'  + vm.info.filename;
                        vm.image = url;
                    } else {
                        vm.image = '';   
                    }
            })
            .catch(function(error) {
                    console.log(error);
            })
        },
        Previos(){
            if(this.seen){
                const box = document.querySelector(".dropdown-content2");
                box.classList.remove("act2");
            }
            var vm = this;
            
            if(vm.previous.id== vm.info.id){
                alert("No more Page");
            }else{
                this.$router.push({
                    name:"ComuViewPage",
                    params:{
                        contentId: vm.previous.id,
                    }
                })
                vm.Now(vm.previous.id);
            }
            
        },
        Next(){
            if(this.seen){
                const box = document.querySelector(".dropdown-content2");
                box.classList.remove("act2");
            }
            var vm = this
            
            if(vm.next.id== vm.info.id){
                alert("No more Page");
            }else{
                this.$router.push({
                    name:"ComuViewPage",
                    params:{
                        contentId: vm.next.id,
                    }
                })
                vm.Now(vm.next.id);
            }
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
        },
        Incl(id){
            var url ='/api/community/inclvisiter';
                var data={
                    id : id,
                }
                console.log("visiter ", data.id);

                Axios.post(url,data).then(res => {
                    console.log(res);
                })
        },
        dot(){
            const box = document.querySelector(".dropdown-content2");

            console.log("dsdfsf ", this.$store.state.Islogin.is_login);
            // div 클릭 시 act 클래스 토글
            if(this.$store.state.Islogin.is_login){
                box.classList.toggle('act2');  
            }
        },
        MyPage(){
            this.$router.push({
                path:'/user/article'
            })
        }
    }
}

</script>

<style scoped>
p{
    margin: 0px 0px;
}

.arti-content-img{
    width: 40%;
}

.myPage,
.delete,
.update{
    font-size:15px;
    font-style: normal;
    font-weight: 600;
    margin: 10px 0px;
    color: rgb(0, 0, 0);
    margin-bottom: 10px;
    margin-top: 0px;
}

.myPage:hover,
.delete:hover,
.update:hover{
    cursor: pointer;
    color:#5EDB97;
}

.dot{
    height: 15px;
}

.dot:hover{
    cursor: pointer;
}

.dropdown-content2 {
    visibility: hidden;
    width: 100px;
    height: 120px;
    opacity: 0;
    z-index: 2;
    top: 30px;
    right: 0;
    position:absolute;
    border-radius: 20px;
    /*background: linear-gradient(180deg, rgba(238,174,202,1) 0%, rgba(148,187,233,1) 100%); */
    background-color: rgb(239, 239, 239);
    transition: all 0.3s;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.act2 {
    visibility: visible;
    opacity: 1;
}

.none{
    width: 225px;
}
.null{
    width: 100%;
    height: 100px;
}

.arti{
    display: flex;
    flex-direction: column;
    align-items: center;
}

.arti-title{
    font-size: 34px;
    font-style: normal;
    font-weight: 400;
    margin: 10px 0px;
    color: black;
    box-sizing: border-box;
}

.name-time{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: center;
}

.under-title-left,
.under-title-right1,
.under-title-right2,
.chat,
.arti-move,
.arti-move-child{
    display: flex;
    /* flex-direction: row; */
    /* justify-content: center; */
    /* align-content: center; */
    /* flex-wrap: nowrap; */
    align-items: center;
}

.arti-move-child{
    transition-property: background-color;
    transition-duration: 1s;
    padding: 10px 20px;
    border-radius: 30px;
}

.arti-move-child:hover{
    cursor: pointer;
    background-color :   #a6fccd;
}

.arti-username{
    font-size: 16px;
    font-style: normal;
    font-weight: 500;
    width: 100%;
    color: black;
}

.arti-date{
    font-size: 10px;
    font-style: normal;
    font-weight: 200;
    
    color: rgb(147, 147, 147);
    margin-bottom: 5px;
}

.under-title-left {
    flex: 1;
}
.under-title-left> img{
    width: 30px;
    height: 30px;
    border: 0.5px solid #5EDB97;
    border-radius: 60px;
    margin-right: 10px;
}

.main,
.previous,
.next,
.under-title-right1>img,
.under-title-right2>img{
    width: 30px;
    height: 30px;
}

.main:hover{
    cursor: pointer;
}

.main{
    margin: 20px 0px;
}

.previous{
    margin-left: 5px;
}
.next{
    margin-right: 5px;
}

.under-title-right2{
    margin-left: 10px;
}

.arti-mid1{
    display: flex;
    align-items: center;
    width: 100%;
    margin-top: 20px;
    max-width: 680px;
    box-sizing: border-box;
}

.arti-mid2{
    width: 100%;
    max-width: 680px;
}

.arti-hr{
    border-top: 9px #898989;
    margin: 10px 0px;
    width: 100%;
    max-width: 800px
}

.arti-rel{
    width: 50px;
}

.arti-visi{
    width: 50px;
}

.arti-content{
    white-space: pre-wrap;
    font-size: 15px;
    letter-spacing: 0px;
    line-height: 1.6;
    column-width:600px;
    box-sizing: border-box;
}

.chat-img{
    width: 40px;
    margin-right: 10px;
}
.chat-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
}

.arti-move{
    margin-top: 10px;
    width: 770px;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.move-title{
    margin-bottom: 5px ;
    width: 150px;
    overflow : hidden;
    text-overflow: ellipsis;
    word-wrap: break-word;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    text-align : left;


    font-size: 15px;
    font-weight: 700;
    color: rgb(0, 0, 0);
}

.move-button{
    display: flex;
    align-items: center;
    flex-direction: column;
}

.move-time{
    width: 150px;
    font-size: 10px;
    font-weight: 300;
    color: rgb(147, 147, 147);
    text-align : right;
}

.mb{
    background-color: white;
    border: 1px solid #5EDB97;
    border-radius: 30px;
    width: 50px;
    padding: 5px 10px;
}

@media only screen and (max-width:738px) {

    .arti-title {
        padding: 0 20px;
    }
    .arti-mid1 {
        padding: 0 20px;
    }

    .arti-content{
        padding: 0 20px;
    }

    .arti-content-img {
        width: 100%;
        padding: 0 20px;
        box-sizing: border-box;
    }
    
    .arti-move {
        flex-direction: column;
        width: 100%;
        padding: 0 25px;
        box-sizing: border-box;
    }
    .arti-move>div {
        width: 100%;
        box-sizing: border-box;
    }
    .move-button {
        width: 100%;
    }
    .previous {
        margin-left: 25px;
    }
    .next {
        margin-right: 25px;
    }
    .chat {
        margin: 20px 0;
    }
    .chat-button {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
    }

}
</style>