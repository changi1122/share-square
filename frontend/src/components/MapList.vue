<template>
    <div>
        <div class="share-list-menu"  >
            <ul class="first-list"  v-for="(item, idx) in paginatedData" :key="idx" @click="Action(item.id)" >
                <p id="share-list-title">{{item.title}}</p>

                <div class="share-list-top">
                    <div class="share-list-left">
                        <div class="share-list-user">
                            <img src="../assets/sprout.png" alt="" id="user-img">
                            <div class="share-user-info">
                                <p>{{item.username}}</p>
                                <p>{{item.created_at}}</p>
                            </div>
                        </div>
            
                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p> {{item.category}}</p>
                        </div>

                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p>1:1채팅</p>
                        </div>

                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p id="trust-num">140m</p>
                        </div>

                    </div>
                    
                    <div class="share-list-r">
                        <img :src='"/api/share/fileview/" + item.filename' alt="" id="text-img">
                        
                        <div class="share-list-info2">
                            <img src="../assets/sprout.png" alt="">
                            <p> {{this.loaction[idx]}}</p>
                        </div>
                    </div>
                </div>
            </ul>
        </div>

        <div class="paging" v-if="pageCount != 0">
            <button :disabled="pageNum === 0" @click="prevPage" class="page-btn">
                이전
            </button>
            <span class="page-count">{{ pageNum + 1 }} / {{ pageCount }} 페이지</span>
            <button :disabled="pageNum >= pageCount - 1" @click="nextPage" class="page-btn">
                다음
            </button>
        </div>

        <div class="none"></div>
    </div>
</template>


<script>
/*global kakao*/

export default{

    data(){
        return{
            loaction:[],
            gecoder:null,
            date:"",
            pageNum :0,
        }
    },
    props:{
        listArray:{
            type : Array,
            required : true
        },
        pageSize:{
            type:Number,
            required: false,
            default : 5
        }
    },
    computed:{
        pageCount(){
            let listLeng = this.listArray.length,
            listSize = this.pageSize,
            page = Math.floor(listLeng/listSize);

            if(listLeng % listSize > 0) page +=1;

            return page;
        },
        paginatedData(){
            const start = this.pageNum * this.pageSize,
            end = start + this.pageSize;
            console.log(start, end);
            return this.listArray.slice(start, end)
        }
    },
    watch:{
        listArray(newList){
            console.log("ddd",newList);
            console.log("thke:", this.listArray);
            this.loaction=[]
            for(var i=0; i<this.listArray.length; i++){
                this.findplace(this.listArray[i].latitude, this.listArray[i].longtitude,i)
            }
        }
    },
    methods:{
        Action(id){
            this.$emit('showsharelist', id);
        },
        findplace(latitude, longitude,idx){
            var vm = this;
            console.log(latitude, longitude, idx);
            
            var coords = new kakao.maps.LatLng(latitude, longitude);
            var gecoder = new kakao.maps.services.Geocoder();
            

            vm.gecoder = gecoder;
            console.log(vm.gecoder)

            vm.searchDetailAddrFromCoords(coords, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    
                    console.log("ddd " , result[0].address.address_name, idx);
                    vm.loaction[idx] = result[0].address.address_name
                }else{
                    console.log(result)
                    console.log(status)
                }
            });
        },

        searchDetailAddrFromCoords(coords, callback) {

            this.gecoder.coord2Address(coords.getLng(), coords.getLat(), callback);
        },
        nextPage () {
            this.pageNum += 1;
        },
        prevPage () {
            this.pageNum -= 1;
        },
    },
}

</script>



<style scoped>
p{
    margin : 0px 0px;
    padding : 0px 0px;
}

img{
    width: 20px;
    height: 20px;
}

.none{
    height: 30px;
}
.paging{
    display: flex;
    justify-content: center;
}

#user-img{
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: 1px solid #5EDB97;
}

.first-list{
    margin: 20px 20px;
    border-top: 1px solid #5EDB97;
    border-bottom: 1px solid #5EDB97;
    position: relative;
    cursor: pointer;
    padding-left: 0px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.first-list:hover{
    background-color: #5EDB97;
}

.share-list-user{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.share-list-info{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-left: 10px;
}

.share-list-info>p{
    margin-left: 17px;
    list-style: none;
    font-size: 13px;
    font-weight: 900;
}


#trust-num{
    color: #5EDB97;
}

.share-list-info2>p{
    width: 100px;
    font-size: 10px;
    text-align: start;
    font-weight: 900;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-list-top{
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin: 10px 0px;
}

#text-img{
    width: 130px;
    height: 130px;
    background-color: blueviolet;
    border-radius: 10px;
    object-fit: cover;
}

#share-list-title{
    font-weight: 900;
    font-size: 20px;
    text-align: center;
    margin-top: 5px;
    width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
}

.share-list-left{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding-right: 10px;
}

.share-list-r{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
}

.share-user-info{
    display: flex;
    flex-direction: column;
    padding-bottom: 5px;
    padding-left: 5px;
}

.share-user-info > p:nth-child(1){
    width: 70px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-user-info > p:nth-child(2){
    font-size:1px;
}

.share-list-info2{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-top: 4px;
}
</style>


