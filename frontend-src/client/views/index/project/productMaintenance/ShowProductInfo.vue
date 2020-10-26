<template >
    <div class="el-dialog__body">

    <div style="float: left">
        <el-form :model="editFormShowProductInfo" :rules="eidtFormRules" v-if="editFormShowProductInfo" ref="editFormShowProductInfo"
                 label-width="80px">
            <div class="divProductAssociateds">
          <!--      <div class="divProductAssociated">
                    <el-form-item label="构件分类" prop="productCategoriesId">
                        <el-input v-model="editFormShowProductInfo.list" style="width: 137px"></el-input>
                    </el-form-item>
                </div>-->
                <div class="divProductAssociated">
                    <el-form-item label="构件类型" prop="productType">
<!--                        <el-input v-model="editFormShowProductInfo.productType.name" style="width: 137px"></el-input>-->
                    </el-form-item>
                </div>
                <div class="divProductAssociated">
                    <el-form-item label="项目名称" prop="project">
                        <el-input v-model="editFormShowProductInfo.project.projectName" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
                <div class="divProductAssociated">
                    <el-form-item label="模具名称" prop="mold">
                        <el-input v-model="editFormShowProductInfo.moldName" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
                <div class="divProductAssociated">
                    <el-form-item label="构件名称" prop="productName">
                        <el-input v-model="editFormShowProductInfo.productName" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
                <div class="divProductAssociated">
                    <el-form-item label="砼方量m³" prop="projectConcrete">
                        <el-input v-model="editFormShowProductInfo.projectConcrete" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
                <div class="divProductAssociated">
                    <el-form-item label="砼等级" prop="productGrade">
                        <el-input v-model="editFormShowProductInfo.productGrade" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
            </div>

            <div style="border: 1px lavender solid" id="p">
                <span style="color: deepskyblue">构件基础信息</span>
                <div class="pd">
                    <el-form-item label="构件厚度m" label-width="100px" prop="productThick" class="pInfo">
                        <el-input v-model="editFormShowProductInfo.productThick" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="构件长度m" label-width="100px" prop="productLen" class="pInfo">
                        <el-input v-model="editFormShowProductInfo.productLen" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="构件宽度m" label-width="100px" prop="productHeight" class="pInfo">
                        <el-input v-model="editFormShowProductInfo.productHeight" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="构件体积m³ " label-width="100px" prop="productVol" class="pInfo">
                        <el-input v-model="editFormShowProductInfo.productVol" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="构件面积m²" label-width="100px" prop="productAcreage" class="pInfo">
                        <el-input v-model="editFormShowProductInfo.productAcreage" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="构件重量t" label-width="100px" prop="productWt" class="pInfo">
                        <el-input v-model="editFormShowProductInfo.productWt" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
            </div>
            <div style="border: 1px lavender solid" id="lobe">
                <span style="color: deepskyblue">构件内叶信息</span>
                <div class="pdO1">
                    <el-form-item label="内叶厚度m" label-width="100px" prop="productThick" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productThick" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="内叶长度m" label-width="100px" prop="productLen" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productLen" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="内叶宽度m" label-width="100px" prop="productHeight" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productHeight" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="内叶体积m³ " label-width="100px" prop="productVol" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productVol" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="内叶重量t" label-width="100px" prop="productWt" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productWt" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
            </div>
            <div style="border: 1px lavender solid" id="pO">
                <span style="color: deepskyblue">构件外叶信息</span>
                <div class="pdO">
                    <el-form-item label="外叶厚度m" label-width="100px" prop="productOutThick" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productOutThick" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="外叶长度m" label-width="100px" prop="productOutLen" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productOutLen" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="外叶宽度m" label-width="100px" prop="productOutHeight" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productOutHeight" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="外叶体积m³" label-width="100px" prop="productOutVol" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productOutVol" style="width: 137px"></el-input>
                    </el-form-item>
                    <el-form-item label="外叶重量t" label-width="100px" prop="productOutWt" class="pOInfo">
                        <el-input v-model="editFormShowProductInfo.productOutWt" style="width: 137px"></el-input>
                    </el-form-item>
                </div>
            </div>
        </el-form>
    </div>
    </div>
</template>

<script>
    export default {
        name: "ShowProductInfo",
        props: ['editFormShowProductInfo'],
        data() {
            return {
                eidtFormRules: null,
            }
        },
        mounted() {
            if (this.editFormShowProductInfo.productType.name == '预制混凝土外挂墙板' || this.editFormShowProductInfo.productType.name == '预制剪力墙板') {
                document.getElementById("pO").style.display = "block"
                document.getElementById("lobe").style.display = "block"
                document.getElementById("p").style.display = "none"
            } else {
                document.getElementById("pO").style.display = "none"
                document.getElementById("lobe").style.display = "none"
                document.getElementById("p").style.display = "block"
            }
        },
        methods: {},

    }
</script>

<style scoped="scoped">
    .el-dialog__body{
        float: left;
        background-color: white;
    }
    #p {
        float: left;
    }

    #p .pInfo {
        display: block;
        float: left;
    }

    .pd {
        float: left;
        margin-top: 30px;
    }

    #pO {
        float: left;
        margin-top: 20px;
        display: none;
    }

    #pO .pOInfo {
        display: block;
        float: left;
    }

    #lobe {
        float: left;
        display: none;
    }

    #lobe .pOInfo {
        display: block;
        float: left;
    }

    .pdO {
        float: left;
        margin-top: 30px;
    }

    .pdO1 {
        float: left;
    }


    divMaterialButtons {
        float: left;
    }

    .divProductAssociateds {
        float: left;
    }

    .divProductAssociated {
        float: left;
    }
</style>
