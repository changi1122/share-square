import App from './App.vue'
import jQuery from 'jquery'
import router from './router'
import { createApp } from 'vue'
import Axios from 'axios'
import  store  from './vuex/store'
import  VueGoodTablePlugin from 'vue-good-table-next'
import 'vue-good-table-next/dist/vue-good-table-next.css'


const app = createApp(App)


app.use(router)
app.use(store)
app.use(VueGoodTablePlugin)

app.config.globalProperties.$axios = Axios
global.$ = jQuery

app.mount('#app')