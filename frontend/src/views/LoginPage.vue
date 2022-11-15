<template>
    <div class="lgin">
        <LogoutTopTitle/>

        <div class="login">
            <p class ="login-title"> 로그인 </p>

            <div>
                <p id="currentusername"></p>
                <p id="currentuserrole"></p>
            </div>

            <div class ="login-form">
                    <input id="login-form-id" v-model="user_id" placeholder="아이디"/>
                    <input type="password" id="login-form-pw" v-model="user_pw" placeholder="비밀번호"/>
                    <div id="keyShow">SHOW</div>
            </div>

            <button @click="login" id="checkKey"> yes</button>

            <button id="checkKey2">currentuser</button>

            <button @click="logout" id="logout">logout</button>

            <div class = "login-addtional">
                <li @click="find" class = "login-find">아이디/비빌번호 찾기</li>
                <li @click="signup" class = "login-signup">회원가입하기</li>
            </div>
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

        mounted(){
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


            window.addEventListener('load', currentUser);
            document.getElementById("checkKey2").addEventListener('click', currentUser);

            function currentUser()
            {
                Axios.get('/api/currentuser').then(function (response) {
                    document.getElementById('currentusername').innerText = response.data.username;
                    document.getElementById('currentuserrole').innerText = response.data.Authorities;
                }).catch(function (error) {
                    console.log(error);
                });
            }

        },
        methods:{
            logout(){
                var vm = this;
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
            signup(){
                this.$router.push({
                    path:'/signup'
                })
            },
            find(){
                this.$router.push({
                    path:'/find'
                })
            },
            login(){

                var vm = this;
                var url ='/api/login';
                var data={
                    username: document.getElementById("login-form-id").value,
                    password: document.getElementById("login-form-pw").value
                }

                Axios.post(url,data)
                .then(function (response) {
                    console.log(response);
                    console.log(response.data.result)
                    
                    
                    var result = response.data.result
                    if(result == "로그인에 성공하였습니다."){
                        Axios.get('api/get/user')
                        .then(function(response){
                            console.log(response)

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

.lgin{
    overflow: hidden;
}
.login {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top : 150px;
}

.login-title{
    margin: 40px 0px;
    font-family:'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 64px;
    line-height: 77px;
    letter-spacing: 0.05em;

    color: black;
}

.login-form{
    
    display: flex;
    flex-direction: column;
}

input{
    padding: 10px 40px;
    margin-bottom: 20px;
    background: #DDDDDD;
    border-radius: 50px;
}

#login-form-id, #login-form-pw{
    
    font-family: 'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 20px;
    line-height: 36px;
    letter-spacing: 0.05em;

    color: #878787;

}

.login-addtional{
    
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    list-style-type: none;
    cursor: pointer;
}

.login-addtional li:hover{
    color:darkseagreen;
}

.login-addtional li{

    width: fit-content;
    margin-top: 20px;
    color: #878787;
}


#login-find, #login-singup{
    font-family: 'Inter';
    font-style: normal;
    font-weight: 400;
    font-size: 25px;
    line-height: 30px;

    color: #878787;

}


#keyShow{
    position: absolute;
    display: none;
    margin-left: 278px;
    margin-top: 98px;
    font-size: 9px;
    cursor: pointer;
    color: grey;
}

#checkKey {

    font-size: 25px;
    border-radius: 20px;
    margin-top: 20px;
    margin-bottom: 10px;

    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    padding: 5px 25px;

}

#checkKey:hover{
    color: white;
    background-color: #5EDB97;
}


</style>


