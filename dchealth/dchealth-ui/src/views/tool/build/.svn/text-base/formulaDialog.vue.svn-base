<template>
    <div>
        <el-dialog v-bind="$attrs" width="60%" top="15vh!important" :close-on-click-modal="false" v-on="$listeners"
            @close="close" destroy-on-close :show-close="false">
            <div class="current_container">
                <div class="dialog_container">
                    <div class="main_tabs">
                        <div class="main_tab" v-for="item in ruleCodes" :key="item.value" :label="item.label"
                            @click="handleClick(item)" :class="number == item.value ? 'blue' : '' ">
                            {{ item.label }}
                        </div>
                    </div>
                    <div v-if="number === '1'">
                        <el-tree :data="list" node-key="formId" :props="defaultProps" @node-click="changeHandle">

                        </el-tree>
                    </div>
                    <div v-if="number === '2'">
                        <el-collapse v-model="activeTag" accordion>
                            <el-collapse-item title="常用" name="1">
                                <el-tree :data="listFunction" node-key="showProperty" :default-expanded-keys="[1]"
                                    :props="defaultPropsFunction" @node-click="changeHandle">
                                </el-tree>
                            </el-collapse-item>
                            <el-collapse-item title="逻辑" name="2">

                            </el-collapse-item>
                            <el-collapse-item title="文字" name="3">

                            </el-collapse-item>
                            <el-collapse-item title="数字" name="4">

                            </el-collapse-item>
                            <el-collapse-item title="时间" name="4">

                            </el-collapse-item>
                        </el-collapse>
                    </div>
                </div>

                <!-- <el-tabs
          type="border-card"
          v-model="activeName"
          @tab-click="handleClick"
        >
          <el-tab-pane label="问题" name="first">
            <el-tree
              :data="list"
              node-key="showProperty"
              :default-expanded-keys="[1]"
              :props="defaultProps"
              @node-click="changeHandle"
            >
            </el-tree>
          </el-tab-pane>
          <el-tab-pane label="函数" name="second">
            
            <el-collapse v-model="activeTag" accordion>
                <el-collapse-item title="常用" name="1">
                    <el-tree
                    :data="listFunction"
                    node-key="showProperty"
                    :default-expanded-keys="[1]"
                    :props="defaultPropsFunction"
                    @node-click="changeHandle"
                    >
                    </el-tree>
                </el-collapse-item>
                <el-collapse-item title="逻辑" name="2">
                    
                </el-collapse-item>
                <el-collapse-item title="文字" name="3">
                    
                </el-collapse-item>
                <el-collapse-item title="数字" name="4">
                    
                </el-collapse-item>
                <el-collapse-item title="时间" name="4">
                    
                </el-collapse-item>
            </el-collapse>
          </el-tab-pane>
        </el-tabs> -->
                <codemirror ref="newCm" v-model="sql" class="codemirror-textarea" :options="cmOptions"
                    @ready="onCmReady">
                </codemirror>
                <!-- <codemirror v-show="false" ref="newCms" v-model="textData" class="codemirror-textarea"
                    :options="cmOptions" @ready="onCmReady">
                </codemirror> -->
            </div>
            <div slot="footer">
                <el-button type="primary" @click="setOptions">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import 'codemirror/mode/vue/vue.js'
    // 主题css
    import 'codemirror/theme/solarized.css'
    // require active-line.js
    import 'codemirror/addon/selection/active-line.js'
    // closebrackets
    import 'codemirror/addon/edit/closebrackets.js'
    // 主题样式（我直接用了纯白色的，看着比较舒服）
    import "codemirror/theme/rubyblue.css";
    // 括号显示匹配
    import "codemirror/addon/edit/matchbrackets";
    import "codemirror/addon/selection/active-line";
    export default {
        props: ["currentItem", "drawingList", ],
        data() {
            return {
                activeName: "first",
                list: [],
                defaultProps: {
                    children: "options",
                    label: "label",
                },
                defaultPropsFunction: {
                    children: "options",
                    label: "name",
                },
                sql: "",
                textData: '',
                cmOptions: {
                    mode: "text/javascript",
                    theme: "base16-light", // 主题样式
                    tabSize: 4, // tabsize默认为4
                    styleActiveLine: true,
                    lineNumbers: false, // 代码行数字
                    autofocus: true, //自动聚焦
                    matchBrackets: true, // 匹配结束符号，比如"]、}"
                    autoCloseBrackets: true, // 自动闭合符号
                    styleActiveLine: true, // 显示选中行的样式
                    line: false,
                    gutters: [
                        "CodeMirror-linenumbers",
                        "CodeMirror-foldgutter",
                        "CodeMirror-lint-markers",
                    ],
                    extraKeys: {
                        "Ctrl": "autocomplete"
                    }, //自动提示配置 
                    lineWrapping: true, // 自动换行
                },
                ruleCodes: [{
                        label: '问题',
                        value: '1'
                    },
                    {
                        label: '函数',
                        value: '2'
                    }
                ],
                number: '1',
                activeTag: '1',
                listFunction: [{
                        name: 'SUM',
                        id: '1',
                        label: 'SUM()',
                        type: 'function'
                    },
                    {
                        name: 'MAX',
                        id: '2',
                        label: 'MAX()',
                        type: 'function',
                        remarks: '返回所有参与字段中的最大值'
                    },
                    {
                        name: 'MIN',
                        id: '3',
                        label: 'MIN()',
                        type: 'function',
                        remarks: '返回所有参与字段中的最小值'
                    },
                    {
                        name: 'AVERAGE',
                        id: '4',
                        label: 'AVERAGE()',
                        type: 'function'
                    },
                    {
                        name: 'ROUND',
                        id: '5',
                        label: 'ROUND()',
                        type: 'function',
                        remarks: '四舍五入为最接近的整数'
                    },
                    {
                        name: 'CONCATENATE',
                        id: '6',
                        label: 'CONCATENATE()',
                        type: 'function'
                    },
                ]
            };
        },
        computed: {},
        watch: {
            drawingList: {
                handler(newVal, oldVal) {
                    let objVal = {}
                    this.drawingList.forEach(item => {
                        this.$set(item, 'label', item.propertyName)
                        // console.log(item)
                    });
                }
            },
            currentItem: {
                handler(newVal, oldVal) {
                    let index = this.drawingList.findIndex(f => f.propertyCode == newVal.propertyCode)
                    this.list = this.drawingList
                    // this.list = this.drawingList.filter((f, formIndex) => formIndex < index);
                    // console.log('index',index,this.list)
                },
                deep: true
            }
        },
        created() {
            // console.log('mathjax', this.drawingList)
            // this.onCmReady()
        },
        methods: {
            close() {
                this.$emit("update:visible", false);
            },
            // 确定保存options
            setOptions() {
                console.log('sql', this.sql)
                console.log('text', this.textData)
                this.close();
            },
            handleClick(item) {
                this.number = item.value
            },
            onCmReady(cm) {
                // 设置代码编辑框宽和高
                this.$refs.newCm.codemirror.setSize("-webkit-fill-available", "100%")
            },
            changeHandle(data, node) {
                const {
                    line,
                    ch,
                } = this.$refs.newCm.codemirror.getCursor()
                this.$refs.newCm.codemirror.replaceSelection(data.label, 'start');
                console.log(data.propertyCode, 'q')
                if(data.type == 'function') {
                    this.$refs.newCm.codemirror.setCursor({
                        line,
                        ch: ch + data.label.length - 1,
                    })
                } else {
                    console.log(line)

                    this.$refs.newCm.codemirror.setCursor({
                        line,
                        ch: ch + data.label.length,
                    })
                }
                this.$refs.newCm.codemirror.focus();
                // let pos1 = this.$refs.newCm.codemirror.getCursor()
                // let pos2 = {
                //     line: pos1.line,
                //     ch: pos1.ch,
                // }
                // this.$refs.newCm.codemirror.replaceRange(data.label, pos2)
                // this.$refs.newCm.codemirror.getSelection()
                // this.$refs.newCm.focus()
                
                // if (data.type == 'function') {
                //     this.$refs.newCms.codemirror.replaceSelection(data.label, 'start');
                // } else {
                //     this.$refs.newCm.codemirror.replaceSelection(data.label, 'start');
                // }
                
                // this.$refs.newCms.codemirror.setCursor({
                //     line,
                //     ch: ch + 4
                // })
                
                // this.$refs.newCms.codemirror.focus();
            },
            getListData() {
                console.log(this.drawingList)
            }

        },
    };
</script>
<style lang="scss" scoped>
    .codemirror-textarea {
        font-family: Arial, monospace;
        font-size: 20px;
    }

    .CodeMirror * {
        font-family: Arial, monospace;
        font-size: 32px;
    }

    .dialog_container {
        width: 24%;
        border-right: 1px solid #dde5ed;
        padding: 10px 15px;
        overflow-y: auto;

        .main_tabs {
            display: flex;
            flex: 0 0 33.3333%;

            .main_tab {
                width: 100%;
                height: 30px;
                border: 1px solid #4797c7;
                text-align: center;
                line-height: 30px;
            }
        }

        .blue {
            background-color: #2e5c9f;
            color: #fff;
        }
    }

    ::v-deep .el-dialog__body {
        height: 60vh;
        overflow-y: auto;
    }

    .current_container {
        width: 100%;
        height: 100%;
        display: flex;
        border: 1px solid #dde5ed;

        .el-tabs {
            width: 24%;
            height: 100%;
            overflow-y: auto;
        }
    }
</style>