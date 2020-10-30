<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                id="entity"
                                :model="entity"
                                ref="entity"
                        >
                            <el-form-item prop="projectId">
                                <el-select
                                        v-model="entity.projectId"
                                        style="width: 150px"
                                        placeholder="项目"
                                        filterable
                                        clearable
                                        @change="changeProject"
                                >
                                    <el-option
                                            v-for="item in arrProject"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="buildId">
                                <el-select
                                        v-model="entity.buildId"
                                        @change="changeBuilding"
                                        placeholder="楼栋"
                                        :disabled="entity.projectId==''"
                                        style="width: 150px;"
                                        clearable
                                        filterable
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.id"
                                            v-for="item in buildings"
                                            :label="item.buildCode"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="floorId">
                                <el-select
                                        v-model="entity.floorId"
                                        :disabled="entity.buildId==''"
                                        clearable
                                        filterable
                                        style="width: 150px;"
                                        placeholder="楼层"
                                >
                                    <el-option
                                            v-for="item in floors"
                                            :key="item.id"
                                            :label="item.floorNum"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item  prop="productTypeId">
                                <el-select v-model="entity.productTypeId" filterable
                                           clearable placeholder="工程部位">
                                    <el-option
                                            v-for="item in arrProductType"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>


                            <el-form-item prop="productGrade">
                                <el-input placeholder="强度等级" v-model="entity.productGrade"></el-input>
                            </el-form-item>


                            <el-form-item prop="checkDate">
                                <el-date-picker
                                        v-model="entity.checkDate"
                                        type="date"
                                        format="yyyyMMdd"
                                        value-format="yyyyMMdd"
                                        clearable
                                        placeholder="选择报告日期">
                                </el-date-picker>
                            </el-form-item>

                        </el-form>
                    </el-col>
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
                        :data="entities"
                        @selection-change="handleSelectionChange"
                        :height="GLOBAL.tabelHeight"
                        style="width:100%"
                        header-row-class-name="header-row"
                        size="mini"
                        stripe
                        border
                        tooltip-effect="light"
                        row-key="id"
                >
                    <el-table-column
                            type="selection"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="productTypeName"
                            label="工程部位"
                            align="center"
                    >
                    </el-table-column>


                    <el-table-column
                            prop="productGrade"
                            label="强度等级"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="productConcrete"
                            label="生产方量"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="checkDate"
                            label="报告日期"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            label="检测报告"
                            align="center"
                            width="150"
                    >

                        <template slot-scope="scope">
                            <a v-if="scope.row.checkImages" @click="findReceiptImg(scope.row)">查看图片</a>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="remark"
                            label="要求"
                            align="center"
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


        <el-dialog :title="mode=='add'?'新建':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="50%" top="10px">
            <model-edit
                    :editForm="editForm"
                    :mode="mode"
                    :arrProductType="arrProductType"
                    :arrProject="arrProject"
                    @model-edit-finish="modelEditFinish"
                    @model-edit-cancel="modelEditCancel"

            ></model-edit>
        </el-dialog>
        <image-viewer v-if="checkImagesVisible" :srcList="srcList" @imageViewClose="checkImagesVisible=false" />
    </div>


</template>
<script>
    import model from '../../../../model/model'
    import CompressiveReportEdit from './CompressiveReportEdit.vue'
    import floorModel from "../../../../model/project/floor";
    import buildingModel from '../../../../model/project/building'
    import projectModel from '../../../../model/project/project'
    import productTypeModel from '../../../../model/basicdata/productType'
    import moment from 'moment'
    import imageViewer from '@/components/imageViewer/index.vue'

    export default {
        components: {
            'model-edit': CompressiveReportEdit,
            imageViewer
        },
        data() {
            return {
                //表查询
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                entity: {
                    projectId: null,
                    productTypeId: null,
                    productGrade: null,
                    checkDate: null,
                    buildId: null,
                    floorId: null,
                },
                entities: [],
                //行操作
                selected: [],
                arrProductType: [],
                buildings: [],
                floors: [],
                arrProject: [],
                //编辑表单
                mode: '',
                editFormVisible: false,
                editForm: "",
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "list"
                    }
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
                ],
                checkImagesVisible:false,
                srcList:[]
            }
        },
        computed: {},
        mounted() {
            projectModel.projectListS().then((data) => {
                this.arrProject = data.entity
            })

            productTypeModel.getOrgProductTypes().then((data) => {
                this.arrProductType = data.entity
            })
            this.list()
        },
        methods: {

            changeProject() {
                buildingModel.findByProjectId(this.entity.projectId).then(data => {
                    this.buildings = data.entity
                    this.entity.buildId = ''
                    this.entity.floorId = ''
                    this.floors = []
                })
            },
            changeBuilding(id) {
                this.entity.floorId = ''
                this.floors = []
                if (id == undefined || id == '') {
                    return
                }
                let building = this.buildings.find(b => b.id == id)
                floorModel.findByBuildingIdAndProjectId(id, building.projectId).then((data) => {
                    this.floors = data.entity
                    this.entity.floorId = ''
                })
            },

            getTooltip(row) {
                // console.log(row.checkImages)
                if (row.checkImages && row.checkImages.indexOf('\\') != -1) {
                    return row.checkImages.split('\\')[row.checkImages.split('\\').length - 1]
                } else if (row.checkImages && row.checkImages.indexOf('/') != -1) {
                    return row.checkImages.split('/')[row.checkImages.split('/').length - 1]
                }
                return row.checkImages;
            },
            //表查询
            list() {
                model.list('compressiveReport', this.entity, this.pageInfo).then(data => {
                    this.entities = data.entity.list;
                    this.pageInfo.total = data.entity.totalCount
                })
            },
            query() {
                this.pageInfo.currentPage = 1
                this.list()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.currentPage = 1
                this.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.list()
            },
            momentFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return cellValue
                }

            },
            //编辑表单
            modelEditFinish() {
                this.editFormVisible = false
                this.list()
            },
            modelEditCancel() {
                this.editFormVisible = false
            },
            //行操作
            addClick() {
                this.mode = 'add'
                this.editForm = {
                    projectId: '',
                    productTypeId: '',
                    productGrade: '',
                    productConcrete: '',
                    checkDate: '',
                    checkTime: '',
                    checkImages: '',
                    remark: '',
                    buildId: '',
                    floorId: '',
                }
                this.editFormVisible = true
            },
            editClick(row) {
                this.mode = 'update'
                this.editForm = this.Clone(row);
                this.editFormVisible = true
            },
            deleteClick(row) {
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    model.delete('compressiveReport', row.id).then(data => {
                        this.$store.commit('endLoading')
                        this.list()
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
                this.$confirm('此操作将永久删除勾选记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    model.deletes('compressiveReport', ids.join(";")).then(() => {
                        this.$store.commit('endLoading')
                        this.list()
                    })
                }).catch(() => {
                })

            },
            findReceiptImg(row){
              this.srcList = row.checkImages.split(",").map(val=>{
                return row.netCheckImages + val
              })
              this.checkImagesVisible = true
            }
        },

    }
</script>
<style>
    .el-table th, .el-table tr.header-row {
        background-color: #f5f7fa;
        font-size: 1.1em;
    }

    #entity .el-form-item {
        margin-bottom: 5px;
    }
</style>