<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col></el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row>
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="datas"
          @expand-change="showSubData"
          size="mini"
          stripe
          border
          :expand-row-keys="expands"
          row-key="id"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="codeExplainContents"
                size="mini"
                stripe
                border
                style="width: 100%"
              >

                <!--<el-table-column-->
                <!--prop="codeNo"-->
                <!--label="代码大类">-->
                <!--</el-table-column>-->
                <el-table-column
                  prop="dictCodeName"
                  label="代码名称"
                >
                </el-table-column>
                <!--<el-table-column-->
                <!--prop="isEnabled"-->
                <!--:formatter="formatBoolean"-->
                <!--label="启用">-->
                <!--</el-table-column>-->
                <!--<el-table-column-->
                <!--prop="sort"-->
                <!--label="排序">-->
                <!--</el-table-column>-->
                <el-table-column
                  prop="remark"
                  label="代码备注"
                >
                </el-table-column>
                <el-table-column
                  prop="dateCreated"
                  label="创建时间"
                  :formatter="formatterDate"
                >
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            prop="codeName"
            label="所属模块"
          >
          </el-table-column>
          <el-table-column
            prop="codeNo"
            label="系统代码"
          >
          </el-table-column>
          <el-table-column
            prop="orderNoCode"
            label="示例"
          >
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
          >
          </el-table-column>

          <el-table-column
            prop="dateCreated"
            :formatter="formatterDate"
            label="创建时间"
          >
          </el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
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
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageInfo.currentPage"
            :page-sizes="pageInfo.pageSizes"
            :page-size="pageInfo.pageSize"
            :total="pageInfo.total"
            layout="total, sizes, prev, pager, next, jumper"
            background
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>

    <el-dialog
      :title="mode==='add'?'新建':'编辑'"
      :visible.sync="editFormVisible"
      width="80%"
    >
      <el-form
        :model="editForm"
        v-if="editForm"
        :rules="eidtFormRules"
        ref="editForm"
        label-width="120px"
      >

        <el-form-item
          label="所属模块"
          prop="codeName"
        >
          <el-select
            v-model="editForm.codeName"
            placeholder=""
            @change="changeModelSelect"
          >
            <el-option
              v-for="item in codeBelongs"
              :key="item.id"
              :label="item.codeName"
              :value="item.codeName"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <!--<el-form-item label="系统代码名称" prop="codeName" hidden>-->
        <!--<el-input v-model="editForm.codeName"  ></el-input>-->
        <!--</el-form-item>-->
        <el-form-item
          label="字典表id"
          prop="codeId"
          hidden
        >
          <el-input v-model="editForm.codeId"></el-input>
        </el-form-item>
        <el-form-item
          label="系统代码"
          prop="codeNo"
        >
          <el-input
            v-model="editForm.codeNo"
            :disabled="''"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="示例"
          prop="orderNoCode"
        >
          <el-input
            v-model="editForm.orderNoCode"
            :disabled="''"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="备注"
          prop="remark"
        >
          <el-input v-model="editForm.remark"></el-input>
        </el-form-item>

        <!--子项动态添加-->
        <el-form-item>
          <el-table
            :data="editForm.codeContents"
            size="mini"
            stripe
            border
            height="400"
          >
            <el-table-column
              prop="codeName"
              label="代码名称"
            >
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.dictCodeId"
                  placeholder=""
                  @change="changeSelect(scope.row)"
                >
                  <el-option
                    v-for="item in codeTypes"
                    :key="item.id"
                    :label="item.codeName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </template>
            </el-table-column>

            <el-table-column
              prop="remark"
              label="代码备注"
            >
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.remark"
                  :disabled="scope.row.remark.indexOf('yyyy')!=-1"
                ></el-input>
              </template>
            </el-table-column>

            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  size="mini"
                  @click="deletCodeContent(scope)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-col>
            <el-button @click="addCodeContent">新增一行</el-button>
            <el-button @click="checkCode">示例</el-button>
          </el-col>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="editFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="editFormSubmmit('editForm')"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>
<script>
import codeExplain from '../../../../model/basicdata/codeExlpain'
import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
import { Notification } from 'element-ui'
import moment from "moment";
export default {
  data() {
    const check = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('必填项'));
      } else {
        callback();
      }

    };

    return {
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 10,
        pageSizes: [2, 5, 10]
      },
      datas: [],
      selected: [],
      expands: [],
      codeBelongs: [],
      codeTypes: [],
      mode: '',
      editFormTitle: '',
      editFormVisible: false,
      contentsVisible: false,
      editForm: null,
      editContentForm: null,
      eidtFormRules: {
        dictCodeName: [
          { required: true, validator: check, trigger: 'blur' },
        ]
      },
      codeExplainContents: [],
      codeExplainId: '',
      disabled: false,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        }
      ],
      searchButtonData: [

      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '删除',
          event: 'deleteClick',
        },

      ]
    }
  },
  mounted() {
    this.codeBelongsList();
    this.list();
  },
  methods: {
    changeModelSelect() {
      for (let i = 0; i < this.codeBelongs.length; i++) {
        if (this.editForm.codeName == this.codeBelongs[i].codeName) {
          this.editForm.codeNo = this.codeBelongs[i].remark;
          this.editForm.codeId = this.codeBelongs[i].id
          break;
        }
      }
    },
    changeSelect(row) {
      for (let i = 0; i < this.codeTypes.length; i++) {
        if (row.dictCodeId == this.codeTypes[i].id) {
          row.remark = this.codeTypes[i].remark;
          row.dictCodeName = this.codeTypes[i].codeName;
          //  row.dictCodeId = this.codeTypes[i].id;
          break;
        }
      }
    },
    dateFormat(fmt, date) {
      let ret;
      const opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "y+": date.getFullYear().toString(),        // 年
        "M+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "h+": date.getHours().toString(),           // 时
        "m+": date.getMinutes().toString(),         // 分
        "s+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      };
      for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        };
      };
      return fmt;
    },
    checkCode() {
      let contents = this.editForm.codeContents;
      let str = "";
      for (let i = 0; i < contents.length; i++) {
        console.log(contents[i])
        if (contents[i].dictCodeName.indexOf("时间") != -1) {//时间类型字符处理时间
          str += this.dateFormat(contents[i].remark, new Date())
        } else if (contents[i].dictCodeName.indexOf("流水") != -1) {//流水号字符生成流水码
          if (!(/(^[1-9]\d*$)/.test(contents[i].remark))) {
            this.$message.warning("流水码请输入的不是正整数")
            return
            //    return false;
          }
          str += (Array(parseInt(contents[i].remark)).join('0') + 1).slice(-Array(parseInt(contents[i].remark)));
        } else {//普通字符直接拼接
          str += contents[i].remark;
        }
      }
      this.editForm.orderNoCode = str;
    },
    codeBelongsList() {
      codeExplainContent.getAllByCodeNo("ORDER_NO_TYPE").then(data => {
        this.codeBelongs = data.entity;
      }),
        codeExplainContent.getAllByCodeNo("ORDER_NO_TYPE_D").then(data => {
          this.codeTypes = data.entity;
        })
    },
    formatBoolean: function (row, column, cellValue) {
      var ret = ''
      if (cellValue) {
        ret = "是"
      } else {
        ret = "否"
      }
      return ret;
    },
    showSubData(row, expandedRows) {
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }

      codeExplain.getCodeNoDetail(row.id).then(data => {
        this.codeExplainContents = data.entity;
      })
    },
    deletCodeContent(scope) {
      this.editForm.codeContents.splice(scope.$index, 1)
    },
    addCodeContent() {
      // if(this.editForm.codeNo =='' || this.editForm.codeNo ==undefined){
      //     this.$message.warning("请先选择所属模块")
      //     return
      // }
      let codeNo = this.editForm.codeNo;
      let tmp = {
        // codeNo:codeNo,
        codeNo: '',
        codeName: '',
        remark: '',
        isEnabled: true,
        dictCodeName: "",
        sort: 999
      }
      this.editForm.codeContents.push(tmp);
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    editFormSubmmit(formName) {
      this.checkCode();
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.mode == 'edit') {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            codeExplain.updateOrderCode(this.editForm)
              .then((data) => {
                if (data.status == 'failed') {
                  Notification({
                    message: "该单号类型已存在编码规则",
                    type: 'warning'
                  })
                  loading.close()
                  return
                }
                Notification({
                  message: '编辑成功',
                  type: 'success'

                })
                loading.close()
                this.editFormVisible = false
                this.list();
              }).catch(() => {

              }
              )
          } else {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            codeExplain.addOrderCode(this.editForm)
              .then((data) => {
                if (data.status == 'failed') {
                  Notification({
                    message: data.msg,
                    type: 'warning'
                  })
                  return
                }
                Notification({
                  message: '新建成功',
                  type: 'success'

                })
                loading.close()
                this.editFormVisible = false
                this.list()
              })
          }

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    addClick() {
      this.mode = 'add';
      this.editForm = {
        codeNo: '',
        codeBelong: '',
        codeName: '',
        orderNoCode: '',
        codeContents: [],
        codeId: ''
      }
      this.editFormVisible = true
    },
    editClick(row) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      codeExplain.getOrderCodeRuleById(row.id).then(data => {
        this.mode = 'edit';
        this.editForm = data.entity;
        this.editFormVisible = true
        loading.close()
      }).catch((data) => {
        /*Notification({
            message: data.msg,
            type: 'warning'
        })*/
      })

    },
    deleteClick(row) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        codeExplain.deleteOrderCodeRuleById(row.id).then(data => {
          this.$store.commit('endLoading')
          this.list();
        })
      }).catch(() => {

      })
    },
    deletesClick() {
      if (this.selected.length == 0) {
        this.$message('请选择删除项');
        return;
      }
      let ids = []
      this.selected.forEach((row) => {
        ids.push(row.id)
      })
      this.$confirm('确认删除选择项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        codeExplain.deleteByIds(ids).then(() => {
          this.$store.commit('endLoading')
          this.list();
        })

      }).catch(() => {

      })
    },
    list() {
      this.$store.commit('startLoading')
      codeExplain.listAllOrderCodeRule().then(data => {
        this.datas = data.entity;
        this.$store.commit('endLoading')
      }).catch(err => {
        console.log("err", err);
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize = val;
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val;
      this.list()
    }
  },

}
</script>
<style>
</style>