<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row style="display:none;">
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                ref="mainContent"
                :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                    :data="newsDatas"
                    size="mini"
                    stripe
                    border
                    ref="handleClickRowSelection"
                    @row-click="handleClickRowSelection"
                    height="100%"
                    style="width: 100%"
                >
                    <el-table-column type="index" label="序号" width="60">
                    </el-table-column>
                    <el-table-column width="100px" label="接收人类型" prop="receiverTypeName">
                    </el-table-column>

                    <el-table-column
                        width="200px"
                        prop="receiverName"
                        label="接收人"
                    >
                    </el-table-column>
                    <el-table-column label="消息内容">
                        <template slot-scope="scope">
                          <el-tooltip placement="top" :content="scope.row.messageContentTemplate">
                          <span class="tooltip">!</span>
                          </el-tooltip>
                          <span>{{scope.row.messageContent}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        width="150px"
                        prop="status"
                        label="是否启用"
                    >
                        <template slot-scope="scope">
                            <el-switch
                                v-model="scope.row.status"
                                @change="changeStatus(scope.row)"
                                active-text="关闭"
                                active-color="#ff4949"
                                :active-value="0"
                                inactive-text="开启"
                                :inactive-value="1"
                                inactive-color="#13ce66"
                            >
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column
                        label="操作"
                        :width="rowButtonGroupWidth(rowButtonData)"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                :row="scope.row"
                                :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog
            :title="mode == 'add' ? '新建' : '编辑'"
            :visible.sync="editFormVisible"
            v-if="editFormVisible"
        >
            <new-message :news="news" :mode="mode" @dialog-finish="dialogFinish" @dialog-cancel="dialogCancel"></new-message>
        </el-dialog>
        <el-dialog
            title="设置时间"
            width="60%"
            :visible.sync="dateFormVisible"
            v-if="dateFormVisible"
        >
            <new-date :news="news" @dialog-finish="dateDialogCancel" @dialog-cancel="dateDialogCancel"></new-date>
        </el-dialog>
    </div>
</template>
<script>
import m2EpWx from '@/model/system/m2EpWx';
import { Notification } from 'element-ui';
import moment from 'moment';
import NewMessage from './NewMessage.vue';
import NewDate from './NewDate.vue';
import newsPush from '@/model/system/newsPush.js';
export default {
    data() {
        return {
            pageInfo: {
                currentPage: 1,
                total: 0,
                pageSize: 10,
                pageSizes: this.GLOBAL.pageSizes
            },
            mode: '',
            newsDatas: [],
            news: [],
            datas: [],
            editFormVisible: false,
            dateFormVisible: false,
            editForm: {},
            eidtFormRules: {
                userId: [
                    { required: true, message: '必填字段', trigger: 'blur' }
                ],
                epWxUserId: [
                    { required: true, message: '必填字段', trigger: 'blur' }
                ],
                type: [{ required: true, message: '必填字段', trigger: 'blur' }]
            },
            buttonData: [
                {
                    name: '新建',
                    event: 'addClick'
                }
            ],
            rowButtonData: [
                {
                    name: '编辑',
                    event: 'editClick'
                },
                {
                    name: '设置时间',
                    event: 'dateEditClick',
                    disabled:'!scope.row.cron'
                },
                {
                    name: '删除',
                    event: 'deleteClick'
                }
            ]
        };
    },
    components: {
        'new-message': NewMessage,
        NewDate
    },
    mounted() {
        this.list();
    },
    methods: {
        changeStatus(row){
            newsPush.newsUpdate(row).then(data=>{
                if (data.status == 'fail') {
                    Notification({
                        message: data.msg,
                        type: 'warning'
                    })
                    return
                }
                Notification({
                    message: '编辑成功',
                    type: 'success'
                })
            })
        },
        addClick() {
            this.mode = 'add';
            this.editFormVisible = true;
        },
        editClick(row) {
            this.mode = 'edit'
            this.news = row
            this.editFormVisible = true;
        },
        dateEditClick(row){
          if(!row.cron){
            Notification({
                message: '不可设置时间',
                type: 'warning'
            })
            return 
          }
          this.news = row
          this.dateFormVisible = true
        },
        deleteClick(row) {
            this.$confirm('确认删除选择项?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    newsPush.newsDelete(row.id).then(data => {
                            if (data.status == 'success') {
                                Notification({
                                    type: 'success',
                                    message: this.GLOBAL.MSG.UPDATE_SUCCESS
                                });
                            }
                            this.list();
                        })
                        .catch(err => {
                            console.log(err);
                        });
                })
                .catch(err => {
                    console.log(err);
                });
        },
        list() {
          newsPush.listAll().then(data => {
            this.newsDatas = data.entity
          })
        },
        dialogFinish() {
          this.editFormVisible = false
          this.list()
        },
        dialogCancel() {
          this.editFormVisible = false
          this.list()
        },
        dateDialogFinish(){
          this.dateFormVisible = false
          this.list()
        },
        dateDialogCancel(){
          this.dateFormVisible = false
          this.list()
        }
    }
};
</script>
<style scoped lang="scss">
    .tooltip{
      padding:0 5.5px;
      background:red;
      color:#fff;
    }
</style>
