<template>
    <el-table :data="tableData" min-height="200px" class="table_list" :key="isShowNo" :show-header="showHeader" :border="isBorder">
        <el-table-column type="index" width="50" label="No" v-if="isShowNo">
        </el-table-column>
        <el-table-column prop="name" width="230" label="检查项目">
            <template slot-scope="scope">{{ scope.row.name}}</template>
        </el-table-column>
        <el-table-column prop="value" label="检测结果" align="center">
            <template slot-scope="scope">
                <span v-if="scope.row.value">{{ scope.row.value | nullFormat}}</span>
                <span class="href_info" v-else-if="scope.row.href" @click="showPDFContent(scope.row)">
                    {{ scope.row.href | nullFormat}}</span>
                <template v-if="scope.row.tip=='H'||scope.row.tip=='L'">
                    <i class="value_icon el-icon-top" v-if="scope.row.tip=='H'"></i>
                    <i class="value_icon el-icon-bottom" v-if="scope.row.tip=='L'"></i>
                </template>
            </template>
        </el-table-column>
        <el-table-column prop="otherInfo" v-if="isShowOtherInfo" label="生物参考区间" align="center">
            <template slot-scope="scope">
                <template v-if="scope.row.rangeValue">{{ scope.row.rangeValue}}</template>
                <template v-else>
                    {{ scope.row.minValue}}
                    <span v-if="scope.row.minValue&&scope.row.maxValue">-</span>
                    {{ scope.row.maxValue}}
                </template>
            </template>
        </el-table-column>
        <el-table-column prop="otherInfo" label="单位" align="center" v-if="isShowUnit">
            <template slot-scope="scope">
                {{ scope.row.unit }}
            </template>
        </el-table-column>
    </el-table>

</template>

<script>
import { getCheckPDFInfo } from '@/api/patient/index'
export default {
    name: "patientDetailItem",
    components: {},
    props: ['listData', 'isShowNo', 'showHeader', 'isBorder'],
    data() {
        return {
            tableData: [],
            isShowOtherInfo: true,
            isShowUnit: true,
            imageUrl: '',
            isShowImage: false,
        };
    },
    created() { },
    watch: {
        listData: {
            immediate: true,
            deep: true,
            handler(newVal) {
                if (newVal) {
                    this.tableData = JSON.parse(JSON.stringify(newVal))
                    if (this.tableData.findIndex(f => f.minValue || f.minValue == 0 || f.maxValue || f.maxValue == 0 ||
                        f.rangeValue || f.rangeValue == 0) > -1) {
                        this.isShowOtherInfo = true
                    } else {
                        this.isShowOtherInfo = false
                    }
                    if (this.tableData.findIndex(f => f.unit) > -1) {
                        this.isShowUnit = true
                    } else {
                        this.isShowUnit = false
                    }
                }
            }
        }
    },
    methods: {
        showPDFContent(event) {
            let params = {
                pdfReport: event.href
            }
            getCheckPDFInfo(params).then(res => {
                let blob = new Blob([res], { type: "application/pdf" });
                //转换为url对象
                let url = window.URL.createObjectURL(blob);
                window.open(url)
            })
        }
    }
};
</script>
<style lang="scss" scoped>
.table_list {
    margin-bottom: 20px;
    margin-top: 10px;
    .value_icon {
        color: #f56c6c;
    }
    .href_info {
        cursor: pointer;
        color: #1890ff;
    }
}
</style>