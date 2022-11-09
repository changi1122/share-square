<template>
    <div>
        <ul class="top-bar">
            <li @click="Main" class="menu-item">Home</li>
            <li @click="Share" class="menu-item">Share</li>
            <li @click="Community" class="menu-item">Community</li>
            <li @click="Login" class="menu-item menu-box">Login</li>
            <li @click="Singup" class="menu-item menu-box">Sign up</li>
        </ul>
    </div>
</template>

<script>
export default{
    methods:{
        Main(){
            this.$router.push({
                path:'/'
            })
        },
        Share(){
            this.$router.push({
                path:'/map'
            })
        },
        Community(){
            this.$router.push({
                path:'/community'
            })
        },
        Login(){
            this.$router.push({
                path:'/login'
            })
        },
        Singup(){
            this.$router.push({
                path:'/signup'
            })
        },
    }
    
};
</script>


<style scoped>

ul {
    margin: 0;
    padding: 0px;
    list-style-type: none;
}

.top-bar {
    width: 100%;
    height: 50px;
    display: flex;
    flex-direction: row;
    justify-content: end;
    align-items: center;
    background-color: white;
    padding: 10px 20px;

    box-sizing: border-box;

    position:fixed;
    top: 0px;

    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.10);

    z-index: 3;
}
.menu-item {
    font-size: 15px;
    margin: 0 20px;
    cursor: pointer;
}

.menu-item:hover {
    color:darkseagreen;
}


.menu-box{
    padding: 4px 20px;

    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;

    color: white;
    background-color: black;
    border-radius: 20px;
}
</style>