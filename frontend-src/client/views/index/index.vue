
<template>
    <el-container style="height: 100%;">
        <el-header style="height:auto">
            <top-header></top-header>
        </el-header>
        <el-container>
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                <aside-menu></aside-menu>
            </el-aside>
            <el-main>
                <content-tab></content-tab>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
    export default{
        data(){
            return {
            }
        },

    }
</script>