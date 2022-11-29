<template>
    <div>
        <ul class="top-bar">
            <li>Admin page</li>
            <li @click="Main" class="menu-item">Home</li>
            <li @click="Share" class="menu-item">Share</li>
            <li @click="Community" class="menu-item">Community</li>
        </ul>
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


.top-bar > img{
    border-radius: 30px;
    border : 0.5px solid rgb(163, 163, 163);
    width: 35px;
    height: 35px;
    cursor: pointer;
}

@media only screen and (max-width:738px) {

    .menu-item {
        margin: 0 12px;
    }

}
</style>