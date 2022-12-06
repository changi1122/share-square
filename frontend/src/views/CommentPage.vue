<template>
    <LogoutTopTitle/>
    
    <div class="none"></div>
    <div class="position">
        <div class="first-top">
            <img class="com-img" src="@/assets/chat.png" alt="" @click="ToBack">
            <p>{{this.rownumber}}</p>
        </div>

        <div :class="{'commentItem':true, 'on': item.is_child===true}"  v-for="(item, idx) in info" :key="idx" >
            <div class="is-child">
                <template v-if="item.is_child">
                    <img class="arrow" src="@/assets/right-arrow.png" style="margin-right:10px">
                </template>

                <div style="width: 100%">
                    <div class="align">
                        <div class="com-top">
                            <img :src="'/api/user/' + item.username + '/profileImage'" alt=""/>
                            <p> {{item.username}}</p>
                        </div>
                        <div id="more-p">
                            <template v-if="item.username === this.$store.state.Username.username">
                                <i class="fa-regular fa-trash-can font-icon" @click="Delete(item.id)"></i>

                                <i class="fa-solid fa-rotate-right font-icon" @click="Update(item.id, item.content)"></i>
                            </template>

                            <template v-if="item.is_child ==false">
                                <i class="fa-solid fa-reply font-icon" @click="ChangeP(item.id)"></i>
                            </template>

                        </div>
                    </div>
        
                    <p class="comment">{{item.content}}</p>
                    <p class="date">{{item.created_at}}</p>
                </div>
            </div>
        </div>


    </div>

    <div class="write-comment">
        <div class="input-group">
            <div style="display: flex; flex-direction: column; align-items: center;">
                <span class="username">Name</span>
                <template v-if="ischild">
                    <p>덧글 to {{this.parentInt}}</p>
                </template>
            </div>
            
            <textarea class="form-control" v-model="content" placeholder="Write" @input="mixin_autoResize_resize"></textarea>
            <button class="btn btn-outline-secondary" type="button" @click="submit">Button</button>

            <template v-if="ischild">
                <button class="btn btn-outline-secondary" type="button" @click="cancle">Cancle</button>
            </template>
        </div>
    </div>

    <div class="none2"></div>
</template>

<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import Axios from 'axios';
//https://www.flaticon.com/free-icon/right-arrow_7151968?term=turn-down-right&page=1&position=8&page=1&position=8&related_id=7151968&origin=search

export default{
    components:{
        LogoutTopTitle,
    },
    data(){
        return{
            info:[],
            ischild : false,
            content :"",
            parentInt : -1,
            rownumber:0,
            id:null
        }
    },
    mounted(){

        this.Anytime();
    },
    methods:{
        mixin_autoResize_resize(event) {
            event.target.style.height = "auto";
            event.target.style.height = `${event.target.scrollHeight}px`;
        },
        submit(){
            var vm = this
            const url = "/api/comment/save";

            const data = {
                id : this.id,
                content : this.content,
                is_child : this.ischild,
                community_id : this.$route.params.contentId,
                parent_id : this.parentInt,
                user_id : this.$store.state.Userid.userid,
            }
            this.id=null
            this.content = ""
            
            console.log(vm, url, data)

            Axios.post(url, data).then(res =>{
                console.log(res)
                vm.Anytime()
            })

        },
        ChangeP(idx){
            this.parentInt = idx
            this.ischild = true
        },
        cancle(){
            this.parentInt = -1
            this.ischild = false
        },
        setting(){
            console.log("tt")
            document.querySelector('.commentItem').classList.add('on');
        },
        Delete(id){
            var vm = this
            Axios.get("/api/comment/delete", {
                params:{
                    id : id,
                }
            }).then(res =>{
                console.log(res)
                if(res.data){
                    alert("Fail : " + res.data)
                }else{
                    alert("Sucess")
                    vm.Anytime()
                }
            }).catch(e=>{
                console.log(e)
                alert("Fail")
            })
        },
        Anytime(){
            Axios.get("/api/comment/rownum",{
            params:{
                id : this.$route.params.contentId,
            }
        })
        .then(res=>{
            console.log(res)
            this.rownumber = res.data
        })

            Axios.get("/api/comment/view", {
                params:{
                    id : this.$route.params.contentId
                }
            })
                .then(res=>{
                    console.log(res)
                    this.info = res.data
                })
        },
        Update(id, content){
            this.id = id
            this.content = content
        },
        ToBack(){
            this.info=[]
            this.$router.push({
                name:"ComuViewPage",
                params:{
                    contentId: this.$route.params.contentId
                }
            })
        }
    }
}
</script>





<style scoped>

    .font-icon{
        margin-left: 10px;
        cursor: pointer;
    }

    .font-icon:hover{
        color : #5EDB97;
    }

    .arrow{
    width:30px;
    height: 30px; 
    }

    .is-child{
        width: 100%;
        display: flex;
    }

    p{
        margin: 0px;
        padding: 0px;
    }

    .write-comment{ 
        position: fixed;
        bottom: 0px;
        width: 100%;
        background:#ecebeb;
        padding: 20px 5px;
    }

    .none{
        height: 50px;
    }

    .none2{
        height: 110px;
    }
    .commentItem{
        width: 60%;
        padding: 10px 20px;
        border-top:1px solid #000;
        border-bottom: 1px solid #000;
    }

    .com-img{
        width: 60px;
        cursor: pointer;
    }

    .first-top,
    #more-p,
    .com-top{
        display: flex;
        align-items: center;
    }
    .first-top{
        margin: 20px 0px;
    }

    .com-top :nth-child(1){
        width: 30px;
        height: 30px;
        border-radius: 40px;
        border: 1px solid #5EDB97;
        margin-right: 10px;
    }

    .com-top>p{
        font-size: 20px;
    }

    .position{
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .date{
        font-size: 13px;
    }

    .align{
        display: flex;
        justify-content: space-between;
        flex-direction: row;
        align-items: center;
        margin-bottom: 10px;
    }

    .on{
        background: #ecebeb;
    }

    .write-com{
        border: 1px solid #000;
        width: 80%;
        border-radius: 10px;
        padding: 10px 15px;
        word-wrap: break-word;
    }

    .input-group{
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-content: flex-end;
        align-items: stretch;
    }

    .form-control{
        width: 70%;
        resize: none;
        font-size: 15px;
        margin: 0px 10px;
        padding: 5px 10px;
        border-radius: 10px;
    }

</style>