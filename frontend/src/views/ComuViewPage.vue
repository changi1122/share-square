<template>
    <LogoutTopTitle/>
    <div class="null"> </div>
    <div>
        <div class="arti">
            <p class="arti-title"> {{this.info.title}}</p>
            
            <div class="arti-mid1">
                <div class="under-title-left">
                    <img class="user-img" src="../assets/user.png" alt=""/>
                    
                    <div class="name-time">
                        <p class="arti-username"> {{this.username}} </p>
                        <p class="arti-date"> {{this.info.created_at}}</p>
                    </div>
                    
                </div>
        
                <div class="under-title-right1">
                    <img src="../assets/sprout.png" alt=""/>
                    <p class="arti-rel">  {{this.reliability}}</p>
                </div>
        
                <div class="under-title-right2">
                    <img src="../assets/sprout.png" alt=""/>
                    <p class="arti-visi"> {{this.info.visiter}} </p>
                </div>

                <template v-if="this.seen">
                    <img @click="dot" class="dot" src="../assets/dot-menu-more.png" alt="">
                </template>
            </div>

            <hr class="arti-hr">

    
            <img class="arti-content-img" src="../assets/wallimage.jpg" alt=""/>

            <div class = "dropdown-content2">
                <P @click="Delete" class="delete">Delete</P>
                <P @click="Update" class="update">Update</P>
                <P @click="MyPage" class="myPage">Mypage</P>
            </div>

            <div class="arti-mid2">
                <pre class="arti-content" >{{this.info.content}} </pre>
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
                    <img class="chat-img" src="../assets/chat.png" alt="">
                    <p class="chat-num"> 23 </p>
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
            username : "testuser",
            reliability : 0,
            seen: 0,
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
        Now(idx){
            var vm = this;
            
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

            })
            .catch(function(error) {
                    console.log(error);
            })
        },
        Previos(){
            const box = document.querySelector('.dropdown-content2');
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
                box.classList.remove("act2");
                vm.Now(vm.previous.id);
            }
            
        },
        Next(){
            const box = document.querySelector('.dropdown-content2');
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
                box.classList.remove("act2");
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
            const box = document.querySelector('.dropdown-content2');

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
    z-index: 1;
    margin-left:715px;
    top : 250px;
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
    font-size: 20px;
    font-style: normal;
    font-weight: 500;
    width: 300px;
    color: black;

}

.arti-date{
    font-size: 10px;
    font-style: normal;
    font-weight: 200;
    
    color: rgb(147, 147, 147);
    margin-bottom: 7px;
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

.under-title-right1{
    margin-left: 200px;
}

.arti-mid1{
    display: flex;
    align-items: center;
}

.arti-mid2{
    width: 680px;
}

.arti-hr{
    border-top: 9px #898989;
    margin: 10px 0px;
    width: 800px
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
}

.chat-img{
    width: 40px;
    margin-right: 10px;
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
</style>