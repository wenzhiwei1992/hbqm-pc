<template>
    <div>
        <el-container class="bgc">
            <el-header><h1 style="text-align:center;">请选择要前往的页面</h1></el-header>
            <el-main class="box-main">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>智能工厂信息化管理系统</span>
                        <el-button style="float: right; padding: 3px 0" type="text" @click="toRouter(1)">点击前往</el-button>
                    </div>
                </el-card>
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>权限管理系统</span>
                        <el-button style="float: right; padding: 3px 0" type="text" @click="toRouter(2)">点击前往</el-button>
                    </div>
                </el-card>
            </el-main>
        </el-container>
    </div>
</template>

<script>

export default {
    data() {
        return {
            
        }
    },
    mounted() {
    },
    methods: {
        toRouter(i){
            let path
            if(i===1){
                path = '/home'
            }else if(i===2){
                path = '/home'
            }
            this.$router.push(path)
        }

    },

}
</script>

<style lang="scss" scoped>
    .box-header{
        background:#3E8EF7;
        border-bottom:1px solid #EBEEF5;
        box-shadow: 0 0 12px 0 rgba(0,0,0,.1);
    }
    .box-main{
        margin-top:20px;
        display: flex;
        align-items: flex-start;
        justify-content: center;
    }
    .bgc{
        width: 100%;
        // background-color:#f9f9f9;
    }
    .box-card {
        width: 45%;
        margin-right:5px;
    }
</style>