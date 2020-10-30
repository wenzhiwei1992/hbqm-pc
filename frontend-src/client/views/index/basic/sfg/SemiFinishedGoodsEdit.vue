<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="editForm" v-if="editForm" :rules="editFormRules" ref="editForm"
                                 label-width="80px">
                            <el-form-item prop="sfgNo" label="编码">
                                <el-input v-model="editForm.sfgNo" placeholder="半成品编码" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="sfgName" label="名称">
                                <el-input v-model="editForm.sfgName" placeholder="半成品名称" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="rebarDiameter" label="钢筋直径">
                                <el-input v-model="editForm.rebarDiameter" placeholder="钢筋直径" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="rebarShape" label="钢筋形状">
                                <el-input v-model="editForm.rebarShape " placeholder="钢筋形状" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="sfgLen" label="下料长度">
                                <el-input v-model="editForm.sfgLen" placeholder="下料长度(mm)" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="sfgWt" label="下料重量">
                                <el-input v-model="editForm.sfgWt" placeholder="下料重量(kg)" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="unitId" label="计量单位">
                                <el-select v-model="editForm.unitId" style="width: 150px" placeholder="计量单位">
                                    <el-option
                                            v-for="item in optionsUnit"
                                            :key="item.id"
                                            :label="item.unitCNName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="categoryName" label="物料类别">
                                <el-input
                                        style="width: 150px"
                                        placeholder="原料类别"
                                        v-model="editForm.categoryName"
                                        @focus="showSelectTrees"
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="lossRatio" label="损耗比例">
                                <el-input v-model="editForm.lossRatio" placeholder="损耗比例(%)" style="width: 150px"></el-input>
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
                        :data="editForm.sfgMatls"
                        size="mini"
                        stripe
                        border
                        height="100%"
                >
                    <el-table-column
                            type="selection"
                            width="40">
                    </el-table-column>
                    <el-table-column
                            prop="materialStorageId"
                            label="原料编码"
                    >
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
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.num" @keyup.native="parseValue(scope.row)"
                                      placeholder="请输入内容"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            align="center"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar"></el-footer>
            <el-dialog
                    :visible.sync="showSelectTree"
                    width="340px"
                    append-to-body
            >
                <tree-info
                        :treeArr="treeArr"
                        :baseCheckedId="baseCheckedId"
                        :baseCheckParentId="baseCheckParentId"
                        @tree-edit-submit="checkoutStatusFinish"
                        @tree-edit-cancel="checkoutStatusCancel"
                ></tree-info>
            </el-dialog>
            <el-dialog :visible.sync="sfgSelectVisible" v-if="sfgSelectVisible" width="65%" append-to-body>
                <semi-finished-goods-select
                        :sfgMatlDtoList="sfgMatlDtoList"
                        @sfg-select-finish="sfgSelectFinish"
                        @sfg-select-cancel="sfgSelectCancel"
                ></semi-finished-goods-select>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import treeInfo from '../materialStorage/TreeInfo.vue'
    import materialStorageModel from "../../../../model/basicdata/materialStorage"
    import sfgModel from "../../../../model/basicdata/sfg"
    import semiFinishedGoodsSelect from './SemiFinishedGoodsSelect.vue'
    import categoryModel from "../../../../model/basicdata/category";
    import {Notification} from "element-ui";

    export default {
        name: "SemiFinishedGoodsEdit",
        components: {
            'tree-info': treeInfo,
            'semi-finished-goods-select': semiFinishedGoodsSelect
        },
        props: ['mode', 'sfgInfoEdit'],
        data() {
            return {
                //用于过滤已添加原料
                sfgMatlDtoList: [],
                editForm: {
                    sfgMatls: []
                },
                editFormRules: {
                    projectId: [
                        {required: true, message: '请输入项目名称', trigger: 'change'},
                    ],
                    sfgName: [
                        {required: true, message: '请输入半成品名称', trigger: 'blur'},
                    ],
                    sfgLen: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    sfgWt: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    rebarDiameter: [
                        {required: true, message: '请输入钢筋直径', trigger: 'blur'},
                    ],
                    rebarShape: [
                        {required: true, validator: checkPlateCount, message: '请输入钢筋形状', trigger: 'blur'},
                    ],
                    lossRatio: [
                        {required: true, validator: checkPlateCount, message: '请输入损耗比例', trigger: 'blur'},
                    ],
                    unitId: [
                        {required: true, message: '请输入单位', trigger: 'blur'},
                    ],
                    categoryName: [
                        {required: true,  message: '请输入物料类别', trigger: 'blur'},
                    ]


                },
                treeArr: '',
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                optionsUnit: [],
                sfgSelectVisible: false,
                materialStorageAdd: '',
                listMaterials: '',
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '保存',
                        event: 'save'
                    },
                ],
                searchButtonData: [],
                rowButtonData: [
                    {
                        name: '删除',
                        event: "deleteClick",
                    },
                ],
            }
            var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^\+?[1-9][0-9]*$/;

                if (value < 0) {
                    callback(new Error('必须大于0'));
                } else {
                    callback();
                }
            };
        },
        mounted() {
            this.sfgInfo()
        },
        methods: {
            addClick() {
                this.sfgMatlDtoList = this.editForm.sfgMatls
                this.sfgSelectVisible = true
            },
            save() {
                if (this.mode == 'add') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    sfgModel.addSfg(this.editForm).then((data) => {
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '新建成功',
                            type: 'success'
                        })
                        loading.close()
                        this.$emit('sfg-edit-finish')
                        // this.$emit('sfg-select-cancel')
                    })
                } else if (this.mode == 'edit') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    sfgModel.updateSfg(this.editForm).then((data) => {
                        if (data.status == 'failed') {
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
                        loading.close()
                        this.$emit('sfg-edit-finish')
                    })
                }

            },
            sfgInfo() {
                materialStorageModel.findAllByOrgIds().then((data) => {
                    this.optionsUnit = data.entity
                })
                categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
                //编辑操作执行，加载数据
                if (this.mode == 'edit') {
                    this.editForm = this.sfgInfoEdit
                }
            },
            deleteClick(scope) {
                this.editForm.sfgMatls.splice(scope.$index, 1)
            },
            showSelectTrees() {
                this.baseCheckedId.splice(0, this.baseCheckedId.length);
                this.baseCheckedId.push(this.editForm.categoryId);

                this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                this.baseCheckParentId.push(this.editForm.categoryId);

                this.showSelectTree = true
            },
            checkoutStatusFinish(id, name) {
                //子插件回传赋值
                this.editForm.categoryName = name;
                this.editForm.categoryId = id;
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            parseValue(row) {
                let value = row.num;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入4个小数
                row.num = value;
            },
            sfgSelectFinish(list) {
                for (var i = 0; i < list.length; i++) {
                    this.editForm.sfgMatls.push(list[i])
                }
                this.sfgSelectVisible = false
            },
            sfgSelectCancel() {
                this.sfgSelectVisible = false
            }
        }
    }
</script>

<style scoped>

</style>
