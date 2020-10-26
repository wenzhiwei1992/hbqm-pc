<template>
    <div>
        <el-row v-if="user" type="flex" justify="center" align="center">
            <el-col :span="4"><h1>{{fullTitle}}</h1>

            </el-col>
            <el-col :span="18">
                <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="1">处理中心</el-menu-item>
                    <el-submenu index="2">
                        <template slot="title">我的工作台</template>
                        <el-menu-item index="2-1">选项1</el-menu-item>
                        <el-menu-item index="2-2">选项2</el-menu-item>
                        <el-menu-item index="2-3">选项3</el-menu-item>
                        <el-submenu index="2-4">
                            <template slot="title">选项4</template>
                            <el-menu-item index="2-4-1">选项1</el-menu-item>
                            <el-menu-item index="2-4-2">选项2</el-menu-item>
                            <el-menu-item index="2-4-3">选项3</el-menu-item>
                        </el-submenu>
                    </el-submenu>
                    <el-menu-item index="3" disabled>消息中心</el-menu-item>
                    <el-menu-item index="4">订单管理</el-menu-item>
                </el-menu>
            </el-col>
            <el-col :span="2">
                <el-dropdown>
                    <div class="user-info">
                        <span class="username">{{user.username}}</span>
                        <i class="el-icon-caret-bottom"></i>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item><a @click="dialogFormVisible = true">修改密码</a></el-dropdown-item>
                        <el-dropdown-item><a @click="clickLogout">退出</a></el-dropdown-item>
                    </el-dropdown-menu>

                </el-dropdown>
            </el-col>
        </el-row>
        <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
            <el-form :model="form">
                <el-form-item label="原密码" :label-width="formLabelWidth">
                    <el-input v-model="form.oldPassword" type="password"></el-input>
                </el-form-item>
                <el-form-item label="新密码" :label-width="formLabelWidth">
                    <el-input v-model="form.newPassword" type="password"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>

</template>
<script>
    import {
        mapState,
        mapGetters,
        mapMutations,
        mapActions
    } from 'vuex'
    export default {
        mounted(){
            this['header/updateTitle']('持云智能工厂')
        },
        data() {
            return {
                activeIndex:'1',
                dialogFormVisible: false,
                form: {
                    oldPassword:'',
                    newPassword:''
                },
                formLabelWidth: '100px'


            };
        },
        methods:{
            ...mapActions(['logout']),
            ...mapMutations(['header/updateTitle']),
            clickLogout(){
                this.logout().then(()=>{
                    this.$router.replace('/login')
                })
            },
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },

        },
        computed:{
            ...mapState(['header','user']),
            ...mapGetters({
                fullTitle:'header/fullTitle'
            })
        }
    }
</script>
<style scoped>
    .el-col{
         display: flex;
         align-items: center;
    }
    .user-info{
        font-size:18px;
        cursor:pointer;
    }
    .user-info .username{
        margin-right:0px;
    }
    .el-menu--horizontal {
         border-bottom: none;
    }
</style>