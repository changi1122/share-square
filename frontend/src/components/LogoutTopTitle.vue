<template>
    <div>
        <ul class="top-bar">

            <template v-if="this.$store.state.IsAdmin.is_admin == true">
                <li @click="Admin" class="menu-item">Admin</li>
            </template>

            <li @click="Main" class="menu-item">Home</li>
            <li @click="Share" class="menu-item">Share</li>
            <li @click="Community" class="menu-item">Community</li>

            <template v-if="this.$store.state.Islogin.is_login">
                <img @click="UserInfo" class="user-img" :src="'/api/user/' + this.$store.state.Username.username + '/profileImage'" alt="" />
                <img @click="Chat" class="chat-img" src="../assets/message-in-a-bottle.png" alt=""/>
            </template>

            <template v-else>
                <li @click="Login" class="menu-item menu-box menu-button">Login</li>
                <li @click="Singup" class="menu-item menu-box menu-button mobile-none">Sign up</li>
            </template>
        </ul>

        <div class="dropdown-content">
            <div class="showout">
                <img v-if="this.$store.state.Islogin.is_login" class="SO-img" :src="'/api/user/' + this.$store.state.Username.username + '/profileImage'">
                <p @click="Userpage" class="Userpage">MyPage</p>
                <p @click="MyArticlePage" class="MyArticlePage">Active</p>
                <p @click="Logout" class="Logout">Logout</p>
            </div>
        </div>

    </div>
</template>

<script>
import Axios from 'axios';

export default{
    name:'ComuViewPage',
    methods:{
        Main(){
            this.$router.push({
                path:'/'
            })
        },
        Share(){
            this.$router.push({
                path:'/map'
            })
        },
        Community(){
            this.$router.push({
                path:'/community'
            })
        },
        Login(){
            this.$router.push({
                path:'/login'
            })
        },
        Singup(){
            this.$router.push({
                path:'/signup'
            })
        },
        Chat(){
            this.$router.push({
                path:'/chat'
            })
        },
        Admin(){
            this.$router.push({
                path:'/CBUN/javaIsland/threeDork'
            })
        },
        Logout(){
            const box = document.querySelector('.dropdown-content');
            var vm = this;
            vm.$store.commit('Username/setUsername', "NO user");
            vm.$store.commit('Email/setEmail', "NO email");
            vm.$store.commit('Islogin/setIsLogin', 0);
            vm.$store.commit('Userid/setuserid', -1);
            if(vm.$store.state.IsAdmin.is_admin == true){
                vm.$store.commit('IsAdmin/setIsadmin', false);
            }
            
            box.classList.remove('act');

            Axios.get('/api/logout').then(function (response) {
                console.log(response);
                vm.Main();
            }).catch(function (error) {
                console.log(error);
            });
        },
        MyArticlePage(){
            this.$router.push({
                path:'/user/article'
            })
        },
        Userpage(){
            this.$router.push({
                path:'/user/page'
            })
        },
        UserInfo(){
            console.log("help");
            const box = document.querySelector('.dropdown-content');

            if(this.$store.state.Islogin.is_login){
                box.classList.toggle('act');
            }
        },
    },
    mounted(){
        console.log("dsdfsf ", this.$store.state.Islogin.is_login);
        // div 클릭 시 act 클래스 토글
    }
    
};
</script>


<style scoped>

ul {
    margin: 0;
    padding: 0px;
    list-style-type: none;
}

.top-bar {
    width: 100%;
    height: 50px;
    display: flex;
    flex-direction: row;
    justify-content: end;
    align-items: center;
    background-color: white;
    padding: 10px 20px;

    box-sizing: border-box;

    position:fixed;
    top: 0px;

    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.10);

    z-index: 100;
}
.menu-item {
    font-size: 15px;
    margin: 0 20px;
    cursor: pointer;
}

.menu-item:hover {
    color:darkseagreen;
}


.menu-box {
    padding: 4px 20px;

    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;

    color: white;
    background-color: black;
    border-radius: 20px;
}

.menu-button {
    margin: 0 10px;
}



.top-bar > img{
    border-radius: 30px;
    border : 0.5px solid rgb(163, 163, 163);
    width: 35px;
    height: 35px;
    cursor: pointer;
}

.showout{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 30px;
}
.showout > p{
    cursor: pointer;
}

.showout>p:hover{
    color:darkseagreen;
}

.user-img{
    margin-right: 30px;
    margin-left : 20px;
}

.chat-img{
    margin-right: 120px;
}

.SO-img{
    width: 80px;
    height: 80px;
    border-radius: 70px;
    margin-bottom: 35px;
}

.dropdown-content p{
    margin: 0px 0px;
    display: block;
    color: rgb(94, 94, 94);
    font-size: 15px;
    font-weight: 900;
    padding-bottom: 25px;

}

.dropdown-content {
    visibility: hidden;
    width: 200px;
    height: 300px;
    opacity: 0;
    z-index: 100;
    right: 60px;
    top : 60px;
    position:fixed;
    border-radius: 20px;
    /*background: linear-gradient(180deg, rgba(238,174,202,1) 0%, rgba(148,187,233,1) 100%); */
    background-color: rgb(239, 239, 239);
    transition: all 0.3s;
}
.act {
    visibility: visible;
    opacity: 1;
}

@media only screen and (max-width:738px) {

    .mobile-none {
        display: none;
    }
    .menu-item {
        margin: 0 12px;
    }
    .chat-img {
        margin-right: 0;
    }

}
</style>