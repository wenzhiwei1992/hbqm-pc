<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="semiFinishedGoodsQuery"
                                ref="semiFinishedGoodsQuery"
                                style="padding-bottom:8px;"
                        >
                            <el-form-item prop="sfgName">
                                <el-input
                                        placeholder="半成品名称"
                                        v-model="semiFinishedGoodsQuery.sfgName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="sfgNo">
                                <el-input
                                        placeholder="半成品编码"
                                        v-model="semiFinishedGoodsQuery.sfgNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <!--<el-form-item prop="sfgSpecification">
                                <el-input
                                        placeholder="规格"
                                        v-model="semiFinishedGoodsQuery.sfgSpecification"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>-->


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
                        :data="sfgList"
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        @expand-change="showSubData"
                        :expand-row-keys="expands"
                        :row-key="getRowKeys"
                        size="mini"
                        :default-sort="{prop: 'materialStorageId',}"
                        stripe
                        border
                        height="100%"
                >
                    <el-table-column
                            type="selection"
                            width="39"
                    >
                    </el-table-column>
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table
                                    :data="subDatas"
                                    @selection-change="handleSelectionChangeForPL"
                                    size="mini"
                                    stripe
                                    border
                                    style="width: 100%"
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
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="materialSpecification"
                                        label="原料规格"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="materialUnit"
                                        label="单位"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="num"
                                        label="数量"
                                >
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgNo"
                            label="编码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgName"
                            label="名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="categoryName"
                            label="类别"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgSpecification"
                            label="规格"
                    >
                    </el-table-column>
                    <!--<el-table-column
                            prop="unitCNName"
                            label="计量单位"
                    >
                    </el-table-column>-->
                    <el-table-column
                            prop="sfgLen"
                            label="总长度"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgWt"
                            label="总重量"
                    >
                    </el-table-column>
                    <!--<el-table-column
                            prop="rebarDiameter"
                            label="钢筋直径"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="rebarShape"
                            label="钢筋形状"
                    >
                    </el-table-column>-->
                    <el-table-column
                            label="查看图纸"
                            align="center"
                            width="150"
                    >
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.sfgOriginalFilename" placement="top" effect="light">
                                <a v-if="scope.row.bomPath" @click="findReceiptImg(scope.row)" style="color: #409EFF;">查看</a>
                            </el-tooltip>

                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0);" @click="deleteBomInfo(scope.row)" style="color: red;" v-if="scope.row.bomPath">删除</a>
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
                <el-table
                        :data="exportdatas"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        :default-sort="{prop: 'materialStorageId',}"
                        border
                        id="exportXlsx"
                        hidden
                        height="100%"
                >

                    <el-table-column
                            type="index"
                            label="序号"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgNo"
                            label="半成品编码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgName"
                            label="半成品名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="categoryName"
                            label="原料类别"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="计量单位"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgLen"
                            label="下料长度(mm)"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgWt"
                            label="下料重量(kg)"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="rebarDiameter"
                            label="钢筋直径"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="rebarShape"
                            label="钢筋形状"
                    >
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="semiFinishedGoodsQuery.pageInfo.currentPage"
                            :page-sizes="semiFinishedGoodsQuery.pageInfo.pageSizes"
                            :page-size="semiFinishedGoodsQuery.pageInfo.pageSize"
                            :total="semiFinishedGoodsQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
            <el-dialog :visible.sync="editFormVisible" v-if="editFormVisible" width="65%" append-to-body>
                <semi-finished-goods-edit
                        :mode="mode"
                        :sfgInfoEdit="sfgInfoEdit"
                        @sfg-edit-finish="sfgEditFinish"
                        @sfg-edit-cancel="sfgEditCancel"
                ></semi-finished-goods-edit>
            </el-dialog>
            <!-- 批量下载二维码 -->
            <el-dialog
                    append-to-body
                    :visible.sync="editFormVisibleQRBatch"
                    width="40%"
                    v-if="editFormVisibleQRBatch"
            >
                <shelf-qr-code-edit-batch
                        :shelfNoQRCodes="sfgNoQRCodes"
                        :shelfNameQRCodes="sfgNameQRCodes"
                        :shelfIdQRCodes="sfgIdQRCodes"
                ></shelf-qr-code-edit-batch>
            </el-dialog>
        </el-container>
        <!--上传图片-->
        <el-dialog
                append-to-body
                title="导入BOM图纸"
                :visible.sync="dialogUploadVisiblePDF"
                width="40%"
        >
            <upload-sfgPDF
                    :upload-edit-form="uploadPDFForm"
                    :arrsfgIds="arrsfgIds"
                    :arrsfgNos="arrsfgNos"
                    @sfg-edit-finish="sfgEditFinish"
                    @sfg-edit-cancel="sfgEditCancel"
            >
            </upload-sfgPDF>
        </el-dialog>
        <image-viewer v-if="checkImagesVisible" :srcList="srcList" @imageViewClose="checkImagesVisible=false" />
    </div>
</template>

<script>
    import sfgModel from '../../../../model/basicdata/sfg'
    import semiFinishedGoodsEdit from './SemiFinishedGoodsEdit.vue'
    import {Notification} from "element-ui";
    import clientModel from "../../../../model/client-model";
    import model from "../../../../model/model";
    import serviceConfig from "../../../../../server.config";
    import xlsx from "xlsx";
    import fileSaver from "file-saver";
    import ShelfQRcodeEdit from "../yard/ShelfQRcodeEdit.vue";
    import ShelfQRcodeEditBatch from "../yard/ShelfQRcodeEditBatch.vue";
    import UploadSfgPDF from "../../basic/sfg/UploadSfgPDF.vue";
    import productModel from "../../../../model/project/product";
    import imageViewer from '@/components/imageViewer/index.vue';
    import orderChooseModel from "../../../../model/po/orderChoose";

    export default {
        name: "SemiFinishedGoodsList",
        components: {
            'semi-finished-goods-edit': semiFinishedGoodsEdit,
            'shelf-qr-code-edit': ShelfQRcodeEdit,
            'shelf-qr-code-edit-batch': ShelfQRcodeEditBatch,
            'upload-sfgPDF': UploadSfgPDF,
            imageViewer,
        },
        data() {
            return {
                uploadPDFForm: null,//导入内嵌框表单
                arrsfgNos:[],
                arrsfgIds:[],
                selected:[],
                selectedFoPL: [],
                expands: [],
                dialogUploadVisiblePDF: false,//上传文件内嵌框visible.sync
                checkImagesVisible: false,
                srcList:[],
                checkSelected: null,
                //编辑操作传值
                sfgInfoEdit: [],
                mode: '',
                subDatas: [],
                editFormVisible: false,
                semiFinishedGoodsQuery: {
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                sfgList: [],
                exportdatas: [],
                editFormVisibleQRBatch:false,
                sfgNoQRCodes: [],
                sfgNameQRCodes: [],
                sfgIdQRCodes:[],
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick',
                        authorized: 'sfg:add',
                    },
                    {
                        name: '导入',
                        action: '',
                        multiple: true,
                        methods: {
                            onError: this.uploadError,
                            onSuccess: this.uploadSuccess,
                            beforeUpload: this.beforeAvatarUpload
                        },
                        authorized: 'sfg:upload',
                    },
                    {
                        name: '下载',
                        event: 'exportExcelSfg',
                        authorized: 'sfg:export',
                    },
                     {
                         name: '导出',
                         event: 'sfgExport'
                     },

                    {
                        name: '删除',
                        event: 'deletesClicks',
                        authorized: 'sfg:del',
                    },
                    {
                        name:'下载二维码',
                        event:'downloadQrcords',
                    },
                    {
                        name: '图纸导入',
                        event: 'uploadFile',
                        authorized: 'productsMaintenance:print',
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('semiFinishedGoodsQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("semiFinishedGoodsQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                        authorized: 'sfg:edit',
                    },
                ]

            }
        },
        mounted() {
            this.list()
        },
        methods: {
            //导出
            sfgExport(){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let page = this.semiFinishedGoodsQuery.pageInfo.currentPage;
                let size = this.semiFinishedGoodsQuery.pageInfo.pageSize;
                this.semiFinishedGoodsQuery.pageInfo.currentPage = 1;
                this.semiFinishedGoodsQuery.pageInfo.pageSize = this.semiFinishedGoodsQuery.pageInfo.total;
                //TODO 分页
                sfgModel.findAllByQuery(this.semiFinishedGoodsQuery).then((data) => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "半成品" + excelName;

                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        var wb = xlsx.utils.table_to_book(table);
                        /* get binary string as output */
                        var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                        try {
                            fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }
                        this.semiFinishedGoodsQuery.pageInfo.pageSize = size;
                        this.semiFinishedGoodsQuery.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {
                })
            },
            //下载二维码
            downloadQrcords(){
                if(this.selected.length){
                    this.sfgIdQRCodes = []
                    this.sfgNoQRCodes = []
                    this.sfgNameQRCodes = []
                    this.selected.map(val=>{
                        this.sfgIdQRCodes.push(val.id)
                        this.sfgNoQRCodes.push(val.sfgNo)
                        this.sfgNameQRCodes.push(val.sfgName)
                    })
                    this.editFormVisibleQRBatch = true
                }else{
                    Notification({
                        message: this.GLOBAL.PROJECT_MSG.SELECTED_BATCH_PROJECT,
                        type: 'warning'
                    })
                }
            },
            uploadSuccess() {
            },
            // 上传错误
            uploadError(response, file, fileList) {
                console.log('上传失败，请重试！')
            },
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
                    clientModel.ax.post('/semiFinishedGoods/addSfgByImport', file, {
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
                            this.list()
                            return
                        }
                        loading.close()
                        this.list()
                        Notification({
                            message: '导入成功',
                            type: 'success'
                        })
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },
            list() {
                sfgModel.findAllByQuery(this.semiFinishedGoodsQuery).then((data) => {
                    this.sfgList = data.entity.list
                    this.semiFinishedGoodsQuery.pageInfo.total = data.entity.totalCount
                })
            },
            //点击小箭头展示半成品相关的原料
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                //根据半成品的id查询相关原料
                sfgModel.findSemiFinishedGoodsVoById(id).then((data) => {
                    this.subDatas = data.entity.sfgMatls;
                    console.log("this.subDatas", this.subDatas)
                })
            },
            getRowKeys(row) {
                return row.id;
            },
            /*sortChange(column) {

                //获取字段名称和排序类型
                let fieldName = column.prop;
                let sortingType = column.order;

                // console.log("fieldName: ",fieldName);
                // console.log("sortingType: ",sortingType);
                this.formInline.sortField = fieldName;
                this.formInline.sortType = sortingType
                this.list();
            },*/
            addClick() {
                this.mode = 'add'
                this.editFormVisible = true
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.list()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
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
                this.$confirm('此操作将永久删除勾选半成品, 是否继续?', '提示', {
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
                    sfgModel.deleteSfg(ids.join(";")).then((data) => {
                        if (data.status == 'failed') {
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
                        loading.close()
                        this.list()
                    })
                }).catch(() => {
                })

            },
            editClick(row) {
                sfgModel.findSemiFinishedGoodsVoById(row.id).then((data) => {
                    this.sfgInfoEdit = data.entity
                    this.mode = 'edit'
                    this.editFormVisible = true
                })

            },
            exportExcelSfg() {
                let fileName = '半成品模版.xlsx';
                model.exportFileExists(fileName).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    location.href = serviceConfig.baseURL + '/semiFinishedGoods/exportExcelSfg';
                    Notification({
                        message: '下载成功',
                        type: 'success'
                    })
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.semiFinishedGoodsQuery.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.semiFinishedGoodsQuery.pageInfo.currentPage = val
                this.list()
            },
            /*图片导入*/
            uploadFile() {
                if (this.selected.length == 0) {
                    this.$message('请选择要导入图纸的半成品');
                    return;
                }
                let ids = []
                let nos = []
                this.selected.forEach((row) => {
                    ids.push(row.id)
                    nos.push(row.sfgNo)
                })
                this.arrsfgIds = ids;
                this.arrsfgNos = nos

                this.dialogUploadVisiblePDF = true
            },
            findReceiptImg(row){
                this.srcList = row.bomPath.split(";").map(val=>{
                    return row.bomPathStr+val
                })
                this.checkImagesVisible = true
            },
            showCheckImages(row) {
                this.checkSelected = row;
                let imgs = []
                if (row.bomPathStr) {
                    row.bomPathStr.split(";").forEach((fileName) => {
                        imgs.push({
                            id: fileName,
                            src: fileName
                        });
                    })
                }
                row.checkImages = imgs;
                this.checkImagesVisible = true;
            },
            deleteBomInfo(row) {
                this.$confirm('确认删除图纸信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: "Loading",
                        spinner: "el-icon-loading",
                        background: "rgba(0, 0, 0, 0.7)"
                    });
                    let id = row.id;
                    sfgModel.deleteSfgBomPath(parseInt(id)).then(data => {
                        this.list();
                        loading.close();
                    }).catch(err => {
                    });

                }).catch(() => {

                })

                return false;
            },
            sfgEditFinish() {
                this.editFormVisible = false
                this.dialogUploadVisiblePDF = false
                this.list()
            },
            sfgEditCancel() {
                this.editFormVisible = false
                this.dialogUploadVisiblePDF = false
            },

            handleSelectionChangeForPL(selected) {
                this.selectedFoPL = selected
            },
        },
    }
</script>

<style scoped>

</style>
