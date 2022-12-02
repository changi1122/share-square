<template>

    <div class="item" v-for="(item, idx) in list" :key="idx" @click="ToMap">   
        <img :src='"/api/share/fileview/" + item.filename' alt="" class="imageCase">    
        <div class="move"> {{item.category}}도!</div>
        <div class="bottom"> 있고~ </div>
    </div>

</template>


<script>
import Axios from 'axios';

export default{
    data(){
        return{
            list:[],
        }
    },
    mounted(){
        var vm = this

        Axios.get('/api/share/recent')
        .then(response =>{
            vm.list = response.data
            console.log(response.data)
            console.log("list : ", vm.list)
        })
        .catch(e=>{
            console.log(e);
        })
    },
    methods:{
        ToMap(){
            this.$router.push({
                path:'/map'
            })
        }
    }
    
}

</script>

<style scoped>
.item {
    background-color: white;
    display: inline-block;
    position: relative;
    width: 100%;
    height: 240px;
    margin: 0 10px;
    border: 1px solid #6BD69C;
    box-sizing: border-box;
    overflow: hidden;
}

.imageCase{
    width: 100%;
    height: 240px;
    object-fit: cover;
}

.item:hover::after,
.item:hover > .bottom,
.item:hover > .move{
    cursor: pointer;
    display: block;
}

.item::after, .move, .bottom{
    display: none;
}

.item::after{
    content:'';
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.3);
    z-index: 10;
}

.item:hover >.imageCase{
    transform: scale(1.2);
    transition : 1s;
}

.move{
    position: absolute;
    top: 76px;
    left: 30px;
    color: rgb(255, 255, 255);
    z-index: 20;
    font-weight: bold;
    font-size: 24px;
}

.bottom{
    position: absolute;
    top: 114px;
    left: 30px;
    color: rgb(255, 255, 255);
    z-index: 20;
    font-weight: bold;
    font-size: 24px;
}

@media only screen and (max-width:738px) {
    .item {
        width: calc(50% - 12px);
        margin: 0 6px 24px;
    }
}

</style>