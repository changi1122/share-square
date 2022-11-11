<template>
    <div>
        <ul class="top-bar">
            <li @click="Main" class="menu-item">Home</li>
            <li @click="Share" class="menu-item">Share</li>
            <li @click="Community" class="menu-item">Community</li>

            <template v-if="this.$store.state.Islogin.is_login">
                <img @click="UserInfo" class="user-img" src="../assets/user.png" alt="" />
                <img @click="Chat" class="chat-img" src="../assets/logo.png" alt=""/>
            </template>

            <template v-else>
                <li @click="Login" class="menu-item menu-box">Login</li>
                <li @click="Singup" class="menu-item menu-box">Sign up</li>
            </template>
        </ul>

        <div class = "dropdown-content">
            <div class="showout">
                <img class="SO-img" src="../assets/user.png">
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
        Logout(){
            const box = document.querySelector('.dropdown-content');
            var vm = this;
            vm.$store.commit('Username/setUsername', "NO user");
            vm.$store.commit('Email/setEmail', "NO email");
            vm.$store.commit('Islogin/setIsLogin', 0);
            vm.$store.commit('Userid/setuserid', -1);
            
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
        }
    },
    mounted(){
        const btn = document.querySelector('.user-img');
        const box = document.querySelector('.dropdown-content');

        console.log("dsdfsf ", this.$store.state.Islogin.is_login);
        // div 클릭 시 act 클래스 토글
        if(this.$store.state.Islogin.is_login){
                btn.addEventListener('click', () => {
                box.classList.toggle('act');
            })    
        }
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

    z-index: 3;
}
.menu-item {
    font-size: 15px;
    margin: 0 20px;
    cursor: pointer;
}

.menu-item:hover {
    color:darkseagreen;
}


.menu-box{
    padding: 4px 20px;

    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;

    color: white;
    background-color: black;
    border-radius: 20px;
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
    z-index: 4;
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
</style>