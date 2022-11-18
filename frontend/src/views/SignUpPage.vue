<template>
    <div class="su">
        <LogoutTopTitle/>

        <div class="login">
            <p class="login-title" v-if="page === 1">회원가입</p>
            <form class="row login-form">
                <ul v-if="page === 1">
                    <li class="id-section">
                        <div class="wrap">
                            <input v-model="username" @keyup="checkId" type="text" id="login-form-id" placeholder="아이디(6~20자)"/>
                            <button type="button" id="check-overlap" @click="checkUsernameDuplicate">중복 확인</button>
                        </div>
                        <span :class="{'red': idErrorColor === 'red', 'green': idErrorColor === 'green', 'login-msg': idError !== '', 'id': true}" id="idError">{{ idError }}</span>
                    </li>
                    <li class="pw-section">
                        <input v-model="password" @keyup="[checkPasswordRule(), checkPasswordMatched()]" type="password" id="login-form-pw" placeholder="비밀번호(영문, 숫자, 특수문자 포함 8~20자)"/>
                        <div id="keyShow">SHOW</div>
                        <span class="login-msg id" id="passwordError">{{ passwordError }}</span>
                    </li>
                    <li class="pw-check-section">
                        <input v-model="rePassword" @keyup="checkPasswordMatched" type="password" id="login-form-pw2" placeholder="비밀번호 재입력"/>
                        <div id="keyShow2">SHOW</div>
                        <span v-if="isPasswordNotMatched" class="login-msg id" id="passwordCheckError">* 일치하지 않는 비밀번호입니다.</span>
                    </li>
                    <li class="email-section">
                        <input v-model="emailUsername" type="email" id="login-form-email" placeholder="이메일 주소"/>
                        <span id="email-center"> @</span>
                        <input v-model="emailHost" type="text" id="login-form-email-backaddress" placeholder="직접입력" :readonly="emailHostSelect !== 'SELF' ? true : false"/>

                        <select v-model="emailHostSelect" @change="selectEmailHost" title="emailHostSelect" name="Email" id="email-address">
                            <option value="gmail.com" class="email-back-address">gmail.com</option>
                            <option value="naver.com" class="email-back-address">naver.com</option>
                            <option value="daum.net" class="email-back-address">daum.net</option>
                            <option value="SELF" class="email-back-address">직접입력</option>
                        </select>
                    </li>
                    <li class="btn-section">
                        <button @click="nextPage" class="commonbutton" type="button">다음</button>
                    </li>
                </ul>
                <ul v-if="page === 2">
                    <p class="login-title" style="text-align: center">프로필 이미지 선택</p>
                    <li class="profileimage-section">
                        <div class="profile-flex">
                            <div @click="() => { profileImage = 'man1'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'man1' }">
                                <img alt="남자 1 프로필 이미지" src="../assets/profiles/man1.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'woman1'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'woman1' }">
                                <img alt="여자 1 프로필 이미지" src="../assets/profiles/woman1.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'man2'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'man2' }">
                                <img alt="남자 2 프로필 이미지" src="../assets/profiles/man2.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'woman2'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'woman2' }">
                                <img alt="여자 2 프로필 이미지" src="../assets/profiles/woman2.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'man3'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'man3' }">
                                <img alt="남자 3 프로필 이미지" src="../assets/profiles/man3.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'woman3'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'woman3' }">
                                <img alt="여자 3 프로필 이미지" src="../assets/profiles/woman3.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'neutral'; }" :class="{'profile-select': true, 'profile-selected': profileImage === 'neutral' }">
                                <img alt="중성 프로필 이미지" src="../assets/profiles/man4.jpg"/>
                            </div>
                            <div @click="() => { profileImage = 'upload'; this.$refs['image'].click(); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'upload' }">
                                <img v-if="profileImageUrl !== undefined" alt="업로드된 이미지" :src="profileImageUrl"/>
                                <img v-else alt="이미지 업로드" src="../assets/profiles/upload.jpg"/>
                            </div>
                            <input style="display: none;" ref="image" @change="uploadImage()" type="file" id="profileImage" name="profileImage" accept="image/*"/>
                        </div>
                    </li>
                    <li class="btn-section" style="display: flex; justify-content: space-between; margin-top: 40px;">
                        <button  @click="previousPage" class="commonbutton" type="button">이전</button>
                        <button @click="submit" class="accentbutton" type="button" id="checkKey">회원 가입</button>
                    </li>
                </ul>
            </form>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from "../components/LogoutTopTitle.vue";
import $ from 'jquery';
import Axios from 'axios';

export default{
        mounted(){
            $("#login-form-pw").on("keyup", function(event){
                if(event.keyCode === 13) {
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                } else {
                    if(this.value){
                        $("#keyShow").css("display", "inline-block");
                    }else{
                        $("#keyShow").hide();
                    }
                }
            })

            $("#keyShow").on("click", function() {
                if($("#login-form-pw").attr("type") == "password") {
                    $("#login-form-pw").attr("type", "text");
                    $($(this)).text("HIDE");
                } else {
                    $("#login-form-pw").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            })

            $("#login-form-pw2").on("keyup", function(event){
                if(event.keyCode === 13) {
                    event.preventDefault();
                    $("#checkKey").triggerHandler("click");
                } else {
                    if(this.value) {
                        $("#keyShow2").css("display", "inline-block");
                    } else{
                        $("#keyShow2").hide();
                    }
                }
            })

            $("#keyShow2").on("click", function() {
                if($("#login-form-pw2").attr("type") == "password") {
                    $("#login-form-pw2").attr("type", "text");
                    $($(this)).text("HIDE");
                } else {
                    $("#login-form-pw2").attr("type","password" );
                    $($(this)).text("SHOW");
                }
            });
        },
        name:"SignUpPage",
        components: {
            LogoutTopTitle
        },
        data() {
            return {
                page: 1,

                username: "",
                password: "",
                rePassword: "",
                emailUsername: "",
                emailHost: "gmail.com",
                emailHostSelect: "gmail.com",
                profileImage: "man1",
                profileImageUrl: undefined,
                profileImageFile: undefined,

                idError: "",
                idErrorColor: "red",
                isUsernameChecked: false,

                passwordError: "",
                isPasswordChecked: false,
                isPasswordNotMatched: false,
            }
        },
        methods:{
            checkId() {
                if (this.username === '') {
                    this.idError = '* 아이디를 입력해주세요.';
                    this.idErrorColor = 'red';
                    this.idError = '';
                } else {
                    this.idError = '* 사용가능한 아이디인지 중복 확인이 필요합니다.';
                    this.idErrorColor = 'red';
                    this.isUsernameChecked = false;
                }
            },
            async checkUsernameDuplicate() {
                const url ='/api/checkforduplicate';
                const data = {
                    username: this.username
                };

                try {
                    const response = await Axios.post(url,data);
                    this.isUsernameChecked = response.data.result;
                    if (this.isUsernameChecked) {
                        this.idError = '* 사용가능한 아이디입니다.';
                        this.idErrorColor = 'green';
                    }
                    else {
                        this.idError = '* 사용할 수 없는 아이디입니다.';
                        this.idErrorColor = 'red';
                    }
                }
                catch (e) {
                    this.isUsernameChecked = false;
                    this.idError = '* 사용할 수 없는 아이디입니다.';
                    this.idErrorColor = 'red';
                }
            },
            checkPasswordRule() {
                this.isPasswordChecked = false;

                const num = this.password.search(/[0-9]/g);
                const eng = this.password.search(/[a-z]/ig);
                const spe = this.password.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

                if(this.password.length < 8 || this.password.length > 20) {
                    this.passwordError = '* 8 ~ 20자리 이내로 입력해주세요.';
                } else if(this.password.search(/\s/) != -1) {
                    this.passwordError = '* 공백없이 입력해주세요.';
                } else if(2 <= (num < 0) + (eng < 0) + (spe < 0)) {
                    this.passwordError = '* 영어, 숫자, 특수문자를 중 두 종류를 조합해 사용해주세요.';
                } else {
                    this.isPasswordChecked = true;
                    this.passwordError = '';
                }
            },
            checkPasswordMatched() {
                if(this.password !== '' || this.rePassword !== ''){
                    if (this.password === this.rePassword){
                        this.isPasswordNotMatched = false;
                    } else {
                        this.isPasswordNotMatched = true;
                    }
                } else {
                    this.isPasswordNotMatched = false;
                }
            },
            selectEmailHost() {
                if (this.emailHostSelect !== 'SELF')
                    this.emailHost = this.emailHostSelect;
                else
                    this.emailHost = '';
            },
            nextPage() {
                if (!this.isUsernameChecked || !this.isPasswordChecked || this.isPasswordNotMatched ||
                    this.emailUsername == '' || this.emailHost === '') {
                    return;
                }

                this.page = 2;
            },
            previousPage() {
                this.page = 1;
            },
            uploadImage() {
                const image = this.$refs['image'].files[0];

                const url = URL.createObjectURL(image);
                this.profileImageUrl = url;
                this.profileImageFile = image;
            },
            async submit() {
                if (!this.isUsernameChecked || !this.isPasswordChecked || this.isPasswordNotMatched ||
                    this.emailUsername == '' || this.emailHost === '') {
                    return;
                }
                
                const url ='/api/register';
                const formData = new FormData();

                formData.append('username', this.username);
                formData.append('password', this.password);
                formData.append('email', this.emailUsername + '@' + this.emailHost);
                formData.append('profileImage', this.profileImage);
                formData.append('image', this.profileImageFile);

                try {
                    const response = await Axios.post(url, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    if (response.status.toString().startsWith('2')) {
                        this.$router.push({
                            path:'/signup/end'
                        });
                    } else {
                        throw new Error('회원 가입 중 오류');
                    }
                }
                catch (e) {
                    console.log(e);
                    alert('회원 가입 중 알 수 없는 오류가 발생하였습니다.');
                }
            }
        }
    };
</script>

<style scoped>
input[type=text], input[type=password] {
    width: 100%;
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

.red {
    color: red !important;
}
.green {
    color: green !important;
}

.su {
    overflow: hidden;
}

ul {
padding-left: 0px;
}

.login {
    margin-top : 80px;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.login-title {

    margin-top: 60px;
    margin-bottom: 10px;

    font-style: normal;
    font-weight: bold;
    font-size: 30px;
    line-height: 77px;
    letter-spacing: 0.05em;

    color: black;
}

.login-form {
    max-width: 480px;
    height: fit-content;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
}

.login-form > ul, .login-form li {
    width: 100%;
    height: fit-content;
}

.login-form input{
    padding: 7px 30px;
    background: #DDDDDD;
    border-radius: 50px;
    font-family: inherit;
}
.login-form button {
    font-family: i;
}

.login-form li {
    width: 100%;
    margin-bottom: 20px;
    position: relative;
}

.login-form li:nth-child(1) > div {
    width: 100%;
    display:  flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.login-form > ul li:nth-child(1) > div,
.login-form > ul li:nth-child(2) > input,
.login-form > ul li:nth-child(3) > input {
    margin-bottom: 3px;
}

.login-form > ul li .login-msg {
    margin-left: 10px;
    font-size: 12px;
    color: red;
}

#login-form-id {
    flex-grow: 1;
    margin-right: 10px;
}

#login-form-id, 
#login-form-pw, 
#login-form-pw2, 
#login-form-email,
#login-form-email-backaddress {
    font-style: normal;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;

    color: #878787;
}


#keyShow,
#keyShow2{
    position: absolute;
    display: none;
    font-size: 9px;
    cursor: pointer;
    color: grey;
    top: 20px;
    right: 20px;
}

#email-center{
    margin: 0 6px;
    font-size: 20px;
    font-weight: 900;
    flex-grow: 0;
}

.login-form > ul li:nth-child(4) { 
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

#email-address:focus {
    outline: none;
}

#login-form-email {
    height: fit-content;
    flex-grow: 1;
    width: 100%;
}

#email-address{
    padding: 7px 30px;
    background: #DDDDDD;
    border-radius: 50px;
    appearance: none;
    
    font-family: inherit;
    font-weight: 900;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;

    margin-left: 6px;
    flex-grow: 1;
    width: 100%;

    border: none;
    color: #878787;
}

.login-form > ul li:nth-child(5) {
    display: flex;
    flex-direction: row;
    justify-content: end;
    margin-top: 20px;
}

#check-overlap {
    font-size: 13px;
    font-family: inherit;
    border-radius: 30px;
    width: 140px;

    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    padding: 15px 18px;
    cursor: pointer;
}

#check-overlap:hover{
    color: white;
    background-color: #5EDB97;
}

.profile-flex {
    display: flex;
    flex-wrap: wrap;
}
.profile-select {
    display: inline-flex;
    width: 25%;
    padding: 10px;
    border-radius: 50%;
    cursor: pointer;
}
.profile-select img {
    width: 100%;
    max-width: 100%;
    border-radius: 50%;
    aspect-ratio: 1 / 1;
}
.profile-selected {
    background-color: #5EDB97;
}

@media only screen and (max-width:738px) {
    .row {
        max-width: 100%;
        padding: 0 15px;
    }
}

</style>
