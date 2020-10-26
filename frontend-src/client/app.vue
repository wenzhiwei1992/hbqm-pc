<template>
    <transition name="fade">
        <router-view></router-view>
    </transition>
</template>

<script>
    export default {
        mounted(){
            window.log = window.console.log
        }
    }
</script>

