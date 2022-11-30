<template>
    <div class="Admins" style="width:100%; height:100%;">
        <AdminTop/>
        <div class="null"></div>
        <div style="width:100%; height:100%;">
            <div class="All">
                <div class="side-category">
                    <div class="chose-list">
                        <h2>ExtendInfo</h2>
                        <p @click="get(0)" >전체보기</p>
                        <p @click="get(1)" >작성</p>
                        <template v-if="this.$store.state.IsAdmin.is_admin == false">
                            <button @click="setAdmin">Login</button>
                        </template>
                        <template v-else>
                            <button @click="removeAdmin">Logout</button>
                        </template>
                    </div>
                </div>
                
                <div class="for_data">
                    <template v-if="this.show === 0">
                        <AdminList @updateID="toWrite"/>
                    </template>
                    <template v-else>
                        <AdminWrite @isupdate="isUp" @cancle="cancle" ref="UpdateId"/>
                    </template>

                </div>
            </div>
        </div>
    </div>

</template>

<script>
import AdminTop from "@/components/AdminTop.vue";
import AdminWrite from "@/components/AdminWrite.vue"
import AdminList from "@/components/AdminList.vue";

export default{
    components:{
        AdminTop,
        AdminWrite,
        AdminList,
    },
    data(){
        return{
            show:0,
            tochildId:-1,
        }
    },
    methods:{
        get(num){
            this.show = num;
        },
        setAdmin(){
            this.$store.state.IsAdmin.is_admin = true;
        },
        removeAdmin(){
            this.$store.state.IsAdmin.is_admin = false;
        },
        toWrite(params){
            console.log("to child : ", params);
            this.tochildId = params;
            console.log("to child : ", this.tochildId);
            this.show = 1;
        },
        isUp(params){
            console.log(params)
            if(this.tochildId != -1){
                this.$refs.UpdateId.id = this.tochildId;
                this.$refs.UpdateId.is_update = true;
            }
        },
        cancle(params){
            console.log(params)
            this.tochildId = -1
        }
    },
}
</script>




<style scoped>

.chose-list>p{
    cursor: pointer;
}

.chose-list>p:hover{
    color:darkseagreen ;
}
.null{
    height: 80px;
}


.Off{
    display: none;
}
.All{
    display: flex;
    flex-direction: row;
    align-items: flex-start;

    width:100%; 
    height:100%;
}

.for_data{
    display: flex;
    flex-direction: column;
    align-items: stretch;
    width: 80%;
    margin-right: 20px;
}

.input{
        min-width: 782px;
        box-sizing: border-box;
        border: 1px solid #000000;
        margin-bottom: 20px;
        padding: 5px 30px;
        box-sizing: border-box;
        font-size: 14px;
        font-family: inherit;
        border: 1px solid #d1d5db;
    }

.side-category{    
    min-width: 100px;
    width: 15%;
    height: 70%;
    border: 1px solid #d1d5db;
    margin: 0px 25px;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
}
</style>