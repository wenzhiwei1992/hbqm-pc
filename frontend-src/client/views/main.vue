<template>
  <div
      class="site-wrapper"
      :class="{ 'site-sidebar--fold': sidebarFold }"
  >
    <template>
      <main-navbar/>
      <main-sidebar v-if="menu.menuAuthorized"/>
      <div class="site-content__wrapper" v-if="menu.menuAuthorized">
        <main-content/>
      </div>
    </template>
  </div>
</template>

<script>
import MainNavbar from './main-navbar.vue'
import MainSidebar from './main-sidebar.vue'
import MainContent from './main-content.vue'

export default {
  data() {
    return {}
  },
  components: {
    MainNavbar,
    MainSidebar,
    MainContent
  },
  computed: {
    documentClientHeight: {
      get() {
        return this.$store.state.documentClientHeight
      },
      set(val) {
        this.$store.commit('updateDocumentClientHeight', val)
      }
    },
    sidebarFold: {
      get() {
        return this.$store.state.sidebarFold
      }
    },
    menu() {
      return this.$store.state.menu.menu
    },
  },
  created() {
    this.loadConfigLocalStorage()
  },
  mounted() {
    this.resetDocumentClientHeight()
  },
  methods: {
    addEventOnResize(callback) {
      var originFn = window.onresize
      window.onresize = () => {
        originFn && originFn()
        callback()
      }
    },
    // 重置窗口可视高度
    resetDocumentClientHeight() {
      this.documentClientHeight = document.documentElement['clientHeight']
      this.addEventOnResize(() => {
        this.documentClientHeight = document.documentElement['clientHeight']
      })
      // window.onresize = () => {
      //     this.documentClientHeight = document.documentElement['clientHeight']
      // }
    },
    loadConfigLocalStorage() {
      if (localStorage.navbarLayoutType) {
        this.$store.commit('updateNavbarLayoutType', localStorage.navbarLayoutType)
      }
      if (localStorage.sidebarLayoutSkin) {
        this.$store.commit('updateSidebarLayoutSkin', localStorage.sidebarLayoutSkin)
      }
    }

  }
}
</script>
