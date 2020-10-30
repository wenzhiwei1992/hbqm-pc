<template>
    <div id="login" class="login">
        <!-- <div class="header"></div> -->
        <el-form :model="loginForm" :rules="rules" ref="loginForm" >
          <div class="form-box">
            <div class="input-box">
                <div class="user">
                    <div class="label">用户名:</div>
                    <el-form-item prop="username" >
                        <input type="text" name="用户" v-model="loginForm.username">
                    </el-form-item>
                </div>
                <div class="password">
                    <div class="label">密码:</div>
                    <el-form-item prop="password" >
                        <input type="password" name="密码"  v-model="loginForm.password">
                    </el-form-item>
                </div>
            </div>
            <div class="btn-box" @click="submitForm('loginForm')">
                <i class="el-icon-right"></i>
            </div>
          </div>
        </el-form>
    </div>
</template>

<script>
    import {
        mapActions
    } from 'vuex'

    import clientModel from '../../model/client-model'
    export default {
        metaInfo: {
            title: 'Login Page'
        },
        data () {
            return {
                loginForm: {
                    username: '',
                    password: '',
                    tenantId:'',
                    selectedOptions:[]
                },
                options: [
                ],

                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                    ]
                },
            }
        },
        methods: {
            ...mapActions(['login']),
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        this.loginForm.tenantId = this.loginForm.selectedOptions[0]||this.loginForm.tenantId;

                        this.login(this.loginForm)
                            .then(() => {
                                this.$router.replace('/index')
                                // this.$router.replace('/menu')
                            }).catch((data)=>{
                            console.log('lodin.data',data);//TO DELETE
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        },
        mounted(){
             //加载菜单
            clientModel.ax.get('/user/login/orgs')
                .then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: 'data.msg',
                            type: 'warning'
                        })
                        return
                    }

                    this.loginForm.tenantId = data.entity[0].value

                    this.options=data.entity;
                })
            // enter键 登录
            document.onkeyup = event => {
              let e = event || window.event || arguments.callee.caller.arguments[0];
              if(e && e.keyCode==13){ // enter 键
                  document.querySelector('.btn-box').click()
              }
            }
        }
    }
</script>
<style scoped lang="scss">
    .login{
      width: 100%;
      height:50vh;
      position: absolute;
      bottom:0;
      background:#fff;
      display: flex;
      justify-content: center;
      .header{
        position: fixed;
        width: 100%;
        height: 60px;
        background: #0084f0;
        top:0;
      }
      .form-box{
        display: flex;
        justify-content:center;
        align-items:center;
        margin-top:10%;
        .input-box{
          .label{
            margin-bottom:5px;
            font-weight:500;
            font-size:13px;
          }
          input{
            width: 300px;
            height: 30px;
            box-sizing: border-box;
            padding:5px;
            border:none;
            min-height: 30px;
            border-bottom: 1px solid #555555;
            color: #212121;
            outline:none;
            &:-webkit-autofill{
              box-shadow: 0 0 0 1000px white inset; // 背景设为白色
              -webkit-text-fill-color: #000; // 字体颜色
            }
            &:focus{
              border-bottom:2px solid #0084f0;
            }
          }
        }
        .btn-box{
          width: 50px;
          height: 50px;
          margin-left:80px;
          background:#4682b4;
          border-radius:50%;
          color:#fff;
          font-size:25px;
          text-align: center;
          line-height: 50px;
          transition: all 500ms;
          &:hover{
            transform:rotate(360deg);
          }
        }
        
      }
    }
    
</style>
