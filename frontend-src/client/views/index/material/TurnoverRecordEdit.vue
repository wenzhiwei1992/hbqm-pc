<template>
    <div>
        <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
            <!-- <el-tab-pane label="基本信息" name="first">-->
            <el-form :inline="true" :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm"
                     label-width="80px">
                <!-- <el-form-item label="供方"  prop="shipments">
                    <el-input v-model="editForm.shipments"  style="width: 150px"></el-input>
                </el-form-item> -->

                <el-form-item label="部门" prop="departmentId">
                    <el-select v-model="editForm.departmentId" placeholder="请选择" clearable style="width: 150px"
                               filterable>
                        <el-option
                                v-for="item in departmentSelect"
                                :key="item.id"
                                :label="item.deptName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="仓库" prop="stockId">
                    <el-select v-model="editForm.stockId" placeholder="请选择" style="width: 150px">
                        <el-option
                                v-for="item in stockSelect"
                                :key="item.stockName"
                                :label="item.stockName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="供方" prop="shipmentsId">
                    <el-select v-model="editForm.shipmentsId" placeholder="请选择" style="width: 150px" filterable>
                        <el-option
                                v-for="item in shipmentsSelect"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-button type="primary" @click="skip('../basicdata/supplier')">添加</el-button>
                <el-form-item label="运输单位" prop="transport">
                    <el-input v-model="editForm.transport " style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="运费" prop="freight">
                    <el-input v-model="editForm.freight" style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="车牌号" prop="licenseNo">
                    <el-input v-model="editForm.licenseNo" style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="editForm.remark" type="textarea" style="width: 150px"></el-input>
                </el-form-item>
                <!--          <el-form-item>
                              <RegShopImg :imgN='2' :nameN='receipt' @imgthing = 'imgthing' v-model="editForm.receipt"></RegShopImg>
                          </el-form-item>-->
            </el-form>
            <div>
                <el-row type="flex" justify="space-around" align="middle">
                    <el-col>
                        <el-button type="primary" @click="addClick">添加</el-button>
                        <el-button type="danger" @click="deletesClicks">删除</el-button>
                        <el-upload
                                style="display: inline;margin-left: 10px"
                                class="upload-demo"
                                action
                                :before-upload="beforeAvatarUploadMaterial"
                                :multiple="true"
                        >
                            <el-button size="mini" type="primary">导入
                            </el-button>
                            <!--   <span style="color: red;font-size: small">*请先勾选项目</span>-->
                        </el-upload>
                    </el-col>
                </el-row>
                <el-table
                        :data="editForm.turnovers"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="300"
                        style="width:100%"
                >
                    <el-table-column
                            type="selection"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="materialStorageId"
                            label="原料编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialName"
                            label="原料名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialSpecification"
                            label="原料规格"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialUnitName"
                            label="单位"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            v-if="show"
                            prop="checkout"
                            label="是否免检"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            label="原料数量"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.amount"
                                    @input="sunmToal(scope.row,scope.$index)"
                                    @keyup.native="parseAmount(scope.row,scope.$index)"
                                    placeholder="请输入内容"
                            ></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="单价"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.univalence"
                                    @input="sunmToal(scope.row,scope.$index)"
                                    @keyup.native="parseValue(scope.row,scope.$index)"
                                    placeholder="请输入内容"
                            ></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="总价"
                            width="120"
                    >
                        <!--prop="total"-->
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.totalUnivalence"
                                    @input="sunmUnivalence(scope.row,scope.$index)"
                                    @keyup.native="parseTotalValue(scope.row,scope.$index)"
                                    placeholder="请输入内容"
                            ></el-input>
                        </template>
                    </el-table-column>

                </el-table>

                <el-dialog :title="turnoverAddTitle" :visible.sync="editFormTurnoverAddVisible"
                           v-if="editFormTurnoverAddVisible" width="70%" append-to-body>
                    <turnover-add-edit :editFormTurnoverAdd="editFormTurnoverAdd"
                                       :editForm="editForm"
                                       v-on:headCallBack="headCall"
                                       @turnover-add-finish="turnoverAddFinish"
                                       @turnover-add-cancel="turnoverAddCancel"
                    ></turnover-add-edit>
                </el-dialog>
            </div>
            <!--     </el-tab-pane>-->
            <!--            <el-tab-pane label="收货单据" name="second">&lt;!&ndash;  :file-list="fileList2"       :auto-upload="false"   &ndash;&gt;
                           <el-upload
                                    ref="upload"
                                    class="upload-demo"
                                    action=""
                                    :on-preview="handlePreview"
                                    :on-remove="handleRemove"
                                    :before-upload="beforeAvatarUpload"
                                    list-type="picture">
                                <el-button  size="small" type="primary">上传文件</el-button>
                                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                            </el-upload>
            &lt;!&ndash;              <el-upload
                                    ref="upload"
                                    class="upload-demo"
                                    action=""
                                    :auto-upload="false"
                                    :on-preview="handlePreview"
                                    :on-remove="handleRemove"
                                    :before-upload="beforeAvatarUpload"
                                    list-type="picture">
                                <el-button slot="trigger"  size="small" type="primary">选取文件</el-button>
                                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                            </el-upload>&ndash;&gt;
                        </el-tab-pane>-->


        </el-tabs>


        <div slot="footer" class="dialog-footer">
            <el-button type="danger" @click="editFormCancel">关闭</el-button>
            <el-button type="success" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>


        <!--        <el-table-column
                        prop="picture"
                        header-align="center"
                        align="center"
                        width="150px"
                        label="图片">
                    <template slot-scope="scope">&lt;!&ndash;&ndash;&gt;
                        <el-popover
                                placement="right"
                                title=""
                                trigger="hover">
                            <img :src="scope.row.picture"/>
                            <img slot="reference" :src="scope.row.picture" :alt="scope.row.picture" style="max-height: 50px;max-width: 130px">
                        </el-popover>
                    </template>
                </el-table-column>-->
        <el-dialog
                title="提示"
                :visible.sync="dialogVisible"

                width="30%"
                :before-close="handleClose"
                append-to-body>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="testUpload">上传到服务器</el-button>
            <span></span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import turnoverDetailModel from '../../../model/materials/turnoverDetail'
    import turnoverRecordModel from '../../../model/materials/turnoverRecord'
    import materialStorageModel from '../../../model/basicdata/materialStorage'
    import clientModel from '../../../model/client-model'
    import TurnoverAdd from './TurnoverAdd.vue'
    import {Notification} from 'element-ui'

    export default {
        props: ['editForm', 'mode', 'departmentSelect', 'stockSelect', 'shipmentsSelect'],
        components: {
            'turnover-add-edit': TurnoverAdd
        },
        computed: {
            turnoverAddTitle() {

            }
        },
        mounted() {
            this.getMaterialStorage()
        },
        methods: {
            skip(a) {
                this.$emit('turnoverRecord-edit-finish')
                this.$router.push(a)
            },
            sunmUnivalence(row, index) {
                let value = (row.totalUnivalence / row.amount).toFixed(3);
                this.editForm.turnovers[index].univalence = value == 0 ? "" : value;
            },
            sunmToal(row, index) {
                let value = row.univalence * row.amount;
                this.editForm.turnovers[index].totalUnivalence = value == 0 ? "" : value;
            },
            parseTotalValue(row, index) {
                let value = row.totalUnivalence;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.totalUnivalence = value;
                this.sunmUnivalence(row, index);
            },
            parseValue(row, index) {
                let value = row.univalence;
                if(!value){
                    return;
                }
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.univalence = value;
                this.sunmToal(row, index);
            },
            parseAmount(row, index) {
                let value = row.amount;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.amount = value;
                    this.sunmToal(row, index);
            },
            /*            changeUpload: function(file, fileList) {
                            console.log('TEST-----fileList');//TO DELETE
                            console.log(fileList);//TO DELETE
                            this.fileList = fileList;
                            this.$nextTick(
                                () => {
                                    let upload_list_li = document.getElementsByClassName('el-upload-list')[0].children;
                                    for (let i = 0; i < upload_list_li.length; i++) {
                                        let li_a = upload_list_li[i];
                                        let imgElement = document.createElement("img");
                                        imgElement.setAttribute('src', fileList[i].url);
                                        imgElement.setAttribute('style', "max-width:50%;padding-left:25%");
                                        if (li_a.lastElementChild.nodeName !== 'IMG') {
                                            li_a.appendChild(imgElement);
                                        }
                                    }
                                })
                        },*/
            submitUpload() {
                this.$refs.upload.submit();
            },
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {
                    });
            },
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                /*             var formData=new FormData()
                              formData.append('file',file)
                              var file = formData
                              this.fileUploads = file;
                              this.dialogVisible= true*/
                console.log(file);
            },

            /**
             * 自定义上传
             * 选择图片
             */
            beforeAvatarUpload(file) {
                var formData = new FormData()
                formData.append('file', file)
                var file = formData
                // this.file = files;
                clientModel.ax.post('/turnoverRecord/uploadPhoto', file, {
                    headers: {
                        'Content-Type': 'charset=UTF-8'
                    },
                }).then((data) => {

                })
            },
            /**
             * 上传图片
             */
            testUpload() {
                var file = this.fileUploads
                console.log('TEST----file');//TO DELETE
                console.log(file);//TO DELETE

            },
            handleClick(tab, event) {
                console.log(tab, event);
            },
            headCall(msg) {
                this.arrMaterialsAdd = msg
                this.editFormTurnoverAddVisible = false
                this.showList()
            },
            showList() {
                /* materialStorageModel.getMaterialArrayList().then((data)=>{
                    for(var i = 0;i<data.entity.length;i++){
                        this.editForm.turnovers.push(data.entity[i])
                    }
                })*/
                materialStorageModel.getMaterialArrayAdd(this.arrMaterialsAdd).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    for (var i = 0; i < data.entity.length; i++) {
                        var info = {
                            materialId: data.entity[i].id,
                            materialStorageId: data.entity[i].materialStorageId,
                            materialName: data.entity[i].materialName,
                            materialSpecification: data.entity[i].materialSpecification,
                            materialUnit: data.entity[i].materialUnit,
                            checkout: data.entity[i].checkout,
                            univalence: '',
                            amount: '',
                          // materialUnit: data.entity[i].materialUnit,
                            materialUnitName: data.entity[i].materialUnitName,
                            totalUnivalence: '',
                        }
                        this.editForm.turnovers.push(info)
                    }
                })
            },
            addClick() {
                if(!this.editForm.stockId || !this.editForm.shipmentsId){
                    this.$message('请先确认仓库及供方信息');

                    return false;
                }
                this.editFormTurnoverAddVisible = true
            },
            /**
             * 删除操作
             */
            deletesClicks() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row) => {
                    for (var i = 0; i < this.editForm.turnovers.length; i++) {
                        if (this.editForm.turnovers[i].materialStorageId == row.materialStorageId) {
                            this.editForm.turnovers.splice(i, 1);
                        }
                    }
                })
            },
            turnoverAddFinish() {
                this.editFormTurnoverAddVisible = false
                this.showList()
            },
            turnoverAddCancel() {
                this.editFormTurnoverAddVisible = false
                //this.showList()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            getSuttleAndRealityWeightByCount() {
                var turnoverQuery = {
                    roughWeight: this.editForm.roughWeight,
                    tare: this.editForm.tare,
                    sundries: this.editForm.sundries
                }
                turnoverDetailModel.getSuttleAndRealityWeightByCount(turnoverQuery).then((data) => {
                    this.editForm.suttle = data.entity.suttle
                    this.editForm.realityWeight = data.entity.realityWeight
                })
            },
            getMaterialStorage() {
                materialStorageModel.getMaterialStorageByMaterialStorage().then((data) => {
                    var arr = new Array()
                    for (var i = 0; i < data.entity.length; i++) {
                        var materialStorageId = parseInt(data.entity[i]).materialStorageId
                        var v = {
                            label: materialStorageId,
                            value: data.entity[i].materialStorageId + '[' + data.entity[i].materialName + ';' + data.entity[i].materialSpecification + ']'
                        }
                        arr.push(v)
                    }
                    this.arrMaterialStorage = arr
                })
            },
            getMaterialStorageByMaterialStorageId() {
                var materialStorageId = this.editForm.materialStorageId.substring(0, 4);
                materialStorageModel.getMaterialStorageByMaterialStorageId(materialStorageId).then((data) => {
                    this.editForm.materialName = data.entity.materialName
                    this.editForm.materialSpecification = data.entity.materialSpecification
                    this.editForm.materialUnit = data.entity.materialUnit
                })
            },
            editFormCancel() {
                this.$emit('turnoverRecord-edit-cancel')
            },
            /**
             * 读取excel
             */
            beforeAvatarUploadMaterial(file) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let Xls = file.name.split(".");
                if (Xls[1] === "xls" || Xls[1] === "xlsx") {
                    var formData = new FormData();
                    formData.append("file", file);
                    var file = formData;
                    clientModel.ax
                        .post('/turnoverRecord/getMaterilBelongTRToExcel', file, {
                            headers: {
                                "Content-Type": "charset=UTF-8"
                            }
                        })
                        .then(data => {
                            if (data.status == "failed") {
                                Notification({
                                    message: data.msg,
                                    type: "warning"
                                });
                                loading.close()
                                return;
                            }
                            this.editForm.turnovers=data.entity
                            Notification({
                                message: "导入成功",
                                type: "success"
                            });
                            loading.close()
                        });
                    return false;
                } else {
                    this.$message.error("上传文件只能是 xls/xlsx 格式!");
                    return false;
                }
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'update') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            if (this.editForm.turnovers != false) {
                                this.editForm.shipments = '' + '-' + this.editForm.id
                                var isNullUpdate = true
                                for (var i = 0; i < this.editForm.turnovers.length; i++) {
                                    if (this.editForm.turnovers[i].amount == "") {
                                        isNullUpdate = false
                                        break
                                    }
                                }
                                if (isNullUpdate == true) {
                                    turnoverRecordModel.turnoverRecordUpdate(this.editForm)
                                        .then((data) => {
                                            if (data.status == 'fail') {
                                                Notification({
                                                    message: data.msg,
                                                    type: 'warning'
                                                })
                                                loading.close()
                                                return
                                            }
                                            turnoverDetailModel.turnoverDetailUpdates(this.editForm).then((data) => {
                                                if (data.status == 'fail') {
                                                    Notification({
                                                        message: data.msg,
                                                        type: 'warning'
                                                    })
                                                    loading.close()
                                                    return
                                                }
                                                Notification({
                                                    message: '编辑成功',
                                                    type: 'success'
                                                })
                                                loading.close();
                                                this.$emit('turnoverRecord-edit-finish')
                                            })
                                        })
                                } else {
                                    this.$message('数量不能为空！')
                                    // isNullUpdate = true
                                    loading.close();
                                }
                            } else {
                                this.$message('请添加原料！')
                                loading.close();
                            }

                        } else if (this.mode == 'add') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            if (this.editForm.turnovers != false) {
                                var isNull = true
                                for (var i = 0; i < this.editForm.turnovers.length; i++) {
                                    if (this.editForm.turnovers[i].amount == '') {
                                        isNull = false
                                        break
                                    }
                                }
                                if (isNull == true) {
                                    turnoverRecordModel.turnoverRecordAdd(this.editForm)
                                        .then((data) => {
                                            if (data.status == 'fail') {
                                                Notification({
                                                    message: data.msg,
                                                    type: 'warning'
                                                })
                                                loading.close()
                                                return
                                            }
                                           /* turnoverDetailModel.turnoverDetailAdds(this.editForm.turnovers).then((data) => {
                                                if (data.status == 'fail') {
                                                    Notification({
                                                        message: data.msg,
                                                        type: 'warning'
                                                    })
                                                    loading.close()
                                                    return
                                                }
                                            })*/
                                            Notification({
                                                message: '新建成功',
                                                type: 'success'
                                            })
                                            loading.close();
                                            this.$emit('turnoverRecord-edit-finish')
                                        })
                                } else {
                                    this.$message('数量不能为空！')
                                    // isNull= true
                                    loading.close();
                                }

                            } else {
                                this.$message('请添加原料！')
                                loading.close();
                            }
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        },
        
        data() {
            return {
                arrMaterialsAdd: [],//收料单添加的原料数组
                fileUploads: '',
                dialogVisible: false,
                fileList: [],
                activeName2: 'first',
                show: '',//隐藏tabel
                selected: [],
                editFormTurnoverAddVisible: false,
                editFormTurnoverAdd: '',
                turnovers: [],
                testArr: [],
                arrMaterialStorage: [],
                options: [{
                    value: '吨',
                    label: '吨'
                },],
                eidtFormRules: {
                    shipmentsId: [
                        {required: true, message: '供方不能为空', trigger: 'blur'},
                    ],
                    stockId: [
                        {required: true, message: '仓库不能为空', trigger: 'blur'},
                    ],
                    // departmentId: [
                    //     {required: true, message: '部门不能为空', trigger: 'blur'},
                    // ],
                    /*          transport: [
                                  {required: true, message: '运输单位不能为空', trigger: 'blur'},
                              ],*/
                    /*       licenseNo: [
                               {required: true, message: '车牌号不能为空', trigger: 'blur'},
                           ],*/
                },
            }
        }
    }
</script>

<style scoped>

</style>