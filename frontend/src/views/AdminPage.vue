<template>
    <AdminTop/>
    <div class="null"></div>
    <div style="width:100%; height:100%;">
        <div class="All">
            <div class="side-category">
                <div>
                    <p>User</p>
                    <p>Community</p>
                    <p>Share</p>
                    <p>ExtendInfo</p>
                </div>
            </div>
            
            <div class="for_data">
                <input v-model="title" name="title" type="text" class="input" placeholder="물품 이름 입력"/>
                <AdminList/>
            </div>
        </div>
    </div>

</template>

<script>
import AdminTop from "@/components/AdminTop.vue";
import AdminList from "@/components/AdminList.vue";

export default{
    components:{
        AdminTop,
        AdminList,
    }
}
</script>




<style scoped>
.null{
    height: 80px;
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