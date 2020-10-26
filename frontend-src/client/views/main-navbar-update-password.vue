<template>
  <el-dialog title="修改密码" :visible.sync="visible" :append-to-body="true">
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="80px"
    >
      <el-form-item label="账号">
        <span>{{ userName }}</span>
      </el-form-item>
      <el-form-item label="原密码" prop="password">
        <el-input type="password" v-model="dataForm.password"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="dataForm.newPassword"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="dataForm.confirmPassword"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="modifyPwd('dataForm')">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import userModel from "../model/model.js";
  import {Notification} from 'element-ui'
export default {
  data() {
    var validateConfirmPassword = (rule, value, callback) => {
      if (this.dataForm.newPassword !== value) {
        callback(new Error("确认密码与新密码不一致"));
      } else {
        callback();
      }
    };
    return {
      visible: false,
      dataForm: {
        id: this.$store.state.user.id,
        username: this.$store.state.user.username,
        password: "",
        newPassword: "",
        confirmPassword: ""
      },
      dataRule: {
        password: [
          { required: true, message: "原密码不能为空", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "新密码不能为空", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { validator: validateConfirmPassword, trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    userName: {
      get() {
        return this.$store.state.user.username;
      }
    },
    mainTabs: {
      get() {
        return this.$store.state.mainTabs;
      },
      set(val) {
        this.$store.commit("updateMainTabs", val);
      }
    }
  },
  methods: {
    // 初始化
    init() {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
      });
    },
    // 表单提交
    dataFormSubmit() {},
    modifyPwd(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          userModel.userUpdatePwd(this.dataForm).then(data => {
            if (data.status == "fail") {
              Notification({
                message: data.msg,
                type: "warning"
              });
              return;
            }
            Notification({
              message: "修改成功",
              type: "success"
            });
            this.$router.replace("/login");
          });
        }
      });
    }
  }
};
</script>

