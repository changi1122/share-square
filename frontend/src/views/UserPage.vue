<template>
    <div>
        <LogoutTopTitle/>

        <div class="mypage">
            <div class="test"></div>
            <input class="mypage-title" placeholder="MyPage" disabled/>
            <hr class="mypage-title-bar">
            
            <div class="mypage-main">
                <template v-if="!isProfileImageEdit">
                    <div class="mypage-profile">
                        <img class="user-profile-img" :src="'/api/user/' + this.$store.state.Username.username + '/profileImage'" alt="" >
                        <p @click="() => { this.isProfileImageEdit = true; }" class="user-profile-cng">프로필 변경</p>
                    </div>

                    <div class="mypage-userinfo">
                        <div class="user-info-id">
                            <template v-if="!isPasswordEdit">
                                <p class="user-id-title">UserID</p>
                                <p class="user-id">{{this.$store.state.Username.username}}</p>
                                <p @click="() => { this.isPasswordEdit = true; }" class="user-idpw-cng">비밀번호 변경</p>
                            </template>
                            <template v-else>
                                <p class="edit-title">비밀번호 변경</p>
                                <input v-model="PEpassword" class="editInput" type="password" placeholder="기본 비밀번호 입력"/>
                                <input v-model="PENewpassword" @keyup="newPasswordChange" class="editInput" type="password" placeholder="새 비밀번호 입력"/>
                                <span v-if="newPasswordError !== ''" class="login-msg">{{ newPasswordError }}</span>
                                <div class="edit-buttons">
                                    <button @click="() => { this.isPasswordEdit = false; }">취소</button>
                                    <button @click="tryChangePassword" class="accentbutton">변경</button>
                                </div>
                            </template>
                        </div>
                        <div class="user-info-email">
                            <template v-if="!isEmailEdit">
                                <p class="user-email-title">Email Address</p>
                                <p class="user-email"> {{ this.$store.state.Email.email}}</p>
                                <p @click="() => { this.isEmailEdit = true; }" class="user-email-cng">이메일 주소 변경</p>
                            </template>
                            <template v-else>
                                <p class="edit-title">이메일 주소 변경</p>
                                <input v-model="EEEmail" @keyup="emailChange" class="editInput" type="email" placeholder="새 이메일 주소 입력"/>
                                <span v-if="emailError !== ''" class="login-msg">{{ emailError }}</span>
                                <input v-model="EEpassword" class="editInput" type="password" placeholder="비밀번호 입력"/>
                                <div class="edit-buttons">
                                    <button @click="() => { this.isEmailEdit = false; }">취소</button>
                                    <button @click="tryChangeEmail" class="accentbutton">변경</button>
                                </div>
                            </template>
                        </div>
                    </div>

                    <div class="mypage-trust">
                        <img class="trust-img" src="../assets/sprout.png" alt="">
                        <p class="turst-cm">{{ reliability }}m</p>
                    </div>
                </template>
                <template v-else>
                    <div class="profile-image-input">
                        <p class="edit-title">프로필 이미지 변경</p>
                        <ProfileImageSelector :profileImage="profileImage" :profileImageUrl="profileImageUrl" :profileImageFile="profileImageFile"
                        @profileImage="(pi) => profileImage = pi" @profileImageUrl="(piu) => profileImageUrl = piu"
                        @profileImageFile="(pif) => profileImageFile = pif" />
                        <div class="edit-buttons" style="margin-top: 20px">
                            <button @click="() => { this.isProfileImageEdit = false; }">취소</button>
                            <button @click="tryChangeProfileImage" class="accentbutton">변경</button>
                        </div>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
import Axios from 'axios';
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import ProfileImageSelector from "../components/ProfileImageSelector.vue";

export default {
    name:"UserPage",
    components: {
        LogoutTopTitle,
        ProfileImageSelector
    },
    mounted() {
        const vm = this;

        Axios.get(`/api/user/${this.$store.state.Username.username}/reliability`)
        .then((res) => {
            vm.reliability = res.data.result;
        })
        .catch(() => {
            vm.reliability = 0;
        });
    },
    data() {
        return {
            isProfileImageEdit: false,
            profileImage: "man1",
            profileImageUrl: undefined,
            profileImageFile: undefined,

            isPasswordEdit: false,
            PEpassword: "",
            PENewpassword: "",
            isNewPasswordChecked: false,
            newPasswordError: "",

            isEmailEdit: false,
            EEEmail: "",
            EEpassword: "",
            isEmailChecked: false,
            emailError: "",

            reliability: 0,
        }
    },
    methods: {
        newPasswordChange() {
            this.isNewPasswordChecked = false;
            const num = this.PENewpassword.search(/[0-9]/g);
            const eng = this.PENewpassword.search(/[a-z]/ig);
            const spe = this.PENewpassword.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

            if (this.PENewpassword.length < 8 || this.PENewpassword.length > 20) {
                this.newPasswordError = '* 8 ~ 20자리 이내로 입력해주세요.';
                return;
            } else if(this.PENewpassword.search(/\s/) != -1) {
                this.newPasswordError = '* 공백없이 입력해주세요.';
                return;
            } else if(2 <= (num < 0) + (eng < 0) + (spe < 0)) {
                this.newPasswordError = '* 영어, 숫자, 특수문자를 중 두 종류를 조합해 사용해주세요.';
                return;
            }
            
            this.newPasswordError = '';
            this.isNewPasswordChecked = true;
        },
        emailChange() {
            this.isEmailChecked = false;
            const EMAIL_REGEX = /^(([^<>()\],;:\s@]+(\.[^<>()\],;:\s@]+)*)|(.+))@(([^<>()[\],;:\s@]+\.)+[^<>()[\],;:\s@]{2,})$/i;

            if (!this.EEEmail.match(EMAIL_REGEX)) {
                this.emailError = '이메일 형식에 맞춰 입력해주세요.';
                return;
            }

            this.emailError = '';
            this.isEmailChecked = true;
        },
        async tryChangePassword() {
            if (!this.PEpassword || !this.PENewpassword ||
                !this.isNewPasswordChecked) {
                return;
            }
            
            const username = this.$store.state.Username.username;
            const url =`/api/user/${username}`;
            const data = {
                password: this.PEpassword,
                newPassword: this.PENewpassword
            };

            try {
                const response = await Axios.put(url, data);
                if (response.status.toString().startsWith('2')) {
                    alert("비밀번호 변경에 성공하였습니다.");
                    this.isPasswordEdit = false;
                    this.PEpassword = "";
                    this.PENewpassword = "";
                } else {
                    alert('비밀번호가 틀리거나, 알 수 없는 오류가 발생하였습니다.');
                }
            }
            catch (e) {
                alert('비밀번호 변경 중 알 수 없는 오류가 발생하였습니다.');
            }
        },
        async tryChangeEmail() {
            if (!this.EEEmail || !this.EEpassword ||
                !this.isEmailChecked) {
                return;
            }
            
            const username = this.$store.state.Username.username;
            const url =`/api/user/${username}`;
            const data = {
                password: this.EEpassword,
                email: this.EEEmail
            };

            try {
                const response = await Axios.put(url, data);
                if (response.status.toString().startsWith('2')) {
                    alert("이메일 변경에 성공하였습니다.")
                    this.$store.commit('Email/setEmail', this.EEEmail);
                    this.isEmailEdit = false;
                    this.EEpassword = "";
                    this.EEEmail = "";
                } else {
                    alert('비밀번호가 틀리거나, 알 수 없는 오류가 발생하였습니다.');
                }
            }
            catch (e) {
                alert('이메일 변경 중 알 수 없는 오류가 발생하였습니다.');
            }
        },
        async tryChangeProfileImage() {
            if (!this.profileImage) {
                return;
            }
            
            const username = this.$store.state.Username.username;
            const url =`/api/user/${username}/profileImage`;
            const formData = new FormData();

            formData.append('username', this.username);
            formData.append('profileImage', this.profileImage);
            formData.append('image', this.profileImageFile);

            try {
                const response = await Axios.post(url, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                if (response.status.toString().startsWith('2')) {
                    alert("프로필 이미지 변경에 성공하였습니다.")
                    this.isProfileImageEdit = false;
                    this.profileImage = "man1";
                    this.profileImageUrl = undefined;
                    this.profileImageFile = undefined;
                } else {
                    alert('프로필 이미지 변경 중 알 수 없는 오류가 발생하였습니다.');
                }
            }
            catch (e) {
                alert('프로필 이미지 변경 중 알 수 없는 오류가 발생하였습니다.');
            }
        }
    }
};
</script>

<style scoped>
p{
    margin:0px 0px ;
}
.mypage{
    width: 100%;
    height: 100%;

    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
    position: relative;
    overflow: hidden;
    
}

.test{
    margin-top: 50px;
    width: 100%;
    height: 100px;
    background-image: url("../assets/community.png");
    opacity: 0.6;
}



.mypage-title{


    margin-top: -28px;
    
    text-align: center;
    font-weight: bold;
    font-size: 20px;
    font-family: inherit;
    
    line-height: 50px;
    letter-spacing: 0.05em;

    padding: 0px 10px;
    border-radius: 50px;

    border: 1px solid #5EDB97;

    color: black;
    z-index: 2;
    
    background-color: white;
}


.mypage-title-bar{

    width: 100%;
    border: 0.5px solid #5EDB97;
    background-color: #5EDB97;
    margin-top: -25px;
    z-index: 1;

}


.mypage-title::placeholder{
    color: black;
}

.mypage-main{
    margin-top: 150px;
    width: 100%;
    max-width: 1080px;
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    align-items: center;
}

.mypage-profile,
.mypage-trust{
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
}

.user-id-title,
.user-email-title{
    font-size: 20px;
    color: #5EDB97;
}

.user-id,
.user-email{
    font-size: 30px;
    color: #000000;
}


.user-profile-cng,
.user-idpw-cng,
.user-email-cng{
    font-size: 15px;
    font-weight: normal !important;
    color: #898989;
}

.user-info-email > p,
.user-info-id >p{
    margin-bottom: 7px;
    font-weight: bold;
}

.mypage-profile > p,
.user-info-id > p:nth-child(3),
.user-info-email >p:nth-child(3){
    cursor: pointer;
} 

.user-info-id,
.user-info-email{
    margin-bottom: 40px;
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
}


.trust-img,
.user-profile-img{
    margin-bottom: 20px;
    width: 150px;
    height: 150px;
}

.user-profile-img{
    border-radius: 50%;
    border: 1.5px solid #5EDB97;
}

.edit-title {
    margin-bottom: 20px !important;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
}

.edit-buttons {
    text-align: center;
}
.edit-buttons button {
    font-size: 14px;
    font-family: inherit;
    margin: 10px 6px 0;
    border-radius: 30px;
    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    padding: 6px 12px;
    cursor: pointer;
}
.edit-buttons button:hover {
    color: white;
    background-color: #5EDB97;
}
.edit-buttons .accentbutton {
    border: none;
    background-color:#5EDB97;
    color: #ffffff;
}
.edit-buttons .accentbutton:hover {
    background-color: #48a773;
}


.editInput {
    width: 300px;
    padding: 7px 30px;
    background: #DDDDDD;
    border-radius: 50px;
    font-family: inherit;
    font-style: normal;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;
    color: #878787;
    margin-bottom: 12px;
    box-sizing: border-box;
}

.login-msg {
    margin-bottom: 16px;
    margin-left: 10px;
    font-size: 12px;
    color: red;
}

.profile-image-input {
    max-width: 480px;
}

@media only screen and (max-width:738px) {
    .mypage-main {
        margin-top: 60px;
        width: 100%;
        padding: 0 20px;
        box-sizing: border-box;
        flex-direction: column;
    }
    .mypage-main>div {
        margin-bottom: 80px;
    }
}


</style>