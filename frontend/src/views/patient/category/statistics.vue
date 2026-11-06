<template>
    <div class="template-container">
        <div class="statisticsTop">
            <div class="statisticsNav" :class='currentIndex==index?"active":""' :key="item.id"
                v-for="(item,index) in listTitle" @click="changTab(index)">
                <i :class="item.icon"></i>
                <span>{{item.title}}</span>
            </div>
        </div>
        <el-scrollbar class="content-scrollbar">
            <div class="statisticsList" v-show="(currentIndex == 0)">
                <list-data :user-list="userList" :id="articleId" :total="total" @serachList="searchtableList">
                </list-data>
            </div>
            <div class="statisticsList" v-show="(currentIndex == 1)">
                <tab-data :statisiucs-list-data="statisiucsListData" v-if="statisiucsListData.length"></tab-data>
                <div v-else class="noCount_text">
                    {{errorMessage}}
                </div>
            </div>
            <div class="statisticsList" v-show="(currentIndex == 2)">
                <cross-analysis :analysis-data="analysisData" v-if="statisiucsListData.length">
                </cross-analysis>
                <div v-else class="noCount_text">
                    {{errorMessage}}
                </div>
            </div>
        </el-scrollbar>
    </div>
</template>

<script>
//按需引入
import listData from './children/listChild'
import tabData from './children/tabChild'
import CrossAnalysis from './children/crossAnalysis'
import {
    listStaticalData,
    questionnaireAnsweredList
} from "@/api/patient/config";
export default {
    name: "statisticsList",
    components: {
        listData,
        tabData,
        CrossAnalysis
    },
    data() {
        return {
            // 遮罩层
            currentIndex: 0,
            statisiucsListData: [], //问卷统计列表
            optionData: [],
            listTitle: [
                { title: '问卷列表', id: 1, icon: 'el-icon-s-grid' },
                { title: '统计分析', id: 2, icon: 'el-icon-s-data' },
                { title: '交叉分析', id: 3, icon: 'el-icon-s-help' },
            ],
            articleId: '',
            listQueryParams: {
                pageNum: 1,
                pageSize: 10,
                title: undefined, // 标题
                articleId: '',
            },
            countQueryParams: {
                pageNum: 1,
                pageSize: 1000,
                articleId: '',
            },
            userList: [],
            total: 0,
            errorMessage: '',
            analysisData: []
        };
    },
    computed: {},
    watch: {
        '$route.query'(newVal) {
            if (this.$route.path == '/patient/category/statistics') {
                this.articleId = newVal.id;
                this.searchtableList()
            }
        },
    },
    created() {
        this.articleId = this.$route.query.id;
        // this.changTab(2)
        this.searchtableList()
    },
    methods: {
        changTab(index) {
            this.currentIndex = index;
        },
        // 请求问卷数据
        searchtableList(event) {
            if (event) {
                this.listQueryParams = event;
            }
            this.getTableList();
            this.getCountData();

        },
        /** 查询统计分析列表 */
        getTableList() {
            this.loading = true;
            this.listQueryParams['articleId'] = this.articleId;
            questionnaireAnsweredList(this.listQueryParams).then(response => {
                this.userList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        /** 查询用户列表 */
        getCountData() {
            this.loading = true;
            this.countQueryParams.articleId = this.articleId
            listStaticalData(this.countQueryParams).then((response) => {
                let responseData = response.rows;
                if (responseData && responseData.length) {
                    responseData.forEach(item => {
                        item['isShowTable'] = true
                    })
                    responseData.reverse()
                }
                this.statisiucsListData = responseData;
                let list = []
                const typeList = ['Radio', 'Checkbox', 'Select']
                if (responseData && responseData.length) {
                    list = responseData.filter(info => typeList.indexOf(info.propertyType) > -1)
                }
                this.analysisData = list
                this.errorMessage = response.errorMessage
                this.loading = false;
            });
        },
    },
};
</script>
<style lang="scss" scoped>
.template-container {
    display: flex;
    width: 100%;
    padding-top: 0;
    background-color: #f8f8f8;
    display: flex;
    flex-direction: column;

    .statisticsTop {
        display: flex;
        flex-direction: row;
        background: #fff;
        justify-content: center;

        .statisticsNav {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 10px 20px;
            color: #888;
            cursor: pointer;

            i {
                font-size: 40px;
            }

            span {
                font-size: 14px;
                line-height: 2;
            }
        }

        .statisticsNav.active {
            color: rgb(64, 158, 255);
            background: rgba(64, 158, 255, 0.1);
        }
    }
}

.statisticsList {
    width: 1000px;
    margin: 20px auto;
    padding: 20px 30px;
    background: #fff;

    .statisticsTitle {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: center;
        font-size: 24px;
        line-height: 1.5;
        font-weight: bold;
    }

    .noCount_text {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 300px;
        color: #666;
        font-size: 18px;
    }
}

.content-scrollbar {
    height: calc(100vh - 180px);
}
</style>