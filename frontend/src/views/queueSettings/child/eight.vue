<template>
    <div class="tabPane">
        <div class="eightBox">
            <el-result icon="success" title="进行中" v-if="this.status ==1">
                <template slot="subTitle">
                    <div class="result-sub-title">
                        <div class="public-time">队列发布时间：{{ publicTime || '--' }}</div>
                        <div>{{ subTitle }}</div>
                    </div>
                </template>
                <template slot="extra">
                    <el-button type="primary" @click="changeStatus('2')">关闭队列</el-button>
                </template>
            </el-result>
            <el-result title="待发布" v-if="this.status ==0">
                <template slot="icon">
                    <i class="el-icon-close-notification" style="font-size:60px;color:#e6a23c ;"></i>
                </template>
                <template slot="subTitle">
                    <div class="result-sub-title">
                        <!-- <div class="public-time">队列发布时间：{{ publicTime || '--' }}</div> -->
                        <div>{{ subTitle }}</div>
                    </div>
                </template>
                <template slot="extra">
                    <el-button type="primary" @click="changeStatus('1')">发布队列</el-button>
                </template>
            </el-result>
            <el-result icon="info" title="已关闭" v-if="this.status ==2">
                <template slot="subTitle">
                    <div class="result-sub-title">
                        <div class="public-time">队列发布时间：{{ publicTime || '--' }}</div>
                        <div>{{ subTitle }}</div>
                    </div>
                </template>
                <template slot="extra">
                    <el-button type="text" @click="changeStatus('1')">开启队列</el-button>
                </template>
            </el-result>
        </div>
    </div>
</template>
  
<script>
import cache from '@/plugins/cache'
import { infoQuery, researchStatus } from "@/api/info/config";
export default {
    props: {
        researchId: { type: String, default: null, },
    },
    data() {
        return {
            // subTitle: '请根据提示进行操作',
            subTitle: '队列发布后随访将立即生效，若需患者入组后统—开始随访，可待患者入组完成后再发布队列',
            publicTime: '',
            // 遮罩层
            loading: true,
            // 队列发布状态
            status: undefined,
        };
    },
    watch: {
        researchId: {
            immediate: true, // 监听到后，立即执行 handler方法
            handler(newVal, oldValue) {
                if (newVal != 0 && newVal != undefined) {
                    this.getStatus()
                }
            }
        }
    },
    computed: {

    },
    methods: {
        //获取队列详情
        getStatus() {
            const id = this.researchId;
            infoQuery(id).then(response => {
                cache.session.setJSON('researchStatus', response.data.researchStatus)
                this.status = response.data.researchStatus
                this.publicTime = response.data.publicTime
            })
        },
        /** 队列状态信息 */
        changeStatus(val) {
            this.$confirm(`${this.status == 0 ?
                '发布后将按照队列规则开始进行随访，患者导入若超过设置时间将在次日生效。'
                : '关闭后随访将不再纳入新患者，进行中的随访也将立即终止。'}`, '提示', {
                type: 'warning'
            }).then(() => {
                researchStatus(this.researchId, val).then(response => {
                    this.$modal.msgSuccess("修改成功");
                    this.loading = false;
                    this.getStatus()
                });
            }).catch(error => { })

        }
    }
};
</script>

<style lang="scss" scoped>
.eightBox {
    margin-top: 16vh;
}

.result-sub-title {
    line-height: 24px;
}

.public-time {
    margin-top: 8px;
    color: #606266;
    margin-bottom: 15px;
}

.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.el-tree {
    max-height: 400px;
    width: 100%;
    overflow-y: scroll;
}

.el-tree > .el-tree-node {
    display: inline-block;
    min-width: 100%;
}
</style>