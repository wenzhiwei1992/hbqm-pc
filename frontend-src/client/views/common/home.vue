<template>
  <div>
    <home-ct v-if="baseName==='ct'"/>
    <home v-else/>
  </div>
</template>

<script>
import Home from './home/index.vue'
import HomeCt from './home-ct/index.vue'
export default {
  components:{
      Home,
      HomeCt
  },
  data() {
    return {}
  },
  computed:{
    baseName:{
      get () { return this.$store.state.baseName },
      set (val) { this.$store.commit('updateBaseName', val) }
    }
  },
  mounted() {},
  methods: {
    
  }
}
</script>

// <style lang="scss" scoped>

// </style>

