<template>
  <div>
    <el-form :model="newsData" :rules="newsRules" ref="newsMessage">
        <el-form-item label="接收人类型" prop="receiverType">
            <el-select v-model="newsData.receiverType" placeholder="请选择">
              <el-option
                v-for="item in recipientTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="接收人" prop="receiverId">
            <el-select style="width:70%" v-model="newsData.receiverId" :disabled="newsData.recipientType===''" multiple placeholder="请选择">
              <el-option
                v-for="item in receiverNameList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
        </el-form-item>
        
        <el-form-item label="消息模板" prop="messageContent">
            <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入内容"
              v-model="newsData.messageContent">
            </el-input>
        </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="editFormSubmmit('newsMessage')">确 定</el-button>
    </div>
  </div>
</template>

<script>
import newsPush from '@/model/system/newsPush.js'
import {Notification} from 'element-ui'
let id = 0;
export default {
    props:['news','mode'],
    data(){
      return{
        recipientTypeList:[
          {
            value:0,
            label:'用户'
          },
          // {
          //   value:1,
          //   label:'部门'
          // },
          // {
          //   value:2,
          //   label:'角色'
          // },
        ],
        receiverNameList:[],
        newsData:{
          id: "",
          messageContent: "",
          receiverType: 0,
          receiverTypeName: "",
          receiverId: "",
          receiverName: "",
          status: false,
          typeCode: "",
          recipientType: 0,
        },
        newsRules: {
            receiverType: [
                {required: true, message: '请选择接收人类型', trigger: 'blur'},
            ],
            receiverId: [
                {required: true, message: '请选择接收人', trigger: 'blur'},
            ],
            messageContent: [
                {required: true, message: '请填写消息模板', trigger: 'blur'},
            ]
        },
      }
    },
    mounted(){
      let news = this.Clone(this.news)
      this.getReceiverNameList()
      if(news){
        news.receiverId = news.receiverId.split(',')
        news.receiverName = news.receiverName.split(',')
        this.newsData = news
        log(this.newsData)
      }
    },
    methods:{
      getReceiverNameList(){
        newsPush.listByOrg().then(data=>{
          this.receiverNameList.push(...data.entity.map(val=>({
            value:val.id+"",
            label:val.realName
          })))
        }).catch(err => {});
      },
      editFormSubmmit(formName){
        this.$refs[formName].validate((valid) => {
            if (valid) {
                if(this.mode=='edit'){
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    })
                    this.newsData.receiverId = this.newsData.receiverId.join(',')
                    this.newsData.receiverName = this.newsData.receiverName.join(',')
                    newsPush.newsUpdate(this.newsData).then(data=>{
                      loading.close()
                      this.finish()
                    }).catch(err=>{
                      loading.close()
                      this.cancel()
                    })
                }
            } else {
                console.log('error submit!!');
                return false;
            }
        });
      },
      finish(){
        this.$emit('dialog-cancel')
      },
      cancel(){
        this.$emit('dialog-cancel')
      },
    }
}
</script>

<style>

</style>