<template>
  <nav
    class="site-navbar"
    :class="'site-navbar--' + navbarLayoutType"
    style="overflow:hidden;"
  >
    <div class="site-navbar__header">
      <h1
        class="site-navbar__brand"
        :style="user&&user.org&&user.org.netCompanyLogo?`background-image:url(${user.org.netCompanyLogo})`:``"
        @click="$router.push({ name: 'home' })"
      >
        <a
          class="site-navbar__brand-lg"
          href="javascript:;"
        >{{fullTitle}}</a>
        <a
          class="site-navbar__brand-mini"
          href="javascript:;"
        > </a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">

      <el-menu
        class="site-navbar__menu"
        mode="horizontal"
      >
        <el-menu-item
          v-if="this.$route.name!='select-page'"
          class="site-navbar__switch"
          @click="zheDie()"
        >
          <i class="el-icon-s-fold"></i>
        </el-menu-item>
      </el-menu>
      <el-menu
        class="site-navbar__menu site-navbar__menu--right"
        mode="horizontal"
      >
        <el-menu-item @click="$router.push({ name: 'theme' })">
          <template slot="title">
            <i class="el-icon-setting"></i>
          </template>
        </el-menu-item>
        <el-menu-item>
          <span id="dateInfo">{{date}}</span>
        </el-menu-item>
        <el-menu-item v-if="user">
          <a
            :href="user.org.companyPath"
            target="_blank"
          >{{ user.org.orgName }}</a>
        </el-menu-item>
        <el-menu-item
          class="site-navbar__avatar"
          v-if="user"
        >
          <el-dropdown
            :show-timeout="0"
            placement="bottom"
            size="medium"
          >
            <span class="el-dropdown-link">
              <img
                src="~@/assets/img/avatar.png"
                :alt="user.username"
              >{{ user.username }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password
      v-if="updatePassowrdVisible"
      ref="updatePassowrd"
    ></update-password>
  </nav>
</template>

<script>
import {
  mapState,
  mapGetters,
  mapMutations,
  mapActions
} from 'vuex'
import UpdatePassword from './main-navbar-update-password.vue'

export default {
  data() {
    return {
      updatePassowrdVisible: false,
      date: this.nowTime(),
    }
  },
  components: {
    'update-password': UpdatePassword
  },
  mounted() {
    this['header/updateTitle']('');
    let _this = this; // 声明一个变量指向Vue实例this，保证作用域一致 持云智能工厂
    this.timer = setInterval(() => {
      _this.date = this.nowTime(); // 修改数据date
    }, 1000)
    // this.zheDie();
  },
  computed: {
    navbarLayoutType: {
      get() {
        return this.$store.state.navbarLayoutType
      }
    },
    sidebarFold: {
      get() {
        return this.$store.state.sidebarFold
      },
      set(val) {
        this.$store.commit('updateSidebarFold', val)
        console.log(val)
      }
    },
    mainTabs: {
      get() {
        return this.$store.state.mainTabs
      },
      set(val) {
        this.$store.commit('updateMainTabs', val)
      }
    },
    ...mapState(['header', 'user']),
    ...mapGetters({
      fullTitle: 'header/fullTitle'
    })
  },
  methods: {
    ...mapActions(['logout']),
    ...mapMutations(['header/updateTitle']),
    zheDie(){
      this.sidebarFold = !this.sidebarFold
    },
    // 修改密码
    updatePasswordHandle() {
      this.updatePassowrdVisible = true
      this.$nextTick(() => {
        this.$refs.updatePassowrd.init()
      })
    },
    // 退出
    logoutHandle() {
      this.$confirm(`确定进行[退出]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.logout().then(() => {
          this.$router.push({path:'/login'})
          // location.reload()
        })
      })
    },
    nowTime() {
      //获取年月日
      var time = new Date();
      var year = time.getFullYear();
      var month = time.getMonth() + 1;
      var day = time.getDate();

      //获取时分秒
      var h = time.getHours();
      var m = time.getMinutes();
      var s = time.getSeconds();

      //检查是否小于10
      h = this.check(h);
      m = this.check(m);
      s = this.check(s);
      return year + "年" + month + "月" + day + "日  " + h + ":" + m + ":" + s;
      //document.getElementById("dateInfo").innerHTML = "当前时间：" + year + "年" + month + "月" + day + "日  " + h + ":" + m + ":" + s;
    },
    //时间数字小于10，则在之前加个“0”补位。
    check(i) {
      //方法一，用三元运算符
      var num;
      i < 10 ? num = "0" + i : num = i;
      return num;
    }

  }
}
</script>
