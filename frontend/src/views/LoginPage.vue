<template>
    <div class="lgin">
        <LogoutTopTitle/>

        <div class="login">
            <p class="login-title">로그인</p>

            <div class="row">
                <div class="login-form">
                        <input id="login-form-id" v-model="user_id" @keyup.enter="login" placeholder="아이디"/>
                        <input type="password" id="login-form-pw" v-model="user_pw" @keyup.enter="login" placeholder="비밀번호"/>
                        <div id="keyShow">SHOW</div>
                </div>

                <button @click="login" id="checkKey">로그인</button>
                <div class="login-addtional">
                    <a @click="find" class="login-find">아이디/비밀번호 찾기</a>
                    <a @click="signup" class="login-signup">회원가입</a>
                </div>
            </div>

            <button v-if="this.$store.state.Username.username !== 'NO user'"
                    @click="logout" id="logout">logout</button>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import $ from 'jquery';
import Axios from 'axios';

export default{
    name:"LoginPage",
        components: {
            LogoutTopTitle
        },

        mounted() {
            $("#login-form-pw").on("keyup", function(event){
                if(event.keyCode === 13){
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                }else{
                    if(this.value){
                        $("#keyShow").css("display", "inline-block");
                    }else{
                        $("#keyShow").hide();
                    }
                }
            }).focus()

            $("#keyShow").on("click", function() {
                if($("#login-form-pw").attr("type") == "password"){
                    $("#login-form-pw").attr("type", "text");
                    $($(this)).text("HIDE");
                }else{
                    $("#login-form-pw").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            })
        },
        methods:{
            logout() {
                const vm = this;
                vm.$store.commit('Username/setUsername', "NO user");
                vm.$store.commit('Email/setEmail', "NO email");
                vm.$store.commit('Islogin/setIsLogin', 0);
                vm.$store.commit('Userid/setuserid', -1);
                
                Axios.get('/api/logout').then(function (response) {
                    console.log(response);
                }).catch(function (error) {
                    console.log(error);
                });
            },
            signup() {
                this.$router.push({
                    path:'/signup'
                })
            },
            find() {
                this.$router.push({
                    path:'/find'
                })
            },
            login() {
                const vm = this;
                const url ='/api/login';
                const data = {
                    username: document.getElementById("login-form-id").value,
                    password: document.getElementById("login-form-pw").value
                }

                Axios.post(url, data)
                .then(function (response) {

                    const result = response.data.result
                    if(result == "로그인에 성공하였습니다."){
                        Axios.get('api/get/user')
                        .then(function(response) {
                            const result = response.data;
                            console.log("resutl ", result);
                            vm.$store.commit('Username/setUsername', result.username);
                            vm.$store.commit('Email/setEmail', result.email);
                            vm.$store.commit('Islogin/setIsLogin', 1);
                            vm.$store.commit('Userid/setuserid', result.id);
                            //IsLogin : 1 status login, 0 status logout
                            vm.$router.push({
                                path:'/'
                            })

                        }).catch(function(error){
                            console.log(error)
                        })
                    }
                    
                }).catch(function (error) {
                    console.log(error);
                    alert("Check your ID and PW");
                });
            }
        }
    };
    
</script>


<style scoped>

.row {
    max-width: 380px;
    margin: 0 auto;
    padding: 0 20px;
    width: 100%;
    box-sizing: border-box;
}

.lgin {
    overflow: hidden;
}
.login {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top : 120px;
}

.login-title {
    margin: 30px 0px;
    font-style: normal;
    font-weight: bold;
    font-size: 48px;
    line-height: 77px;
    letter-spacing: 0.05em;

    color: black;
}

.login-form {
    
    display: flex;
    flex-direction: column;
}

input {
    padding: 10px 40px;
    margin-bottom: 20px;
    background: #DDDDDD;
    border-radius: 50px;
}

#login-form-id, #login-form-pw {
    width: 100%;
    box-sizing: border-box;
    font-family: inherit;
    font-weight: bold;
    font-size: 18px;
    line-height: 28px;
    color: #878787;
}

.login-addtional {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;

    margin-top: 20px;
    padding-bottom: 60px;

    list-style-type: none;
    cursor: pointer;
}

.login-addtional a:hover {
    color:darkseagreen;
}

.login-addtional a {

    width: fit-content;
    margin-top: 20px;
    color: #878787;
}


#login-find, #login-singup{
    font-size: 25px;
    line-height: 30px;

    color: #878787;
}


#keyShow{
    position: absolute;
    display: none;
    margin-left: 285px;
    margin-top: 85px;
    font-size: 9px;
    cursor: pointer;
    color: grey;
}

#checkKey {
    border-radius: 24px;
    width: 100%;
    height: 48px;
    margin: 10px 0;
    padding: 5px 25px;
    box-sizing: border-box;

    font-size: 18px;
    font-family: inherit;
    font-weight: bold;
    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    cursor: pointer;
}

#checkKey:hover{
    color: white;
    background-color: #5EDB97;
}

@media only screen and (max-width:738px) {
    .row {
        padding: 0 15px;
    }
}

</style>


