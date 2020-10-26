<template>
    <div class="page">
        <div class="left">
            <div class="time">{{dateTime}}</div>
            <div class="slogan">更快, 更简单地实现 构件工厂数字化转型</div>
            <div class="logo">SMARTPC</div>
        </div>
        <div class="right">
            <header>
                <div class="header-title">应用中心</div>
                <div class="to-login">
                  <el-button v-if="!isLogin" size="medium" icon="el-icon-user-solid" @click="toLoginPage">登录</el-button>
                  <el-button v-else size="medium" icon="el-icon-switch-button" @click="logout">退出</el-button>
                </div>
            </header>
            <main>
                <ul class="apps-list">
                    <li v-for="(item,index) of menuList" :key="index" @click="toPage(item)">
                        <div class="app" target="_blank">
                            <div class="menu-icon">
                              <img :src="item.icon">
                            </div>
                            <div class="menu-name">{{item.title}}</div>
                        </div>
                    </li>
                </ul>
            </main>
        </div>
    </div>
</template>

<script>
import {Notification} from 'element-ui'
import crm_icon from '@/assets/images/login/crm_icon.png'
import isr_icon from '@/assets/images/login/isr_icon.png'
import menuTabService from '@/service/menuTabService'
import clientModel from '@/model/client-model'
import storage from 'good-storage'

export default {
    data () {
        return {
          dateTime:null,
          isLogin:false,
          menuData:[
            {
              title:'管理系统',
              path:'/home',
              icon:crm_icon
            },
            {
              title:'权限系统',
              path:'/system/orgManage',
              icon:isr_icon
            },
          ],
          menuList:[]
        }
    },
    mounted(){
        setInterval(this.getTime(),1000)
        if(this.$store.state.user&&this.$store.state.user.level){
          this.getMenuListByUser()
        }else{
          this.menuList = this.menuData
        }
        this.isLogin = !!this.$store.state.user
    },
    methods:{
      getMenuListByUser(){
        var menuList = [];
        return clientModel.ax.post('/sys/menu/getMenuListByUser', this.$store.state.user, {showLoading: false}).then((data) => {
            menuList = data.entity.content.filter(option=>{
              return option.title !== 'App权限'
            })
            console.log('menuList----------',menuList)
            this.menuList = menuList
        }).catch(err => {
            console.error('err-------->', err)
        });
      },
      getTime(){
          let date = new Date();
          let strDate = date.toLocaleTimeString().replace(/[年月]/g, '-').replace(/[日]/g, '');
          strDate = strDate.slice(0,-3)
          if(strDate.indexOf('上午')!=-1){
              strDate  += ' AM'
          }else{
              strDate  += ' PM'
          }
          this.dateTime = strDate.replace(/[上下午]/g,'');
      },
      toPage(item){
          menuTabService.setCurrentSystem(this.$store, item.title)
          menuTabService.setCurrentMenus(this.$store, [])
          storage.set('currentSystem',item.title)
          if(this.isLogin){
              this.$router.push({
                  path:item.path
              })
          }else{
              Notification({
                message: '请先登录',
                type: 'warning'
              })
              this.toLoginPage()
          }
      },
      toLoginPage(){
          this.$router.push({
              path:'/login'
          })
      },
      logout() {
        this.$confirm(`确定进行[退出]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout').then(_=>{
              this.isLogin = false
              this.menu = this.menuData
              Notification({
                  message: '已退出登录',
                  type: 'success'
              })
          })
        })
      },
    }
}
</script>

<style scoped lang="scss">
.page{
    height: 100vh;
    overflow: hidden;
    display: flex;
    .left{
        position:relative;
        background: url(../../assets/images/login/background-industrial.jpg) 0% 0% / cover;
        flex: 1;
        height: 100vh;
        .time{
            position: absolute;
            right: 50px;
            top:50px;
            font-size: 64px;
            color: #fff;
        }
        .slogan{
            color: #fff;
            position: absolute;
            font-size: 28px;
            top:30%;
            left: 0;
            right: 0;
            text-align: center;
        }
        .logo{
            color: #fff;
            position: absolute;
            font-size: 64px;
            top:50px;
            left: 50px;
        }
    }
    .right{
        position:relative;
        width: 550px;
        height: 100vh;
        header{
            width: 100%;
            height: 60px;
            background: #0084f0;
            position:absolute;
            right:0;
            top:0;
            .header-title{
                color: #fff;
                font-size:13px;
                font-weight:700;
                line-height: 60px;
                margin-left:60px;
            }
            .to-login{
                position:absolute;
                top:50%;
                transform:translateY(-50%);
                right:30px;
            }
        }
        main{
            background: #fff;
            margin-top:60px;
            .apps-list{
                width: 100%;
                height: calc(100vh - 60px);
                box-sizing: border-box;
                padding:50px 75px;
                display: flex;
                flex-wrap:wrap;
                li{
                    margin:0 16.6px 30px;
                    .app{
                      cursor:pointer;
                      .menu-icon{
                          display: block;
                          height: 100px;
                          width: 100px;
                          background-size:cover;
                          background-position:0% 0%;
                          img{
                            width: 100%;
                            height:100%;
                          }
                      }
                      .menu-name{
                          margin-top: 15px;
                          text-align:center;
                          line-height:1.5;
                          color: #555;
                          font-weight: 500;
                          font-size: 13px;
                          word-wrap: break-word;
                          overflow: hidden;
                      }
                    }
                }
            }
        }
    }
}
</style>
