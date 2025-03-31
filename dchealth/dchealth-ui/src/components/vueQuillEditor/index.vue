<template>
    <div>
        <el-upload :action="uploadUrl" :before-upload="handleBeforeUpload" :on-success="handleUploadSuccess"
            :on-error="handleUploadError" name="file" :show-file-list="false" :headers="headers" style="display: none"
            ref="upload" v-if="this.type == 'url'">
        </el-upload>
        <div class="in-editor-wrapper">
            <div class="in-editor ql-editor"></div>
        </div>
    </div>
</template>

<script>
import { getToken } from "@/utils/auth";
// 引入原始组件
import Quill from "quill";
// import QuillBetterTable from "quill-better-table";

// 引入核心样式和主题样式
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
// import "quill-better-table/dist/quill-better-table.css";
// Quill.register({
//     "modules/better-table": QuillBetterTable
// },
//     true
// );
const titleConfig = [
    { Choice: '.ql-insertMetric', title: '跳转配置' },
    { Choice: '.ql-bold', title: '加粗' },
    { Choice: '.ql-italic', title: '斜体' },
    { Choice: '.ql-underline', title: '下划线' },
    { Choice: '.ql-header', title: '段落格式' },
    { Choice: '.ql-strike', title: '删除线' },
    { Choice: '.ql-blockquote', title: '块引用' },
    { Choice: '.ql-code', title: '插入代码' },
    { Choice: '.ql-code-block', title: '插入代码段' },
    { Choice: '.ql-font', title: '字体' },
    { Choice: '.ql-size', title: '字体大小' },
    { Choice: '.ql-list[value="ordered"]', title: '编号列表' },
    { Choice: '.ql-list[value="bullet"]', title: '项目列表' },
    { Choice: '.ql-direction', title: '文本方向' },
    { Choice: '.ql-header[value="1"]', title: 'h1' },
    { Choice: '.ql-header[value="2"]', title: 'h2' },
    { Choice: '.ql-align', title: '对齐方式' },
    { Choice: '.ql-color', title: '字体颜色' },
    { Choice: '.ql-background', title: '背景颜色' },
    { Choice: '.ql-image', title: '图像' },
    { Choice: '.ql-video', title: '视频' },
    { Choice: '.ql-link', title: '添加链接' },
    { Choice: '.ql-formula', title: '插入公式' },
    { Choice: '.ql-clean', title: '清除字体格式' },
    { Choice: '.ql-script[value="sub"]', title: '下标' },
    { Choice: '.ql-script[value="super"]', title: '上标' },
    { Choice: '.ql-indent[value="-1"]', title: '向左缩进' },
    { Choice: '.ql-indent[value="+1"]', title: '向右缩进' },
    { Choice: '.ql-header .ql-picker-label', title: '标题大小' },
    { Choice: '.ql-header .ql-picker-item[data-value="1"]', title: '标题一' },
    { Choice: '.ql-header .ql-picker-item[data-value="2"]', title: '标题二' },
    { Choice: '.ql-header .ql-picker-item[data-value="3"]', title: '标题三' },
    { Choice: '.ql-header .ql-picker-item[data-value="4"]', title: '标题四' },
    { Choice: '.ql-header .ql-picker-item[data-value="5"]', title: '标题五' },
    { Choice: '.ql-header .ql-picker-item[data-value="6"]', title: '标题六' },
    { Choice: '.ql-header .ql-picker-item:last-child', title: '标准' },
    { Choice: '.ql-size .ql-picker-item[data-value="small"]', title: '小号' },
    { Choice: '.ql-size .ql-picker-item[data-value="large"]', title: '大号' },
    { Choice: '.ql-size .ql-picker-item[data-value="huge"]', title: '超大号' },
    { Choice: '.ql-size .ql-picker-item:nth-child(2)', title: '标准' },
    { Choice: '.ql-align .ql-picker-item:first-child', title: '居左对齐' },
    { Choice: '.ql-align .ql-picker-item[data-value="center"]', title: '居中对齐' },
    { Choice: '.ql-align .ql-picker-item[data-value="right"]', title: '居右对齐' },
    { Choice: '.ql-align .ql-picker-item[data-value="justify"]', title: '两端对齐' }
]
export default {
    name: "inEditor",
    props: {
        // 用于双向绑定
        value: String,
        /* 类型（base64格式、url格式） */
        type: {
            type: String,
            default: "url",
        },
    },
    data() {
        return {
            // 待初始化的编辑器
            editor: null,
            uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
            headers: {
                Authorization: "Bearer " + getToken()
            },
            // 配置参数
            options: {
                theme: "snow",
                modules: {
                    // 工具栏的具体配置
                    toolbar: {
                        container: [
                            ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
                            [{
                                color: []
                            }, {
                                background: []
                            }], // 字体颜色、字体背景颜色
                            [{
                                list: "ordered"
                            }, {
                                list: "bullet"
                            }], // 有序、无序列表
                            [{
                                size: ["small", false, "large", "huge"]
                            }], // 字体大小
                            ["clean"], // 清除文本格式
                            ["image"], // 链接、图片、视频
                            // [{
                            //     table: "TD"
                            // }]
                        ],
                        // handlers: {
                        //     table: function () {
                        //         //默认插入的表格行列数
                        //         this.quill.getModule("better-table").insertTable();
                        //     }
                        // }
                    },
                    // "better-table": {
                    //     operationMenu: {
                    //         items: {
                    //             unmergeCells: {
                    //                 text: "Another unmerge cells name"
                    //             },
                    //             insertColumnRight: {
                    //                 text: "右边插入一列"
                    //             },
                    //             insertColumnLeft: {
                    //                 text: "左边插入一列"
                    //             },
                    //             insertRowUp: {
                    //                 text: "上边插入一行"
                    //             },
                    //             insertRowDown: {
                    //                 text: "下边插入一行"
                    //             },
                    //             mergeCells: {
                    //                 text: "合并单元格"
                    //             },
                    //             unmergeCells: {
                    //                 text: "拆分单元格"
                    //             },
                    //             deleteColumn: {
                    //                 text: "删除列"
                    //             },
                    //             deleteRow: {
                    //                 text: "删除行"
                    //             },
                    //             deleteTable: {
                    //                 text: "删除表格"
                    //             }
                    //         },
                    //         // background: {
                    //         //     color: "#333"
                    //         // },
                    //         // color: {
                    //         //     colors: ["green", "red", "yellow", "blue", "white"],
                    //         //     text: "background:"
                    //         // }
                    //     }
                    // },
                    // keyboard: {
                    //     bindings: QuillBetterTable.keyboardBindings
                    // }
                },
                placeholder: "请输入内容 ..."
            }
        };
    },
    watch: {
        // 监听外部值的传入，用于将值赋予编辑器
        value(val) {
            // 如果编辑器没有初始化，则停止赋值
            if (!this.editor) {
                return;
            }

            // 获取编辑器当前内容
            let content = this.editor.root.innerHTML;

            // 外部传入了新值，而且与当前编辑器的内容不一致
            if (val && val !== content) {
                // 将外部传入的HTML内容转换成编辑器识别的delta对象
                let delta = this.editor.clipboard.convert({
                    html: val
                });

                // 编辑器的内容需要接收delta对象
                this.editor.setContents(delta);
            }
        }
    },
    mounted() {
        // 初始化编辑器
        this._initEditor();
        this.$nextTick(() => {
            this.initTitle();
        });
    },
    methods: {
        initTitle() {
            for (let item of titleConfig) {
                let tip = document.querySelector('.my_editor .ql-snow .ql-formats ' + item.Choice)
                if (!tip) continue
                tip.setAttribute('title', item.title)
            }
        },
        // 初始化编辑器
        _initEditor() {
            // 获取编辑器的DOM容器
            let editorDom = this.$el.querySelector(".in-editor");

            // 初始化编辑器
            this.editor = new Quill(editorDom, this.options);

            // 双向绑定
            // this.editor.on("text-change", () => {
            //     this.$emit("input", this.editor.root.innerHTML);
            // });
            if (this.type == 'url') {
                let toolbar = this.editor.getModule("toolbar");
                toolbar.addHandler("image", (value) => {
                    this.uploadType = "image";
                    if (value) {
                        this.$refs.upload.$children[0].$refs.input.click();
                    } else {
                        this.editor.format("image", false);
                    }
                });
            }
            this.editor.root.addEventListener("paste", this.pastHandle.bind(this), true);
            // this.editor.root.addEventListener("drop", this.dropFilesHandle.bind(this), true);
            // this.editor.root.innerHTML = this.currentValue
            // this.editor.pasteHTML(this.currentValue);
            this.editor.clipboard.dangerouslyPasteHTML(0, this.value)
            this.editor.on("text-change", (delta, oldDelta, source) => {
                this.$emit("input", this.editor.root.innerHTML);
                // const html = this.editor.root.innerHTML;
                // const text = this.editor.getText();
                // const editor = this.editor;
                // this.currentValue = html;
                // this.$emit("input", html);
                // this.$emit("on-change", {
                //     html,
                //     text,
                //     editor
                // });
            });
            this.editor.on("text-change", (delta, oldDelta, source) => {
                this.$emit("on-text-change", delta, oldDelta, source);
            });
            this.editor.on("selection-change", (range, oldRange, source) => {
                this.$emit("on-selection-change", range, oldRange, source);
            });
            this.editor.on("editor-change", (eventName, ...args) => {
                this.$emit("on-editor-change", eventName, ...args);
            });
        },
        dropFilesHandle(evt) {
            let _this = this;
            console.log('dropFilesHandle', evt)
            if (evt.dataTransfer && evt.dataTransfer.files && evt.dataTransfer.files.length) {
                evt.preventDefault();
                [].forEach.call(evt.dataTransfer.files, (file) => {
                    if (!file.type.match(/^image\/(gif|jpe?g|a?png|bmp)/i)) {
                        return;
                    }
                    let params = new FormData()
                    params.append('file', file)
                    uploadImage(params).then(res => {
                        if (res.code == 200) {
                            let length = _this.editor.getSelection().index; //光标位置
                            // 插入图片  图片地址
                            _this.editor.insertEmbed(length, "image", res.url);
                            // 调整光标到最后
                            _this.editor.setSelection(length + 1); //光标后移一位
                        }
                    })
                });
            }
        },
        pastHandle(evt) {
            let _this = this;
            // console.log(evt)
            if (evt.clipboardData && evt.clipboardData.files && evt.clipboardData.files.length) {
                evt.preventDefault();
                [].forEach.call(evt.clipboardData.files, (file) => {
                    if (!file.type.match(/^image\/(gif|jpe?g|a?png|bmp)/i)) {
                        return;
                    }
                    let params = new FormData()
                    params.append('file', file)
                    uploadImage(params).then(res => {
                        if (res.code == 200) {
                            let length = _this.editor.getSelection().index; //光标位置
                            // 插入图片  图片地址
                            _this.editor.insertEmbed(length, "image", res.url);
                            // 调整光标到最后
                            _this.editor.setSelection(length + 1); //光标后移一位
                        }
                    })
                });
            }
        },
        // 上传前校检格式和大小
        handleBeforeUpload(file) {
            // 校检文件大小
            if (this.fileSize) {
                const isLt = file.size / 1024 / 1024 < this.fileSize;
                if (!isLt) {
                    this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
                    return false;
                }
            }
            return true;
        },
        handleUploadSuccess(res, file) {
            // 获取富文本组件实例
            let quill = this.editor;
            // 如果上传成功
            if (res.code == 200) {
                // 获取光标所在位置
                let length = quill.getSelection().index;
                // 插入图片  res.url为服务器返回的图片地址
                quill.insertEmbed(length, "image", res.url);
                // 调整光标到最后
                quill.setSelection(length + 1);
            } else {
                this.$message.error("图片插入失败");
            }
        },
        handleUploadError() {
            this.$message.error("图片插入失败");
        },
    }
};
</script>

<style lang="scss" scope>
.in-editor-wrapper {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .ql-toolbar {
        .ql-formats {
            .ql-picker-label {
                &::before {
                    position: relative;
                    top: -5px;
                }
            }

            button {
                i.icon {
                    font-size: 14px;
                }
            }
        }
    }

    .ql-container {
        flex-grow: 1;
        min-height: 180px;
        overflow: hidden;
    }
}
</style>