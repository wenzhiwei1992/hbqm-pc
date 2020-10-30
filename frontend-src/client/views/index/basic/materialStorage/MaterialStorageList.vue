<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              id="projectList"
              :model="materialStorageQuery"
              ref="materialStorageQuery"
              style="padding-bottom:8px;"
            >
              <el-form-item prop="materialName">
                <el-input
                  placeholder="物料名称"
                  v-model="materialStorageQuery.materialName"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="categoryName">
                <el-input
                  placeholder="物料类别"
                  v-model="materialStorageQuery.categoryName"
                  @focus="showSelectTrees"
                ></el-input>
              </el-form-item>
              <el-form-item
                prop="categoryId"
                style="display:none"
              >
                <el-input
                  placeholder="物料分类Id"
                  v-model="materialStorageQuery.categoryId"
                ></el-input>
              </el-form-item>

              <!-- <el-form-item label="物料分类" prop="classificationId">
                      <el-select  v-model="materialStorageQuery.classificationId" placeholder="请选择" @change="showCategory"
                                style="width:150px">
                          <el-option
                                  v-for="item in arrClassification"
                                  :key="item.id"
                                  :label="item.classificationName"
                                  :value="item.id">
                          </el-option>
                      </el-select>
                  </el-form-item>
                  <el-form-item label="物料类别" prop="categoryId">
                      <el-select clearable v-model="materialStorageQuery.categoryId" placeholder="请选择"
                                style="width:150px">
                          <el-option
                                  v-for="item in arrCategory"
                                  :key="item.id"
                                  :label="item.categoryName"
                                  :value="item.id">
                          </el-option>
                      </el-select>
                  </el-form-item> -->
              <el-form-item prop="materialSpecification">
                <el-input
                  placeholder="物料规格"
                  v-model="materialStorageQuery.materialSpecification"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="checkout">
                <el-select
                  clearable
                  v-model="materialStorageQuery.checkout"
                  placeholder="是否免检"
                  style="width:150px"
                >
                  <el-option
                    value="是"
                    label="是(免检)"
                  ></el-option>
                  <el-option
                    value="否"
                    label="否(免检)"
                  ></el-option>
                </el-select>
              </el-form-item>
              <!--<el-form-item prop="decrease">-->
                <!--<el-select-->
                  <!--clearable-->
                  <!--v-model="materialStorageQuery.decrease"-->
                  <!--placeholder="自动销库"-->
                  <!--style="width:150px"-->
                <!--&gt;-->
                  <!--<el-option-->
                    <!--value="是"-->
                    <!--label="是(自动销库)"-->
                  <!--&gt;</el-option>-->
                  <!--<el-option-->
                    <!--value="否"-->
                    <!--label="否(自动销库)"-->
                  <!--&gt;</el-option>-->
                <!--</el-select>-->
              <!--</el-form-item>-->
              <el-form-item prop="materialNo">
                <el-input
                  placeholder="物料编码"
                  v-model="materialStorageQuery.materialNo"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model='searchButtonData'></my-search-button-group>
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
          :data="materialStorages"
          @selection-change="handleSelectionChange"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          size="mini"
          stripe
          border
          height="100%"
        >
          <el-table-column
            type="selection"
            width="39"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
          >
          </el-table-column>
          <el-table-column
            prop="materialStorageId"
            label="系统编码"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="materialNo"
            label="物料编码"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="物料名称"
            width="130"
          >
          </el-table-column>
          <el-table-column
            prop="categoryName"
            label="物料类别"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialSpecification"
            label="物料规格"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="materialUnit"
            label="计量单位"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="checkout"
            label="是否为免检物料"
            width="150"
          >
          </el-table-column>
          <!--<el-table-column-->
            <!--prop="decrease"-->
            <!--label="是否自动销存库"-->
            <!--width="150"-->
          <!--&gt;-->
          <!--</el-table-column>-->
          <el-table-column
            prop="purchasePeriod"
            label="采购周期"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="inventory"
            label="库存预警"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="lossRate"
            label="损耗率"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="dateCreated"
            :formatter="formatterDate"
            label="创建时间"
          >
          </el-table-column>
          <el-table-column
            prop="dateModified"
            :formatter="formatterDate"
            label="修改时间"
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
            :current-page="materialStorageQuery.pageInfo.currentPage"
            :page-sizes="materialStorageQuery.pageInfo.pageSizes"
            :page-size="materialStorageQuery.pageInfo.pageSize"
            :total="materialStorageQuery.pageInfo.total"
            layout="total, sizes, prev, pager, next, jumper"
            background
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>
    <el-dialog
      :title="mode=='add'?'新建':'编辑'"
      v-if="editFormVisible"
      :visible.sync="editFormVisible"
    >
      <material-storage-edit
        :edit-form="editForm"
        :mode="mode"
        :arrClassification="arrClassification"
        :treeArr="treeArr"
        @materialStorage-edit-finish="materialStorageEditFinish"
        @materialStorage-edit-cancel="materialStorageEditCancel"
      ></material-storage-edit>
    </el-dialog>
    <el-dialog
            :visible.sync="editFormUpoloadVisible"
            width="35%"
            append-to-body
    >
      <file-upload
              fileName="materialStorageExcel.xlsx"
              @download-finish="downloadFinish"
              @download-cancel="downloadCancel"
      ></file-upload>
    </el-dialog>
    <el-dialog
      :visible.sync="showSelectTree"
      width="340px"
      append-to-body
    >
      <tree-add-edit1
        :treeArr="treeArr"
        :baseCheckedId="baseCheckedId"
        :baseCheckParentId="baseCheckParentId"
        @tree-edit-submit="checkoutStatusFinish"
        @tree-edit-cancel="checkoutStatusCancel"
      ></tree-add-edit1>
    </el-dialog>
  </div>
</template>

<script>
import serviceConfig from '../../../../../server.config.js'
import clientModel from '../../../../model/client-model'
import materialStorageModel from '../../../../model/basicdata/materialStorage'
import classificationModel from '../../../../model/basicdata/classification'
import categoryModel from '../../../../model/basicdata/category'
import MaterialStorageEdit from './MaterialStorageEdit.vue'
import FileUpload from '../../../../components/fileUpload/index.vue'
import TreeInfo from './TreeInfo.vue'
import { Notification } from 'element-ui'
import moment from "moment"
import { isURL } from '@/util/validate'

export default {
  components: {
    'material-storage-edit': MaterialStorageEdit,
     'file-upload': FileUpload,
    'tree-add-edit1': TreeInfo,
  },
  mounted() {
    this.getUnit();
    this.materialList()
    this.showClassification()
    this.showCategoryTree()
  },

  methods: {
    checkoutStatusFinish(id, name) {
      this.materialStorageQuery.categoryName = name;
      this.materialStorageQuery.categoryId = id;
      this.materialStorageQuery.classificationId = id;

      this.showSelectTree = false
    },
    checkoutStatusCancel() {
      this.showSelectTree = false
    },
    showSelectTrees() {
      this.baseCheckedId.splice(0, this.baseCheckedId.length);
      this.baseCheckedId.push(this.materialStorageQuery.categoryId);

      this.baseCheckParentId.splice(0, this.baseCheckedId.length);
      this.baseCheckParentId.push(this.materialStorageQuery.categoryId);

      this.showSelectTree = true
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.materialStorageQuery.classificationId = '';
      this.materialStorageQuery.categoryName = '';
    },
    getUnit() {
      materialStorageModel.findAll().then((data) => {
        this.optionsUnit = data.entity
      })
    },
    base64(s) {
      return window.btoa(unescape(encodeURIComponent(s)))
    },
    materialExport() {
      this.editFormUpoloadVisible = true
    },
    materialList() {
      materialStorageModel.materialStorageList(this.materialStorageQuery).then((data) => {
        this.materialStorages = data.entity.content
        this.materialStorageQuery.pageInfo.total = data.entity.totalElements
      })
    },
    /**
     *模糊查询
     */
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.materialList()
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    addClick() {
      this.mode = 'add'
      this.editForm = {
        materialName: '',
        materialType: '2',
        materialSpecification: '',
        materialUnit: '',
        inventory: '',
        checkout: '否',
        // decrease: '是',
        purchasePeriod: '',
        initial: '',
        classificationId: '',
        categoryId: '',
        categoryName: '',
        materialStorageId: '',
      }
      this.editFormVisible = true
    },
    deletesClicks() {
      if (this.selected.length == 0) {
        this.$message('请选择删除项');
        return;
      }
      let ids = []
      this.selected.forEach((row) => {
        ids.push(row.id)
      })
      this.$confirm('此操作将永久删除勾选物料, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        materialStorageModel.materialStorageDeletes(ids.join(";")).then((data) => {
          if (data.status == 'fail') {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: '删除成功',
            type: 'success'

          })
          this.materialList()
          loading.close()
        })
      }).catch(() => {
      })
    },
    deleteClick(row) {
      this.$confirm('此操作将永久删除该物料, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        materialStorageModel.materialStorageDelete(row.id).then(data => {
          if (data.status == 'fail') {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            return
          }
          Notification({
            message: '删除成功',
            type: 'success'

          })
          this.materialList()
          loading.close()
        })
      }).catch(() => {
      })
    },
    editClick(row) {
      this.mode = this.GLOBAL.STATUS.UPDATE
      materialStorageModel.materialStorageLoad(row.id).then((data) => {
        data.entity.materialUnit = parseInt(data.entity.materialUnit)
        this.editForm = data.entity
        this.editFormVisible = true
      })
    },
    uploadSuccess() {
    },
    // 上传错误
    uploadError(response, file, fileList) {
      console.log('上传失败，请重试！')
    },
    /**
     * 自定义上传
     */
    beforeAvatarUpload(file) {
      let Xls = file.name.split('.')
      if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        var formData = new FormData()
        formData.append('file', file)
        var file = formData
        clientModel.ax.post('/materialStorageExcel/readExcel', file, {
          headers: {
            'Content-Type': 'charset=UTF-8'
          },
        }).then((data) => {
          if (data.status == 'failed') {
            Notification({
              message: data.msg,
              type: 'warning',
              duration: 0,
            })
            loading.close()
            this.materialList()
            return
          }
          loading.close()
          this.materialList()
          let message = "导入成功";
          if(data.entity){
            message = message+data.entity+"条"
          }
          Notification({
            message: message,
            type: 'success'
          })
        })
        return false
      } else {
        this.$message.error('上传文件只能是 xls/xlsx 格式!')
        return false
      }
    },
    exportExcel() {
      var url = serviceConfig.baseURL + '/materStorageDownloadController/getMaterialsByExportExcel'
      var prams = { "materialName": this.materialStorageQuery.materialName, "classificationId": this.materialStorageQuery.classificationId, "categoryId": this.materialStorageQuery.categoryId, "materialSpecification": this.materialStorageQuery.materialSpecification, "checkout": this.materialStorageQuery.checkout, "decrease": this.materialStorageQuery.decrease, "materialNo": this.materialStorageQuery.materialNo }
      this.exportExcelPost(url, prams)
    },
    exportExcelPost(URL, PARAMS) {
      var temp = document.createElement("form");
      temp.action = URL;
      temp.method = "post";
      temp.style.display = "none";
      for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp.appendChild(opt);
      }
      document.body.appendChild(temp);
      temp.submit();
      return temp;
    },
    downloadFinish() {
      this.editFormUpoloadVisible = false
    },
    downloadCancel() {
      this.editFormUpoloadVisible = false
    },
    showClassification() {
      classificationModel.getClassifications().then((data) => {
        this.arrClassification = data.entity
      })
    },
    showCategoryTree() {
      // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
      categoryModel.getCategorysToTreeParentDisabled().then((data) => {
        this.treeArr = data.entity
      })
    },
    showCategory(value) {
      categoryModel.getCategorysByClassificationId(value).then((data) => {
        this.arrCategory = data.entity
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.materialStorageQuery.pageInfo.pageSize = val
      this.materialList()
    },
    handleCurrentChange(val) {
      this.materialStorageQuery.pageInfo.currentPage = val
      this.materialList()
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    //编辑表单
    materialStorageEditFinish() {
      this.editFormVisible = false
      this.materialList()
    },
    materialStorageEditCancel() {
      this.editFormVisible = false
      // this.materialList()
    },
    // rowButtonGroupWidth() {
    //   let width = this.rowButtonData.length * 50
    //   width = width > 80 ? width : 80
    //   return `${width}px`
    // },
  },
  data() {
    return {
      editFormUpoloadVisible: false,
      show: '',
      template: '',
      options: [{
        value: '备件',
        label: '备件'
      }, {
        value: '保温材料',
        label: '保温材料'
      }, {
        value: '吊具',
        label: '吊具'
      }, {
        value: '粉料',
        label: '粉料'
      }, {
        value: '工具',
        label: '工具'
      }],
      optionsUnit: [],
      showSelectTree: false,
      baseCheckedId: [],
      baseCheckParentId: [],
      treeArr: '',
      materialStorageQuery: {
        materialNo: '',
        materialName: '',
        classificationId: '',
        classificationName: '',
        categoryId: '',
        materialType: '',
        materialSpecification: '',
        checkout: '',
        decrease: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      mold: '',
      materialStorages: [],
      selected: [],
      //编辑表单
      mode: '',
      editFormTitle: '',
      editFormVisible: false,
      editForm: null,
      arrCategory: '',
      arrClassification: '',
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '下载',
          event: 'materialExport'
        },
        {
          name: '导出',
          event: 'exportExcel'
        },
        {
          name: '导入',
          action: '',
          multiple: true,
          methods: {
            onError: this.uploadError,
            onSuccess: this.uploadSuccess,
            beforeUpload: this.beforeAvatarUpload
          }
        },
        {
          name: '删除',
          event: 'deletesClicks'
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "submitForm('materialStorageQuery')"
        },
        {
          name: '重置',
          event: 'resetForm("materialStorageQuery")'
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: "editClick",
        },

      ]
    }
  }
}
</script>
