<template>
    <div class="top-bar">
        <div class="row">
            <nav class="nav-area">
                <a @click="() => { this.$router.push({ path:'/' }); }" class="title">나눔 광장</a>
                <a @click="() => { this.$router.push({ path:'/map' }); }" class="menu-item text-menu">물품 공유</a>
                <a @click="() => { this.$router.push({ path:'/community' }); }" class="menu-item text-menu">커뮤니티</a>
    
                <template v-if="this.$store.state.IsAdmin.is_admin == true">
                    <a @click="() => { this.$router.push({ path:'/CBUN/javaIsland/threeDork' }); }" class="menu-item text-menu">어드민</a>
                </template>
            </nav>

            <div class="user-area" v-if="this.$store.state.Islogin.is_login">
                <img @click="showUserDialog" class="user-img" :src="'/api/user/' + this.$store.state.Username.username + '/profileImage'" alt="" />
                <img @click="() => { this.$router.push({ path:'/chat' }); }" class="chat-img" src="../assets/message-in-a-bottle.png" alt=""/>
                <div :class="{ 'dropdown-content': true, 'act': isUserDialogVisible }">
                    <div class="showout">
                        <img v-if="this.$store.state.Islogin.is_login" class="SO-img" :src="'/api/user/' + this.$store.state.Username.username + '/profileImage'">
                        <p @click="() => { this.$router.push({ path:'/user/page' }); }" class="Userpage">마이페이지</p>
                        <p @click="() => { this.$router.push({ path:'/user/article' }); }" class="MyArticlePage">내가 쓴 글</p>
                        <p @click="logout" class="Logout">로그아웃</p>
                    </div>
                </div>
            </div>
    
            <template v-else>
                <a @click="() => { this.$router.push({ path:'/login' }); }" class="menu-item text-menu link-button">로그인</a>
                <a @click="() => { this.$router.push({ path:'/signup' }); }" class="menu-item text-menu link-button mobile-none">회원가입</a>
            </template>
        </div>
    </div>
</template>

<script>
import Axios from 'axios';

export default {
    name:'TopHeader',
    data() {
        return {
            isUserDialogVisible: false,
        }
    },
    methods: {
        logout(){
            const vm = this;
            vm.$store.commit('Username/setUsername', 'NO user');
            vm.$store.commit('Email/setEmail', 'NO email');
            vm.$store.commit('Islogin/setIsLogin', 0);
            vm.$store.commit('Userid/setuserid', -1);
            if(vm.$store.state.IsAdmin.is_admin == true) {
                vm.$store.commit('IsAdmin/setIsadmin', false);
            }
            
            this.isUserDialogVisible = false;

            Axios.get('/api/logout').then(() => {
                vm.Main();
            }).catch(function (error) {
                console.log(error);
            });
        },
        showUserDialog(){
            if(this.$store.state.Islogin.is_login){
                this.isUserDialogVisible = !this.isUserDialogVisible;
            }
        },
    },
};
</script>


<style scoped>

.row {
    margin: 0 auto;
    padding: 0 20px;
    display: flex;
    align-items: center;
    height: 100%;
    box-sizing: border-box;
}

ul {
    margin: 0;
    padding: 0px;
    list-style-type: none;
}

.top-bar {
    width: 100%;
    height: 64px;
    background-color: white;
    padding: 10px 20px;
    box-sizing: border-box;
    position:fixed;
    top: 0px;
    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.10);
    z-index: 100;
}

.nav-area {
    flex: 1;
    display: flex;
    align-items: center;
}
.title {
    font-size: 20px;
    font-weight: bold;
    margin-right: 20px;
    cursor: pointer;
}
.menu-item {
    font-size: 16px;
    margin: 0 6px;
    cursor: pointer;
}

.menu-item:hover {
    color:#5EDB97;
}

.text-menu {
    padding: 4px;
}
.link-button {
    margin: 0 0 0 16px;
    color:#48a773;
}


.user-area {
    position: relative;
}
.user-area > img {
    border-radius: 30px;
    border : 0.5px solid rgb(163, 163, 163);
    width: 40px;
    height: 40px;
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
    color:#5EDB97;
}

.user-img{
    margin-right: 20px;
}

.SO-img{
    width: 80px;
    height: 80px;
    border-radius: 70px;
    margin-bottom: 35px;
}

.dropdown-content p {
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
    right: 0;
    top : 60px;
    position: absolute;
    border-radius: 20px;
    background-color: rgb(239, 239, 239);
    transition: all 0.3s;
}
.act {
    visibility: visible;
    opacity: 1;
}

@media only screen and (max-width:738px) {

    .row {
        padding: 0 0;
    }

    .mobile-none {
        display: none;
    }
    .title {
        margin-right: 12px;
    }
    .menu-item {
        margin: 0 4px;
    }

    .user-area > img {
        width: 36px;
        height: 36px;
    }
    .chat-img {
        margin-right: 0;
    }
    .user-img{
        margin-top: 4px;
    }

}
</style>