import App from './App.vue'
import jQuery from 'jquery'
import router from './router'
import { createApp } from 'vue'
import Axios from 'axios'
import  store  from './vuex/store'

import {library} from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(faUserSecret)

const app = createApp(App)


app.use(router)
app.use(store)
app.component('font-awesome-icon', FontAwesomeIcon)

app.config.globalProperties.$axios = Axios
global.$ = jQuery

app.mount('#app')